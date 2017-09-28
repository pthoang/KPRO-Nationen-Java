package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Main.MainApp;

public class Database {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/Nationen-Landbruksmakt?useSSL=false";

	// Database credentials
	static final String USER = "admin";
	static final String PASS = "password"; 

	private final String dbName = "Landbruksmakt"; 
	private MainApp mainApp;

	/**
	 * Create the database object
	 */
	public Database() {

		try {
			try {
				// Register JDBC driver in program
				Class.forName(JDBC_DRIVER);
			} catch (ClassNotFoundException e) {
				System.out.println("ERROR: can't find MYSQL JDBC Driver");
				e.printStackTrace();
				return;
			}

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String query = "CREATE DATABASE IF NOT EXIST " + dbName + ";";
			pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
		} catch(SQLException se) {
			// Handle errors for DriverManager.getConnection() (JDBC)
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (this.pstmt != null) {
					this.pstmt.close();
				} 
			} catch (SQLException se) {
				se.printStackTrace();
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	/**
	 * Set the mainApp
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

}
