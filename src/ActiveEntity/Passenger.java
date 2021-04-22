package ActiveEntity;

import SharedRegions.*;
import lib.*;
import Main.*;

/**
 *   Passenger thread.
 *
 *   It simulates the passenger life cycle.
 *
 */

public class Passenger extends Thread
{
    // Para os métodos que só o Passageiro tem acesso nas zonas partilhadas.

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
     *  Passenger state
     */

    private final int state;

    /**
     *  Passenger identification
     */

    private final int id;


    /**
     *   Instantiation of a Passenger thread.
     *
     *     @param name thread name
     *     @param id passenger id
     *     @param depAir reference to the Departure Airport
     *     @param destAir reference to the Destination Airport
     *     @param plane reference to the Plane
     */


    public Passenger(String name, int id, DepartureAirport depAir, DestinationAirport destAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.destAir = destAir;
        this.plane = plane;
        this.state = States.GOING_TO_AIRPORT;
        this.id = id;
    }

    /**
     *   Life cycle of the passenger.
     */

    @Override
    public void run()
    {
        Boolean notEnd = true;

        while(notEnd)
        {
            // Exemplo na zona partilhada Avião: Passageiro espera o voo terminar
            plane.waitForEndOfFlight();

            // Exemplo na zona partilhada Aeroporto Destino: Passageiro avisa que saiu do avião
            destAir.leaveThePlane();
        }
    }

    public void travelToAirport() {

    }

    /**
     *    Definition of the internal states of the passenger during his life cycle.
     */

    public final class States{

        /**
         *   The customer takes the bus to go to the departure airport.
         */

        public static final int GOING_TO_AIRPORT = 0;

        /**
         *   The customer queue at the boarding gate waiting for the flight to be announced.
         */

        public static final int IN_QUEUE = 1;

        /**
         *   The customer flies to the destination airport.
         */

        public static final int IN_FLIGHT = 2;

        /**
         *   The customer arrives at the destination airport, disembarks and leaves the airport.
         */

        public static final int AT_DESTINATION = 3;

    }
}
