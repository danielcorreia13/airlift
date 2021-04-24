package ActiveEntity;

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

    
    
    public Pilot(String name, DepartureAirport depAir, DestinationAirport destAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.destAir = destAir;
        this.plane = plane;
        this.pilotState = States.AT_TRANSFER_GATE;
    }
 
    
    public int getPilotState()
    {
		return this.pilotState;
	}
    
    public void setPilotState(int pState)
    {
		this.pilotState = pState;
	}
    

	// Vida do Thread (Piloto)
    public void run()
    {
        Boolean notEnd = true;
        
        do {
            parkAtTransferGate();
            depAir.informPlaneReadyForBoarding();
            plane.waitForAllInBoard();
            flyToDestinationPoint();

            destAir.announceArrival(/*plane.getNPassengers()*/);

            flyToDeparturePoint();

//            System.out.println("\n\tTransported passengers so far (" + destAir.arrivedPassengers.size() +"): ");
//            for (Integer id : destAir.arrivedPassengers){
//                System.out.println(id);
//            }
            System.out.println("\n");
        }while (true);

    }

    public void parkAtTransferGate() {
    	
        try
        { 
        	sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException e) {}
        
        this.pilotState = States.AT_TRANSFER_GATE;
        System.out.println("PILOT: Park transfer gate");
        
        
    }

    /**
     *  Operation inform that the plane is going to the destination point
     *
     *  It is called by the PILOT while flying to destination point
     *
     *    @return void
     */

    public synchronized void flyToDestinationPoint()
    {
    	
        System.out.println("\nPILOT: Flying to destination");
        System.out.println("=================================\n");
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.FLYING_FORWARD);
        try
        {
            sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }

    /**
     *  Operation inform that the plane is ready for take off
     *
     *  It is called by the PILOT while flying to departure point
     *
     *    @return void
     */

    public synchronized void flyToDeparturePoint() {
        System.out.println("\nPILOT: Flying back to departure");
        System.out.println("=================================\n");
        ((Pilot) Thread.currentThread()).setPilotState(States.FLYING_BACK);
        notifyAll();
        try
        {
            sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }

    /**
     *    Definition of the internal states of the pilot during his life cycle.
     */

    public final class States{

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
