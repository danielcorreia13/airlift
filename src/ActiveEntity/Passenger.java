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
    // Para os métodos que só o Passageiro tem acesso nas zonas partilhadas.

    /**
     *  Reference to Departure Airport
     */

    private final DepartureAirport depAir;

    /**
     *  Reference to Destination Airport
     */

    private final DestinationAirport destAir;

    public int getpState() {
        return pState;
    }
    public void setpState(int state){
        pState = state;
    }

    public int getpId() {
        return pId;
    }

    /**
     *  Reference to Plane
     */

    private final Plane plane;

    /**
     *  Passenger state
     */

    private int pState;

    public boolean getShowDocuments() {
        return showDocuments;
    }

    public void setShowDocuments(boolean showDocuments) {
        this.showDocuments = showDocuments;
    }

    /**
     * Show documents flag
     */

    private boolean showDocuments;

    /**
     *  Passenger identification
     */

    private final int pId;


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
        this.showDocuments = false;
    }

    /**
     *   Life cycle of the passenger.
     */

    @Override
    public void run()
    {
        Boolean notEnd = true;

        travelToAirport();

        depAir.waitInQueue();
        
        plane.boardThePlane();

        plane.waitForEndOfFlight();

        destAir.leaveThePlane();



    }

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
