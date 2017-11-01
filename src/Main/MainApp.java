
package Main;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import controllers.*;
import interfaces.DataSourceInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AmazonBucketUploader;
import model.Candidate;
import model.ScoringList;
import model.Settings;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private RootController rootController;
	private ScoringListController scoringListController;
	private AddSourcesController addSourcesController;
	private SettingsController settingsController;
	private ScoringList scoringList;
	private Settings settings;
	private DataSources ds = new DataSources();
	private AmazonBucketUploader bucketUploader;
	

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

		showScoringListView();
		
		// During testing
		scoringList.createFromNameList("resources/NameListTest.txt");
		updateView();
		
		bucketUploader = new AmazonBucketUploader(
				settings.getBucketName(),
				settings.getFolderName(),
				settings.getBucketAccessKey(),
				settings.getBucketSecretKey()
				);
		
		scoringListController.setBucketUploader(bucketUploader);
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

			rootLayout.setCenter(scoringListView);

			scoringListController = loader.getController();
			scoringListController.setMainApp(this);
			scoringListController.setBucketUploader(bucketUploader);
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
			GridPane addSourcesView = (GridPane) loader.load();

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
			loader.setLocation(MainApp.class.getResource("../view/SettingsView.fxml"));
			GridPane settingsView = (GridPane) loader.load();

			rootLayout.setCenter(settingsView);

			settingsController = loader.getController();
			settingsController.setMainApp(this);
			settingsController.refreshRegisterSelectors(getDataSourcesController().getDsList());
			System.out.println("Setting settings in showSettingsView: " + settings);
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
		scoringListController.fillTable();
	}

	/**
	 * Creates a new and empty list
	 */
	public void newList() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		scoringList = new ScoringList(year);
	}
	
	public File choseFileAndGetFile() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(primaryStage);
		return file;
	}
	
	public void setNumCandidates(int numCandidates) {
		scoringList.setMaxLength(numCandidates);	
	}

	public DataSources getDataSourcesController() {
		return ds;
	}

	public void updateAmazonBucketUploader() {
		bucketUploader.setBucketName(settings.getBucketName());
		bucketUploader.setFolderName(settings.getFolderName());
		bucketUploader.setKeys(settings.getBucketAccessKey(), settings.getBucketSecretKey());
	}

	public void generateAll() {

		for (DataSourceInterface datasource : ds.getDsList()) {
			datasource.getData(scoringList.getCandidates());
		}




	}

}
