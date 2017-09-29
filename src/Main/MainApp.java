
package Main;

import java.awt.List;
import java.io.IOException;

import controllers.RootController;
import controllers.ViewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Candidate;
import model.ScoringList;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private RootController rootController;

	private ViewController viewController;

	private ObservableList<Candidate> candidatesData;
	private ScoringList scoringList;


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Nationen - Landbruksmakt");

		initRootLayout();

		populateDatabase();

		ViewController viewController = new ViewController();
		viewController.setRootLayout(rootLayout);
		rootController.setViewController(viewController);
		viewController.setMainApp(this);

		viewController.showStartMenu();
	}

	/**
	 * Initializes the root layout
	 * @param args
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
			primaryStage.setScene(scene);
			primaryStage.show();
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

	public static void main(String[] args) {
		launch(args);
	}
	
	public void setScoringList(ScoringList scoringList) {
		this.scoringList = scoringList;
	}
	
	public ScoringList getScoringList() {
		return scoringList;
	}

	// From here and down should be moved to database
	private void populateDatabase() {
		populateWithCandidates();
	}

	private void populateWithCandidates() {
		candidatesData = FXCollections.observableArrayList();

		candidatesData.add(new Candidate("Alfa Al", "apic", "description Alfa", 1));
		candidatesData.add(new Candidate("Beta Be", "bpic", "description Beta", 2));
	}
		
}
