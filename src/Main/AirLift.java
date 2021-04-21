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
	// Regiões partilhadas
	private final DepartureAirport sharedDepartureAirport;
	private final DestinationAirport sharedDestinationAirport;
	private final Plane sharedPlane;
	
	// Entidades ativas
	private final Hostess hostess;	
	private final Pilot pilot;
	private final Passenger[] passenger;

	// Configuração
	private final Integer TTL_PASSENGER;
	private final Integer MAX_PASSENGER;
	private final Integer MIN_PASSENGER;
	
	public AirLift( String args[] )
	{
		TTL_PASSENGER = 21; // Passageiros que participam na simulação
		MAX_PASSENGER = 10; // Capacidade máxima do avião
		MIN_PASSENGER = 5; // Capacidade mínima do avião
		
		// Instanciar as regiões partilhadas
		sharedDepartureAirport = new DepartureAirport();
		sharedDestinationAirport = new DestinationAirport();
		sharedPlane = new Plane();
		
		// Instanciar as entidades ativas (Threads) - Para já são passadas as interfaces que cada um usa.
		pilot = new Pilot( (IDepartureAirport_Pilot) sharedDepartureAirport, (IDestinationAirport_Pilot) sharedDestinationAirport, (IPlane_Pilot) sharedPlane /* Passar mais argumentos*/ );		
		hostess = new Hostess( (IDepartureAirport_Hostess) sharedDepartureAirport, (IPlane_Hostess) sharedPlane /* Passar mais argumentos*/);
		
			// Como existem vários passageiros
		passenger = new Passenger[TTL_PASSENGER];
		
		for (int i = 0; i < MAX_PASSENGER; i++)
		{
			passenger[i] = new Passenger( (IDepartureAirport_Passenger) sharedDepartureAirport, (IDestinationAirport_Passenger) sharedDestinationAirport, (IPlane_Passenger) sharedPlane /* Passar mais argumentos*/);	
		}
		
	}
	
	// Iniciar a simulação
	public void startSimulation()
	{
		System.out.println("Simulação iniciada");
		
		// Iniciar os threads
		
		
		// Esperar que os threads morram
	}
	
	
	
	
	public static void main(String args[])
	{
		new AirLift ( args ).startSimulation();
	}
}
