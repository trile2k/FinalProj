/*
 File Name: DBConnect.java
  
 Final Project: Retail Point of Sales (POS) Application
 
 This program implements the Retail Point of Sales (POS) Application
 which provides functionalities for the admin to add products, update product 
 pricing, and delete products from the retail inventory. The admin can also 
 view all products in the inventory, along with all the orders that the clients 
 have made. The client has the capabilities to order a product from a list of 
 products, as well as to cancel an order. The client can view a list of products 
 for purchasing, in addition to view his/her own order history. The program 
 checks login credentials to determine the user's role (whether admin or client)
 and capabilities.
 
 This program use the MVC framework consisting of the Model module to manage data, 
 the Controller module to interpret user's inputs, and the View module to manage 
 graphical/textual output. Also, the Dao module for database connectivity and 
 the Application module for the Main method and styles.

 Course #: IT-D 810 (Object-Oriented Application Development Certification)
  
 Programmer: Tri Le
 Hawk ID: A20101471

 */

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Database connection
public class DBConnect {

	protected Connection connection;

	// Get connection
	public Connection getConnection() {
		return connection;
	}

	// DB URL, Login and Password
	private static String url = "jdbc:mysql://www.papademas.net:3307/510fp";
	private static String username = "fp510";
	private static String password = "510";

	public DBConnect() {
		try {
			// Connect to DB with credentials.
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("Error in creating connection to database: " + e);
			System.exit(-1);
		}
	}

}