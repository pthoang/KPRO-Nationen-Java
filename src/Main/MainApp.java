
package Main;

import java.awt.List;
import java.io.IOException;

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
	
	private ViewController viewController;
	
	private ObservableList<Candidate> candidatesData;
	private ObservableList<ScoringList> listsData;

	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Nationen - Landbruksmakt");
		
		initRootLayout();
		
		populateDatabase();
				
		ViewController viewController = new ViewController();
		viewController.setRootLayout(rootLayout);
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
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getStage() {
		return this.primaryStage;
	}
		
	public static void main(String[] args) {
		launch(args);
	}
	
	// From here and down should be moved to database
	private void populateDatabase() {
		populateWithCandidates();
		populateWithLists();
	}

	private void populateWithCandidates() {
		candidatesData = FXCollections.observableArrayList();
		
		candidatesData.add(new Candidate("Alfa Al", "apic", "description Alfa"));
		candidatesData.add(new Candidate("Beta Be", "bpic", "description Beta"));
	}
	
	private void populateWithLists() {
		// TODO
		
		listsData = FXCollections.observableArrayList();
		
		ScoringList scoringList = new ScoringList();
		scoringList.addCandidate(candidatesData.get(0), 2);
		scoringList.addCandidate(candidatesData.get(1), 1);
		
		listsData.add(scoringList);
		
	}
	
	public void addCandidate(Candidate candidate) {
		candidatesData.add(candidate);
		System.out.println("Candidate saved");
	}
	
	public ObservableList<Candidate> getCandidates() {
		return candidatesData;
	}
}
