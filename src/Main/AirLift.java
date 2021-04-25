package Main;

import SharedRegions.*;
import ActiveEntity.*;


public class AirLift
{

	/**
	 *  Reference to Departure Airport
	 */
	private final DepartureAirport sharedDepartureAirport;

	/**
	 *  Reference to Destination Airport
	 */
	private final DestinationAirport sharedDestinationAirport;

	/**
	 *  Reference to Plane
	 */
	private final Plane sharedPlane;

	/**
	 *  Reference to the general repository of information
	 */
	private final GeneralRep generalRep;

	/**
	 *  Reference to Hostess
	 */
	private final Hostess hostess;

	/**
	 *  Reference to Pilot
	 */
	private final Pilot pilot;

	/**
	 *  References to Passengers
	 */
	private final Passenger[] passenger;

	/**
	 * Constructor
	 * Initializes all the shared regions and entities for the simulation
	 */
	public AirLift()
	{
		generalRep = new GeneralRep("LOG_FILE.txt");
		
		sharedDepartureAirport = new DepartureAirport(generalRep);		
		sharedPlane = new Plane(generalRep);
		sharedDestinationAirport = new DestinationAirport(generalRep, sharedPlane);
		
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

	/**
	 * Runs the simulation
	 * Launches the threads and waits for them to finish
	 */
	public void startSimulation()
	{
		System.out.println("Simulacao iniciada\n\n");
		
		pilot.start();
		hostess.start();
		for (Passenger p : passenger){
			p.start();
		}

		try {
			hostess.join();
			for (Passenger p : passenger) {
				p.join();
			}
			pilot.join();
		}catch (InterruptedException e){
			System.out.println("Something went wrong");
		}
		generalRep.endReport();

	}


	/**
	 * Main function
	 *
	 */
	
	public static void main(String args[])
	{
		new AirLift ().startSimulation();
	}
}
