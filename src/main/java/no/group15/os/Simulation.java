package no.group15.os;

import no.group15.os.exceptions.CouldNotAddCustomerException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts the simulation.
 * @author Steinar Hjelle Midthus & Kenneth Misund
 * @version 0.1
 */
public class Simulation {

    //The amount of delay in ms for the long pause.
    private static int longTime;

    //The amount of delay in ms for the short pause.
    private static int shortTime;


    public static void main(String[] args) {
        startSimulation();
    }

    /**
     * Starts the simulation and holds all the default values.
     */
    public static void startSimulation(){
        longTime = 15000;
        shortTime = 1000;
        Logger logger = setConsole();
        List<Barber> barbers = addNAmountOfBarbers(1);
        int maxSize = 5;
        Salon salon = new Salon("Man's breaking back", maxSize, barbers);
        try {
            startPhaseOne(salon, maxSize);
            sleepLong();
            startPhaseTwo(salon, maxSize);
            sleepLong();
            startPhaseThree(salon);
            salon.stopCustomerIntake();
        }catch (CouldNotAddCustomerException | InterruptedException exception){
            logger.log(Level.SEVERE, "There is no more seats in this saloon. 2");
        }
    }

    /**
     * Adds a n amount of barbers to a list and returns it.
     * @param amountOfBarbers the amount of barbers.
     * @return the list with the barbers.
     */
    private static List<Barber> addNAmountOfBarbers(int amountOfBarbers){
        List<Barber> barbers = new ArrayList<>();
        for (int i = 0; i < amountOfBarbers; i++){
            barbers.add(new Barber("Bjarne " + i, State.LOOKINGFORWORK));
        }
        return barbers;
    }

    /**
     * Sets the console so that fine messages also show.
     * @return the logger.
     */
    private static Logger setConsole(){
        Logger log = Logger.getLogger(Salon.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        Barber.setConsole();
        return log;
    }

    /**
     * Starts the first phase that adds n customers to the salon.
     * @param salon the salon to add to.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     * @throws CouldNotAddCustomerException gets thrown if the customer could not be added.
     */
    private static void startPhaseOne(Salon salon, int size) throws InterruptedException, CouldNotAddCustomerException {
        System.out.println("Now adding the first batch of customers. - Will have " + size + " customers.");
        sleepShort();
        addNAmountOfCustomers(salon, size, "Lise");
    }

    /**
     * Starts the second phase where there is added n + 2 more customers.
     * @param salon the salon that this phase should manipulate.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     * @throws CouldNotAddCustomerException gets thrown if the customer could not be added.
     */
    private static void startPhaseTwo(Salon salon, int size) throws InterruptedException, CouldNotAddCustomerException {
        int extra = size/4;
        int amountTotal = size + extra;
        System.out.println("Now adding the second batch of customers. - Will have " + extra + " more customers that there is seat for.");
        sleepShort();
        addNAmountOfCustomers(salon, amountTotal, "Fjell");
    }

    /**
     * Starts the third phase where 2 customers are added.
     * @param salon the salon that this phase should manipulate.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     * @throws CouldNotAddCustomerException gets thrown if the customer could not be added.
     */
    private static void startPhaseThree(Salon salon) throws InterruptedException, CouldNotAddCustomerException {
        System.out.println("Now adding the third and last batch of customers. - Will only be 2 people.");
        sleepShort();
        addNAmountOfCustomers(salon, 2, "Bob");
    }

    /**
     * Adds an N amount of customers to the salon.
     * @param salon the salon to add to.
     * @param amountN the amount of customers to add.
     * @param name the name of the customers.
     * @throws CouldNotAddCustomerException gets thrown if the customer could not be added.
     */
    private static void addNAmountOfCustomers(Salon salon, int amountN, String name) throws CouldNotAddCustomerException {
        for (int i = 0; i < amountN; i++){
            salon.addCustomer(new Customer(name + " " + i, CustomerState.NEEDSCUT));
        }
    }

    /**
     * Makes the thread sleep long.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     */
    private static void sleepLong() throws InterruptedException {
        Thread.sleep(longTime);
    }

    /**
     * Makes the thread sleep short.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     */
    private static void sleepShort() throws InterruptedException {
        Thread.sleep(shortTime);
    }

}
