package SharedRegions;

import ActiveEntity.*;
import myLib.*;
import Main.*;

import java.util.Arrays;

public class DepartureAirport
{
    /**
     * Departure Passenger Queue
     */
    private MemFIFO<Integer> passengerQueue;  // passengers waiting to check documents

    // Maybe not necessary
    private Passenger passengers[];  // passenger objects

    /**
     * Ready for boarding flag
     */
    private boolean readyForBoardig;

    public int getnPassengers() {
        return nPassengers;
    }

    /**
     * Keeps track of how many passengers are entering the plane
     */

    private int nPassengers;

    /**
     * Reference to General Repository
     */
    private final GeneralRep generalRep;

   
    /*                                  CONSTRUCTOR                                    */
    /*---------------------------------------------------------------------------------*/    
    
    /**
     *  Departure Airport instantiation.
     *
     *    @param repos reference to the general repository
     */
    public DepartureAirport(GeneralRep repos)
    {
        generalRep = repos;
        
        try {
        	passengerQueue = new MemFIFO<>(new Integer [Settings.nPassengers]);
        } catch (MemException e) {
            System.err.println("Instantiation of waiting FIFO failed: " + e.getMessage ());
            passengerQueue = null;
            System.exit (1);
        }

        passengers = new Passenger[Settings.nPassengers];
        Arrays.fill(passengers, null);

        readyForBoardig = false;
        nPassengers = 0;
    }

    public boolean empty(){
        return passengerQueue.empty();
    }

    /*                                   HOSTESS                                       */
    /*---------------------------------------------------------------------------------*/
    
    /**
     *  Operation inform that the hostess needs do check the passenger documents
     *
     *  It is called by the HOSTESS when she is requesting a passenger documents
     *
     *    @return void
     */
    public synchronized void checkDocuments() 
    {
        int passId = -1;
        
        try {
            passId = passengerQueue.read();
        } catch (MemException e) {
            System.err.println("Retrieval of passenger from waiting queue failed: " + e.getMessage());
            System.exit(1);
        }
        
        System.out.println("HOSTESS: Passenger "+ passId +" is next on queue");

        passengers[passId].setShowDocuments(true);
        generalRep.writeLog("Passenger " + passId + " checked");
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.CHECK_PASSENGER);
        generalRep.setHostess(Hostess.States.CHECK_PASSENGER);
        
        notifyAll();
        
        while (passengers[passId].getShowDocuments())
        {
        	System.out.println("HOSTESS: Checking passenger "+ passId +" documents");
            
        	try {
                wait();
            } catch (InterruptedException e) {}
        }

        System.out.println("	HOSTESS: Passenger "+ passId +" documents checked!");
        System.out.println("		HOSTESS: Passenger "+ passId +" allowed to board");
        passengers[passId].setpState(Passenger.States.IN_FLIGHT);
        generalRep.setPassengerState(passId, Passenger.States.IN_FLIGHT);
        notifyAll();
    }

    /**
     *  Operation inform that the hostess is waiting for a passenger on the departure queue
     *
     *  It is called by the HOSTESS when she is waiting for a passenger on the queue
     *
     *    @return void
     */
    public synchronized void waitForNextPassenger() 
    {
    	System.out.println("HOSTESS: Checking if queue not empty");
//        notifyAll();
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.WAIT_FOR_PASSENGER);
        generalRep.setHostess(Hostess.States.WAIT_FOR_PASSENGER);
        
        while (passengerQueue.empty()) 
        {
        	System.out.println("HOSTESS: Waiting for next passenger");
            
        	try {
            	wait();
            } catch (InterruptedException e) {}
        }
    }

    /**
     *  Operation inform that the passenger is waiting for the end of the flight
     *
     *  It is called by the PASSENGER when he is on flight waiting to reach the destination
     *
     *    @return void
     */
    public synchronized void waitForNextFlight() 
    {
        System.out.println("HOSTESS: Waiting for next flight");
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.WAIT_FOR_NEXT_FLIGHT);
        generalRep.setHostess(Hostess.States.WAIT_FOR_NEXT_FLIGHT);
        while (!readyForBoardig) 
        {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        readyForBoardig = false;
        nPassengers = 0;
        generalRep.nextFlight();
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.WAIT_FOR_PASSENGER);

        generalRep.setHostess(Hostess.States.WAIT_FOR_PASSENGER);

    }
    
    
    /*                                 PASSENGER                                       */
    /*---------------------------------------------------------------------------------*/
    
    /**
     *  Operation inform the hostess that the passenger is waiting on departure airport queue
     *
     *  It is called by the PASSENGER when he is on the queue
     *
     *    @return void
     */
    public synchronized void waitInQueue() 
    {
        int passId = ((Passenger) Thread.currentThread()).getpId();
        passengers[passId] = (Passenger) Thread.currentThread();
        passengers[passId].setpState(Passenger.States.IN_QUEUE);
        generalRep.setPassengerState(passId,Passenger.States.IN_QUEUE);
        System.out.println("[!] PASSENGER " + passId + ": Arrived at departure airport");
        //TODO: repository

        try {
            passengerQueue.write(passId);
        } catch (MemException e){
            System.err.println("Insertion of passenger in waiting queue failed: " + e.getMessage());
            System.exit(1);
        }
        notifyAll();

        while (!((Passenger) Thread.currentThread()).getShowDocuments()){
            try{
                wait();
            }catch (InterruptedException e){

            }
        }

        showDocuments();

        while (((Passenger) Thread.currentThread()).getpState() != Passenger.States.IN_FLIGHT){
            try{
                wait();
            }catch (InterruptedException e){

            }
        }
        nPassengers++;
    }

    /**
     *  Operation inform that the passenger is showing his documents
     *
     *  It is called by the PASSENGER when he need to show his documents to the hostess
     *
     *    @return void
     */
    public synchronized void showDocuments() 
    {
        ((Passenger)Thread.currentThread()).setShowDocuments(false);
        int passId = ((Passenger)Thread.currentThread()).getpId();
        System.out.println("PASSENGER "+ passId +": Shows documents");
        notify();
    }
    

    /*                                     PILOT                                       */
    /*---------------------------------------------------------------------------------*/
    
    /**
     *  Operation inform that the plane is ready for boarding
     *
     *  It is called by the PILOT when plane is parked on departure and ready for boarding
     *
     *    @return void
     */
    public synchronized void informPlaneReadyForBoarding() 
    {
        readyForBoardig = true;
        generalRep.writeLog("Boarding Started");
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.READY_FOR_BOARDING);
        generalRep.setPilotState(Pilot.States.READY_FOR_BOARDING);

        System.out.println("PILOT: Plane is ready for boarding");
        notifyAll();
    }
}
