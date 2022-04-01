package no.group15.os;

/**
 * Represents a object that wants update on a barber.
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public interface BarberObserver {

    /**
     * Notifies the observer that the barber is done with their task.
     * @param barber the customer that is done.
     */
    void notifyObserver(Barber barber);
}
