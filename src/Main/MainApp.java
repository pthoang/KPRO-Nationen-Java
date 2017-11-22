
package Main;

import java.io.File;
import java.io.IOException;

import controllers.*;
import controllers.datasources.ConnectionsDb;
import interfaces.DataSourceInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import org.apache.commons.io.FileUtils;

public class MainApp extends Application {

	private static MainApp instance = null;

	private Stage primaryStage;
	private BorderPane rootLayout;
	private EditListController editListController;
	private AddJuryController addjurycontroller;
	private ScoringList scoringList;
	private Candidate candidate;


	private boolean stateSaved;

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

	public void showJuryAdmin() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/JuryAdmin.fxml"));
			GridPane JuryAdminView = (GridPane) loader.load();
			rootLayout.setCenter(JuryAdminView);
			addjurycontroller = loader.getController();

			saveState();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
        instance = this;

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Nationen - Maktkampen");

		initRootLayout();
		scoringList = ScoringList.getOrCreateInstance();
		showEditListView();

		newList();
	}

	/**
	 * Initializes the root layout
	 */
	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/RootLayout2.fxml"));
			rootLayout = loader.load();

			RootController rootController = loader.getController();
			rootController.setMainApp(this);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(this.getClass().getResource("/resources/style/style.css").toExternalForm());
			primaryStage.setScene(scene);
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
			loader.setLocation(MainApp.class.getResource("/view/EditListView.fxml"));
			GridPane editListView = loader.load();
			rootLayout.setCenter(editListView);

			editListController = loader.getController();
			if (stateSaved) {
				ScoringListController.getOrCreateInstance().loadState();
				CandidateController.getOrCreateInstance().setCandidate(candidate);
			}
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
			loader.setLocation(MainApp.class.getResource("/view/SettingsView2.fxml"));
			GridPane settingsView = loader.load();
			rootLayout.setCenter(settingsView);

			SettingsController settingsController = loader.getController();

			settingsController.refreshRegisterSelectors(getDataSourcesController().getDsList());

			saveState();
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
		ScoringListController.getOrCreateInstance().refreshTable();
		CandidateController.getOrCreateInstance().cleanFields();
	}

	public DataSources getDataSourcesController() {
		return ds;
	}

    public File chooseAndGetFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);
        return file;
    }

	public void generateAll() {

		for (DataSourceInterface datasource : ds.getDsList()) {
			datasource.getData(scoringList.getCandidates());
		}


		ConnectionsDb connectionsDb = new ConnectionsDb();
		connectionsDb.setConnections(scoringList.getCandidates());

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Informasjon");
		alert.setHeaderText("Analysen er gjennomført");
		alert.setContentText("Resultatet av analysen kan ses når filen eksporteres");
		alert.showAndWait();

	}

	// Called when closing the program
	private static void deleteImageFolder() {
        File tempFilesFolder = new File("tempFiles");
        try {
			FileUtils.cleanDirectory(tempFilesFolder);
		} catch (IOException e) {
        	System.out.println("Error: could not delete tempFiles: " + e);
		}
    }

    private void saveState() {
		scoringList = ScoringList.getOrCreateInstance();
		candidate = CandidateController.getOrCreateInstance().getCandidate();
		stateSaved = true;
	}

}
