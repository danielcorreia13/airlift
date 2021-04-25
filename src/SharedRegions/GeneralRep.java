package SharedRegions;

import myLib.*;

import ActiveEntity.Passenger;
import ActiveEntity.Hostess;
import ActiveEntity.Pilot;
import Main.*;

//import genclass.TextFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;

public class GeneralRep 
{
	 /**
	   *  Log file handler.
	   */

        private PrintWriter log;       


	  /**
	   *  State of the passengers.
	   */

	   private final int [] passengerState;

	  /**
	   *  State of the hostess.
	   */

	   private int hostessState;
	   
	   
	  /**
	   *  State of the hostess.
	   */
	   private int pilotState;

	/**
	 *  Current flight number
	 */

	private int flightId;
	
	 int nPassenger;

	/**
	 * Number of passengers per flight
	 */

	private int nPassFlight[];

//	/**
//	 *  Number of passengers in queue
//	 */
//
//	private int nPassQueue;
//
//	/**
//	 *  Number of passengers in queue
//	 */
//
//	private int nPassPlane;
//
//	/**
//	 *  Number of passengers in queue
//	 */
//
//	private int nPassArrived;
//	 private int hostess_last_state;


	/**
	   *   Instantiation of a general repository object.
	   *
	   *     @param logFileName name of the logging file
	   *
	   */
	   public GeneralRep (String logFileName)
	   {
	       try {
               if ((logFileName == null) || Objects.equals(logFileName, ""))
                   log = new PrintWriter("logger");
               else
                   log = new PrintWriter(logFileName);
           }catch (IOException e){
               System.out.println ("The operation of creating the file " + logFileName + " failed!");
               System.exit (1);
           }

	      passengerState = new int [Settings.nPassengers];
	      for (int i = 0; i < Settings.nPassengers; i++)
	        passengerState[i] = Passenger.States.GOING_TO_AIRPORT;
	      
	      hostessState = Hostess.States.WAIT_FOR_NEXT_FLIGHT;
	      pilotState = Pilot.States.AT_TRANSFER_GATE;
	      flightId = 0;
	      nPassenger = 0;
//	      this.hostess_last_state = 100;
	      nPassFlight = new int[10];
	      Arrays.fill(nPassFlight, 0);

	      reportInitialStatus ();
	   }
	   
	   
	    /*                                     SETTING STATES                            */
	    /*-------------------------------------------------------------------------------*/	   

	   /**
	    *   Set Passenger state.
	    *
	    *     @param id barber id
	    *     @param state barber state
	    */
	    public synchronized void setPassengerState (int id, int state)
	    {
	    	
	    	if (passengerState[id] != state) 
	    	{
		    	passengerState[id] = state;
		    	reportStatus ();
	    	}
	    	else	    		
	    		return;
	    }

	   /**
	    *   Set customer state.
	    *
	    *
	    *     @param state customer state
	    */
	    public synchronized void setPilotState (int state)
	    {
	    	if (pilotState != state)
	    	{
	    		pilotState = state;
	    		reportStatus ();
	    	}
	    	else
	    		return;
	    }
	    
	    public synchronized void setHostess (int state)
	    {
	    	if (hostessState != state)
	    	{
	    		hostessState = state;
	    		reportStatus ();
	    	}
	    	else	    		
	    		return;
	    }

	    public synchronized void nextFlight(){
	    		flightId++;
		}

		public synchronized void writeLog(String msg){
	    	log.println("\nFlight " + flightId + ": " + msg);
		}
	    
	    /*                                     STATUS                                    */
	    /*-------------------------------------------------------------------------------*/

