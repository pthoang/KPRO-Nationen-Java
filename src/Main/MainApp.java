
package Main;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.amazonaws.services.s3.model.Bucket;
import controllers.*;
import interfaces.DataSourceInterface;
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

	private static MainApp instance = null;

	private Stage primaryStage;
	private BorderPane rootLayout;
	private EditListController editListController;
	private AmazonBucketUploader bucketUploader;
	private ScoringList scoringList;
	private Settings settings;

	private DataSources ds = new DataSources();

	public static MainApp getInstance() {

		return instance;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Nationen - Maktkampen");

		instance = this;

		initRootLayout();
		settings = Settings.getOrCreateInstance();

		newList();
		showEditListView();
		
		// TODO: During testing
		scoringList.createFromNameList("resources/NameListTest.txt");
		updateView();
		editListController.setCandidate(scoringList.getCandidates().get(0));

		/*
		bucketUploader = new AmazonBucketUploader(
				settings.getBucketName(),
				settings.getFolderName(),
				settings.getBucketAccessKey(),
				settings.getBucketSecretKey()
				);
		*/
		bucketUploader = AmazonBucketUploader.getOrCreateInstance();

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
			System.out.println("editListView: " + editListView);

			System.out.println("MainAPp in showEdit: " + this);
			System.out.println("ROotLayout in showEdit: " + rootLayout);
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
			//settingsController.setMainApp(this);

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

	/*
	public void updateAmazonBucketUploader() {
		bucketUploader.setBucketName(settings.getBucketName());
		bucketUploader.setFolderName(settings.getFolderName());
		bucketUploader.setKeys(settings.getBucketAccessKey(), settings.getBucketSecretKey());
	}
	*/

	public void generateAll() {
		for (DataSourceInterface datasource : ds.getDsList()) {
			datasource.getData(scoringList.getCandidates());
		}
	}

}
