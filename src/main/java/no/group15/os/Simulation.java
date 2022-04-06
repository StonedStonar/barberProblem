package no.group15.os;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Starts the simulation.
 * @author Group 13
 * @version 0.1
 */
public class Simulation {

    //Todo: Statiske felter så vi kan få taki det i hele denne statiske klassen.
    // I java skal alle felter være private.
    private static int longTime;

    private static int shortTime;

    //Todo: Liste med customers. "List<Innhold>" order "innhold" i denne taggen til venstre sier hvilken klasse denne listen
    // kan inneholde. Dermed bare customers i tilfelle under.
    private static List<Customer> customerList;

    //Todo: Bruker executorservice siden det er et threadpool og man kan dermed stanse trådene om det skjer noe galt.
    private static ExecutorService executorService;


    //Todo: Under er en liste med keywords det er greit å kunne som står foran metoder.
    // private void test() - Da kan bare klassen eller instanser av klassen se metodene.
    // public void test() - Med public kan alle se metodene og bruke dem. Bruk enten private eller public.
    // void test() - Nå er metoden "package private" og bare klasser i pakken kan se den. Ikke bruk denne.
    // private void static main() - Metoden kan nå brukes bare internt av klassen men alle objektene kan bruke denne.
    //                              Alle "statiske" variabler og metoder gjelder for hele klasssen.

    public static void main(String[] args) {
        startSimulation();
    }

    /**
     * Starts the simulation and holds all the default values.
     */
    public static void startSimulation(){
        customerList = new ArrayList<>();
        //The amount of delay in ms for the long pause.
        //1500
        longTime = 15;
        //The amount of delay in ms for the short pause.
        //1500
        shortTime = 10;
        setConsoles();
        List<Barber> barbers = addNAmountOfBarbers(5);
        int maxSize = 100;
        Salon salon = new Salon("Man's breaking back", maxSize, barbers);
        int totalAmountOfThreads = maxSize * 3 + 2;
        executorService = Executors.newFixedThreadPool(totalAmountOfThreads);
        try {
            //Starter å legge til kunder og tar det i puljer.
            startPhaseOne(salon, maxSize);
            sleepLong();
            startPhaseTwo(salon, maxSize);
            sleepLong();
            startPhaseThree(salon);

            //Todo: Siden kundene selv legger seg til trenger vi noen ms pause så de ikke blir utestengt med en gang.
            sleepShort();
            salon.stopCustomerIntake();
            //Stopper salongen fra å ta imot nye kunder. Dette vil si at de må gjøre seg "ferdige" med kunder som allerede
            //sitter i en ventestol før de kan gå om dagen.
            Thread.sleep(1000);
            startPhaseFour(salon, maxSize);
            closeSaloon(salon);
        }catch (InterruptedException exception){
            System.err.println("The thread was interrupted.");
        }
    }

    /**
     * Checks if the saloon has closed and all barbers has gone home. Loops until the salon is closed.
     * @param salon the salon to check.
     * @throws InterruptedException gets thrown if the thread is interrupted in their sleep.
     */
    private static void closeSaloon(Salon salon) throws InterruptedException {
        boolean closed = false;
        do {
            Thread.sleep(1000);
            closed = salon.isClosedAndBarbersHasGoneHome();
        }while (!closed && !Thread.interrupted());
        System.out.println("The salon has now closed and all barbers has gone home.");
        executorService.shutdown();
    }

    /**
     * Adds an n amount of barbers to a list and returns it.
     * @param amountOfBarbers the amount of barbers.
     * @return the list with the barbers.
     */
    private static List<Barber> addNAmountOfBarbers(int amountOfBarbers){
        List<Barber> barbers = new ArrayList<>();
        for (int i = 0; i < amountOfBarbers; i++){
            barbers.add(new Barber("Bjarne " + i, State.LOOKINGFORWORK, shortTime));
        }
        return barbers;
    }

    /**
     * Sets the console so that fine messages also show.
     */
    private static void setConsoles(){
        Barber.setConsole();
        Customer.setConsole();
        Salon.setConsole();
    }

    /**
     * Starts the first phase that adds n customers to the salon.
     * @param salon the salon to add to.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     */
    private static void startPhaseOne(Salon salon, int size) throws InterruptedException {
        System.out.println("Now adding the first batch of customers. - Will have " + size + " customers.");
        sleepShort();
        addNAmountOfCustomers(salon, size, "Lise");
    }

    /**
     * Starts the second phase where there is added n + 2 more customers.
     * @param salon the salon that this phase should manipulate.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     */
    private static void startPhaseTwo(Salon salon, int size) throws InterruptedException {
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
     */
    private static void startPhaseThree(Salon salon) throws InterruptedException {
        System.out.println("Now adding the third and last batch of customers. - Will only be 2 people.");
        sleepShort();
        addNAmountOfCustomers(salon, 2, "Bob");
    }

    /**
     * Adds an N amount of customers to the salon.
     * @param salon the salon to add to.
     * @param amountN the amount of customers to add.
     * @param name the name of the customers.
     */
    private static void addNAmountOfCustomers(Salon salon, int amountN, String name) {
        for (int i = 0; i < amountN; i++){
            Customer customer = new Customer(name + " " + i, CustomerState.NEEDSCUT, salon);
            customerList.add(customer);
            //Kundene is seg selv legger seg selv til. Så ingen "add" på salon.
            executorService.submit(customer);
        }
    }

    /**
     * Adds n amount of customers after the salon has closed.
     * @param salon the saloon.
     * @param maxSize the max size.
     * @throws InterruptedException gets thrown if the thread is interrupted.
     */
    private static void startPhaseFour(Salon salon, int maxSize) throws InterruptedException {
        sleepShort();
        System.out.println("Starting the last phase after the two last customers of the day was added. - Supposed to fail");
        addNAmountOfCustomers(salon, maxSize, "Late Lars");
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
