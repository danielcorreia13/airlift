package Main;

import ActiveEntity.Hostess;
import ActiveEntity.Passenger;
import ActiveEntity.Pilot;

import DepartureAirport.DepartureAirport;
import DepartureAirport.IDepartureAirport_Hostess;
import DepartureAirport.IDepartureAirport_Passenger;
import DepartureAirport.IDepartureAirport_Pilot;

import DestinationAirport.DestinationAirport;
import DestinationAirport.IDestinationAirport_Passenger;
import DestinationAirport.IDestinationAirport_Pilot;

import Plane.IPlane_Hostess;
import Plane.IPlane_Passenger;
import Plane.IPlane_Pilot;
import Plane.Plane;

public class AirLift
{
	// Regi�es partilhadas
	private final DepartureAirport sharedDepartureAirport;
	private final DestinationAirport sharedDestinationAirport;
	private final Plane sharedPlane;
	
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
		sharedDepartureAirport = new DepartureAirport();
		sharedDestinationAirport = new DestinationAirport();
		sharedPlane = new Plane();
		
		// Instanciar as entidades ativas (Threads) - Para j� s�o passadas as interfaces que cada um usa.
		pilot = new Pilot( (IDepartureAirport_Pilot) sharedDepartureAirport, (IDestinationAirport_Pilot) sharedDestinationAirport, (IPlane_Pilot) sharedPlane /* Passar mais argumentos*/ );		
		hostess = new Hostess( (IDepartureAirport_Hostess) sharedDepartureAirport, (IPlane_Hostess) sharedPlane /* Passar mais argumentos*/);
		
			// Como existem v�rios passageiros
		passenger = new Passenger[TTL_PASSENGER];
		
		for (int i = 0; i < MAX_PASSENGER; i++)
		{
			passenger[i] = new Passenger( (IDepartureAirport_Passenger) sharedDepartureAirport, (IDestinationAirport_Passenger) sharedDestinationAirport, (IPlane_Passenger) sharedPlane /* Passar mais argumentos*/);	
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
