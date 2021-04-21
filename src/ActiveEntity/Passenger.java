package ActiveEntity;

import SharedRegions.*;
import lib.*;
import Main.*;

public class Passenger extends Thread
{
    // Para os métodos que só o Passageiro tem acesso nas zonas partilhadas.
    private final DepartureAirport depAir;
    private final DestinationAirport destAir;
    private final Plane plane;


    public Passenger(String name, DepartureAirport depAir, DestinationAirport destAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.destAir = destAir;
        this.plane = plane;
    }

    //Vida do Thread (Passageiro)
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
}
