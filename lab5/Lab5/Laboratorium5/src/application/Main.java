package application;
	
import static java.lang.Thread.sleep;

import api.ClusteringException;
import api.DataSet;
import clustering.CentroidClustering;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		Pane root = (Pane) FXMLLoader.load(getClass().getClassLoader().getResource("views//MainWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		}catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	
	public static void main(String[] args) {
		launch(args);
			try{
				CentroidClustering centroidClustering = new CentroidClustering();
				String [] options = {"2", "2"};
				centroidClustering.setOptions(options);
				
				DataSet ds = new DataSet();
				
				String [] headers = {"RecordId", "CategoryId", "x", "y"};
				ds.setHeader(headers);
				String [][] data = {{"1", "1", "2", "3"}, {"1", "1", "2", "4"}, {"1", "1", "1", "1"}};
				ds.setData(data);
				
				centroidClustering.submit(ds);
				
				while(centroidClustering.retrieve(false) == null){
					sleep(100);
				}
				DataSet dsds = centroidClustering.retrieve(true);
				
				for(int i = 0; i < dsds.getHeader().length; ++i){
					System.out.print(dsds.getHeader()[i] + " ");
				}
				System.out.println();
				
				for(int j = 0; j < dsds.getData().length; ++j){
					for(int i = 0; i < dsds.getHeader().length; ++i){
						System.out.print(dsds.getData()[j][i] + " ");
					}
					System.out.println();
				}
				
				
			}catch (ClusteringException e){
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}

}