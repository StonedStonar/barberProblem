package no.group15.os;

import no.group15.os.exceptions.CouldNotAddCustomerException;
import no.group15.os.exceptions.CouldNotGetCustomerException;
import no.group15.os.exceptions.CouldNotRemoveCustomerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kenneth Johansen Misund.
 * @version 0.1
 */
public class Barberer {

    List<Customer> customerList;
    private String salonName;


    /**
     * Makes an instance of the Barberer class.
     */
    public Barberer(String salon) {
        this.customerList = new ArrayList<>(5);
        checkString(salon, "salon name");
    }

    /**
     * Adds a customer to a list that could hold max 5 elements.
     * @param customer to be added.
     * @throws CouldNotAddCustomerException gets thrown if the customer could not be added.
     */
    public void addCustomer(Customer customer) throws CouldNotAddCustomerException {
    }

    /**
     * Remove a customer from the list that holds max 5 elements.
     * @param customer to be removed form list.
     * @throws CouldNotRemoveCustomerException gets thrown if the customer could not be removed.
     */
    public void removeCustomer(Customer customer) throws CouldNotRemoveCustomerException {

    }

    /**
     * Gets the next customer from the list when called.
     * @throws CouldNotGetCustomerException gets thrown when you couldn't get the next customer.
     */
    public void getNextCustomer() throws CouldNotGetCustomerException {

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
     *
     * @param object the object you want to check.
     * @param error  the error message the exception should have.
     * @throws IllegalArgumentException gets thrown if the object is null.
     */
    private void checkIfObjectIsNull(Object object, String error) {
        if (object == null) {
            throw new IllegalArgumentException("The " + error + " cannot be null.");
        }
    }
}
