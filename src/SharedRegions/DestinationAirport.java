package SharedRegions;

import ActiveEntity.Passenger;
import ActiveEntity.Pilot;
import myLib.MemException;

import java.util.LinkedList;

public class DestinationAirport
{

    private final GeneralRep generalRep;

    private boolean arrived;

    private int nPassengers;
    
    private Plane plane;
    
    private boolean lastPassengerOut;

    // testes ------------------

    public LinkedList<Integer> arrivedPassengers = new LinkedList<>();

    // testes ---------------

    /**
     *  Destination Airport instantiation.
     *
     *    @param repos reference to the general repository
     */

    public DestinationAirport(GeneralRep repos, Plane plane)
    {
        this.generalRep = repos;
        this.arrived = false;
        this.nPassengers = 0;
        this.lastPassengerOut = false;
        this.plane = plane;
    }
    

    public boolean isArrived() {
		return this.arrived;
	}


	public void setArrived(boolean arrived) {
		this.arrived = arrived;
	}


    //----------------------------------------
    // Passageiro
	
	public synchronized void leaveThePlane() {
		int passId = ((Passenger) Thread.currentThread()).getpId();


        // testes ------------------

        arrivedPassengers.add(passId);

        // testes ---------------

		System.out.println("PASSENGER " + passId + ": Left the plane");
        
		((Passenger) Thread.currentThread()).setpState(Passenger.States.AT_DESTINATION);
		generalRep.setPassengerState(passId, Passenger.States.AT_DESTINATION);
        plane.passengerLeave();
//        try {
//        	passId = plane.getPassengerSeats().read();
//        } catch (MemException e) {}
        
//        System.out.println("    PASSENGER: " +passId+ " left the plane");
        
        if (plane.getNPassengers() == 0) {
            System.out.println("        PASSENGER : " + passId + " Was the last to left the plane, notify the pilot");

        }
        notifyAll();
            
        }


    //----------------------------------------
    // Piloto

    public synchronized void announceArrival(/*int nPass*/) {
        //this.arrived = true;
        //nPassengers = nPass;
        
        System.out.println("PILOT: Plane arrived at destination");
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.DEBOARDING);
        generalRep.writeLog("Arrived");
        generalRep.setPilotState(Pilot.States.DEBOARDING);

        
        System.out.println("    [!] Set destinanion flag at TRUE");
        plane.setAtDestination(true);  
        
        notifyAll();
        
        System.out.println("PILOT: Waiting for all passengers to leave the plane");
        while ( plane.getNPassengers() != 0) {
            try{          	
                wait();
            }catch (InterruptedException e){}
        }

        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.FLYING_BACK);
        generalRep.writeLog("Returning");
        generalRep.setPilotState(Pilot.States.FLYING_BACK);


        System.out.println("\n\n    [!] Set destinanion flag at FALSE");
        plane.setAtDestination(false);  
    }
}
