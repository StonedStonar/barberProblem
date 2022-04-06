package no.group15.os;

/**
 * Represents an object that wants update on a barber. Using the designpattern "observer"
 * @author Group 13
 * @version 0.1
 */
public interface BarberObserver {

    /**
     * Notifies the observer that the barber is done with their task.
     * @param barber the customer that is done.
     */
    void notifyObserver(Barber barber);
}
