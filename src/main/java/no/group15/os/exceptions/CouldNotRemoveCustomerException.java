package no.group15.os.exceptions;

import java.io.Serializable;

/**
 * CouldNotRemoveCustomerException represents an exception that gets thrown when a customer could not be removed.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
public class CouldNotRemoveCustomerException extends Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotRemoveCustomerException class.
     * @param message the error message to be displayed.
     */
    public CouldNotRemoveCustomerException(String message) {
        super(message);
    }
}
