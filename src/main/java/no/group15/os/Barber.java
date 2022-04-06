package no.group15.os;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the barber class. The barber uses all the methods listed bellow.
 * @author Group 13
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

    /**
     * Used to make the logger messages in the console visible.
     */
    public static void setConsole(){
        Logger logger = Logger.getLogger(Barber.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    @Override
    public synchronized void run() {
        try {
            while (checkIfBarberShouldStop()){
                switch (state) {
                    case LOOKINGFORWORK -> alertObservers();
                    case WORKING -> cutHair();
                    default -> {
                        logger.log(Level.FINE, "{0} sees no customers and falls asleep.", barberName);
                        wait();
                        if (state != State.FREEDOM) {
                            this.state = State.LOOKINGFORWORK;
                            logger.log(Level.FINE, "{0} wakes up after hearing the sound of a door opening", barberName);
                        } else {
                            logger.log(Level.FINE, "{0} wakes up and looks at the clock.", barberName);
                        }
                    }
                }
            }
            if (state == State.FREEDOM){
                logger.log(Level.FINE, "{0} goes home for the night.", barberName);
                barberObserverList.clear();
            }
        }catch (InterruptedException exception){
            logger.log(Level.SEVERE, "{0} has stopped since the thread as interrupted.", barberName);
        }
    }

    /**
     * Gets the name of the barber.
     * @return the name of the barber.
     */
    public String getBarberName() {
        return barberName;
    }

    /**
     * Gets if this barber has a customer right now.
     * @return <code>true</code> if the barber has a customer.
     *         <code>false</code> if the barber does not have a customer.
     */
    public synchronized boolean hasCustomer(){
        return customer != null;
    }

    /**
     * Checks if the barber should stop.
     * @return <code>true</code> if the barber should continue working.
     *         <code>false</code> if the barber should stop working.
     */
    public boolean checkIfBarberShouldStop(){
        return state != State.FREEDOM && !Thread.currentThread().isInterrupted();
    }


    /**
     * Makes sure that the customers have a delay before next customer get a cut. Delay set to 10 seconds.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     */
    public void cutHair() throws InterruptedException {
        if (customer != null){
            logger.log(Level.FINE, "Cutting the hair of {0}." , customer.getCustomerName());
            Thread.sleep(1000);
            this.state = State.LOOKINGFORWORK;
            this.customer.setState(CustomerState.NORMAL);
            customer.notifyCustomer();
            this.customer = null;
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
    public void alertObservers() throws InterruptedException {
        Thread.sleep(500);
        for (BarberObserver obs : barberObserverList) {
            obs.notifyObserver(this);
        }
        if (this.state == State.LOOKINGFORWORK && customer == null){
            this.state = State.SLEEP;
        }
    }

    /**
     * Notifies this barber. Solves the "syncronized block" problem.
     */
    public synchronized void notifyBarber(){
        this.notify();
    }
}
