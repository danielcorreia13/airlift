package ActiveEntity;

import SharedRegions.*;
import lib.*;
import Main.*;

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

    /**
     *  Hostess state
     */

    private final int state;

    public Hostess(String name, DepartureAirport depAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.plane = plane;
        this.state = States.WAIT_FOR_NEXT_FLIGHT;
    }

    //Vida do Thread (Hospedeira)
    public void run()
    {
        Boolean notEnd = true;

        while(notEnd)
        {
            // Exemplo: Hospedeira avisa ao piloto que os passageiros estão todos no avião
            plane.informPlaneReadyToTakeOff();
        }
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
