package ActiveEntity;

import SharedRegions.*;
import lib.*;
import Main.*;

public class Hostess extends Thread
{
    // Para os métodos que só a Hospedeira tem acesso nas zonas partilhadas.
    private final DepartureAirport depAir;
    private final Plane plane;

    public Hostess(String name, DepartureAirport depAir, Plane plane) {
        super(name);
        this.depAir = depAir;
        this.plane = plane;
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
}
