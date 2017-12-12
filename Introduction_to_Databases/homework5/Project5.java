package homework5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Project5 {

	
	private static java.sql.Connection getConnection() {
		/** Set up connections with mySQL */
		java.sql.Connection conn = null;

		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/finalexamhw5?" + "user=root&password=zsz941209");
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			conn = null;
		}
		return conn;
	}

	
	
	private static Map<String, String> find_mysql_by_id(String id) {
		/**
		 * This function connects to MySQL and returns the tuple from the Master
		 * table with playerID equal to the input value.
		 * 
		 * @paramin id: The playerID that query runs on. Defined by user
		 * @paramout result: The tuple in Master table stored in a Map
		 */

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> result = null;
		
		try {
			conn = getConnection(); // Set up connection to MySQL
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Master WHERE playerID='" + id + "'"); // SQL query
			rs.first();
			result = new HashMap<>();
			String playerID, nameLast, nameFirst, birthYear, birthMonth, birthDay, birthCountry, birthState, birthCity;
			
			// Parse attributes and add them to the result map
			playerID = rs.getString("playerID"); result.put("playerID", playerID);
			nameLast = rs.getString("nameLast"); result.put("nameLast", nameLast);
			nameFirst = rs.getString("nameFirst"); result.put("nameFirst", nameFirst);
			birthYear = rs.getString("birthYear"); result.put("birthYear", birthYear);
			birthMonth = rs.getString("birthMonth"); result.put("birthMonth", birthMonth);
			birthDay = rs.getString("birthDay"); result.put("birthDay", birthDay);
			birthCountry = rs.getString("birthCountry"); result.put("birthCountry", birthCountry);
			birthState = rs.getString("birthState"); result.put("birthState", birthState);
			birthCity = rs.getString("birthCity"); result.put("birthCity", birthCity);			

		} catch (SQLException ex) {
			// Handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// Release recourses in finally block
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
		return result;
	}

	
	
	private static Map<String, String> find_redis_by_id(String id) {
		/**
		 * This function connects to Redis and returns the tuple from the Master
		 * table with playerID equal to the input value.
		 * 
		 * @paramin id: The playerID that query runs on. Defined by user
		 * @paramout result: The query result in Redis stored in a Map
		 */
		Map<String, String> result;
		Jedis jedis = new Jedis("localhost"); 	 // Connecting to Redis server on localhost
		result = jedis.hgetAll("players:" + id); // Fetch data from Redis
		jedis.close();
		return result;
	}

	
	
	private static void add_to_redis(String id, Map<String, String> data) {
		/**
		 * Create an HSET with key players:id and entries from the elements in data.
		 * 
		 * @paramin id: The playerID that query runs on. Defined by user
		 * @paramin data: The attributes related to the player indicated by id
		 */
		Jedis jedis = new Jedis("localhost"); 	 // Connecting to Redis server on localhost
		Set<String> keys = data.keySet();
		Iterator<String> ite = keys.iterator();
		while (ite.hasNext()){
			String att = ite.next();
			if (att.equals("playerID"))
				continue;
			jedis.hset("players:" + id, att, data.get(att));
		}
		jedis.close();
	}

	
	
	private static Map<String, String> find_by_id(String id) {
		/**
		 * Call find_redis_by_id(id), if the result is found, return the result.
		 * Else fetch data from MySQL using find_mysql_by_id(id) and add data into Redis.
		 * 
		 * @paramin id: The playerID that query runs on. Defined by user
		 * @paramout tuple: The query result in Redis/MySQL
		 */
		Map<String, String> result = find_redis_by_id(id);
		if (result.size() == 0){ 				// If this id doesn't exist in Redis
			System.out.println("This player is not in Redis");
			System.out.println("Fetch data for this player in MySQL");
			result = find_mysql_by_id(id);		// Run query in MySQL
			System.out.println("Insert this player into Redis");
			add_to_redis(id, result);			// Add this player into Redis
		}
		return result;
	}

	
	
	public static void main(String[] args) {
		// Test find_mysql_by_id(id)
		String id1 = "aardsda01";
		System.out.println("Test find_mysql_by_id(id)");
		System.out.println("Look for player " + id1 + " in MySQL");
		System.out.println("Search result: " + find_mysql_by_id(id1));
		System.out.println();
		
		// Test find_redis_by_id(id)
		System.out.println("Test find_redis_by_id(id)");
		System.out.println("Look for player " + id1 + " in Redis");
		System.out.println("Search result: " + find_redis_by_id(id1));
		System.out.println();
		
		// Test find_by_id(id)
		System.out.println("Test find_by_id(id)");
		System.out.println("Look for player " + id1 + " in Redis first");
		System.out.println(find_by_id(id1));
		System.out.println();
		
		// Test find_redis_by_id(id)
		System.out.println("Test find_redis_by_id(id)");
		System.out.println("Look for player " + id1 + " in Redis");
		System.out.println("Search result: " + find_redis_by_id(id1));
		System.out.println();
	}
}