	    /**
	    *  Write the header to the logging file.
	    *
	    *  The hostess is waiting for passenger and pilot is sleeping waiting for passengers on board
	    *  Passengers are going to airport
	    *  Internal operation.
	    */   
	   private void reportInitialStatus ()
	   {

	      log.println ("\n\tAirlift - Description of the internal state:\n\n");
	      //log.println ("\nNumber of iterations = " + nIter + "\n");
	      log.println ("  PT    HT   P00   P01   P02   P03   P04   P05   P06   P07   P08   P09   P10   P11   P12   P13   P14   P15   P16   P17   P18   P19   P20   InQ  InF  PTAL");
	      log.flush();
	      reportStatus ();
	   }
	   
	   /**
	    *  Write a state line at the end of the logging file.
	    *
	    *  The current state of the passengers, pilot and hostess is organized in a line to be printed.
	    *  Internal operation.
	    */	   
	private void reportStatus ()
	{
		int nPassQueue = 0;
		int nPassPlane = 0;
		int nPassArrived = 0;
		
		String lineStatus = "";                              // state line to be printed

		switch (pilotState)
		{
			case Pilot.States.AT_TRANSFER_GATE:  lineStatus += " ATRG ";
									 break;
			case Pilot.States.DEBOARDING: lineStatus += " DRPP ";
									 break;
			case Pilot.States.FLYING_BACK: lineStatus += " FLBK ";
									 break;
			case Pilot.States.FLYING_FORWARD: lineStatus += " FLFW ";
									 break;
			case Pilot.States.READY_FOR_BOARDING: lineStatus += " RDFB ";
									break;
			case Pilot.States.WAIT_FOR_BOARDING: lineStatus += " WTFB ";
									break;
		}

		switch (hostessState)
		{
			case Hostess.States.CHECK_PASSENGER:
//				hostess_last_state = Hostess.States.CHECK_PASSENGER;
				lineStatus += " CKPS ";
				break;
			//-----------------------------------------------------------------------------	
			case Hostess.States.READY_TO_FLY:
				lineStatus += " RDTF ";

//				for (int i = 0; i < Settings.nPassengers; i++)
//					if (passengerState[i] == Passenger.States.IN_FLIGHT)
//						nPassenger++;
//
//				if (Hostess.States.READY_TO_FLY != hostess_last_state)
//				{
//					hostess_last_state = Hostess.States.READY_TO_FLY;
//					log.println ("\nFlight "+ flightId +": Departed with "+nPassenger+" passengers ");
//				}
//
//				nPassenger = 0;
				break;
			//------------------------------------------------------------------------------	
			case Hostess.States.WAIT_FOR_NEXT_FLIGHT: lineStatus += " WTFL ";
//				hostess_last_state = Hostess.States.WAIT_FOR_NEXT_FLIGHT;
				break;
			case Hostess.States.WAIT_FOR_PASSENGER: lineStatus += " WTFP ";
//				hostess_last_state = Hostess.States.WAIT_FOR_NEXT_FLIGHT;
				break;
		}


		for (int i = 0; i < Settings.nPassengers; i++)
			switch (passengerState[i])
			{
				case Passenger.States.GOING_TO_AIRPORT:   lineStatus += " GTAP ";
					break;
				case Passenger.States.AT_DESTINATION: lineStatus += " ATDS "; nPassArrived++;
					break;
				case Passenger.States.IN_FLIGHT:   lineStatus += " INFL "; nPassPlane++;
					break;
				case Passenger.States.IN_QUEUE: lineStatus += " INQE "; nPassQueue++;
					break;
			}

		lineStatus += "  " + nPassQueue + "    " + nPassPlane + "    " + nPassArrived;
		if (hostessState == Hostess.States.READY_TO_FLY){
			if (nPassPlane > nPassFlight[flightId-1])
				nPassFlight[flightId-1] = nPassPlane;
		}
		log.println (lineStatus);
		log.flush();
	}

    /**
     *  Close the logging file
     */

    public void endReport(){
    	log.println("\nAirlift sum up:");
		for (int i = 0; i < nPassFlight.length; i++) {
			if (nPassFlight[i] == 0) break;
			log.println("Flight " + (i+1) + " transported " + nPassFlight[i] + " passengers");
		}
        log.close();
    }
}
