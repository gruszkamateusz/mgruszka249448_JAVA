package clustering;

import api.ClusterAnalysisService;
import api.ClusteringException;
import api.DataSet;
import api.Row;

import static java.lang.Thread.sleep;
import api.ClusterAnalysisService;
import java.util.Scanner;
import java.util.ServiceLoader;



public class CentroidClustering implements api.ClusterAnalysisService{
	private DataSet dataSet;
	private int clusters;
	private int clusteringSteps;
	
	
	private SolvingThread solvingThread;
	private DataSet result;
	
	public CentroidClustering() {
		clusters = 0;
		dataSet = null;
		solvingThread = null;
	}
	
	@Override
	public void setOptions(String[] options) throws ClusteringException {
		clusters = Integer.valueOf(options[0]);
		clusteringSteps = Integer.valueOf(options[1]);
	}
	
	@Override
	public String getName() {
		return "Centroid Clustering";
	}
	
	@Override
	public void submit(DataSet ds) throws ClusteringException {
		dataSet = ds;
		
		if(ds == null) throw new NullPointerException();
		

		
		solvingThread = new SolvingThread();
		solvingThread.start();
	}
	
	@Override
	public DataSet retrieve(boolean clear) throws ClusteringException {
		
		if(clear){
			dataSet = null;
			clusters = 0;
			clusteringSteps = 0;
		}
		
		return result;
		}
	
	
	private class SolvingThread extends Thread{
		
		String [] headers;
		Row [] rows;
		Row [] centers;
		
		@Override
		public void run() {
			super.run();
			
			result = new DataSet();
			headers = dataSet.getHeader();
			rows = Row.make_rows(dataSet.getData());
			
			
			centers = new Row [clusters];
			for(int i = 0; i < centers.length; ++i){
				centers[i] = new Row(rows[i]);
				centers[i].CategoryId = i;
			}
			
			for(int i = 0; i < clusteringSteps; ++i){
				get_new_Centers();
			}
			
			result.setHeader(headers);
			String [][] data = new String [centers.length][centers[0].data.length + 2];
			for(int i = 0; i < centers.length; ++i) data[i] = centers[i].getDataRow();
			result.setData(data);
		}
		
		
		
		private void get_new_Centers(){
			for(int i = 0; i < rows.length; ++i){
				
				int cluster_index = 0;
				double cost = centers[0].calculateEuclideanDistance(rows[i]);
				
				for(int j = 1; j < centers.length; ++j){
					double new_cost = centers[j].calculateEuclideanDistance(rows[i]);
					if(new_cost < cost){
						cost = new_cost;
						cluster_index = j;
					}
				}
				
				rows[i].CategoryId = cluster_index;
				
			}
			
			double [][] data_sums = new double [centers.length][centers[0].data.length];
			int [] categories_count = new int [centers.length];
			
			for(int i = 0; i < rows.length; ++i){
				++categories_count[rows[i].CategoryId];
				for(int j = 0; j < centers[0].data.length; ++j){
					data_sums[rows[i].CategoryId][j] += rows[i].data[j];
				}
			}
			
			for(int i = 0; i < centers.length; ++i){
				for(int j = 0; j < centers[0].data.length; ++j){
					centers[i].data[j] = data_sums[i][j] / categories_count[i];
				}
			}
		}
	}
	
	private static DataSet loadData(){
        DataSet ds = new DataSet();

        String [] headers = {"Id", "Category", "x", "y"};
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
	public static void main(String[] args) {
		
		ServiceLoader<ClusterAnalysisService> loader = ServiceLoader.load(ClusterAnalysisService.class);
		
		
		try{
			CentroidClustering centroidClustering = new CentroidClustering();
			System.out.print("Podaj k: ");
			Scanner sck= new Scanner(System.in); //System.in is a standard input stream.
			String k= sck.nextLine();
			System.out.print("Podaj n: ");
			Scanner scn= new Scanner(System.in); //System.in is a standard input stream.
			System.out.println("");
			String n= scn.nextLine();
			 
			String [] options = {k, n};
			centroidClustering.setOptions(options);
			
			DataSet ds = new DataSet();
			String [] headers = {"Id", "Category", "x", "y"};
			ds.setHeader(headers);
			
			ds.setData(loadData().getData());
			
			for(int i = 0; i < ds.getHeader().length; ++i){
				System.out.print(ds.getHeader()[i] + " ");
			}
			System.out.println();
			
			for(int j = 0; j < ds.getData().length; ++j){
				for(int i = 0; i < ds.getHeader().length; ++i){
					System.out.print(ds.getData()[j][i] + " ");
				}
				System.out.println();
			}
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