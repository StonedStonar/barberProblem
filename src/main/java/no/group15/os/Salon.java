package no.group15.os;

import no.group15.os.exceptions.CouldNotAddCustomerException;
import no.group15.os.exceptions.CouldNotGetCustomerException;
import no.group15.os.exceptions.CouldNotRemoveCustomerException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a salon where people can come and get their haircut.
 * @author Steinar Hjelle Midthus && Kenneth Misund
 * @version 0.1
 */
public class Salon implements BarberObserver{

    private List<Customer> customerList;

    private List<Barber> barbers;

    private String salonName;

    private boolean closed;

    private int maxSize;

    private Logger logger;

    private ExecutorService executorService;

    public static void main(String[] args) {

        List<Barber> barbers = new ArrayList<>();
        Logger log = Logger.getLogger(Salon.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        Barber.setConsole();
        barbers.add(new Barber("Bjarne", State.LOOKINGFORWORK));
        //barbers.add(new Barber("Kjell", State.LOOKINGFORWORK));
        Salon salon = new Salon("Man's breaking back", 5, barbers);
        try {
            System.out.println("Now adding the first batch of customers. - Will have 5 customers.");
            Thread.sleep(1000);
            salon.addCustomer(new Customer("Pepe", CustomerState.NEEDSCUT));
            salon.addCustomer(new Customer("Leel", CustomerState.NEEDSCUT));
            for (int i = 0; i < 3; i++){
                salon.addCustomer(new Customer("Tom " + i, CustomerState.NEEDSCUT));
            }
            Thread.sleep(15000);
            System.out.println("Now adding the second batch of customers. - Will have 2 more customers that there is seat for.");
            Thread.sleep(1000);
            for (int i = 0; i < 7; i++){
                salon.addCustomer(new Customer("Tom " + (i + 3), CustomerState.NEEDSCUT));
            }
            Thread.sleep(15000);
            System.out.println("Now adding the third and last batch of customers. - Will only be 2 people.");
            Thread.sleep(1000);
            for (int i = 0; i < 2; i++){
                salon.addCustomer(new Customer("Tom " + (i + 10), CustomerState.NEEDSCUT));
            }
            salon.stopCustomerIntake();
        }catch (CouldNotAddCustomerException | InterruptedException exception){
            log.log(Level.SEVERE, "There is no more seats in this saloon. 2");
        }
    }

    /**
     * Makes an instance of the Salon class.
     * @param salonName the name of the salon.
     * @param maxSize the max size of the salon.
     * @param barbers a list with all the barbers.
     */
    public Salon(String salonName, int maxSize, List<Barber> barbers) {
        checkString(salonName, "salon name");
        checkIfNumberIsAboveZero(maxSize, "the max size");
        checkIfObjectIsNull(barbers, "barbers");
        this.logger = Logger.getLogger(getClass().getName());
        logger.setLevel(Level.ALL);
        this.maxSize = maxSize;
        this.customerList = new ArrayList<>(maxSize);
        this.barbers = barbers;
        this.salonName = salonName;
        this.closed = false;


        //Makes all the barbers start.
        this.executorService = Executors.newFixedThreadPool(barbers.size());
        for (Barber barber : barbers) {
            barber.addObserver(this);
            executorService.submit(barber);
        }
    }

    /**
     * Adds a customer to the salon if there is space.
     * @param customer the customer to add.
     * @throws CouldNotAddCustomerException gets thrown if the customer is already in the salon.
     */
    public synchronized void addCustomer(Customer customer) throws CouldNotAddCustomerException {
        checkIfObjectIsNull(customer, "customer");
        if (customerList.size() < 5){
            logger.log(Level.FINE, "{0} has entered the salon with the name \"{1}\" and sits down in a chair.", new String[]{customer.getCustomerName(), salonName});
            if (!this.customerList.contains(customer)){
                this.customerList.add(customer);
                notifyBarbers();
            }else {
                throw new CouldNotAddCustomerException("The customer with the name " + customer.getCustomerName());
            }
        }else{
            //Todo: Do something...
            logger.warning("There is too many customers right now..");
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
     * @throws CouldNotRemoveCustomerException if the customer that is next could not be removed.
     */
    public Customer getNextCustomer() throws CouldNotGetCustomerException, CouldNotRemoveCustomerException {
        Customer customer = null;
        if (!customerList.isEmpty()) {
            customer = customerList.get(0);
            removeCustomer(customer);
        }else {
            throw new CouldNotGetCustomerException("There is no more customers in queue.");
        }

        return customer;
    }

    /**
     * Makes it not possible to add more customers but the barbers has to be done with the ones they currently have
     * inside.
     */
    private void stopCustomerIntake(){
        this.closed = true;
    }

    /**
     * Tells every barber that the saloon is closing.
     */
    private void closeSalon(){
        try {
            Thread.sleep(2000);
        }catch (InterruptedException exception){
            exception.printStackTrace();
        }
        barbers.forEach(Barber::goHome);
        executorService.shutdown();
    }

    /**
     * Checks if the number is above zero.
     * @param number the number to check.
     * @param prefix the shorthand name for the number.
     */
    private void checkIfNumberIsAboveZero(long number, String prefix){
        if (number <= 0){
            throw new IllegalArgumentException("The " + prefix + " cannot be less or equal to zero.");
        }
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
     * Notifies the barbers about a new customer at the door or that the saloon closes.
     */
    private void notifyBarbers(){
        barbers.stream().filter(barber -> barber.getState() == State.SLEEP).forEach(Barber::notifyBarber);
        logger.fine("Notifies the barbers");
    }

    /**
     * Notifies the barbers about a new change.
     */
    private void notifyBarbersAboutClosingTheSalon(){
        barbers.forEach(Barber::notifyBarber);
    }

    @Override
    public synchronized void notifyObserver(Barber barber) {
        checkIfObjectIsNull(barber, "barber");
        try {
            Customer customer = getNextCustomer();
            barber.setCustomer(customer);
            logger.fine(barber.getBarberName() + " the barberer gets " + customer.getCustomerName());
        }catch (CouldNotGetCustomerException exception){
            logger.fine("There is no customers left.");
            if (customerList.isEmpty() && closed){
                logger.log(Level.FINE, "The \"{0}\" is closing for the night.", salonName);
                closeSalon();
            }
        }catch (CouldNotRemoveCustomerException exception){
            logger.warning("The customer could not be removed.");
        }
    }
}
