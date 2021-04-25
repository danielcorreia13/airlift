package ActiveEntity;

import Main.Settings;
import SharedRegions.*;

/**
 *   Pilot thread.
 *
 *   It simulates the pilot life cycle.
 *
 */

public class Pilot extends Thread
{
    /**
     *  Reference to Departure Airport
     */
    private final DepartureAirport depAir;

    /**
     *  Reference to Destination Airport
     */
    private final DestinationAirport destAir;

    /**
     *  Reference to Plane
     */
    private final Plane plane;

    /**
     *  Pilot state
     */
    private int pilotState;

    /**
     * Pilots constructor
     * Creates a Pilot thread and initializes it's parameters
     * @param name name of the thread
     * @param depAir Reference to Departure Airport
     * @param destAir Reference to Destination Airport
     * @param plane Reference to Plane
     */
    
    public Pilot(String name, DepartureAirport depAir, DestinationAirport destAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.destAir = destAir;
        this.plane = plane;
        this.pilotState = States.AT_TRANSFER_GATE;
    }

    /**
     * Get the pilot's state
     * @return pilot's current state
     */
    
    public int getPilotState()
    {
		return this.pilotState;
	}

    /**
     * Set the pilot state
     * @param pState new state
     */
    
    public void setPilotState(int pState)
    {
		this.pilotState = pState;
	}

    /**
     * Main loop of the pilot that runs it's life cycle
     */

    public void run()
    {
        
        do {
            depAir.parkAtTransferGate();
            depAir.informPlaneReadyForBoarding();
            int nPass = plane.waitForAllInBoard();
            flyToDestinationPoint();

            plane.setAtDestination(true);
            destAir.announceArrival(nPass);
            plane.setAtDestination(false);

            flyToDeparturePoint();

//            System.out.println("\n");
        }while (destAir.getTotalPassengers() != Settings.nPassengers);
        depAir.parkAtTransferGate();
    }

    /**
     *  Operation fly to the destination point
     *
     *  Puts the thread a sleep for a random amount of time
     *
     *
     */

    public synchronized void flyToDestinationPoint()
    {
    	
//        System.out.println("\nPILOT: Flying to destination");
//        System.out.println("=================================\n");
        try
        {
            sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException ignored) {}
    }

    /**
     *  Operation fly to the departure point
     *
     *  Puts the thread a sleep for a random amount of time
     *
     *
     */

    public synchronized void flyToDeparturePoint() {
//        System.out.println("\nPILOT: Flying back to departure");
//        System.out.println("=================================\n");

        notifyAll();
        try
        {
            sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException ignored) {}
    }

    /**
     *    Definition of the internal states of the pilot during his life cycle.
     */

    public static final class States{

        /**
         *   The plane is at transfer gate.
         */

        public static final int AT_TRANSFER_GATE = 0;

        /**
         *   The pilot informs that the plane is ready for boarding.
         */

        public static final int READY_FOR_BOARDING = 1;

        /**
         *   The pilot waits for the boarding to be complete.
         */

        public static final int WAIT_FOR_BOARDING = 2;

        /**
         *   The pilot flies the plane to the destination airport.
         */

        public static final int FLYING_FORWARD = 3;

        /**
         *   The pilot waits for the last passenger to exit the plane.
         */

        public static final int DEBOARDING = 4;

        /**
         *   The pilot flies the plane back to the departure airport.
         */

        public static final int FLYING_BACK = 5;

    }

}
