package com.gmail.webos21.fx.mdbviewer;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(final Stage stage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MdbScene.fxml"));
			Parent root = fxmlLoader.load();

			Scene scene = new Scene(root);

			stage.setTitle("Serial Terminal");
			stage.setScene(scene);
			stage.setMinWidth(650);
			stage.setMinHeight(450);
			stage.show();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error on Start");
			alert.setHeaderText("Cannot load the FXML!");
			alert.setContentText(e.toString());
			alert.showAndWait();

			Platform.exit();
			System.exit(0);
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.out.println("Application.stop()");
	}

	public static void main(final String[] args) {
		launch(args);
	}

}
