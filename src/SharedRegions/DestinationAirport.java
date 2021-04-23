package SharedRegions;

import ActiveEntity.Passenger;
import ActiveEntity.Pilot;

public class DestinationAirport
{

    private final GeneralRep generalRep;

    private boolean arrived;

    private int nPassengers;

    /**
     *  Destination Airport instantiation.
     *
     *    @param repos reference to the general repository
     */

    public DestinationAirport(GeneralRep repos)
    {
        generalRep = repos;
        arrived = false;
        nPassengers = 0;
    }

    //----------------------------------------
    // Passageiro

    public synchronized void leaveThePlane() {
        while (!arrived){
            try{
                wait();
            }catch (InterruptedException e){

            }
        }
        System.out.println("Passenger " + ((Passenger) Thread.currentThread()).getpId() + " left the plane");
        ((Passenger) Thread.currentThread()).setpState(Passenger.States.AT_DESTINATION);
        nPassengers--;

        if (nPassengers == 0) {
            arrived = false;
            notifyAll();
        }
    }

    //----------------------------------------
    // Piloto

    public synchronized void announceArrival(int nPass) {
        arrived = true;
        nPassengers = nPass;
        notifyAll();
        ((Pilot) Thread.currentThread()).setPilotState(Pilot.States.DEBOARDING);
        while (arrived){
            try{
                wait();
            }catch (InterruptedException e){

            }
        }
    }
}
