package SharedRegions;

import myLib.*;

import ActiveEntity.Passenger;
import ActiveEntity.Hostess;
import ActiveEntity.Pilot;
import Main.*;

//import genclass.TextFile;

import java.io.IOException;
import java.io.PrintWriter;
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
	       passengerState[id] = state;
	       reportStatus ();
	    }

	   /**
	    *   Set customer state.
	    *
	    *
	    *     @param state customer state
	    */
	    public synchronized void setPilotState (int state)
	    {
	       pilotState = state;
	       reportStatus ();
	    }
	    
	    public synchronized void setHostess (int state)
	    {
	       hostessState = state;
	       reportStatus ();
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

	      log.println ("                Problem of the Airlift");
	      //log.println ("\nNumber of iterations = " + nIter + "\n");
	      log.println ("   PT   HT   P01   P02   P03   P04   P05   P06   P07   P08   P09   P10   P11   P12   P13   P14   P15   P16   P17   P18   P19   P20   P21  inQu inF PTAL");
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
			case Hostess.States.CHECK_PASSENGER:  lineStatus += " CKPS ";
				break;
			case Hostess.States.READY_TO_FLY: lineStatus += " RDTF ";
				break;
			case Hostess.States.WAIT_FOR_NEXT_FLIGHT: lineStatus += " WFNF ";
				break;
			case Hostess.States.WAIT_FOR_PASSENGER: lineStatus += " WTFP ";
				break;
		}

		int nPassQueue = 0;
		int nPassPlane = 0;
		int nPassArrived = 0;
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

		log.println (lineStatus);
		log.flush();
	}

    /**
     *  Close the logging file
     */

    public void endReport(){
        log.close();
    }
}
