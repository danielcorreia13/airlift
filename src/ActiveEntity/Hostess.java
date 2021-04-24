package ActiveEntity;

import Main.Settings;
import SharedRegions.*;

/**
 *   Passenger thread.
 *
 *   It simulates the hostess life cycle.
 *
 */

public class Hostess extends Thread
{
    // Para os métodos que só a Hospedeira tem acesso nas zonas partilhadas.

    /**
     *  Reference to Departure Airport
     */

    private final DepartureAirport depAir;

    /**
     *  Reference to Plane
     */

    private final Plane plane;

    public int gethState() {
        return hState;
    }

    public void sethState(int hState) {
        this.hState = hState;
    }

    /**
     *  Hostess state
     */

    private int hState;

    public Hostess(String name, DepartureAirport depAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.plane = plane;
        this.hState = States.WAIT_FOR_NEXT_FLIGHT;
    }

    //Vida do Thread (Hospedeira)
    public void run()
    {
        Boolean notEnd = true;
        int count = 0;
        do {
            depAir.waitForNextFlight();

            prepareForPassBoarding();
            int max = Settings.maxPassengers;
            int min = Settings.minPassengers;
            while (true) {
                if(plane.getNPassengers() == max)break;
                if(depAir.empty() && plane.getNPassengers() > min)break;
                if(count == Settings.nPassengers)break;
                count++;
                depAir.waitForNextPassenger();
                depAir.checkDocuments();
            }
            plane.informPlaneIsReadyToTakeOff();
        }while (count < Settings.nPassengers);

    }

    public void prepareForPassBoarding() {
    	System.out.println("HOSTESS: Preparing for pass board");
        try
        { sleep ((long) (1 + 10 * Math.random ()));
        }
        catch (InterruptedException e) {}
        
        sethState(States.WAIT_FOR_PASSENGER);
    }

    /**
     *    Definition of the internal states of the hostess during his life cycle.
     */

    public final class States{

        /**
         *   The hostess waits for the next flight.
         */

        public static final int WAIT_FOR_NEXT_FLIGHT = 0;

        /**
         *   The hostess waits for a passenger to arrive.
         */

        public static final int WAIT_FOR_PASSENGER = 1;

        /**
         *   The hostess checks the passenger's documents.
         */

        public static final int CHECK_PASSENGER = 2;

        /**
         *   The hostess tells the pilot that all the passengers have boarded the plane.
         */

        public static final int READY_TO_FLY = 3;

    }
}
