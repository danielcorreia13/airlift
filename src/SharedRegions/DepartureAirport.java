package SharedRegions;

import ActiveEntity.*;
import myLib.*;
import Main.*;

import java.util.Arrays;

public class DepartureAirport
{

    private MemFIFO<Integer> passengerQueue;  // passengers waiting to check documents

    private Passenger passengers[];  // passenger objects

    private boolean readyForBoardig;

    private final GeneralRep generalRep;

    /**
     *  Departure Airport instantiation.
     *
     *    @param repos reference to the general repository
     */

    public DepartureAirport(GeneralRep repos)
    {
        generalRep = repos;
        try{
            passengerQueue = new MemFIFO<>(new Integer [Settings.nPassengers]);
        }catch (MemException e){
            System.err.println("Instantiation of waiting FIFO failed: " + e.getMessage ());
            passengerQueue = null;
            System.exit (1);
        }

        passengers = new Passenger[Settings.nPassengers];
        Arrays.fill(passengers, null);

        readyForBoardig = false;
    }


    //--------------------------------------------------------------------------------
    // HOSTESS
    //--------------------------------------------------------------------------------

    public synchronized void checkDocuments() {
        System.out.println("Check doc");
        int passId = -1;
        try {
            passId = passengerQueue.read();
        }catch (MemException e){
            System.err.println("Retrieval of passenger from waiting queue failed: " + e.getMessage());
            System.exit(1);
        }
        passengers[passId].setShowDocuments(true);
        
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.CHECK_PASSENGER);
        
        notifyAll();
        
        while (passengers[passId].getShowDocuments()){
            try{
                wait();
            }catch (InterruptedException e){}
        }

        passengers[passId].setpState(Passenger.States.IN_FLIGHT);
        notify();
    }


    public synchronized void waitForNextPassenger() {
        System.out.println("wait for next pass");
        ((Hostess) Thread.currentThread()).sethState(Hostess.States.WAIT_FOR_PASSENGER);
        
        notifyAll();
        
        while (passengerQueue.empty()) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
    }


    public synchronized void waitForNextFlight() {
        System.out.println("Wait for next flight");
        while (!readyForBoardig) 
        {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
    }
    

    //--------------------------------------------------------------------------------
    // PASSENGER
    //--------------------------------------------------------------------------------
    public synchronized void waitInQueue() {
        int passId = ((Passenger) Thread.currentThread()).getpId();
        passengers[passId] = (Passenger) Thread.currentThread();
        passengers[passId].setpState(Passenger.States.IN_QUEUE);
        System.out.println("Passenger " + passId + " arrived at airport");
        //TODO: repository

        try{
            passengerQueue.write(passId);
        }catch (MemException e){
            System.err.println("Insertion of passenger in waiting queue failed: " + e.getMessage());
            System.exit(1);
        }
        notify();

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
        System.out.println("Passenger " + passId + " ready for board");
    }


    public synchronized void showDocuments() {
        ((Passenger)Thread.currentThread()).setShowDocuments(false);
        System.out.println("Show documents");
        notify();
    }
    

    //--------------------------------------------------------------------------------
    // PILOT
    //--------------------------------------------------------------------------------
    public synchronized void informPlaneReadyForBoarding() {
        readyForBoardig = true;
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.READY_FOR_BOARDING);
        System.out.println("PILOT: Ready for boarding");
        notifyAll();
    }
}
