package api;

public class Row {
	public String RecordId;
	public int CategoryId;
	public double [] data;
	
	public Row(String [] data) {
		RecordId = data[0];
		CategoryId = Integer.valueOf(data[1]);
		
		this.data = new double[data.length - 2];
		for(int i = 0; i < this.data.length; ++i){
			this.data[i] = Double.valueOf(data[i + 2]);
		}
	}
	public Row(Row r){
		RecordId = r.RecordId;
		CategoryId = r.CategoryId;
		data = new double [r.data.length];
		for(int i = 0; i < data.length; ++i){
			data[i] = r.data[i];
		}
	}
	
	public double calculateEuclideanDistance(Row r){
		double distance = 0;
		for(int i = 0; i < r.data.length; ++i){
			distance += Math.sqrt(Math.pow(data[i] - r.data[i], 2));
		}
		return distance;
	}	
	
	public String [] getDataRow(){
		String [] row = new String [data.length + 2];
		
		row[0] = RecordId;
		row[1] = String.valueOf(CategoryId);
		for(int i = 0; i < data.length; ++i) row[i + 2] = String.valueOf(data[i]);
		
		return row;
	}
	
	public static Row [] make_rows(String [][] data){
		Row [] rows = new Row [data.length];
		for(int i = 0; i < data.length; ++i){
			rows[i] = new Row(data[i]);
		}
		return rows;
	}
}
