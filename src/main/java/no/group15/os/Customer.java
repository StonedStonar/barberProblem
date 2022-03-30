package no.group15.os;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Customer {

    private CustomerState customerState;

    private String customerName;

    /**
     * Makes an instance of the Customer class.
     */
    public Customer(String customerName) {
        checkString(customerName, "customer name");
        this.customerName = customerName;
        this.customerState = CustomerState.NEEDSCUT;
    }

    /**
     * Gets the name of the customer.
     * @return the name of the customer.
     */
    public String getCustomerName(){
        return customerName;
    }

    /**
     * Sets the customer state to a new value.
     * @param customerState the new customer state.
     */
    public void setCustomerState(CustomerState customerState){
        checkIfObjectIsNull(customerState, "customer state");
        this.customerState = customerState;
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
