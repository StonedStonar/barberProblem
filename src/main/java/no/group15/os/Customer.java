package no.group15.os;

import no.group15.os.exceptions.CouldNotAddCustomerException;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Customer class holds the information for a customer.
 * @author Group 13
 * @version 0.1
 */
public class Customer implements Runnable{

    private CustomerState state;

    private final String customerName;

    private final Salon salon;

    private Logger logger;

    /**
     * Makes an instance of the Customer class.
     */

    public Customer(String customerName, CustomerState state, Salon salon) {
        checkString(customerName, "Name of customer");
        checkIfObjectIsNull(state, "state");
        checkIfObjectIsNull(salon, "salon");
        this.customerName = customerName;

        this.state = state;
        this.salon = salon;
        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Gets the name of a customer.
     * @return Customers name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets the state of a customer.
     * @return customer state.
     */
    public CustomerState getCustomerState() {
        return state;
    }

    /**
     * Sets the customers state.
     * @param state return state.
     */
    public synchronized void setState(CustomerState state) {
        checkIfObjectIsNull(state, "state");
        this.state = state;
    }

    /**
     * Checks if a string is of a valid format or not.
     *
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

    /**
     * Used to make the logger messages in the console visible.
     */
    public static void setConsole(){
        Logger logger = Logger.getLogger(Customer.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }


    @Override
    public synchronized void run() {
        try {
            salon.addCustomer(this);
            while (state != CustomerState.NORMAL && !Thread.interrupted()){
                wait();
            }
        }catch (CouldNotAddCustomerException exception){
        }catch (InterruptedException exception){
            logger.log(Level.FINE, "The customer was interrupted while they where sleeping.");
        }
        if (Thread.interrupted()){
            logger.log(Level.WARNING, "The customer has stopped since it was interrupted.");
        }else if (state == CustomerState.NORMAL){
            logger.log(Level.FINE, "{0} leaves the salon with a new haircut.", customerName);
        }else {
            logger.log(Level.FINE, "{0} left without a haircut.", customerName);
        }
    }

    /**
     * Notifies the customer that they are done with their haircut.
     */
    public synchronized void notifyCustomer(){
        this.state = CustomerState.NORMAL;
        this.notify();
    }
}
