package no.group15.os;

import no.group15.os.exceptions.CouldNotAddCustomerException;
import no.group15.os.exceptions.CouldNotGetCustomerException;
import no.group15.os.exceptions.CouldNotRemoveCustomerException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a salon where people can come and get their haircut.
 * @author Group 13
 * @version 0.1
 */
public class Salon implements BarberObserver{

    private List<Customer> customerList;

    private List<Barber> barbers;

    private String salonName;

    private boolean closed;

    private int maxSize;

    private final Logger logger;

    private final ExecutorService executorService;

    private int time;

    /**
     * Makes an instance of the Salon class.
     * @param salonName the name of the salon.
     * @param maxSize the max size of the salon.
     * @param barbers a list with all the barbers.
     * @param time the time this salon should wait.
     * @param logging true if It's supposed to log all activities.
     */
    public Salon(String salonName, int maxSize, List<Barber> barbers, int time, boolean logging) {
        checkString(salonName, "salon name");
        checkIfNumberIsAboveZero(maxSize, "the max size");
        checkIfObjectIsNull(barbers, "barbers");
        if (time < 0){
            throw new IllegalArgumentException("The time cannot be under or equal to zero.");
        }
        this.customerList = new ArrayList<>(maxSize);
        this.barbers = barbers;
        this.salonName = salonName;
        this.closed = false;
        this.maxSize = maxSize;
        this.time = time;
        this.logger = Logger.getLogger(getClass().getName());
        if (logging){
            logger.setLevel(Level.ALL);
        }else {
            logger.setLevel(Level.OFF);
        }


        //Makes all the barbers start.
        this.executorService = Executors.newFixedThreadPool(barbers.size());
        for (Barber barber : barbers) {
            barber.addObserver(this);
            executorService.submit(barber);
        }
    }

    /**
     * Used to make the logger messages in the console visible.
     */
    public static void setConsole(){
        Logger log = Logger.getLogger(Salon.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
    }

    /**
     * Adds a customer to the salon if there is space.
     * @param customer the customer to add.
     * @throws CouldNotAddCustomerException gets thrown if the customer is already in the salon.
     */
    public synchronized void addCustomer(Customer customer) throws CouldNotAddCustomerException {
        checkIfObjectIsNull(customer, "customer");
        if (!closed){
            if (customerList.size() < maxSize){
                logger.log(Level.FINE, "{0} has entered the salon with the name \"{1}\" and sits down in a chair.", new String[]{customer.getCustomerName(), salonName});
                if (!this.customerList.contains(customer)){
                    this.customerList.add(customer);
                    notifyBarbers();
                }else {
                    throw new CouldNotAddCustomerException("The customer with the name " + customer.getCustomerName() + " could not be added.");
                }
            }else{
                logger.warning("There is too many customers right now..");
            }
        }else {
            throw new CouldNotAddCustomerException("The customer with the name " + customer.getCustomerName() + " turned in the door since the saloon is closed.");
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
    public void stopCustomerIntake(){
        this.closed = true;
        notifyBarbers();
    }

    /**
     * Checks if the store has closed and all barbers has gone home.
     * @return <code>true</code> if all the barbers has gone home.
     *         <code>false</code> if one of the barbers are still working.
     */
    public boolean isClosedAndBarbersHasGoneHome(){
        boolean valid;
        //Todo: Her har vi en while loop som sjekker det samme som en stream gjør nederst. Har ikke sjekket at den funker 100%
        // but is 99% sure.
//        Iterator<Barber> barberIterator = barbers.iterator();
//        int amountOfBarbersAtHome = 0;
//        //Spør om den har neste og går videre.
//        while (barberIterator.hasNext()){
//            //Tar ut neste barber
//            Barber barber = barberIterator.next();
//            //Sjekker staten dems og om de ikke har en kunde.
//            if (barber.getState() == State.FREEDOM && !barber.hasCustomer()){
//                amountOfBarbersAtHome++;
//            }
//        }
//        valid = barbers.size() == amountOfBarbersAtHome && closed;
        //Todo: Og her har vi streamen. En stream tar alt i en liste og har det i en "kø" hvor man kan gjøre ulike
        // operasjoner. Under sier vi at alle skal matche landautrykket som er etter "->". Hvis en av dem ikke macher
        // gir den false. Alle barbers skal være ferdige på jobb og ikke ha noen kunder før de kan gå hjem.
        // istedet for å skrive 10 linjer kan man skrive en. Samtidig må salongen også være "stengt" med "&& closed"
        valid = barbers.stream().allMatch(barber -> barber.getState() == State.FREEDOM && !barber.hasCustomer()) && closed;
        return valid;
    }

    /**
     * Tells every barber that the saloon is closing.
     */
    private void closeSalon() {
        //Todo: Gjør dette så alle tråder dør uansett og programmet kan avslutte. Om man ikke skrur den av kan
        // programmet kjøre i bakgrunnen og ta resusser.
        try {
            Thread.sleep(time);
        }catch (InterruptedException exception){
            System.out.println("FAILED TO SHUT DOWN THE SALOON");
        }
        notifyBarbersAboutClosingTheSalon();
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
     * Notifies the barbers about a new customer at the door or that the saloon closes. Notifies only the barbers who are sleeping.
     */
    private void notifyBarbers(){
        //Todo: Bruker filter til å filtrere ut de som ikke sover. Vil bare notifisere de som sover.
        barbers.stream().filter(barber -> barber.getState() == State.SLEEP).forEach(Barber::notifyBarber);
        logger.fine("Notifies the barbers");
    }

    /**
     * Notifies the barbers about a new change.
     */
    private void notifyBarbersAboutClosingTheSalon(){
        barbers.stream().filter(barber -> barber.getState() != State.FREEDOM).forEach(Barber::goHome);
        barbers.stream().filter(barber -> barber.getState() == State.SLEEP).forEach(Barber::notifyBarber);
    }

    //Todo: Bruker "synchronized" så bare et av objektene som kaller funksjonen kan bruke den av gangen. Dette er per
    // instans av denne klassen.
    @Override
    public synchronized void notifyObserver(Barber barber) {
        checkIfObjectIsNull(barber, "barber");
        try {
            Customer customer = getNextCustomer();
            barber.setCustomer(customer);
            logger.fine(barber.getBarberName() + " the barberer gets " + customer.getCustomerName());
        }catch (CouldNotGetCustomerException exception){
            logger.fine("There is no customers left.");
            if (customerList.isEmpty() && closed && barber.getState() != State.FREEDOM){
                logger.log(Level.FINE, "The \"{0}\" is closing for the night.", salonName);
                closeSalon();
            }
        }catch (CouldNotRemoveCustomerException exception){
            logger.warning("The customer could not be removed.");
        }
    }
}
