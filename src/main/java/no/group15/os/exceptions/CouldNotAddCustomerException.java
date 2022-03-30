package no.group15.os.exceptions;

import java.io.Serializable;

/**
 * CouldNotAddCustomerException represents an exception that gets thrown when a Customer could not be added.
 * @author Kenneth Johansen Misund.
 * @version 0.1
 */
public class CouldNotAddCustomerException extends  Exception implements Serializable {

    /**
     * Makes an instance of the CouldNotAddCustomerException class.
     *
     * @param message the error message to display.
     */
    public CouldNotAddCustomerException(String message) {
        super(message);
    }
}
