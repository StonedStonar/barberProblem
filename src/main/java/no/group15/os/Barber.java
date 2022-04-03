package no.group15.os;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the barber class. The barber uses all the methods listed bellow.
 * @author Kenneth Johansen Misund.
 * @version 0.1
 */
public class Barber implements ObservableBarber, Runnable{

    private final String barberName;

    private State state;

    private Customer customer;

    private final Logger logger;

    private final List<BarberObserver> barberObserverList;


    /**
     * Makes an instance of the Barber class.
     * @param name the name of this barber.
     * @param state the state of this barber.
     */
    public Barber(String name, State state) {
        this.logger = Logger.getLogger(getClass().getName());
        checkString(name, "salon name");
        this.barberName = name;
        logger.setLevel(Level.ALL);

        checkIfObjectIsNull(state, "state");
        this.state = state;
        this.barberObserverList = new ArrayList<>();
    }

    public static void setConsole(){
        Logger logger = Logger.getLogger(Barber.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    /**
     * Gets the name of the barber.
     * @return the name of the barber.
     */
    public String getBarberName() {
        return barberName;
    }

    @Override
    public synchronized void run(){
            while (state != State.FREEDOM){
                try {
                    switch (state) {
                        case LOOKINGFORWORK -> alertObservers();
                        case WORKING -> cutHair();
                        default -> {
                            logger.fine(barberName + " sees no customers and falls asleep.");
                            wait();
                            logger.fine(barberName + " wakes up after hearing the sound of a door opening");
                            this.state = State.LOOKINGFORWORK;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.fine(barberName + " goes home for the night.");
    }


    /**
     * Makes sure that the customers have a delay before next customer get a cut. Delay set to 10 seconds.
     */
    public void cutHair() {
        if (customer != null){
            try {
                logger.fine("Cutting the hair of " + customer.getCustomerName() + ".");
                Thread.sleep(1000);
                this.state = State.LOOKINGFORWORK;
                this.customer = null;
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Makes the barber go home for the night.
     */
    public synchronized void goHome(){
        this.state = State.FREEDOM;
    }

    /**
     * Gets the state of the barber.
     * @return the state of this barber
     */
    public State getState(){
        return state;
    }

    /**
     * Sets the customer of this barber.
     * @param customer the customer to cut the hair of.
     */
    public void setCustomer(Customer customer){
        checkIfObjectIsNull(customer, "customer");
        this.state = State.WORKING;
        this.customer = customer;
    }


    /**
     * Checks if a string is of a valid format or not.
     * @param stringToCheck the string you want to check.
     * @param errorPrefix   the error the exception should have if the string is invalid.
     * @throws IllegalArgumentException gets thrown if the string to check is empty or null.
     */
    private void checkString(String stringToCheck, String errorPrefix) {
        checkIfObjectIsNull(stringToCheck, errorPrefix);
        if (stringToCheck.isEmpty()) {
            throw new IllegalArgumentException("The " + errorPrefix + " cannot be empty.");
        }
    }

    /**
     * Checks if an object is null.
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }

    @Override
    public void addObserver(BarberObserver barberObserver) {
        checkIfObjectIsNull(barberObserver, "barber observer");
        barberObserverList.add(barberObserver);
    }

    @Override
    public void removeObserver(BarberObserver barberObserver) {
        checkIfObjectIsNull(barberObserver, "barber observer");
        barberObserverList.remove(barberObserver);
    }

    @Override
    public void alertObservers() {
        try {
            Thread.sleep(500);
            synchronized (Barber.class){
                for (BarberObserver obs : barberObserverList) {
                    obs.notifyObserver(this);
                }
                if (customer == null){
                    logger.warning(barberName + " got no customer.");
                }
                if (this.state == State.LOOKINGFORWORK && customer == null){
                    this.state = State.SLEEP;
                }
            }
        }catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Notifies this barber. Solves the "syncronized block" problem.
     */
    public synchronized void notifyBarber(){
        this.notify();
    }
}
