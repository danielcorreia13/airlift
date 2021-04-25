package SharedRegions;

import ActiveEntity.Passenger;
import ActiveEntity.Pilot;
import myLib.MemException;

import java.util.LinkedList;

public class DestinationAirport
{
    /**
     * Reference to general repository
     */
    private final GeneralRep generalRep;

    /**
     * Number of passengers
     */
    private int nPassengers;

    /**
     * Number of total passengers
     */
    private int totalPassengers;
    
    /**
     * Last passenger out flag
     */   
    private boolean lastPassengerOut;

    /**
     * Arrived passengers list
     */ 
    public LinkedList<Integer> arrivedPassengers = new LinkedList<>();

    
    /*                                 CONSTRUCTOR                                   */
    /*-------------------------------------------------------------------------------*/ 
    /**
     *  Destination Airport instantiation.
     *
     *    @param repos reference to the general repository
     */
    public DestinationAirport(GeneralRep repos, Plane plane)
    {
        this.generalRep = repos;
        this.nPassengers = 0;
        totalPassengers = 0;
        this.lastPassengerOut = false;
    }
    
    /**
     * Get number of total passengers
     */
    public int getTotalPassengers() 
    {
        return totalPassengers;
    }
    
    
    /*                                 PASSENGER                                     */
    /*-------------------------------------------------------------------------------*/ 
   
    /**
     *  Operation of passenger leaving the plane
     *
     *  It is called by the PASSENGER when he leaves the plane
     *
     *
     */     
	public synchronized void leaveThePlane() {
		int passId = ((Passenger) Thread.currentThread()).getpId();

        arrivedPassengers.add(passId);
		//System.out.println("PASSENGER " + passId + ": Left the plane");       
		((Passenger) Thread.currentThread()).setpState(Passenger.States.AT_DESTINATION);
		generalRep.setPassengerState(passId, Passenger.States.AT_DESTINATION);
        nPassengers--;
        totalPassengers++;
        //System.out.println(nPassengers);
        if (nPassengers == 0) 
        {
            System.out.println("        PASSENGER : " + passId + " Was the last to left the plane, notify the pilot");
            notifyAll();
        }            
    }


    /*                                    PILOT                                      */
    /*-------------------------------------------------------------------------------*/ 

    /**
     *  Operation inform that plane reached the destionation
     *
     *  It is called by the pilot when plane was landed
     *
     *
     */ 
    public synchronized void announceArrival(int nPass) {

        nPassengers = nPass;
        
        //System.out.println("PILOT: Plane arrived at destination");
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.DEBOARDING);
        generalRep.writeLog("Arrived");
        generalRep.setPilotState(Pilot.States.DEBOARDING);
        
        //System.out.println("    [!] Set destinanion flag at TRUE");
        
        notifyAll();
        
        //System.out.println("PILOT: Waiting for all passengers to leave the plane");
        while ( nPassengers != 0) 
        {
            try{          	
                wait();
            }catch (InterruptedException e){}
        }

        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.FLYING_BACK);
        generalRep.writeLog("Returning");
        generalRep.setPilotState(Pilot.States.FLYING_BACK);

        //System.out.println("\n\n    [!] Set destinanion flag at FALSE");
    }
}
