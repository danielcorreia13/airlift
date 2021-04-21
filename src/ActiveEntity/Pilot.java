package ActiveEntity;

import SharedRegions.*;
import lib.*;
import Main.*;

public class Pilot extends Thread
{
    // Para os métodos que só o Piloto tem acesso nas zonas partilhadas.
    private final DepartureAirport depAir;
    private final DestinationAirport destAir;
    private final Plane plane;

    public Pilot(String name, DepartureAirport depAir, DestinationAirport destAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.destAir = destAir;
        this.plane = plane;
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
}
