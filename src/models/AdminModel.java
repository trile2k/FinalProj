/*
 File Name: AdminModel.java
  
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

package models;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import dao.DBConnect;

// Class to manage data for the admin, including insert, updata and delete
// database tables.
public class AdminModel extends DBConnect {

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	private String productTable = "tle_products"; // Products table

	public AdminModel() {
		conn = new DBConnect();
	}

	// Add (insert) a product record into the product table
	public void addProduct(String prodName, String prodDesc,
			String prodPrice, String prodQty) {

		try {
			stmt = conn.getConnection().createStatement();
			String sql = null;

			// Include all object data to the database table
			sql = "insert into " + productTable + "(name, description, price, quantity) values ('"
					+ prodName + "','" + prodDesc + "','" + prodPrice + "','"
					+ prodQty + "')";
			stmt.executeUpdate(sql);

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	// Delete a product based on the PID
	public void deleteProduct(String productID) {

		try {
			stmt = conn.getConnection().createStatement();
			String sql = "DELETE from " + productTable + " WHERE pid = " + productID;
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Update a price for the product
	public void priceUpdate(String PID, String updatedPrice) {

		try {
			PreparedStatement prepStmt = null;
			String sql = "UPDATE " + productTable + " SET price = ?" + " WHERE pid = " + PID;

			prepStmt = conn.getConnection().prepareStatement(sql);
			prepStmt.setString(1, updatedPrice);
			prepStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
