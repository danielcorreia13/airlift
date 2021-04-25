package SharedRegions;

import ActiveEntity.Hostess;
import ActiveEntity.Passenger;
import ActiveEntity.Pilot;

/**
 * Shared region : Plane
 */
public class Plane
{	
    /**
     * Reference to general repository
     */
    private final GeneralRep generalRep;
    
    /**
     * All passengers on board flag
     */
    private boolean allInBoard;
    
    /**
     * Number of passengers flag 
     */
    private int nPassengers;
   
    /**
     * Plane at destination flag
     */
    private boolean atDestination;

    /*                                 CONSTRUCTOR                                   */
    /*-------------------------------------------------------------------------------*/   

    /**
     *  Plane instantiation.
     *
     *    @param repos reference to the general repository
     */
    public Plane (GeneralRep repos) 
    {
    	generalRep = repos;
        this.allInBoard = false;
        setAtDestination(false);
        this.nPassengers = 0;
    }

    /*                                  HOSTESS                                      */
    /*-------------------------------------------------------------------------------*/
    
    /**
     *  Operation inform that the plane is ready for take off
     *
     *  It is called by the HOSTESS when all passengers are on board, 
     *	Requiring minimum passengers on board and departure queue is empty
     *  Or if maximum passengers on board was reached
     *  Or if number of passengers on board is between min and max, and departure queue is empty
     * @param nPass number of passengers that boarded the plane
     */    
    public synchronized void informPlaneIsReadyToTakeOff(int nPass)
    {
        while (nPassengers != nPass){
            try{
                wait();
            }catch (InterruptedException ignored){}
        }

    	allInBoard = true;
    	
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.READY_TO_FLY);
        generalRep.setHostess(Hostess.States.READY_TO_FLY);
        generalRep.writeLog("Departed with " + nPass + " passengers");
        //System.out.println("HOSTESS->PILOT: Plane is ready for takeoff");
        
        notifyAll();

    }

    
    /**
     *  Operation to set plane at destination
     *
     * It is called by pilot on destination point
     *
     * @param atDestination new value
     */    
	public synchronized void setAtDestination(boolean atDestination)
    {
    	this.atDestination = atDestination;
    	notifyAll();
    }
    
    /**
     *  Operation inform that plane is at destination
     *
     *  It is called by the pilot to notify passengers that the plane was landed
     *
     * @return true if at destination
     */ 
    public synchronized boolean isAtDestination()
    {
    	return this.atDestination;
    }
    
    //                                  PASSENGER                                    //
    //---------------------------------------------------------------------------------
    
    /**
     *  Operation inform that the passenger is waiting for the end of the flight
     *
     *  It is called by the PASSENGER when he is on the plane
     *
     *
     */   
    public synchronized void waitForEndOfFlight() 
    {
    	
//    	int passId = ((Passenger) Thread.currentThread()).getpId();
    	//System.out.println("[!] PASSENGER "+ passId +": Waiting for the end of the flight");
    		
    	while ( !isAtDestination() )
    	{
    		try {
    			wait();
    		} catch (InterruptedException ignored) {}
    	}
    	    	
    	notifyAll();
    	    	
    }

    /**
     *  Operation inform that the passenger as entered the plane
     *
     *  It is called by the PASSENGER when enters the plane
     *
     *
     */
    public synchronized void boardThePlane()
    {

        int passId = ((Passenger) Thread.currentThread()).getpId();
        ((Passenger) Thread.currentThread()).setpState(Passenger.States.IN_FLIGHT);
        generalRep.setPassengerState(passId, Passenger.States.IN_FLIGHT);
        //System.out.println("PASSENGER "+passId+ ": Seated on plane");
        nPassengers++;
        notifyAll();
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
    public synchronized int waitForAllInBoard()
    {
    	//System.out.println("PILOT: Waiting for all passengers on board");
        nPassengers = 0;

    	((Pilot) Thread.currentThread()).setPilotState(Pilot.States.WAIT_FOR_BOARDING);
    	generalRep.setPilotState(Pilot.States.WAIT_FOR_BOARDING);
    	try 
    	{
            while( !allInBoard)
    			wait();
    	}
    	catch (InterruptedException ignored) {}
    	allInBoard = false;
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.FLYING_FORWARD);

        generalRep.setPilotState(Pilot.States.FLYING_FORWARD);
    	return nPassengers;
    }
}
