package ActiveEntity;

import DepartureAirport.IDepartureAirport_Passenger;
import DestinationAirport.IDestinationAirport_Passenger;
import Plane.IPlane_Passenger;

public class Passenger extends Thread
{
    // Para os métodos que só o Passageiro tem acesso nas zonas partilhadas.
    private final IDepartureAirport_Passenger iDepartureAirport;
    private final IDestinationAirport_Passenger iDestinationAirport;
    private final Plane.IPlane_Passenger iPlane;

    public Passenger(IDepartureAirport_Passenger iDepartureAirport, IDestinationAirport_Passenger iDestinationAirport, IPlane_Passenger iPlane)
    {
        this.iDepartureAirport = iDepartureAirport;
        this.iDestinationAirport = iDestinationAirport;
        this.iPlane = iPlane;
    }

    //Vida do Thread (Passageiro)
    public void run()
    {
        Boolean notEnd = true;

        while(notEnd)
        {
            // Exemplo na zona partilhada Avião: Passageiro espera o voo terminar
            iPlane.waitForEndOfFlight();

            // Exemplo na zona partilhada Aeroporto Destino: Passageiro avisa que saiu do avião
            iDestinationAirport.leaveThePlane();
        }
    }
}
