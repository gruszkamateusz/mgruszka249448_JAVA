package bilboards;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfejs, który powinien zaimplementowaæ klient (nazwijmy tê alikacjê
 * Klient) komunikuj¹cy siê z menad¿erem tablic. Klient powinien mieæ interfejs
 * pozwalaj¹cy: i) definiowaæ zamówienia wyœwietlania og³oszenia (tekst
 * og³oszenia, czas wyœwietlania) ii) sk³adaæ zamówienia wyœwietlania og³oszenia
 * Menad¿erowi, iii) wycofywaæ z³o¿one zamówienia.
 * 
 * Przy okazji sk³adania zamówienia wyœwietlania og³oszenia Klient przekazuje
 * Menad¿erowi namiastkê IClient. Menagd¿er u¿yje tej namiastki, by zwrotnie
 * przekazaæ klientowi numer zamówienia (jeœli oczywiœcie zamówienie zostanie
 * przyjête). Ma to dzia³aæ podobnie jak ValueSetInterface z przyk³adu RMITest.
 * 
 * Numery zamówieñ i treœci og³oszeñ przyjêtych przez Menad¿era powinny byæ
 * widoczne na interfejsie Klienta. Klient powinien sam zadbaæ o usuwanie
 * wpisów, których okres wyœwietlania zakoñczy³ siê (brak synchronizacji w tym
 * wzglêdzie z menad¿erem)
 * 
 * Uwaga: Klient powinien byæ sparametryzowany numerem portu i hostem rejestru
 * rmi, w którym zarejestrowano namiastkê Menad¿era, oraz nazwa, pod któr¹
 * zarejestrowano tê namiastkê.
 * 
 * @author tkubik
 *
 */
public interface IClient extends Remote { // host, port, nazwa
	/**
	 * Metoda s³u¿¹ca do przekazania numeru przyjêtego zamówienia (wywo³ywana przez
	 * Menad¿era na namiastce klienta przekazanej w zamówieniu)
	 * 
	 * @param orderId
	 * @throws RemoteException
	 */
    public void setOrderId(int orderId) throws RemoteException;
}