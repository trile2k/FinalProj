/*
 File Name: AdminController.java
  
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
import models.AdminModel;
import models.ProductModel;

// Class to interpret the admin's input to view products, view all orders, add a
// product, update product price, delete a product and log out.
public class AdminController {

	@FXML
	private Pane paneUpdatePricing; // Pane to update price
	@FXML
	private Pane paneDeleteProduct; // Pane to delete a product
	@FXML
	private Pane paneAddProduct;    // Pane to add a product
	@FXML
	private TextField txtProdName;  // Product name field
	@FXML
	private TextField txtProdDesc;  // Product description field
	@FXML
	private TextField txtProdQty;   // Product quantity field
	@FXML
	private TextField txtProdPrice; // Product price field
	@FXML
	private TextField txtDeleteProduct; // Delete a product using PID
	@FXML
	private TextField txtPID;           // PID field to update price
	@FXML
	private TextField txtUpdatedPrice;  // Price field for updating
	@FXML
	private Label lblAddStatus;         // Add product status label
	@FXML
	private Label lblDeleteStatus;      // Delete product status label
	@FXML
	private Label lblProdUpdatePrice;   // Product update status label

	private ProductModel products; // Products Model for tableview
	private AdminModel adminModel;

	private String productTable = "tle_products"; // Products table
	private String ordersTable = "tle_orders";    // Orders table

	public AdminController() {
		adminModel = new AdminModel();
	}

	// View all product records
	public void viewProducts() {

		// Turn off Add, Update and Delete product panes since
		// they don't apply in this view.
		paneUpdatePricing.setVisible(false);
		paneAddProduct.setVisible(false);
		paneDeleteProduct.setVisible(false);

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
	
	// View all product records
	public void viewAllOrders() {

		// Turn off Add, Update and Delete product panes since
		// they don't apply in this view.
		paneUpdatePricing.setVisible(false);
		paneAddProduct.setVisible(false);
		paneDeleteProduct.setVisible(false);

		System.out.println("Preparing to build and display for all Orders...");
		String SQL = "SELECT oid, uid, pid, product_name, product_desc, price, quantity, purchase_date from " 
				+ ordersTable;
		products = new ProductModel(); // Products for tableview
		products.buildData(SQL); // Build products for tableview

		// Create a new scene to display the product table
		Scene scene = new Scene(products.getTableview(), 600, 400);
		Stage stageViewProduct = new Stage();
		stageViewProduct.setTitle("View All Orders");
		stageViewProduct.setScene(scene);
		stageViewProduct.show();
		System.out.println("All Orders displayed...");
	}

	// Update product pricing so turn its pane on.
	public void updateProductPrice() {

		paneUpdatePricing.setVisible(true);
		paneAddProduct.setVisible(false);
		paneDeleteProduct.setVisible(false);
	}

	// Delete a product so turn its pane on.
	public void deleteProduct() {

		paneDeleteProduct.setVisible(true);
		paneUpdatePricing.setVisible(false);
		paneAddProduct.setVisible(false);
	}

	// Add a product so turn its pane on.
	public void addProductRec() {

		paneAddProduct.setVisible(true);
		paneUpdatePricing.setVisible(false);
		paneDeleteProduct.setVisible(false);
	}

	// Clear the product merchandise fields
	public void clearProduct() {
		txtProdName.setText("");
		txtProdDesc.setText("");
		txtProdPrice.setText("");
		txtProdQty.setText("");
	}

	// Add (insert) a product record
	public void submitProduct() {

		System.out.println("Inserting a product record into the product table...");
		adminModel.addProduct(txtProdName.getText(), txtProdDesc.getText(),
				txtProdPrice.getText(), txtProdQty.getText());

		lblAddStatus.setText("Merchandise product '" + txtProdName.getText() + "' inserted.");
		System.out.println("Product record inserted into the product table...");

		// Clear the various product fields for the next product add
		txtProdName.setText("");
		txtProdDesc.setText("");
		txtProdPrice.setText("");
		txtProdQty.setText("");
	}

	// Clear the PID field to delete a product
	public void clearDeleteProduct() {

		txtDeleteProduct.setText("");
	}

	// Delete a product based on the PID
	public void submitDeleteProduct() {

		String productID = this.txtDeleteProduct.getText();
		if (productID == null || productID.trim().equals("")) {
			lblDeleteStatus.setText("Product ID (PID) cannot be empty!");
			return;
		}

		System.out.println("Deleting a product record");
		adminModel.deleteProduct(productID);
		lblDeleteStatus.setText("Merchandise product ID '" + productID + "' deleted.");
		txtDeleteProduct.setText("");
		System.out.println("Product ID '" + productID + "' deleted.");
	}

	// Clear the PID and price fields as a part of Price Update
	public void clearUpdatedPrice() {

		txtPID.setText("");
		txtUpdatedPrice.setText("");
	}

	// Update a price for the product
	public void submitPriceUpdate() {

		String PID = this.txtPID.getText();
		String updatedPrice = this.txtUpdatedPrice.getText();

		// Ensure the PID and Price fields are not empty.
		if (PID == null || PID.trim().equals("") && (updatedPrice == null || updatedPrice.trim().equals(""))) {
			lblProdUpdatePrice.setText("Product ID & Price cannot be empty!");
			return;
		}
		if (PID == null || PID.trim().equals("")) {
			lblProdUpdatePrice.setText("Product ID (PID) cannot be empty!");
			return;
		}
		if (updatedPrice == null || updatedPrice.trim().equals("")) {
			lblProdUpdatePrice.setText("Price cannot be empty!");
			return;
		}

		// Update a price for the product
		System.out.println("Updating the product price...");
		adminModel.priceUpdate(PID, updatedPrice);
		lblProdUpdatePrice.setText("Product ID '" + PID + "' updated with price: $" + updatedPrice);
		txtPID.setText("");
		txtUpdatedPrice.setText("");
		System.out.println("Product price updated...");
	}

	// Logout when the logout button is pressed
	public void adminLogout() {

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

}
