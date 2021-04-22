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

	
	public AirLift( String args[] )
	{
		
		// Instanciar as regi�es partilhadas
		generalRep = new GeneralRep();
		sharedDepartureAirport = new DepartureAirport(generalRep);
		sharedDestinationAirport = new DestinationAirport(generalRep);
		sharedPlane = new Plane(generalRep);
		
		// Instanciar as entidades ativas (Threads) - Para j� s�o passadas as interfaces que cada um usa.
		pilot = new Pilot("Pilot1", sharedDepartureAirport, sharedDestinationAirport, sharedPlane /* Passar mais argumentos*/ );
		hostess = new Hostess( "Hostess1", sharedDepartureAirport, sharedPlane /* Passar mais argumentos*/);
		
			// Como existem v�rios passageiros
		passenger = new Passenger[Settings.nPassengers];
		
		for (int i = 0; i < Settings.nPassengers; i++)
		{
			passenger[i] = new Passenger( "Passenger"+i,i, sharedDepartureAirport, sharedDestinationAirport, sharedPlane /* Passar mais argumentos*/);
		}
		
	}
	
	// Iniciar a simula��o
	public void startSimulation()
	{
		System.out.println("Simula��o iniciada");
		
		pilot.start();
		hostess.start();
		for (Passenger p : passenger){
			p.start();
		}

		
		
		// Esperar que os threads morram
		try {
			pilot.join();
			hostess.join();
			for (Passenger p : passenger) {
				p.join();
			}
		}catch (InterruptedException e){
			System.out.println("Morreuuuuu");
		}

	}
	
	
	
	
	public static void main(String args[])
	{
		new AirLift ( args ).startSimulation();
	}
}
