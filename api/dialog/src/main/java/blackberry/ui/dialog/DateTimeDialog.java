/*
 * DateTimeDialog.java
 *
 * Research In Motion Limited proprietary and confidential
 * Copyright Research In Motion Limited, 2011-2011
 */

package blackberry.ui.dialog;

import java.util.Calendar;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.picker.DateTimePicker;
import net.rim.device.cldc.util.GregorianCalendar;

import blackberry.ui.dialog.DateTimeAsyncFunction;

public class DateTimeDialog implements IWebWorksDialog {

    private Html5DateTimeObject _HTML5Calendar; 
    private DateTimePicker _dateTimePicker = null;

    public DateTimeDialog ( int type, String value, String min, String max, double step ) {
        switch ( type ) {
            case DateTimeAsyncFunction.TYPE_DATE:
                _HTML5Calendar = new HTML5Date ( value );
                break;
            case DateTimeAsyncFunction.TYPE_MONTH:
                _HTML5Calendar = new HTML5Month( value );
                break;
            case DateTimeAsyncFunction.TYPE_TIME:
                _HTML5Calendar = new HTML5Time( value );
                break;
            case DateTimeAsyncFunction.TYPE_DATETIME:
                _HTML5Calendar = new HTML5DateTimeGlobal( value );
                break;
            case DateTimeAsyncFunction.TYPE_DATETIMELOCAL:
                _HTML5Calendar = new HTML5DateTimeLocal( value );
                break;
            default:
                // Shouldn't reach this.
                break;
        }

        if ( _HTML5Calendar != null ) {
            _HTML5Calendar.createDateTimePickerInstance();
            if ( min != null && min.length() != 0 ) {
                _HTML5Calendar.setMin( min );
            }
            if ( max != null && max.length() != 0 ) {
                _HTML5Calendar.setMax( max );
            }
            if ( step > 0.0 ) { //must be positive floating-point number
                _HTML5Calendar.step(step);
            }
        }
    }
    
    public boolean show() {
        return _dateTimePicker.doModal();
    }
    
    public String getSelectedValue() {
        SimpleDateFormat sdf = new SimpleDateFormat( _HTML5Calendar.getFormat().toString() );
        StringBuffer sb = sdf.format( _dateTimePicker.getDateTime(), new StringBuffer(16), null );
        
        return sb.toString();
    }
    
    private static int parseInt( String s, int from, int to ) {
        return net.rim.device.api.util.NumberUtilities.parseInt( s, from, to, 10 );
    }
    
    private static void setMonth( Calendar calendar, int year, int month ) {
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
    }
    
