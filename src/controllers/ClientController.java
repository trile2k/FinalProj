/*
 File Name: ClientController.java
  
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

package controllers;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ClientModel;
import models.ProductModel;

// Class to interpret the client's input to view products, view order history,
// order a product, cancel an order, and log out. 
public class ClientController {

	@FXML
	private Pane paneOrderProduct; // Pane to order product
	@FXML
	private Pane paneCancelOrder;  // Pane to delete a product
	@FXML
	private TextField txtPID;      // PID field to order a product
	@FXML
	private TextField txtQuantity; // Quantity field for ordering a product
	@FXML
	private TextField txtOID;      // OID field to cancel an order
	@FXML
	private Label lblOrderProduct; // Order product status label
	@FXML
	private Label lblCancelOrder;  // Cancel order status label

	static int userid;
	static String userLastName;
	static String userFirstName;
	private ClientModel clientModel;
	private ProductModel products; // Products Model for tableview

	private String productTable = "tle_products"; // Products table
	private String ordersTable = "tle_orders";    // Orders table

	public ClientController() {

		clientModel = new ClientModel();
	}

	// Order a product so turn its pane on.
	public void orderProduct() {

		paneOrderProduct.setVisible(true);
		paneCancelOrder.setVisible(false);
	}

	// Cancel an order so turn its pane on.
	public void cancelOrder() {

		paneOrderProduct.setVisible(false);
		paneCancelOrder.setVisible(true);
	}

	// Clear the order product fields
	public void clearOrderProduct() {
		txtPID.setText("");
		txtQuantity.setText("");

	}

	// Clear the OID field for cancel an order
	public void clearCancelOrder() {

		txtOID.setText("");
	}

	// Order a product based on the PID and quantity on hand
	public void submitOrderProduct() {

		if (clientModel.addOrder(userid, txtPID.getText(), txtQuantity.getText())) {
			System.out.println("Inserting an order record into the orders table...");
			lblOrderProduct.setText("You Ordered Product ID = " + txtPID.getText() +
					" with Quantity = " + txtQuantity.getText());
			System.out.println("Product and quantity inserted into the orders table...");
		} 
		else {
			lblOrderProduct.setText("Error: Qty Exceeded. Cannot order PID = " + txtPID.getText() +
					" with Qty = " + txtQuantity.getText());
		}

		// Clear the PID and quantity fields for the next order
		txtPID.setText("");
		txtQuantity.setText("");
	}

	// Cancel an order based on the OID
	public void submitCancelOrder() {

		// Ensure the Order ID field cannot be empty
		String orderID = this.txtOID.getText();
		if (orderID == null || orderID.trim().equals("")) {
			lblCancelOrder.setText("Order ID (OID) cannot be empty!");
			return;
		}

		System.out.println("Deleting an order...");
		clientModel.deleteOrder(orderID);
		lblCancelOrder.setText("Order ID '" + orderID + "' deleted.");
		txtOID.setText("");
		System.out.println("Order ID '" + orderID + "' deleted.");
	}

	// View order history records for a client user
	public void viewOrderHistory() {
		// Turn off Order Product and Cancel Order panes since
		// they don't apply in this view.
		paneOrderProduct.setVisible(false);
		paneCancelOrder.setVisible(false);

		System.out.println("Preparing to build and display the Orders table...");
		String SQL = "SELECT oid, pid, product_name, product_desc, price, quantity, purchase_date from "
				+ ordersTable + " where uid = " + userid;

		products = new ProductModel(); // Product orders for tableview
		products.buildData(SQL); // Build orders for tableview

		// Create a new scene to display the orders table
		Scene scene = new Scene(products.getTableview(), 600, 400);
		Stage stageViewProduct = new Stage();
		stageViewProduct.setTitle("View Order History for Client: "
				+ userFirstName + " " + userLastName);
		stageViewProduct.setScene(scene);
		stageViewProduct.show();
		System.out.println("Orders table displayed...");
	}

	// View all product records
	public void viewProducts() {

		// Turn off order product and cancel order panes since
		// they don't apply in this view.
		paneOrderProduct.setVisible(false);
		paneCancelOrder.setVisible(false);

		System.out.println("Preparing to build and display the product table...");
		String SQL = "SELECT pid, name, description, price, quantity from " + productTable;
		products = new ProductModel(); // Products for tableview
		products.buildData(SQL); // Build products for tableview

		// Create a new scene to display the product table
		Scene scene = new Scene(products.getTableview(), 600, 400);
		Stage stageViewProduct = new Stage();
		stageViewProduct.setTitle("View Products");
		stageViewProduct.setScene(scene);
		stageViewProduct.show();
		System.out.println("Product table displayed...");
	}

	// Logout when the logout button is pressed
	public void clientLogout() {

		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("Login View - Welcome to the Retail Point of Sales Application");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}

	// Set user id
	public static void setUserid(int user_id) {
		userid = user_id;
	}

	/**
	 * @return the userLastName
	 */
	public static String getUserLastName() {
		return userLastName;
	}

	/**
	 * @param userLastName the userLastName to set
	 */
	public static void setUserLastName(String userLastName) {
		ClientController.userLastName = userLastName;
	}

	/**
	 * @return the userFirstName
	 */
	public static String getUserFirstName() {
		return userFirstName;
	}

	/**
	 * @param userFirstName the userFirstName to set
	 */
	public static void setUserFirstName(String userFirstName) {
		ClientController.userFirstName = userFirstName;
	}

}

