package controllers;

import api.ClusteringException;
import api.DataSet;
import clustering.CentroidClustering;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;



public class MainController {
	
    @FXML
    private TableView Dane;
    @FXML
    private TableView DanePrzetworzone;
    @FXML
    private Button WykonajAnalize;
    @FXML
    private ComboBox WyborAlgorytmu;

    
    private String[] algorytmy = {"K-MEANS","drugi","trzeci"};

    
	public void initialize() {
		setUpAllTexts();
	}
	
	public void btnActionHandler(ActionEvent event){
		String[] options = {"1","2"};
		DataSet dt = new DataSet();
		CentroidClustering CC = new CentroidClustering();
		try {
			CC.setOptions(options);
			dt = loadData();
			Dane.getItems();
			CC.submit(dt);
			CC.retrieve(false);
			
		} catch (ClusteringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	  }
	
	
    private void setUpAllTexts() {	
    	WyborAlgorytmu.getItems().setAll(algorytmy); 
    }
    
    private DataSet loadData(){
        DataSet ds = new DataSet();

        String [] headers = {"RecordId", "CategoryId", "x", "y"};
        ds.setHeader(headers);

        int data_len = 10;
        String [][] data = new String [data_len][headers.length];
        for(int i = 0; i < data_len; ++i){
            data[i][0] = String.valueOf(i);
            data[i][1] = "0";
            for(int j = 0; j < headers.length - 2; ++j){
                data[i][j + 2] = String.valueOf((int)(Math.random() * 10));
            }
        }
        ds.setData(data);

        return ds;
    }
}

