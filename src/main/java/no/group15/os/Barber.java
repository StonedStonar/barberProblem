package no.group15.os;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Steinar Hjelle Midthus
 * @version 0.1
 */
public class Barber implements Runnable, ObservableBarber {

    private State state;

    private int workingTime;

    private List<BarberObserver> observableBarberList;

    /**
     * Makes an instance of the Barber class.
     */
    public Barber() {
        Random random = new Random();
        state = State.WORKING;
        workingTime = random.nextInt(1, 3);
        observableBarberList =  new LinkedList<>();
    }

    /**
     * Does the work of the barterer.
     */
    public void work() throws InterruptedException {
        while (state != State.FREEDOM) {

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


    @Override
    public void run() {

    }

    public static void main(String[] args) {
        Barber barber = new Barber();
        barber.runRunnable(() -> System.out.println("Pepe hang"));
    }

    /**
     * Runs a runnable just for shit's and giggles.
     * @param runnable the runnable to run.
     */
    public void runRunnable(Runnable runnable){
        System.out.println("Starts the runnable task: " + runnable);
        synchronized (Barber.class){
            runnable.run();
        }
    }

    @Override
    public void addObserver(BarberObserver barberObserver) {
        checkIfObjectIsNull(barberObserver, "observer");
        observableBarberList.add(barberObserver);
    }

    @Override
    public void removeObserver(BarberObserver barberObserver) {
        checkIfObjectIsNull(barberObserver, "observer");
        observableBarberList.remove(barberObserver);
    }

    @Override
    public void alertObservers(Customer customer) {
        observableBarberList.forEach(observableBarber -> observableBarber.notifyObserver(customer));
    }
}
