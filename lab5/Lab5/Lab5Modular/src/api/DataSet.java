package api;

/**
 * Klasa reprezentuj¹ca zbiór danych w postaci tabelarycznej.
 * Przechowuje nag³ówek (jednowymiarowa tablica z nazwami kolumn) 
 * oraz dane (dwuwymiarowa tablica, której wiersze reprezentuj¹ wektory danych).
 * Zak³adamy, ¿e bêd¹ zawsze istnieæ przynajmniej dwie kolumny o nazwach:
 * "RecordId" - w kolumnie tej przechowywane s¹ identyfikatory rekordów danych;
 * "CategoryId" - w kolumnie tej przechowywane s¹ identyfikatory kadegorii rekordów danych (wynik analizy skupieñ).
*
 * @author tkubik
 *
 */
public class DataSet {
	private String[] header = {}; 
	private String[][] data = {{}};

	private <T> T[][] deepCopy(T[][] matrix) {
	    return java.util.Arrays.stream(matrix).map(el -> el.clone()).toArray(i -> matrix.clone());
	}
	
	public String[] getHeader() {
		return header;
	}
	public void setHeader(String[] header) {
		this.header = header.clone();
	}
	public String[][] getData() {
		return data;
	}
	public void setData(String[][] data) {
		this.data = deepCopy(data);
	}
	
		

}
