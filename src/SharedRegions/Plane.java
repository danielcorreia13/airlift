package SharedRegions;

import ActiveEntity.Hostess;
import ActiveEntity.Passenger;
import ActiveEntity.Pilot;

import SharedRegions.*;
import lib.*;
import Main.*;

public class Plane
{
	
    
	//private int state; //0 - dep / 1 - flight / 2 - dest

	private Passenger passengers[];  // passenger objects
	private Pilot pilot;
    private final GeneralRep generalRep;
    private boolean allInBoard;

    /**
     *  Plane instantiation.
     *
     *    @param repos reference to the general repository
     */

    public Plane (GeneralRep repos) {
        generalRep = repos;
        //state = 0;
    }

    //                                  HOSTESS                                      //
    //---------------------------------------------------------------------------------
    
    /**
     *  Operation inform that the plane is ready for take off
     *
     *  It is called by the HOSTESS when all passengers are on board, requiring the minimum capacity of the plane
     *
     *    @return void
     */
    
    public synchronized void informPlaneIsReadyToTakeOff()
    {
    	allInBoard = true;
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.READY_TO_FLY);
        
        System.out.println("HOSTESS->PILOT: Plane is ready for takeoff");
        notify();
    }

    
    
    //                                  PASSENGER                                    //
    //---------------------------------------------------------------------------------
    
    /**
     *  Operation inform that the passenger is waiting for the end of the flight
     *
     *  It is called by the PASSENGER when he is on the plane
     *
     *    @return void
     */
    
    public synchronized void waitForEndOfFlight() 
    {
    	int  passId = ((Passenger) Thread.currentThread()).getpId();
    	System.out.println("PASSENGER "+ passId +": Waiting for the end of the flight");
    	((Passenger) Thread.currentThread()).setpState(Passenger.States.IN_FLIGHT);
    }

    
    
    //                                   PILOT                                      //
    //--------------------------------------------------------------------------------
    
    /**
     *  Operation inform that PILOT is waiting for the hostess signal, indicating that all passengers are on board.
     *
     *  It is called by the PILOT while waiting for all passengers on board
     *
     *    @return void
     */
    
    public synchronized void waitForAllInBoard() 
    {
    	System.out.println("PILOT: waiting for all passengers on board");
    	((Pilot) Thread.currentThread()).setPilotState(Pilot.States.WAIT_FOR_BOARDING);
    	try 
    	{
    		while( !allInBoard )
    			wait();
    	}
    	catch (InterruptedException e) {}
    	
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
    	System.out.println("PILOT: Flying to destination");
    	((Pilot) Thread.currentThread()).setPilotState(Pilot.States.FLYING_FORWARD);

    }

    /**
     *  Operation inform that the plane is ready for take off
     *
     *  It is called by the PILOT while flying to departure point
     *
     *    @return void
     */
    
	public synchronized void flyToDeparturePoint() {

    }
}
