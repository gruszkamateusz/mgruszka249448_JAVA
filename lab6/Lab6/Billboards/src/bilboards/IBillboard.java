package bilboards;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Duration;

/**
 * Interfejs, który powinna implementowaæ aplikacja pe³ni¹ca rolê tablicy og³oszeniowej (nazwijmy j¹ Tablica).
 * Aplikacja ta powinna wyœwietlaæ cyklicznie teksty og³oszeñ dostarczane metod¹ addAdvertisement.
 * Za wyœwietlanie tych og³oszeñ powinien odpowiadaæ osobny w¹tek.
 * W¹tek powinien mieæ dostêp do bufora na og³oszenia, którego pojemnoœæ i liczbê wolnych miejsc
 * zwraca metoda getCapacity.
 * Za dodawanie og³oszenia do bufora odpowiada metoda addAdvertisment. 
 * Z chwil¹ pierwszego wyœwietlenia og³oszenia na tablicy powinien zacz¹æ zliczaæ siê czas jego wyœwietlania.
 * Usuwanie og³oszenia mo¿e nast¹piæ z dwóch powodów: i) og³oszenie mo¿e zostaæ usuniête na skutek
 * wywo³ania metody removeAdvertisement (przez Menad¿era); ii) og³oszenie mo¿e zostaæ usuniête, gdy skoñczy siê przyznany 
 * mu czas wyœwietlania na tablicy (przez w¹tek odpowiedzialny w Tablicy za cykliczne wyœwietlanie testów).
 * Usuwanie og³oszeñ z bufora i ich dodawanie do bufora wi¹¿e siê z odpowiednim zarz¹dzaniem
 * podleg³¹ struktur¹ danych
 * W "buforze" mog¹ siê robiæ "dziury", bo og³oszenia mog¹ mieæ przyznany ró¿ny czas wyœwietlania. 
 * Nale¿y wiêc wybraæ odpowiedni¹ strukturê danych, która pozwoli zapomnieæ o nieregularnym wystêpowaniu tych "dziur").
 * Metoda start powinna daæ sygna³ aplikacji, ¿e nale¿y rozpocz¹æ cykliczne wyœwietlanie og³oszeñ.
 * Metoda stop zatrzymuje wyœwietlanie og³oszeñ.
 * Metody start i stop mo¿na odpalaæ naprzemiennie, przy czym nie powinno to resetowaæ zliczonych czasów wyœwietlania 
 * poszczególnych og³oszeñ.
 * Uwaga: Tablica powininna byæ sparametryzowany numerem portu i hostem rejestru
 * rmi, w którym zarejestrowano namiastkê Menad¿era, oraz nazwa, pod któr¹
 * zarejestrowano tê namiastkê.
 * Jest to potrzebne, by Tablica mog³a dowi¹zaæ siê do Menad¿era. 
 * Tablica robi to wywo³uj¹c metodê bindBillboard (przekazuj¹c jej swoj¹ namiastkê typu IBillboard).
 * Otrzymuje przy tym identyfikator, który mo¿e u¿yæ, by siê mog³a wypisaæ z Menad¿era 
 * (co mo¿e staæ siê podczas zamykania tablicy).
 * 
 * @author tkubik
 *
 */
public interface IBillboard extends Remote {
	/**
	 * Metoda dodaj¹ca tekst og³oszenia do tablicy og³oszeniowej (wywo³ywana przez
	 * Menad¿era po przyjêciu zamówienia od Klienta)
	 * 
	 * @param advertTextn   - tekst og³oszenia, jakie ma pojawiæ siê na tablicy
	 *                      og³oszeniowej
	 * @param displayPeriod - czas wyœwietlania og³oszenia liczony od pierwszego
	 *                      jego ukazania siê na tablicy og³oszeniowej
	 * @param orderId       - numer og³oszenia pod je zarejestrowano w menad¿erze
	 *                      tablic og³oszeniowych
	 * @return - zwraca true, jeœli uda³o siê dodaæ og³oszenie lub false w
	 *         przeciwnym wypadku (gdy tablica jest pe³na)
	 * @throws RemoteException
	 */
    public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException;

	/**
	 * Metoda usuwaj¹ca og³oszenie z tablicy (wywo³ywana przez Menad¿era po
	 * wycofaniu zamówienia przez Klienta)
	 * 
	 * @param orderId - numer og³oszenia pod jakim je zarejestrowano w menad¿erze
	 *                tablic og³oszeniowych
	 * @return - zwraca true, jeœli operacja siê powiod³a lub false w przeciwnym
	 *         wypadku (gdy nie ma og³oszenia o podanym numerze)
	 * @throws RemoteException
	 */
    public boolean removeAdvertisement(int orderId) throws RemoteException;

	/**
	 * Metoda pobieraj¹ca informacjê o zajêtoœci tablicy (wywo³ywana przez
	 * Menad¿era)
	 * 
	 * @return - zwraca tablicê dwóch liczb ca³kowitych - pierwsza to pojemnoœæ
	 *         bufora tablicy, druga to liczba wolnych w nim miejsc
	 * @throws RemoteException
	 */
    public int[] getCapacity() throws RemoteException;

	/**
	 * Metoda pobieraj¹ca informacjê o zajêtoœci tablicy (wywo³ywana przez
	 * Menad¿era)
	 * 
	 * @return - zwraca tablicê dwóch liczb ca³kowitych - pierwsza to pojemnoœæ
	 *         bufora tablicy, druga to liczba wolnych w nim miejsc
	 * @throws RemoteException
	 */
    public void setDisplayInterval(Duration displayInterval) throws RemoteException;

	/**
	 * Metoda startuj¹ca cykliczne wyœwietlanie og³oszeñ (wywo³ywana przez
	 * Menad¿era)
	 * 
	 * @return - zwraca true, jeœli zostanie poprawnie wykonana lub false w
	 *         przeciwnym wypadku
	 * @throws RemoteException
	 */
    public boolean start() throws RemoteException;


	/**
	 * Metoda stopuj¹ca cykliczne wyœwietlanie og³oszeñ (wywo³ywana przez Menad¿era)
	 * 
	 * @return - zwraca true, jeœli zostanie poprawnie wykonana lub false w
	 *         przeciwnym wypadku
	 * @throws RemoteException
	 */
    public boolean stop() throws RemoteException;
}