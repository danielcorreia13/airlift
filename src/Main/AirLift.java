package Main;

import SharedRegions.*;
import ActiveEntity.*;
import lib.*;



public class AirLift
{
	// Regi�es partilhadas
	private final DepartureAirport sharedDepartureAirport;
	private final DestinationAirport sharedDestinationAirport;
	private final Plane sharedPlane;
	private final GeneralRep generalRep;
	
	// Entidades ativas
	private final Hostess hostess;	
	private final Pilot pilot;
	private final Passenger[] passenger;

	// Configura��o
	private final Integer TTL_PASSENGER;
	private final Integer MAX_PASSENGER;
	private final Integer MIN_PASSENGER;
	
	public AirLift( String args[] )
	{
		TTL_PASSENGER = 21; // Passageiros que participam na simula��o
		MAX_PASSENGER = 10; // Capacidade m�xima do avi�o
		MIN_PASSENGER = 5; // Capacidade m�nima do avi�o
		
		// Instanciar as regi�es partilhadas
		generalRep = new GeneralRep();
		sharedDepartureAirport = new DepartureAirport(generalRep);
		sharedDestinationAirport = new DestinationAirport(generalRep);
		sharedPlane = new Plane(generalRep);
		
		// Instanciar as entidades ativas (Threads) - Para j� s�o passadas as interfaces que cada um usa.
		pilot = new Pilot("Pilot1", sharedDepartureAirport, sharedDestinationAirport, sharedPlane /* Passar mais argumentos*/ );
		hostess = new Hostess( "Hostess1", sharedDepartureAirport, sharedPlane /* Passar mais argumentos*/);
		
			// Como existem v�rios passageiros
		passenger = new Passenger[TTL_PASSENGER];
		
		for (int i = 0; i < MAX_PASSENGER; i++)
		{
			passenger[i] = new Passenger( "Passenger"+i,i, sharedDepartureAirport, sharedDestinationAirport, sharedPlane /* Passar mais argumentos*/);
		}
		
	}
	
	// Iniciar a simula��o
	public void startSimulation()
	{
		System.out.println("Simula��o iniciada");
		
		// Iniciar os threads
		
		
		// Esperar que os threads morram
	}
	
	
	
	
	public static void main(String args[])
	{
		new AirLift ( args ).startSimulation();
	}
}
