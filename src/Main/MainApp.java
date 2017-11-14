
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
import model.Utility;

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
		


		// TODO: Only during testing
		/*
		File = Utility.getResourcesAsFile("/resources/NameListTest.txt");
		File file = Utility.convert
		scoringList.createFromNameList();

		ScoringListController.getOrCreateInstance().fillTable();
		Candidate firstCandidate = scoringList.getCandidates().get(0);
		CandidateController.getOrCreateInstance().setCandidate(firstCandidate);
		*/
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
	 * Shows the view for about.
	 */
	public void showAboutView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/AboutView.fxml"));
			GridPane aboutView = loader.load();

			rootLayout.setCenter(aboutView);

			AboutController aboutController = loader.getController();

			saveState();
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
	 * Shows the view for about.
	 */
	public void showUserManualView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/view/UserManual.fxml"));
			GridPane userManualView = loader.load();
			rootLayout.setCenter(userManualView);

			UserManualController userManualController = loader.getController();

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
	}

	// Called when closing the program
	private static void deleteImageFolder() {
        File imagesFolder = new File("images/");
        File[] files = imagesFolder.listFiles();
        if (files != null) {
            for (File f: files) {
                f.delete();
            }
        }

        System.out.println("Delete folder with images");
    }

    private void saveState() {
		scoringList = ScoringList.getOrCreateInstance();
		candidate = CandidateController.getOrCreateInstance().getCandidate();
		stateSaved = true;
	}

}
