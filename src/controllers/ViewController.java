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
			loader.setLocation(MainApp.class.getResource("../view/PreviousListsView.fxml"));
			AnchorPane listView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(listView);
			
			ScoringListsController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for adding candidates to new list.
	 */
	public void showAddCandidatesToListView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/AddCandidateToListView.fxml"));
			AnchorPane addCandidatesToList = (AnchorPane) loader.load();
		
			rootLayout.setCenter(addCandidatesToList);
			
			AddCandidatesToListController controller = loader.getController();
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
	 * Shows the view for creating a new candidate.
	 */
	public void showNewCandidateView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/NewCandidateView.fxml"));
			AnchorPane newCandidateView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(newCandidateView);
			
			NewCandidateController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for editing a candidate.
	 */
	public void showEditCandidateView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/EditCandidateView.fxml"));
			AnchorPane editCandidateView = (AnchorPane) loader.load();
		
			rootLayout.setCenter(editCandidateView);
			
			AddDatabaseController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
