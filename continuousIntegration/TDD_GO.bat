cd mds_dir
start cmd /c run.bat
cd simpack_dir
start cmd /c 9930.bat
fledgecontroller.exe /session=9930 < input_file > output_file