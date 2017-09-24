package controllers;

import java.io.IOException;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ViewController {
	
	@FXML
	private BorderPane rootLayout;
	
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setRootLayout(BorderPane rootLayout) {
		this.rootLayout = rootLayout;
	}
	
	/**
	 * Show the view for start menu.
	 */
	public void showStartMenu() {
		try {
			// Load start screen
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/StartMenu.fxml"));
			AnchorPane startMenuView = (AnchorPane) loader.load();
			
			// Set startscreen in the center of root layout.
			System.out.println("rootLayout: " + rootLayout);
			System.out.println("startMenuView: " + startMenuView);
			rootLayout.setCenter(startMenuView);
			
			StartMenuController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for creating a new list.
	 */
	public void showNewListView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/AddCandidateToListView.fxml"));
			AnchorPane addCandidateToListView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(addCandidateToListView);
			
			AddCandidateController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for listing candidates.
	 */
	public void showListCandidatesView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/ListCandidatesView.fxml"));
			AnchorPane listCandidatesView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(listCandidatesView);
			
			ListCandidatesController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for listing old lists.
	 */
	public void showPreviousListsView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/ListsView.fxml"));
			AnchorPane listView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(listView);
			
			ListCandidatesController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Shows the view for adding databases.
	 */
	public void showAddDatabaseView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/AddDatabaseView.fxml"));
			AnchorPane addDatabaseView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(addDatabaseView);
			
			AddDatabaseController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for adding a candidate.
	 */
	public void showAddCandidateView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/AddCandidateView.fxml"));
			AnchorPane addCandidateView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(addCandidateView);
			
			AddDatabaseController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for changing a candidate.
	 */
	public void showChangeCandidateView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/ChangeCandidateView.fxml"));
			AnchorPane changeCandidateView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(changeCandidateView);
			
			AddDatabaseController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
