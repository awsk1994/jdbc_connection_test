import java.sql.*;   // Use classes in java.sql package
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDateTime;

class dbConnectTest
{
	/* 	String format for String DB_URL:
			MySQL: jdbc:mysql://<HOST>:<PORT>/<DATABASE_NAME>	
			Microsoft SQL Server: jdbc:microsoft:sqlserver://<HOST>:<PORT>;databaseName= <DATABASE_NAME>;<OPTIONS>
			More: https://www.ibm.com/support/knowledgecenter/en/SSEP7J_10.1.1/com.ibm.swg.ba.cognos.vvm_ag_guide.10.1.1.doc/c_ag_samjdcurlform.html
	*/

	final static String DEFAULT_DEBUG = "0";
	final static String DEFAULT_MAX_RETRIES = "3";
	final static String DEFAULT_RPT = "1";
	final static String DEFAULT_RPT_INT_SEC = "0";

	final static String KEY_DB_URL = "url";
	final static String KEY_DB_UNAME = "uname";
	final static String KEY_DB_PW = "pw";
	final static String KEY_MAX_RETRIES = "max_retries";		// maximum retry upon connection failure
	final static String KEY_DEBUG = "debug";					// debug mode - print arguments
	final static String KEY_RPT = "rpt";						// how many times to repeat the program
	final static String KEY_RPT_INT_SEC = "rpt_int_sec";		// seconds to wait between each repeat attempt

	static int max_retries;
	static int rpt;
	static int rpt_int_sec;
	static int retries;

	static Connection conn;

	public static void main(String [] args)
	{
		try{
			HashMap<String, String> argsMap = convertToKeyValuePair(args);
			setStaticVars(argsMap);
			if(argsMap.get(KEY_DEBUG).equals("1")) {
				printArgsMap(argsMap);
			}

			for(int i=0; i<rpt; i++){
				connectToDb(argsMap);
				Thread.sleep(rpt_int_sec * 1000);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private static HashMap<String, String> convertToKeyValuePair(String[] args) throws Exception {
		HashMap<String, String> argsMap = new HashMap<>();
		Pattern pattern = Pattern.compile("-(\\w*)=(.*)");
		for (String arg: args) {
			Matcher matcher = pattern.matcher(arg);
			if (matcher.find())
			{
				String key = matcher.group(1);
				String value = matcher.group(2);
				argsMap.put(key, value);
			}
		}

		return checkArgsMap(argsMap);
	}

	private static void setStaticVars(HashMap<String, String>  argsMap){
		max_retries = Integer.parseInt(argsMap.get(KEY_MAX_RETRIES));
		rpt = Integer.parseInt(argsMap.get(KEY_RPT));
		rpt_int_sec = Integer.parseInt(argsMap.get(KEY_RPT_INT_SEC));
	}

	private static void printArgsMap(HashMap<String, String>  argsMap){
		print("DEBUG", "Printing arguments:");
		argsMap.forEach((k, v) -> print("DEBUG", "Key : " + k + ", Value : " + v));
	}

	private static HashMap<String, String> checkArgsMap(HashMap argsMap) throws Exception {
		argsMap = setOptionArgs(argsMap);
		argsMap = setImptArgs(argsMap);
		return argsMap;
	}

	private static HashMap setOptionArgs(HashMap<String, String> argsMap){
		if(!argsMap.containsKey(KEY_MAX_RETRIES)){
			argsMap.put(KEY_MAX_RETRIES, DEFAULT_MAX_RETRIES);
		}
		if(!argsMap.containsKey(KEY_DEBUG)){
			argsMap.put(KEY_DEBUG, DEFAULT_DEBUG);
		}
		if(!argsMap.containsKey(KEY_RPT)){
			argsMap.put(KEY_RPT, DEFAULT_RPT);
		}
		if(!argsMap.containsKey(KEY_RPT_INT_SEC)){
			argsMap.put(KEY_RPT_INT_SEC, DEFAULT_RPT_INT_SEC);
		}
		return argsMap;
	}

	private static HashMap setImptArgs(HashMap<String, String> argsMap) throws Exception {
		if(!argsMap.containsKey(KEY_DB_URL)){
			print("ERROR", "No db url detected.");
			throw new Exception ("No db url detected in arguments.");
		}
		if(!argsMap.containsKey(KEY_DB_UNAME)){
			print("WARNING", "No db uname detected.");
			argsMap.put(KEY_DB_UNAME, "");
		}
		if(!argsMap.containsKey(KEY_DB_PW)){
			print("WARNING", "No db password detected.");
			argsMap.put(KEY_DB_PW, "");
		}
		return argsMap;
	}

	private static void connectToDb(HashMap<String, String> argsMap) throws Exception {
		establishConnection(argsMap.get(KEY_DB_URL), argsMap.get(KEY_DB_UNAME), argsMap.get(KEY_DB_PW));
		closeConnection();
	}

	private static void establishConnection(String url, String uname, String pw){
		try
		{
			print("INFO", "Attempting to connect to database.");
			conn = DriverManager.getConnection(url, uname, pw);
			print("INFO", "Connected successfully.");
		}
		catch (Exception e)
		{
			retries++;
			e.printStackTrace();
			if(retries < max_retries){
				print("ERROR", "Failed to connect (retries = " + retries + "). Trying again in 1 second.");

				try{
					Thread.sleep(1000);
					establishConnection(url, uname, pw);
				} catch (InterruptedException e2){
					e2.printStackTrace();
				}
			} else {
				print("ERROR", "Failed to connect (retries = " + retries + "). Reached MAX_RETRIES (" + max_retries + ").");
			}
		}
	}

	private static void closeConnection(){
		if(conn != null)
		{
			try
			{
				conn.close();
			}
			catch(SQLException e)
			{
				print("ERROR", "SQL Exception while closing.");
			}
		}
	}

	private static void print(String level, String message){
		LocalDateTime currentTime = LocalDateTime.now();
		System.out.println(currentTime + " | " + level + " | " + message);
	}
}