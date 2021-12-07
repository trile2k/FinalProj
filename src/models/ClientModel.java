/*
 File Name: ClientModel.java
  
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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import dao.DBConnect;

//Class to manage data for the client, including insert, updata and delete
//database tables.
public class ClientModel extends DBConnect {

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	private String productTable = "tle_products"; // Products table
	private String ordersTable = "tle_orders";    // Orders table

	public ClientModel() {

		conn = new DBConnect();
	}

	// Add (insert) a product record into the product table.  Before an insert
	// can be made, check to ensure the quantity ordered is not greater than
	// the quantity on hand (available).
	public Boolean addOrder(int uid, String pid, String qty) {

		String sql = null;
		ResultSet rs = null;
		String pName = null; // Product Name
		String pDesc = null; // Product Description
		double price = 0.0;
		int qtyOnHand = 0;

		try {
			stmt = conn.getConnection().createStatement();

			// Get info from product table
			sql = "select name, description, price, quantity from "
					+ productTable + " where pid = " + pid;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				pName = rs.getString("name");
				pDesc = rs.getString("description");
				price = rs.getDouble("price");
				qtyOnHand = rs.getInt("quantity");
			}
			
			// Can not order if quantity on hand is less than quantity ordered
			if (qtyOnHand < Integer.parseInt(qty)) {
				return false;
			}

			// Date purchase
			Calendar calendar = Calendar.getInstance();
			Date sqlStartDate = new Date((calendar.getTime()).getTime());
			String pDate = sqlStartDate.toString();

			// Add an order to the orders table
			sql = "insert into " + ordersTable + "(uid, pid, product_name, product_desc, price, quantity, purchase_date) values ('"
					+ uid + "','" + pid + "','" + pName + "','"
					+ pDesc + "','" + price + "','" + qty + "','"
					+ pDate + "')";
			stmt.executeUpdate(sql);

			// Update quantity on hand to reflect the quantity ordered.
			sql = "UPDATE " + productTable + " SET quantity = " + (qtyOnHand - Integer.parseInt(qty)) + " WHERE pid = " + pid;
			stmt.executeUpdate(sql);

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return true;
	}

	// Delete an order based on the OID
	public void deleteOrder(String orderID) {

		String sql = null;
		ResultSet rs = null;
		int qtyOrdered = 0;
		int qtyOnHand = 0;
		int pid = 0;

		try {
			stmt = conn.getConnection().createStatement();

			// Get pid and quantity from order table
			sql = "select pid, quantity from "
					+ ordersTable + " where oid = " + orderID;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				pid = rs.getInt("pid");
				qtyOrdered = rs.getInt("quantity");
			}

			// Delete an order based on the OID
			sql = "DELETE from " + ordersTable + " WHERE oid = " + orderID;
			stmt.executeUpdate(sql);

			// Get quantity on hand from product table
			sql = "select quantity from "
					+ productTable + " where pid = " + pid;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				qtyOnHand = rs.getInt("quantity");
			}

			// Update quantity on hand to reflect the quantity returned from cancel order.
			sql = "UPDATE " + productTable + " SET quantity = " + (qtyOnHand + qtyOrdered) + " WHERE pid = " + pid;
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}