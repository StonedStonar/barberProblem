package no.group15.os;

import no.group15.os.exceptions.CouldNotAddCustomerException;
import no.group15.os.exceptions.CouldNotGetCustomerException;
import no.group15.os.exceptions.CouldNotRemoveCustomerException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Salon {

    private List<Customer> customerList;

    private String salonName;

    private boolean closed;

    /**
     * Makes an instance of the Salon class.
     * @param salonName the name of the salon.
     */
    public Salon(String salonName) {
        checkString(salonName, "salon name");
        this.customerList = new LinkedList<>();
        this.salonName = salonName;
        this.closed = true;
    }

    /**
     * Adds a customer to the salon if there is space.
     * @param customer the customer to add.
     * @throws CouldNotAddCustomerException gets thrown if the customer could not be added.
     */
    public void addCustomer(Customer customer) throws CouldNotAddCustomerException {
        checkIfObjectIsNull(customer, "customer");
        if (!this.customerList.contains(customer)){
            this.customerList.add(customer);
        }else {
            throw new CouldNotAddCustomerException("The customer with the name " + customer.getCustomerName());
        }
    }

    /**
     * Removes a customer from the queue.
     * @param customer the customer to remove.
     * @throws CouldNotRemoveCustomerException gets thrown if the customer could not be removed.
     */
    public void removeCustomer(Customer customer) throws CouldNotRemoveCustomerException {
        checkIfObjectIsNull(customer, "customer");
        if (!this.customerList.remove(customer)){
            throw new CouldNotRemoveCustomerException("The customer with the name " + customer.getCustomerName() + " is not in the system");
        }
    }

    /**
     * Gets the next customer to work with.
     * @throws CouldNotGetCustomerException if we cannot get next customer.
     */
    public Customer getNextCustomer() throws IndexOutOfBoundsException {
        return customerList.get(0);
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
