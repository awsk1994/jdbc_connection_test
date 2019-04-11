set url=<URL>
set uname=<UNAME>
set pw=<PW>

set max_retries=3
set debug=1
set rpt=10
set rpt_int_sec=15

javac -cp ".;sqljdbc4-2.0.jar;" dbConnectTest.java
java -cp ".;sqljdbc4-2.0.jar;" dbConnectTest -url=%url% -uname=%uname% -pw=%pw% -max_retries=%max_retries% -debug=%debug% -rpt=%rpt% -rpt_int_sec=%rpt_int_sec%

pause


