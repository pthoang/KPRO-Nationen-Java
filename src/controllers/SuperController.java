package controllers;

import java.lang.annotation.Inherited;

import Main.MainApp;

public class SuperController {

	
	protected MainApp mainApp;
	protected ViewController viewController;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}
	
}
