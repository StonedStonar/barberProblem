package no.group15.os;

/**
 * The Customer class holds the information for a customer.
 * @author Kenneth Johansen Misund
 * @version 0.1
 */
public class Customer {

    private CustomerState state;
    private String customerName;

    /**
     * Makes an instance of the Customer class.
     */

    public Customer(String customerName, CustomerState state) {
        checkString(customerName, "Name of customer");
        this.customerName = customerName;

        this.state = state;
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
    public void setState(CustomerState state) {
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
