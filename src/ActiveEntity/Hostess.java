package ActiveEntity;

import Main.Settings;
import SharedRegions.*;

/**
 *   Passenger thread.
 *
 *   It simulates the hostess life cycle.
 *
 */

public class Hostess extends Thread
{

    /**
     *  Reference to Departure Airport
     */

    private final DepartureAirport depAir;

    /**
     *  Reference to Plane
     */

    private final Plane plane;


    /**
     *  Hostess state
     */

    private int hState;

    /**
     * Hostess constructor
     * Creates a Hostess thread and initializes it's parameters
     * @param name name of the thread
     * @param depAir Reference to Departure Airport
     * @param plane Reference to Plane
     */

    public Hostess(String name, DepartureAirport depAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.plane = plane;
        this.hState = States.WAIT_FOR_NEXT_FLIGHT;
    }

    /**
     * Get Hostess state
     * @return hostess current state
     */

    public int gethState() {
        return hState;
    }

    /**
     * Ste Hostess state
     * @param hState new state
     */

    public void sethState(int hState) {
        this.hState = hState;

    }

    /**
     * Main loop of the hostess that runs it's life cycle
     */

    public void run()
    {
        int count = 0;
        do {
            depAir.waitForNextFlight();

            prepareForPassBoarding();
            int max = Settings.maxPassengers;
            int min = Settings.minPassengers;
            while (true) {
                if(depAir.getnPassengers() == max) {
//                    System.out.println("HOSTESS: Plane full, informing pilot");
                    break;
                }
                if(depAir.empty() && depAir.getnPassengers() > min){
//                    System.out.println("HOSTESS: No passengers waiting, informing pilot");
                    break;
                }
                if(count == Settings.nPassengers){
//                    System.out.println("HOSTESS: Last passenger boarding, informing pilot");
                    break;
                }
                count++;
                depAir.waitForNextPassenger();
                depAir.checkDocuments();
            }
            plane.informPlaneIsReadyToTakeOff(depAir.getnPassengers());
        }while (count < Settings.nPassengers);
    }

    /**
     * Hostess prepares for pass boarding
     *
     * The thread is put to sleep for a random amount of time
     */

    public void prepareForPassBoarding() {
//    	System.out.println("HOSTESS: Preparing for pass board");
        try
        { sleep ((long) (1 + 10 * Math.random ()));
        }
        catch (InterruptedException ignored) {}
        

    }

    /**
     *    Definition of the internal states of the hostess during his life cycle.
     */

    public static final class States{

        /**
         *   The hostess waits for the next flight.
         */

        public static final int WAIT_FOR_NEXT_FLIGHT = 0;

        /**
         *   The hostess waits for a passenger to arrive.
         */

        public static final int WAIT_FOR_PASSENGER = 1;

        /**
         *   The hostess checks the passenger's documents.
         */

        public static final int CHECK_PASSENGER = 2;

        /**
         *   The hostess tells the pilot that all the passengers have boarded the plane.
         */

        public static final int READY_TO_FLY = 3;

    }
}
