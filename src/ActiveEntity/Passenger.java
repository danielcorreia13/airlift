package ActiveEntity;

import SharedRegions.*;

/**
 *   Passenger thread.
 *
 *   It simulates the passenger life cycle.
 *
 */

public class Passenger extends Thread
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
     *  Passenger identification
     */

    private final int pId;
    

    /**
     *  Passenger state
     */

    private int pState;
    
    
    /**
     *   Instantiation of a Passenger thread.
     *
     *     @param name thread name
     *     @param id passenger id
     *     @param depAir reference to the Departure Airport
     *     @param destAir reference to the Destination Airport
     *     @param plane reference to the Plane
     */
    public Passenger(String name, int id, DepartureAirport depAir, DestinationAirport destAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.destAir = destAir;
        this.plane = plane;
        this.pState = States.GOING_TO_AIRPORT;
        this.pId = id;
    }

    /**
     *  Operation to get the passenger state
     *
     *    @return int
     */   
    public int getpState() {
        return pState;
    }
    
    /**
     *  Operation to set the passenger state
     *
     *
     */   
    public void setpState(int state){
        pState = state;
    }

    /**
     *  Operation to get the passenger identification
     *
     *    @return int
     */ 
    public int getpId() {
        return pId;
    }

    /**
     *   Life cycle of the passenger.
     */

    @Override
    public void run()
    {
        travelToAirport();

        depAir.waitInQueue();
        
        plane.boardThePlane();

        plane.waitForEndOfFlight();

        destAir.leaveThePlane();

    }
   
    /**
     *  Going to airport.
     *
     *  Puts the thread a sleep for a random amount of time
     */
    public void travelToAirport() {
        try
        { sleep ((long) (1 + 250 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }
    
  
    /**
     *    Definition of the internal states of the passenger during his life cycle.
     */

    public final class States{

        /**
         *   The customer takes the bus to go to the departure airport.
         */

        public static final int GOING_TO_AIRPORT = 0;

        /**
         *   The customer queue at the boarding gate waiting for the flight to be announced.
         */

        public static final int IN_QUEUE = 1;

        /**
         *   The customer flies to the destination airport.
         */

        public static final int IN_FLIGHT = 2;

        /**
         *   The customer arrives at the destination airport, disembarks and leaves the airport.
         */

        public static final int AT_DESTINATION = 3;

    }
}
