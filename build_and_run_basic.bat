set url=<URL>
set uname=<UNAME>
set pw=<PW>

javac -cp ".;sqljdbc4-2.0.jar;" dbConnectTest.java
java -cp ".;sqljdbc4-2.0.jar;" dbConnectTest -url=%url% -uname=%uname% -pw=%pw%

pause


