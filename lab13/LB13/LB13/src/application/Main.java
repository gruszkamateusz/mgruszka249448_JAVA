package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Pane root = (Pane) FXMLLoader.load(getClass().getResource("window.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(Main.class.getResource("application.css").toString());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {

		launch(args);
	}

}
