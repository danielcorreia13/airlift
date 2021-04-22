package ActiveEntity;

import SharedRegions.*;
import lib.*;
import Main.*;

/**
 *   Pilot thread.
 *
 *   It simulates the pilot life cycle.
 *
 */

public class Pilot extends Thread
{
    /**
     *  Reference to Departure Airport
     */

    private final DepartureAirport depAir;

    /**
     *  Reference to Destination Airport
     */

    private final DestinationAirport destAir;

    /**
     *  Reference to Plane
     */

    private final Plane plane;

    /**
     *  Pilot state
     */

    private final int state;

    public Pilot(String name, DepartureAirport depAir, DestinationAirport destAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.destAir = destAir;
        this.plane = plane;
        this.state = States.AT_TRANSFER_GATE;
    }

    // Vida do Thread (Piloto)
    public void run()
    {
        Boolean notEnd = true;

        while(notEnd)
        {
            // Exemplo na zona partilhada Avião: Piloto espera todos os passageiro a bordo
            plane.waitForAllInBoard();

            // Exemplo na zona partilhada Aeroporto Destino: Piloto avisa aos Passageiros da chegada
            destAir.announceArrival();

            // Exemplo na zona partilhada Aeroporto Partida: Piloto informa à hospedeira que o avião está pronto
            depAir.informPlaneReadyForBoarding();
        }
    }

    /**
     *    Definition of the internal states of the pilot during his life cycle.
     */

    public final class States{

        /**
         *   The plane is at transfer gate.
         */

        public static final int AT_TRANSFER_GATE = 0;

        /**
         *   The pilot informs that the plane is ready for boarding.
         */

        public static final int READY_FOR_BOARDING = 1;

        /**
         *   The pilot waits for the boarding to be complete.
         */

        public static final int WAIT_FOR_BOARDING = 2;

        /**
         *   The pilot flies the plane to the destination airport.
         */

        public static final int FLYING_FORWARD = 3;

        /**
         *   The pilot waits for the last passenger to exit the plane.
         */

        public static final int DEBOARDING = 4;

        /**
         *   The pilot flies the plane back to the departure airport.
         */

        public static final int FLYING_BACK = 5;

    }

}
