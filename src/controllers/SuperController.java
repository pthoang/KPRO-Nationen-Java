package controllers;

import Main.MainApp;

/**
 * Parent to all controllers, where the viewController and mainController is set
 * @author doraoline
 *
 */
public class SuperController {

	
	protected MainApp mainApp;
	protected ViewController viewController;
	
	/**
	 * Set the mainApp
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	/**
	 * Set the viewController
	 * @param viewController
	 */
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}
	
}
