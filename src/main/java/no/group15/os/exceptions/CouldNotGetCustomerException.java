package no.group15.os.exceptions;

import java.io.Serializable;

/**
 * CouldNotGetCustomerException represents an exception that gets thrown when couldn't get the customer.
 * @author Group 13
 * @version 0.1
 */
public class CouldNotGetCustomerException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotGetCustomerException class.
     * @param message the error message to be displayed.
     */
    public CouldNotGetCustomerException(String message) {
        super(message);
    }
}
