/*
 File Name: LoginController.java
  
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.LoginModel;

// Class to interpret the user's input to check user's credentials (admin or
// client) and set up the appropriate views.
public class LoginController {

	@FXML
	private TextField txtUsername;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private Label lblError;

	private LoginModel model;
	private int loginCount = 0;
	private int maxLogin = 3;

	public LoginController() {
		model = new LoginModel();
	}

	// Exit the application when the exit button is clicked
	public void exit() {
		System.exit(0);
	}

	// Attempt to log on to the application when the submit button is clicked
	// User has 3 tries to log on before the application shuts down
	public void login() {

		loginCount++;
		if (loginCount >= maxLogin) {
			System.exit(1);
		}

		lblError.setText("");
		String username = this.txtUsername.getText();
		String password = this.txtPassword.getText();

		// Validations of login name and password
		// Check for empty login name and password
		if (username == null || username.trim().equals("") && (password == null || password.trim().equals(""))) {
			lblError.setText("Login Name & Password cannot be empty or spaces!");
			return;
		}
		// Check for empty login name
		if (username == null || username.trim().equals("")) {
			lblError.setText("Login Name cannot be empty or spaces!");
			return;
		}
		// Check for empty password
		if (password == null || password.trim().equals("")) {
			lblError.setText("Password cannot be empty or spaces!");
			return;
		}

		// Authentication check
		checkCredentials(username, password);

	}

	// Check user's credentials
	public void checkCredentials(String username, String password) {
		Boolean isValid = model.getCredentials(username, password);
		if (!isValid) {
			lblError.setText("User does not exist! Please try again.");
			return;
		}
		try {
			AnchorPane root;
			if (model.isAdmin() && isValid) {
				// If user is admin, inflate admin view
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/AdminView.fxml"));
				Main.stage.setTitle("Admin View - Welcome " + model.getFirstName() +
						" " + model.getLastName());

			} else {
				// If user is customer, inflate customer view
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/ClientView.fxml"));
				// Set user ID acquired from DB
				int user_id = model.getId();
				ClientController.setUserid(user_id);
				ClientController.setUserFirstName(model.getFirstName());
				ClientController.setUserLastName(model.getLastName());
				Main.stage.setTitle("Client View - Welcome " + model.getFirstName() +
						" " + model.getLastName());
			}

			Scene scene = new Scene(root);
			Main.stage.setScene(scene);

		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}

	}
}