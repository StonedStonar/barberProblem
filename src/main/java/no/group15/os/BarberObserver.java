package no.group15.os;

/**
 * Represents a object that wants update on a barber.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface BarberObserver {

    /**
     * Notifies the observer that the barber is done with their task.
     * @param customer the customer that is done.
     */
    void notifyObserver(Customer customer);
}
