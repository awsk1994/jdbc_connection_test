import java.sql.*;   // Use classes in java.sql package

class dbConnectTest
{

	public final static String DB_URL = "jdbc:sqlserver://os-2k8-r2-vm216";
	public final static String USER = "sa";
	public final static String PASS = "123Empirix!";

	public static void main(String [] args)
	{
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("login successfully");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(conn != null)
			{
				try
				{
					conn.close();
				}
				catch(SQLException e)
				{
					System.out.println("SQL Exception while closing");
				}
			}
		}
	}
}