package bilboards;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejs, który powinna implementowaæ aplikacja pe³ni¹ca rolê menad¿era
 * tablic (nazwijmy j¹ Menad¿er). Menad¿er powinien wyœwietlaæ wszystkie
 * dowi¹zane do niego tablice oraz ich bie¿¹cy stan. Tablice dowi¹zuj¹ siê do i
 * odwi¹zuj¹ od menad¿era wywo³uj¹c jego metody bindBillboard oraz
 * unbindBillboard. Z menad¿erem mo¿e po³¹czyæ siê Klient przekazuj¹c mu
 * zamówienie wyœwietlania danego og³oszenia przez zadany czas. Robi to
 * wywo³uj¹c metodê placeOrder. Menad¿er, jeœli przyjmie zamówienie, zwraca
 * Klientowi numer zamówienia wykorzystuj¹c przy tym przekazan¹ w zamówieniu
 * namiastke. Klient rozpoznaje, czy przyjêto zamówienie, sprawdzaj¹c wynik
 * zwracany z metody placeOrder.
 * Zamówienia natychmiast po przyjêciu trafiaj¹ na dowi¹zane Tablice mog¹ce w danej chwili przyj¹æ og³oszenie do wyœwietlania.
 * Jeœli w danej chwili nie ma ¿adnej wolnej Tablicy zamówienie nie powinno byæ przyjête do realizacji.
 * Aby przekonaæ siê o stanie tablic Menad¿er wywo³uje metody ich namiastek getCapacity.
 * Wystarczy, ¿e istnieje jedna wolna tablica by przyj¹æ zamówienie.
 * Na ile tablic trafi dane zamówienie decyduje dostêpnoœæ wolnych miejsc w chwili zamówienia.  
 * 
 * Uwaga: Menad¿er powinien utworzyæ lub po³¹czyæ siê z rejestrem rmi o
 * wskazanym numerze portu. Zak³adamy, ¿e rejestr rmi dzia³a na tym samym
 * komputerze, co Menad¿er (mo¿e byæ czêœci¹ aplikacji Menad¿era).
 * Menad¿er rejestruje w rejestrze rmi posiadan¹
 * namiastke IManager pod zadan¹ nazw¹ (nazwa ta nie mo¿e byæ na twardo
 * zakodowanym ci¹giem znaków). Nazwa namiastki menad¿era, host i port na którym
 * dzia³a rejest rmi powinny byæ dostarczone Klientowi (jako parmetry) oraz Tablicom.
 * 
 * @author tkubik
 *
 */
public interface IManager extends Remote { // port, nazwa, GUI

	/**
	 * Metoda dowi¹zywania namiastki Tablicy do Menad¿era (wywo³ywana przez Tablicê)
	 * 
	 * @param billboard - dowi¹zywana namiastka
	 * @return - zwraca numer przyznany namiastce w Menad¿erze
	 * @throws RemoteException
	 */
    public int bindBillboard(IBillboard billboard) throws RemoteException;


	/**
	 * Metoda odwi¹zuj¹ca namiastkê Tablicy z Menad¿era (wywo³ywana przez Tablicê)
	 * @param billboardId - numer odwi¹zywanej namiastki
	 * @return
	 * @throws RemoteException
	 */
    public boolean unbindBillboard(int billboardId) throws RemoteException;

	/**
	 * Metoda s³u¿¹ca do sk³adania zamówienia wyœwietlania og³oszenia (wywo³ywana przez Klienta)
	 * @param order - szczegó³y zamówienia (wraz z tekstem og³oszenia, czasem jego wyœwietlania i namiastk¹ klienta)
	 * @return - zwraca true jeœli przyjêto zamówienie oraz false w przeciwnym wypadku
	 * @throws RemoteException
	 */
    public boolean placeOrder(Order order) throws RemoteException;

	/**
	 * Metoda s³u¿¹ca do wycofywania zamówienia (wywo³ywana przez Klienta)
	 * @param orderId - numer wycofywanego zamówienia 
	 * @return - zwraca true jeœli wycofano zamówienie oraz false w przeciwnym wypadku
	 * @throws RemoteException
	 */
    public boolean withdrawOrder(int orderId) throws RemoteException;
}