import java.sql.*;   // Use classes in java.sql package

class dbConnectTest
{
	/* 	String format for String DB_URL:
			MySQL: jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>	
			Microsoft SQL Server: jdbc:microsoft:sqlserver://<HOST>:<PORT>;databaseName= <DATABASE_NAME>;<OPTIONS>

		More: https://www.ibm.com/support/knowledgecenter/en/SSEP7J_10.1.1/com.ibm.swg.ba.cognos.vvm_ag_guide.10.1.1.doc/c_ag_samjdcurlform.html
	*/
	final static String DB_URL = "jdbc:sqlserver://SOME_MACHINE_NAME";
	final static String USER = "SOME_USERNAME";
	final static String PASS = "SOME_PASSWORD";
	final static int MAX_RETRIES = 3;

	static int retries = 0;
	static Connection conn;

	public static void main(String [] args)
	{
		connectDb();
		closeConnection();
	}

	static void connectDb(){
		try
		{
			System.out.println("INFO | Attempting to connect to database.");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("INFO | Connected successfully.");
		}
		catch (Exception e)
		{
			retries++;
			e.printStackTrace();
			if(retries < MAX_RETRIES){
				System.out.println("ERROR | Failed to connect (retries = " + retries + "). Trying again in 1 second.");
				try{
					Thread.sleep(1000);
					connectDb();
				} catch (InterruptedException e2){
					e2.printStackTrace();
				}
			} else {
				System.out.println("ERROR | Failed to connect (retries = " + retries + "). Reached MAX_RETRIES (" + MAX_RETRIES + ").");
			}
		}
	}

	static void closeConnection(){
		if(conn != null)
		{
			try
			{
				conn.close();
			}
			catch(SQLException e)
			{
				System.out.println("ERROR | SQL Exception while closing.");
			}
		}
	}
}