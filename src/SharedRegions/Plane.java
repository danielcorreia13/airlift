package SharedRegions;

import ActiveEntity.Hostess;
import ActiveEntity.Passenger;
import ActiveEntity.Pilot;
import SharedRegions.DestinationAirport;
import Main.Settings;
import myLib.MemException;
import myLib.MemFIFO;
import myLib.MemObject;

public class Plane
{
    /**
     * Plane Passenger Seats
     */
	private MemFIFO<Integer> passengerSeats;
	
    /**
     * Reference to pilot
     */
	private Pilot pilot;
	
    /**
     * Reference do destination airport
     */
	private DestinationAirport dest;
	
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
     * Expected number of passengers
     */
    private int expectedPass;
    
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

    public Plane (GeneralRep repos) {
    	generalRep = repos;
        try{
        	passengerSeats = new MemFIFO<>(new Integer [Settings.maxPassengers]); // Para ja fica assim
        }catch (MemException e){
            System.err.println("Instantiation of plane seats FIFO failed: " + e.getMessage ());
            passengerSeats = null;
            System.exit (1);
        }
        this.allInBoard = false;
        setAtDestination(false);
        this.nPassengers = 0;
        this.expectedPass = 0;
    }

    public int getNPassengers(){
        return this.nPassengers;
    }
    public void passengerLeave(){
        this.nPassengers--;
    }
    public void passengerBoard() {
    	this.nPassengers++;
    }

    /*                                  HOSTESS                                      */
    /*-------------------------------------------------------------------------------*/
    
    /**
     *  Operation inform that the plane is ready for take off
     *
     *  It is called by the HOSTESS when all passengers are on board, requiring the minimum capacity of the plane
     *
     *
     */
    
    public synchronized void informPlaneIsReadyToTakeOff(int nPass)
    {
//    	while(!passengerSeats.full())
//    		try {
//    			wait();
//    		} catch (InterruptedException e){
//
//    		}
//    	System.out.println("[??] Aviao cheio -> " +passengerSeats.full());

        while (nPassengers != nPass){
            try{
                wait();
            }catch (InterruptedException e){}
        }


    	allInBoard = true;
//        nPassengers = nPass;
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.READY_TO_FLY);
        generalRep.setHostess(Hostess.States.READY_TO_FLY);
        generalRep.writeLog("Departed with " + nPass + " passengers");
        System.out.println("HOSTESS->PILOT: Plane is ready for takeoff");
        
        notifyAll();

    }

    
    
  
    public synchronized MemFIFO<Integer> getPassengerSeats() 
    {
		return passengerSeats;
	}


	public synchronized void setAtDestination(boolean atDestination)
    {
    	this.atDestination = atDestination;
    	notifyAll();
    }
    
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
    	
    	int passId = ((Passenger) Thread.currentThread()).getpId();
    	System.out.println("[!] PASSENGER "+ passId +": Waiting for the end of the flight");
    		
    	while ( !isAtDestination() )
    	{
    		try {
    			wait();
    		} catch (InterruptedException e) {}
    	}
    	    	
    	notifyAll();
    	    	
    }
    

    public synchronized void boardThePlane()
    {

        int passId = ((Passenger) Thread.currentThread()).getpId();
        ((Passenger) Thread.currentThread()).setpState(Passenger.States.IN_FLIGHT);
        generalRep.setPassengerState(passId, Passenger.States.IN_FLIGHT);
//        try{
//            passengerSeats.write(passId);
//        }catch (MemException e){
//            System.err.println("Insertion of passenger in plane seats failed: " + e.getMessage());
//            System.exit(1);
//        }
        
        //((Passenger) Thread.currentThread()).setpState(Passenger.States.IN_FLIGHT);
        //generalRep.setPassengerState(passId, Passenger.States.IN_FLIGHT);
    	
//    	passengerBoard();
//        notifyAll();

        System.out.println("PASSENGER "+passId+ ": Seated on plane");
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
    	System.out.println("PILOT: Waiting for all passengers on board");
        nPassengers = 0;

    	((Pilot) Thread.currentThread()).setPilotState(Pilot.States.WAIT_FOR_BOARDING);
    	generalRep.setPilotState(Pilot.States.WAIT_FOR_BOARDING);
    	try 
    	{
            while( !allInBoard)
    			wait();
    	}
    	catch (InterruptedException e) {}
    	allInBoard = false;
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.FLYING_FORWARD);

        //generalRep.writeLog("Departed with " + nPassengers + " passengers");
        generalRep.setPilotState(Pilot.States.FLYING_FORWARD);
    	return nPassengers;
    }
    



}
