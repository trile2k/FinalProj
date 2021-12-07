/*
 File Name: ProductModel.java
  
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

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

import dao.DBConnect;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

// Class to build table view for view products, view all orders, and 
// view order history 
public class ProductModel extends DBConnect {

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	private String productTable = "tle_products"; // Products table
	private String ordersTable = "tle_orders";    // Orders table

	public ProductModel() {
		conn = new DBConnect();
	}

	// Create table view and data objects
	@SuppressWarnings("rawtypes")
	private ObservableList<ObservableList> data;
	@SuppressWarnings("rawtypes")
	private TableView tableview = new TableView();

	// Build product data to be displayed.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void buildData(String SQL) {

		DBConnect conn = new DBConnect();
		data = FXCollections.observableArrayList();

		try {
			// Select all attributes based on SQL parameter for table view
			ResultSet rs = conn.getConnection().createStatement().executeQuery(SQL);

			// Add table columns dynamically
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

				// Using non property style for making dynamic table
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
				// Debugging: System.out.println("col = " + rs.getMetaData().getColumnName(i + 1));

				// Build an ObservableList for column headings
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {

							@Override
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});
				// Add each column name to tableview object
				tableview.getColumns().add(col);

				// Debugging: Display column names to console as they are added to table dynamically
				// System.out.println("Column [" + i + "] added [" + rs.getMetaData().getColumnName(i + 1) + "]");
			}

			// Data added to ObservableList dynamically
			// Track a row index to display to console added rows to table
			int ridx = 0;
			while (rs.next()) {
				// Iterate each row
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					// Iterate the column and add its data
					row.add(rs.getString(i));
				}
				// Debugging: System.out.println("Row [" + (ridx++) + "] added " + row);
				data.add(row);
			}
			// Automatically adjust width of columns depending on their content
			tableview.setColumnResizePolicy((param) -> true);
			Platform.runLater(() -> customResize(tableview));

			// Add data to tableview object
			tableview.setItems(data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}
	}

	// Adjust column width based on its content
	public void customResize(TableView<?> view) {

		AtomicLong width = new AtomicLong();
		view.getColumns().forEach(col -> {
			width.addAndGet((long) col.getWidth());
		});
		double tableWidth = view.getWidth();

		if (tableWidth > width.get()) {
			view.getColumns().forEach(col -> {
				col.setPrefWidth(col.getWidth() + ((tableWidth - width.get()) / view.getColumns().size()));
			});
		}
	}

	/**
	 * @return the tableview
	 */
	public TableView getTableview() {
		return tableview;
	}

	/**
	 * @param tableview the tableview to set
	 */
	public void setTableview(TableView tableview) {
		this.tableview = tableview;
	}

}
