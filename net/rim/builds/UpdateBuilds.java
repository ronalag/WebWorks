package net.rim.builds;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Class responsible for updating the html file that contains the builds. This fulfills two goals. First, it removes any build
 * information that corresponds to builds that need to be deleted. Secondly, it removes the resources for the builds that are
 * removed from the html file and need to be removed from the repo.
 */
public class UpdateBuilds {

    public static final int NUM_ARGS = 2;
    private Vector< File > filesToDelete;
    private int builds;
    private File file;
    private static final String RECORD_DELIMITER = "class=\"Record\"></a>";
    private static final String NEW_RECORD_PLACEHOLDER = "id=\"Record\"></a>";

    /**
     * Main constructor for the class. Invoking the constructor also instantiates the updating of the HTML page and the removing
     * of unneeded resources.
     * 
     * @param file
     *            The file name of the HTML page with build info
     * @param builds
     *            The number of builds to retain in the HTML page.
     */
    private UpdateBuilds( File file, int builds ) {
        this.filesToDelete = new Vector< File >( 10, 5 );
        this.builds = builds;
        this.file = file;

        // Update the page
        this.updatePage();
        // Delete any unnecessary artifacts
        this.removeArtifacts();
    }

    /**
     * This method is responsible for parsing the page and removing entries that are no longer needed. If there are n entries on
     * the page and we need to keep k entries and k < n then this method will remove the html content corresponding to the k+1 ...
     * n entries.
     */
    public void updatePage() {

        try {
            // Rename file to tmp + fileName
            File tmpFile = new File( "tmp" + file.getName() );
            file.renameTo( tmpFile );

            FileReader fileReader = new FileReader( "tmp" + file.getName() );
            BufferedReader reader = new BufferedReader( fileReader );

            File newDownloads = new File( file.getName() );
            newDownloads.createNewFile();

            FileWriter fileWriter = new FileWriter( newDownloads );
            BufferedWriter writer = new BufferedWriter( fileWriter );

            StringTokenizer tokenizer;
            String line, token;
            int occurrences = 0;
            while( ( line = reader.readLine() ) != null && occurrences < builds ) {

                tokenizer = new StringTokenizer( line );

                while( tokenizer.hasMoreTokens() ) {
                    token = tokenizer.nextToken();
                    if( token.equals( RECORD_DELIMITER ) ) {
                        occurrences++;
                    } 
                }
                writer.write( line );
                writer.newLine();
            }

            String content = "";
            String regex = "<h[1-6]>[0-9]+\\.[0-9]\\.[0-9]+\\.[0-9]+" + "<\\/h[1-6]>";
            boolean shouldPreserveLine = false;

            while( line != null ) {

                tokenizer = new StringTokenizer( line );
                shouldPreserveLine = true;

                String directory;
                File newFile;
                while( tokenizer.hasMoreTokens() && shouldPreserveLine ) {
                    token = tokenizer.nextToken();

                    if( Pattern.matches( regex, token ) ) {

                        if( !content.equals( "" ) ) {
                            writer.write( content );
                            content = "";
                            shouldPreserveLine = false;
                        }
                        directory = token.substring( 4, token.length() - 5 );
                        newFile = new File( directory );
                        if( !filesToDelete.contains( newFile ) ) {
                            filesToDelete.add( newFile );
                        }

                    } else if( token.equals( RECORD_DELIMITER ) ) {
                        content = "";
                        shouldPreserveLine = false;
                    }
                }

                if( shouldPreserveLine ) {
                    content += line + "\n";
                }

                line = reader.readLine();
            }

            reader.close();

            if( !content.equals( "" ) ) {
                writer.write( content );
            }
            writer.close();

            tmpFile.delete();           

        } catch( FileNotFoundException e ) {
            System.out.println( e.getMessage() );
            System.exit( 0 );
        } catch( IOException e ) {
            System.out.println( e.getMessage() );
            System.exit( 0 );
        }
    }

    /**
     * Helper method for the removeArtifacts method. If the argument is a directory then it deletes the directory and all files in
     * the directory, otherwise it deletes the file.
     * 
     * @param toDelete
     *            File to delete.
     */
    private void removeAllFiles( File toDelete ) {

        if( toDelete.exists() ) {

            if( toDelete.isDirectory() ) {
                File[] dirFiles = toDelete.listFiles();

                for( int i = 0; i < dirFiles.length; i++ ) {
                    removeAllFiles( dirFiles[ i ] );
                }
            }

            if( !toDelete.delete() ) {
                System.out.println( "Could not remove the file : " + toDelete.getName() );
            }
        }
    }

    /**
     * Method to delete the files that are not needed. This method is dependent on the updatePage method being run first as part
     * of its execution compiles the list of artifacts to be deleted.
     */
    private void removeArtifacts() {
        for( int i = 0; i < filesToDelete.size(); i++ ) {
            removeAllFiles( filesToDelete.get( i ) );
        }
    }

    /**
     * Method to ensure that the
     * 
     * @param args
     *            The arguments to the main method of execution. The first argument is the input file and the second argument is
     *            the number of build records to keep.
     */
    private static void processCommandLineArgs( String[] args ) {
        // Check number of arguments
        if( args.length < NUM_ARGS ) {
            System.out.println( "Too few arguments" );
            System.exit( 0 );
        }

        // Make sure first argument is a file
        if( !( new File( args[ 0 ] ) ).exists() ) {
            System.out.println( "The file " + args[ 0 ] + " does not exist" );
            System.exit( 0 );
        }

        // Ensure second argument is a number
        try {
            Integer.parseInt( args[ 1 ] );
        } catch( NumberFormatException e ) {
            System.out.println( args[ 1 ] + " is not a number." );
            System.exit( 0 );
        }
    }

    /**
     * Main method of execution processes command line arguments and updates the page.
     */
    public static void main( String[] args ) {
        // Ensure validity of command line arguments
        processCommandLineArgs( args );

        // If execution reaches this point the arguments are valid so process
        UpdateBuilds updater = new UpdateBuilds( new File( args[ 0 ] ), Integer.parseInt( args[ 1 ] ) );
    }

}

