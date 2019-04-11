# jdbc_connection_test

Simple java file to test jdbc connection to database.

## Get Started.

1. Set url, username and password for your database in build_and_run_basic.bat:

```
set url=<URL>
set uname=<UNAME>
set pw=<PW>
```

2. Double-click on build_and_run_basic.bat

## Notes
String format for db url:

	MySQL: jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>	
	
	Microsoft SQL Server: jdbc:microsoft:sqlserver://<HOST>:<PORT>;databaseName= <DATABASE_NAME>;<OPTIONS>
	
	More: https://www.ibm.com/support/knowledgecenter/en/SSEP7J_10.1.1/com.ibm.swg.ba.cognos.vvm_ag_guide.10.1.1.doc/c_ag_samjdcurlform.html
