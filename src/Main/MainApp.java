
package Main;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AmazonBucketUploader;
import model.ScoringList;
import model.Settings;
import model.DataSources;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private RootController rootController;
	private EditListController editListController;
	private AddSourcesController addSourcesController;
	private SettingsController settingsController;
	private AmazonBucketUploader bucketUploader;
	private ScoringList scoringList;
	private Settings settings;
	private DataSources ds = new DataSources();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Nationen - Maktkampen");

		initRootLayout();
		settings = new Settings();

		newList();
		showEditListView();
		
		// During testing
		scoringList.createFromNameList("resources/NameListTest.txt");
		updateView();
		
		bucketUploader = new AmazonBucketUploader(
				settings.getBucketName(),
				settings.getFolderName(),
				settings.getBucketAccessKey(),
				settings.getBucketSecretKey()
				);
		
		editListController.setBucketUploader(bucketUploader);
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
	public void showEditListView() {
		try {
			FXMLLoader loader= new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../view/EditListView.fxml"));
			GridPane editListView = loader.load();

			rootLayout.setCenter(editListView);

			editListController = loader.getController();
			editListController.setMainApp(this);
			editListController.setBucketUploader(bucketUploader);
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

			addSourcesController = loader.getController();
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

			settingsController = loader.getController();
			settingsController.setMainApp(this);

			settingsController.refreshRegisterSelectors(getDataSourcesController().getDsList());

			settingsController.setSettings(settings);

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
	 * Get the scoringList
	 * @return scoringList
	 */
	public ScoringList getScoringList() {
		return scoringList;
	}
	
	/**
	 * Sets the scoringList
	 * @param scoringList The ScoringList
	 */
	public void setScoringList(ScoringList scoringList) {
		this.scoringList = scoringList;
	}

	/**
	 * Updates the scoringListView (refresh the table)
	 */
	public void updateView() {
		editListController.fillTable();
	}

	/**
	 * Creates a new and empty list
	 */
	public void newList() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		scoringList = new ScoringList(year);
	}
	
	public File chooseAndGetFile() {
		FileChooser fileChooser = new FileChooser();
		return fileChooser.showOpenDialog(primaryStage);
	}

	public DataSources getDataSourcesController() {
		return ds;
	}

	public void updateAmazonBucketUploader() {
		bucketUploader.setBucketName(settings.getBucketName());
		bucketUploader.setFolderName(settings.getFolderName());
		bucketUploader.setKeys(settings.getBucketAccessKey(), settings.getBucketSecretKey());
	}

	public Settings getSettings() {
		return settings;
	}

}
