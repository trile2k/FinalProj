/*
 File Name: LoginModel.java
  
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
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.DBConnect;

//Class to manage user's credential.
public class LoginModel extends DBConnect {

	private String usersTable = "tle_users"; // Users Table
	
	private Boolean admin; // Is user an admin?
	private int uid; // User ID
	private String firstName; // User first name
	private String lastName; // User last name

	// Validate login credentials
	public Boolean getCredentials(String username, String password) {

		String query = "SELECT * FROM " + usersTable + " WHERE ulogin = ? and upasswd = ?;";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// Save user id, admin status, user's first and last name
				setId(rs.getInt("uid"));
				setAdmin(rs.getBoolean("uadmin"));
				setFirstName(rs.getString("ufirst"));
				setLastName(rs.getString("ulast"));
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Getter for ID
	public int getId() {
		return uid;
	}

	// Setter for ID
	public void setId(int id) {
		this.uid = id;
	}

	// Getter for admin
	public Boolean isAdmin() {
		return admin;
	}

	// Setter for admin
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}