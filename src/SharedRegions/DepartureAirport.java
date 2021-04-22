package SharedRegions;

import ActiveEntity.*;
import lib.*;
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


    //----------------------------------------
    // Hostess

    public void prepareForPassBoarding() {

    }


    public void checkDocuments() {

    }


    public void waitForNextPassenger() {

    }


    public void waitForNextFlight() {

    }

    //----------------------------------------
    // Passenger


    public void waitInQueue() {

    }


    public void showDocuments() {

    }

    //----------------------------------------
    // Pilot

    public void parkAtTransferGate() {

    }


    public void informPlaneReadyForBoarding() {

    }
}
