package ActiveEntity;

import DepartureAirport.IDepartureAirport_Pilot;
import DestinationAirport.IDestinationAirport_Pilot;
import Plane.IPlane_Pilot;

public class Pilot
{
    // Para os métodos que só o Piloto tem acesso nas zonas partilhadas.
    private final IDepartureAirport_Pilot iDepartureAirport;
    private final IDestinationAirport_Pilot iDestinationAirport;
    private final Plane.IPlane_Pilot iPlane;

    public Pilot(IDepartureAirport_Pilot iDepartureAirport, IDestinationAirport_Pilot iDestinationAirport, IPlane_Pilot iPlane) {
        this.iDepartureAirport = iDepartureAirport;
        this.iDestinationAirport = iDestinationAirport;
        this.iPlane = iPlane;
    }


    // Vida do Thread (Piloto)
    public void run()
    {
        Boolean notEnd = true;

        while(notEnd)
        {
            // Exemplo na zona partilhada Avião: Piloto espera todos os passageiro a bordo
            iPlane.waitForAllInBoard();

            // Exemplo na zona partilhada Aeroporto Destino: Piloto avisa aos Passageiros da chegada
            iDestinationAirport.announceArrival();

            // Exemplo na zona partilhada Aeroporto Partida: Piloto informa à hospedeira que o avião está pronto
            iDepartureAirport.informPlaneReadyForBoarding();
        }
    }
}