    private static void setTime( Calendar calendar, int hour, int minutes, int seconds, int milli) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milli);
    }
    
    public class HTML5Month extends Html5DateTimeObject {
        
        public HTML5Month( String value ) {
            if ( value != null && value.length() != 0 ) {
                _html5Calendar = parse( value );
            } else {
                 _html5Calendar = Calendar.getInstance();
            }
        }
        
        public HTML5Month( ) {
        }
        
        public StringBuffer getFormat() {
            if ( _format == null ) {
                return new StringBuffer("yyyy-MM");
            }
            return _format;
        }
        
        public void createDateTimePickerInstance() {
            if ( _html5Calendar == null ) {
                _html5Calendar = Calendar.getInstance();
            }
           _dateTimePicker = DateTimePicker.createInstance( _html5Calendar, getFormat().toString(), null );
        }
        
        public void setMin( String value ) {
            Calendar minCalendar = parse( value );
            if ( minCalendar != null ) {
                _dateTimePicker.setMinimumDate( minCalendar );
            }
        }
        
        public void setMax( String value ) {
            Calendar maxCalendar = parse( value );
            if ( maxCalendar != null ) {
                _dateTimePicker.setMaximumDate( maxCalendar );
            }
        }
        
        protected Calendar parse( String text ) {
            try {
                _format = new StringBuffer( 7 );
                int year = parseInt( text, 0, 4);
                if( year <= 0 ) { throw new IllegalArgumentException(); }
                
                if ( text.length() < 5 || text.charAt(4) != '-' ) { throw new IllegalArgumentException(); }
                
                int month = parseInt( text, 5, 7 );
                if( month <= 0 || month > 12 ) { throw new IllegalArgumentException(); }
                
                Calendar calendar = getInstance();
                setMonth( calendar, year, month );
                _format.append( "yyyy-MM" );
                //clear time so that it doesn't interfere when setting setMinimumCalendar and setMaximumCalendar
                setTime( calendar, 0, 0, 0, 0 );
                
                return calendar;
            } catch (Exception e) {
                _format = null;
                return null;
            }
        }
    }
    
    public class HTML5Date extends Html5DateTimeObject {
        
        public HTML5Date( String value ) {
            if ( value != null && value.length() != 0 ) {
                _html5Calendar = parse( value );
            } else {
                 _html5Calendar = Calendar.getInstance();
            }
        }
        
        public HTML5Date() {
        }

        public StringBuffer getFormat() {
            if ( _format == null ) {
                return new StringBuffer("yyyy-MM-dd");
            }
            return _format;
        }
        
        public void createDateTimePickerInstance() {
            if ( _html5Calendar == null ) {
                _html5Calendar = Calendar.getInstance();
            }
            _dateTimePicker = DateTimePicker.createInstance( _html5Calendar, getFormat().toString(), null );
        }
        
        public void setMin( String value ) {
            Calendar minCalendar = parse( value );
            if ( minCalendar != null ) {
                _dateTimePicker.setMinimumDate( minCalendar );
            }
        }
        
        public void setMax( String value ) {
            Calendar maxCalendar = parse( value );
            if ( maxCalendar != null ) {
                _dateTimePicker.setMaximumDate( maxCalendar );
            }
        }
        
        protected Calendar parse( String text ) {
            try {
                int len = text.length();
                //parse month
                HTML5Month month = new HTML5Month();
                Calendar calendar = month.parse( text );
                _format = month.getFormat();
                
                if ( text.length() < 8 || text.charAt(7) != '-' ) { throw new IllegalArgumentException() ;}
                
                //parse day
                int day = parseInt( text, 8, len );
                
                if ( day < 1 || day > 31 ) { throw new IllegalArgumentException(); }
                
                //combine month and day
                calendar.set( Calendar.DAY_OF_MONTH, 0 );
                ((GregorianCalendar) calendar).roll( Calendar.DAY_OF_MONTH, day );
                
                _format.append("-dd");
                return calendar;
            } catch (IllegalArgumentException e) {
                _format = null;
                return null;
            }
        }
    }
    
    public class HTML5Time extends Html5DateTimeObject {
        
        public HTML5Time( String value ) {
            if ( value != null && value.length() != 0 ) {
                _html5Calendar = parse( value );
            } else {
                 _html5Calendar = Calendar.getInstance();
            }
        }
        
        public HTML5Time() {
        }

        public StringBuffer getFormat() {
            if ( _format == null ) {
                return new StringBuffer("HH:mm:ss");
            }
            return _format;
        }
        
        public void createDateTimePickerInstance() {
            if ( _html5Calendar == null ) {
                _html5Calendar = Calendar.getInstance();
            }
            _dateTimePicker = DateTimePicker.createInstance( _html5Calendar, null, getFormat().toString() );
        }
        
        public void setMin( String value ) {
            Calendar minCalendar = parse ( value );
            if ( minCalendar != null ) {
                _dateTimePicker.setMinimumDate( minCalendar );
            }
        }
        
        public void setMax( String value ) {
            Calendar maxCalendar = parse ( value );
            if ( maxCalendar != null ) {
                _dateTimePicker.setMaximumDate( maxCalendar );
            }            
        }

        public void step( double value ) {
            //DateTimePickerController only accepts Calendar.HOUR,
            //or Calendar.MINUTE or Calendar.SECOND
            //and the value comes in second increments
            if (value >= 3600) {
                _dateTimePicker.setIncrement( Calendar.HOUR, ((int) value)/3600 );
            }
            else if (value >= 60) {
                _dateTimePicker.setIncrement( Calendar.MINUTE, ((int) value)/60 );
            }
            else {
                _dateTimePicker.setIncrement( Calendar.SECOND, (int) value );
            }
        }

        protected Calendar parse( String text ) {
            try {
                _format = new StringBuffer( 5 );
                int hour = parseInt( text, 0, 2 );
                if( hour < 0 || hour > 23 ) { throw new IllegalArgumentException(); }
    
                if ( text.length() < 3 || text.charAt(2) != ':' ) { throw new IllegalArgumentException(); }
    
                int minutes = parseInt( text, 3, 5 );
                if( minutes < 0 || minutes > 59 ) { throw new IllegalArgumentException(); }
                
                Calendar calendar = getInstance();
                int seconds = 0;
                
                _format.append("HH:mm");
                
                if ( text.length() > 5 ) { //parse seconds
                    if ( text.charAt(5) != ':' ) { throw new IllegalArgumentException(); }
                    seconds = parseInt( text, 6, 8 );
                    if( seconds < 0 || seconds > 59 ) { throw new IllegalArgumentException(); }
                    
                    _format.append(":ss");
                    
                    //parse milliseconds
                    if ( text.length() > 8 && text.charAt(8) == '.' ) {
                        _format.append(".");
                        StringBuffer sb = new StringBuffer();
                        for ( int i=9; i < text.length() ; i++ ) {
                            sb.append( text.charAt(i) );
                            _format.append("S");
                        }
                        int fracSeconds = parseInt( sb.toString(), 0, sb.toString().length() );
                        setTime( calendar, hour, minutes, seconds, fracSeconds );
                    
                    } else { //no milliseconds
                        setTime( calendar, hour, minutes, seconds, 0 );
                    }
                    return calendar;
                
                } else { // no seconds
                    setTime( calendar, hour, minutes, 0, 0 );
                    return calendar; 
                }
            } catch (Exception e) {
                _format = null;
                return null;
            }
        }
    }
    
    public class HTML5DateTimeLocal extends Html5DateTimeObject {
        
        public HTML5DateTimeLocal( String value ) {
            if ( value != null && value.length() != 0 ) {
                _html5Calendar = parse( value );
            } else {
                 _html5Calendar = Calendar.getInstance();
            }
        }
        
        public HTML5DateTimeLocal() {
        }
        
        public StringBuffer getFormat() {
            if ( _format == null ) {
                return new StringBuffer("yyyy-MM-dd'T'HH:mm");
            }
            return _format;
        }
        
        public void createDateTimePickerInstance() {
            if ( _html5Calendar == null ) {
                _html5Calendar = Calendar.getInstance();
            }
            _dateTimePicker = DateTimePicker.createInstance( _html5Calendar );
        }
        
        public void setMin( String value ) {
            Calendar minCalendar = parse ( value );
            if ( minCalendar != null ) {
                _dateTimePicker.setMinimumDate( minCalendar );
            }
        }
        
        public void setMax( String value ) {
            Calendar maxCalendar = parse( value );
            if ( maxCalendar != null ) {
                _dateTimePicker.setMaximumDate( maxCalendar );
            }
        }
        
        protected Calendar parse( String text ) {
            try {
                int len = text.length();
                //parse date
                HTML5Date date = new HTML5Date();
                Calendar calendar = date.parse( text.substring( 0, 10 ) );
                _format = date.getFormat();
                
                //check for T
                if ( text.indexOf( 'T',10 ) == -1 ) { throw new IllegalArgumentException(); }
                
                _format.append("'T'");
                
                //parse time
                HTML5Time htime = new HTML5Time();
                Calendar cTime = htime.parse( text.substring( 11, len ) );
                _format.append( htime.getFormat() );
                
                //combine date and time
                setTime( calendar, cTime.get(Calendar.HOUR_OF_DAY) , cTime.get(Calendar.MINUTE), cTime.get(Calendar.SECOND), cTime.get(Calendar.MILLISECOND) );
                return calendar;
    
            } catch (IllegalArgumentException e) {
                _format = null;
                return null;
            }
        }
    }
    
    public class HTML5DateTimeGlobal extends Html5DateTimeObject {
        
        public HTML5DateTimeGlobal( String value ) {
            if ( value != null && value.length() != 0 ) {
                _html5Calendar = parse( value );
            } else {
                 _html5Calendar = Calendar.getInstance();
            }
        }
        
        public HTML5DateTimeGlobal() {
        }

        public StringBuffer getFormat() {
            if ( _format == null ) {
                return new StringBuffer("yyyy-MM-dd'T'HH:mm'Z'");
            }
            return _format;
        }
        
        public void setMin( String value ) {
            Calendar minCalendar = parse ( value );
            if ( minCalendar != null ) {
                _dateTimePicker.setMinimumDate( minCalendar );
            }
        }
        
        public void setMax( String value ) {
            Calendar maxCalendar = parse ( value );
            if ( maxCalendar != null ) {
                _dateTimePicker.setMaximumDate( maxCalendar );
            }
        }
        
        public void createDateTimePickerInstance() {
            if ( _html5Calendar == null ) {
                _html5Calendar = Calendar.getInstance();
            }
            _dateTimePicker = DateTimePicker.createInstance( _html5Calendar );
        }
        
        protected Calendar parse ( String text ) {
            try {
                Calendar calendar;
                int len = text.length();
                
                if( text.charAt( len - 1 ) == 'Z' ) {
                    HTML5DateTimeLocal dtl = new HTML5DateTimeLocal();
                    calendar = dtl.parse( text.substring( 0, len - 1 ));
                    _format = dtl.getFormat();
                    _format.append("'Z'");
                    return calendar;
                } else { // Time zone adjustment
                    int signloc = text.indexOf( '-', 10 );
                    if ( signloc == -1 ) {
                        signloc = text.indexOf( '+', 10 );
                    }
                    
                    if ( signloc == -1 ) { throw new IllegalArgumentException(); }
                    String sign = text.substring ( signloc, signloc + 1 );
                    
                    HTML5DateTimeLocal dtl = new HTML5DateTimeLocal();
                    calendar = dtl.parse( text.substring( 0, signloc) );
                    _format = dtl.getFormat();
                    
                    HTML5Time htime = new HTML5Time();
                    Calendar timeShift = htime.parse( text.substring( len - 5, len ));
                    _format.append( htime.getFormat() );
                    
                    int hour = timeShift.get( Calendar.HOUR_OF_DAY );
                    int minute = timeShift.get( Calendar.MINUTE );
                    if ( sign.charAt(0) == '-' ) {
                        hour = timeShift.get( Calendar.HOUR_OF_DAY ) * -1;
                        minute = timeShift.get( Calendar.MINUTE ) * -1;
                    }
    
                    ((GregorianCalendar) calendar).add( Calendar.HOUR_OF_DAY, hour );
                    ((GregorianCalendar) calendar).add( Calendar.MINUTE, minute );
                    return calendar;
                }
            } catch (IllegalArgumentException e) {
                _format = null;
                return null;
            }
        }
    }
}

abstract class Html5DateTimeObject {
    
    protected Calendar _html5Calendar;
    protected StringBuffer _format;
    
    Html5DateTimeObject() {
        _html5Calendar = Calendar.getInstance();
    }
    
    public Calendar getInstance() {
        if ( _html5Calendar == null ) {
            _html5Calendar = Calendar.getInstance();
        }
        return _html5Calendar;
    }
    
    protected abstract Calendar parse( String text );
    public abstract void setMin( String value );
    public abstract void setMax( String value );
    public void step( double value ){
    }
    public abstract StringBuffer getFormat();
    public abstract void createDateTimePickerInstance();
}
