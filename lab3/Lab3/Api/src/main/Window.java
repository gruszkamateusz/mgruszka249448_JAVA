package main;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTextPane;

public class Window  extends JFrame{
	private String[] countryWikiCodes = { "Niemcy", "Brazylia", "Francja", "Nigeria", "Kuba" };
	private String[] liczba = { "100000", "300000", "500000", "1000000", "2000000" };
	private JTextField textFieldOdpowiedz;
	public Window() {
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Ile miast w kraju");
		lblNewLabel.setBounds(10, 24, 115, 22);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("ma liczbe mieszkancow wieksza niz");
		lblNewLabel_1.setBounds(221, 27, 201, 17);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Odpowiedz");
		lblNewLabel_2.setBounds(10, 81, 115, 19);
		getContentPane().add(lblNewLabel_2);
		
		
		JComboBox comboBoxId = new JComboBox(countryWikiCodes);
		comboBoxId.setBounds(124, 25, 76, 21);
		getContentPane().add(comboBoxId);
		
		JComboBox comboBoxLiczba = new JComboBox(liczba);
		comboBoxLiczba.setBounds(442, 25, 76, 21);
		getContentPane().add(comboBoxLiczba);
		
		JButton btnNewButton = new JButton("Sprawdz");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String kod = nazwaNaWikiCode((String)comboBoxId.getSelectedItem());
				String liczba = (String)comboBoxLiczba.getSelectedItem();
				
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create("https://wft-geo-db.p.rapidapi.com/v1/geo/cities?countryIds="+kod+"&minPopulation="+liczba+"&types=CITY"))
						.header("x-rapidapi-key", "ee0f1dfa9dmshb94c64fcd53b738p12ad47jsn9219613b9c91")
						.header("x-rapidapi-host", "wft-geo-db.p.rapidapi.com")
						.method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
				HttpResponse<String> response;
				try {
					response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
					if(stworzOdpowiedz(response) == 1) {
						textFieldOdpowiedz.setText("Istnieje " +stworzOdpowiedz(response).toString()+" takie miasto");
					}
					else if(stworzOdpowiedz(response) != 1 && stworzOdpowiedz(response) < 5) {
						textFieldOdpowiedz.setText("Istnieja " +stworzOdpowiedz(response).toString()+" takie miasta");
					}
					else if(stworzOdpowiedz(response) >= 5 ) {
						textFieldOdpowiedz.setText("Istnieje " +stworzOdpowiedz(response).toString()+" takich miast");
					}
					
				//	textFieldOdpowiedz.setText(response.body());
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				

				
					
			}
		});
		btnNewButton.setBounds(10, 141, 85, 21);
		getContentPane().add(btnNewButton);
		
		textFieldOdpowiedz = new JTextField();
		textFieldOdpowiedz.setBounds(10, 110, 201, 19);
		getContentPane().add(textFieldOdpowiedz);
		textFieldOdpowiedz.setColumns(10);
		


	}
	private Integer stworzOdpowiedz(HttpResponse <String> response) {
		int odpowiedz = 0;
		String[] convertedResponse = response.body().split("wikiDataId");
		odpowiedz = convertedResponse.length - 1;
		return odpowiedz;		
	}
	
	private String nazwaNaWikiCode(String nazwa) {
		if(nazwa.equals("Niemcy")) {
			return "Q183";
		}
		else if (nazwa.equals("Brazylia")) {
			return "Q155";
		}
		else if (nazwa.equals("Francja")) {
			return "Q142";
		}
		else if (nazwa.equals("Nigeria")) {
			return "Q1033";
		}
		else if (nazwa.equals("Kuba")) {
			return "Q241";
		}
		return "";
	}
}
