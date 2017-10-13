
package Main;

import java.io.IOException;
import java.util.Calendar;

import controllers.RootController;
import controllers.ScoringListController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.ScoringList;

// Gives error on Linux - is it necessary?
//import static com.apple.eio.FileManager.getResource;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private RootController rootController;
	private ScoringListController scoringListController;
	
	private ScoringList scoringList;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Nationen - Maktkampen");

		initRootLayout();

		newList();
		
		showScoringListView();
	}

	/**
	 * Initializes the root layout
	 * @param
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			rootController = loader.getController();
			rootController.setMainApp(this);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(this.getClass().getResource("../style.css").toExternalForm());
			primaryStage.setScene(scene);
			String css = this.getClass().getResource("../style.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.show();
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
			GridPane scoringListView = (GridPane) loader.load();

			System.out.println("Rootlayout: " + rootLayout);
			System.out.println("ScoringListView: " + scoringListView);
			rootLayout.setCenter(scoringListView);

			scoringListController = loader.getController();
			scoringListController.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the primaryStage
	 * @return primaryStage
	 */
	public Stage getStage() {
		return this.primaryStage;
	}

	/**
	 * Sets the scoringList
	 * @param scoringList The ScoringList
	 */
	public void setScoringList(ScoringList scoringList) {
		this.scoringList = scoringList;
	}
	
	/**
	 * Get the scoringList
	 * @return scoringList
	 */
	public ScoringList getScoringList() {
		return scoringList;
	}
	
	public void updateView() {
		System.out.println("Update view: " + Integer.toString(scoringList.getLength()));
		System.out.println("ScoringListController in MainApp: " + scoringListController);
		scoringListController.fillTable();
	}
	
	public void cleanList() {
		newList();
	}
	
	public void newList() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		scoringList = new ScoringList(year);

	}
}
