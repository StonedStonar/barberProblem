package no.group15.os;

/**
 * Represents an object that can report to observers about changes.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface ObservableBarber {

    /**
     * Adds a new object as observer.
     * @param barberObserver the observer to add.
     */
    void addObserver(BarberObserver barberObserver);

    /**
     * Removes a object as observer.
     * @param barberObserver the observer to remove.
     */
    void removeObserver(BarberObserver barberObserver);

    /**
     * Alerts the observers about a change.
     * @param customer the customer that is done in the shop.
     */
    void alertObservers(Customer customer);
}
