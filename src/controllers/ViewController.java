package controllers;

import java.io.IOException;

import Main.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Candidate;
import model.ScoringList;

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
			GridPane startMenuView = (GridPane) loader.load();

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
	public void showCandidatesListView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/ListCandidatesView.fxml"));
			GridPane listCandidatesView = (GridPane) loader.load();

			rootLayout.setCenter(listCandidatesView);

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
	public void showCandidateView(Candidate candidate) {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/CandidateView.fxml"));
			GridPane CandidateView = (GridPane) loader.load();

			rootLayout.setCenter(CandidateView);

			CandidateController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
			
			controller.setCandidate(candidate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows the view for the final list.
	 */
	public void showScoringListView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/ScoringListView.fxml"));
			AnchorPane scoringListView = (AnchorPane) loader.load();

			rootLayout.setCenter(scoringListView);

			ScoringListController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setViewController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
