
package Main;

import java.io.File;
import java.io.IOException;

import controllers.*;
import interfaces.DataSourceInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ScoringList;
import model.DataSources;
import model.Candidate;

public class MainApp extends Application {

	private static MainApp instance = null;

	private Stage primaryStage;
	private BorderPane rootLayout;
	private EditListController editListController;
	private ScoringList scoringList;

	private DataSources ds = new DataSources();

	public static MainApp getInstance() {

		return instance;
	}

	public static void main(String[] args) {
        launch(args);

	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
                deleteImageFolder();
            }}, "Shutting down"
        ));
	}

	@Override
	public void start(Stage primaryStage) {
        instance = this;

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Nationen - Maktkampen");

		initRootLayout();
		scoringList = ScoringList.getOrCreateInstance();
		showEditListView();
		
		// TODO: Only during testing
		scoringList.createFromNameList("resources/NameListTest.txt");
		ScoringListController.getOrCreateInstance().fillTable();
		Candidate firstCandidate = scoringList.getCandidates().get(0);
		CandidateController.getOrCreateInstance().setCandidate(firstCandidate);
	}

	/**
	 * Initializes the root layout
	 */
	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/RootLayout2.fxml"));
			rootLayout = loader.load();
			System.out.println("MainAPp in initRoot: " + this);
			System.out.println("ROotLayout in initRoo: " + rootLayout);

			RootController rootController = loader.getController();
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
	public void showEditListView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/EditListView.fxml"));
			GridPane editListView = loader.load();
			rootLayout.setCenter(editListView);

			editListController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Shows the view for adding databases.
	 */
	public void showLoadSourcesView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/AddSourcesView.fxml"));
			GridPane addSourcesView = loader.load();

			rootLayout.setCenter(addSourcesView);

			AddSourcesController addSourcesController = loader.getController();
			addSourcesController.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the view for settings.
	 */
	public void showSettingsView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/SettingsView2.fxml"));
			GridPane settingsView = loader.load();
			rootLayout.setCenter(settingsView);

			SettingsController settingsController = loader.getController();

			settingsController.refreshRegisterSelectors(getDataSourcesController().getDsList());
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
	 * Creates a new and empty list
	 */
	public void newList() {
		scoringList.empty();
	}

	public DataSources getDataSourcesController() {
		return ds;
	}

    public File chooseAndGetFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(primaryStage);
    }

	public void generateAll() {
		for (DataSourceInterface datasource : ds.getDsList()) {
			datasource.getData(scoringList.getCandidates());
		}
	}

	// Called when closing the program
	private static void deleteImageFolder() {
        // TODO
        System.out.println("Delete folder with images");
    }

}
