package ActiveEntity;

import DepartureAirport.IDepartureAirport_Hostess;
import Plane.IPlane_Hostess;

public class Hostess extends Thread
{
    // Para os métodos que só a Hospedeira tem acesso nas zonas partilhadas.
    private final IDepartureAirport_Hostess iDepartureAirport;
    private final Plane.IPlane_Hostess iPlane;

    public Hostess(IDepartureAirport_Hostess iDepartureAirport, IPlane_Hostess iPlane)
    {
        this.iDepartureAirport = iDepartureAirport;
        this.iPlane = iPlane;
    }

    //Vida do Thread (Hospedeira)
    public void run()
    {
        Boolean notEnd = true;

        while(notEnd)
        {
            // Exemplo: Hospedeira avisa ao piloto que os passageiros estão todos no avião
            iPlane.informPlaneReadyToTakeOff();
        }
    }
}
