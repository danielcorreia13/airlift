package SharedRegions;

import myLib.*;

import ActiveEntity.Passenger;
import ActiveEntity.Hostess;
import ActiveEntity.Pilot;
import Main.*;

//import genclass.TextFile;

import java.util.Objects;

public class GeneralRep 
{
	 /**
	   *  Name of the logging file.
	   */

	   private String logFileName;


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
	   *   Instantiation of a general repository object.
	   *
	   *     @param logFileName name of the logging file
	   *     @param nIter number of iterations of the customer life cycle
	   */
	   public GeneralRep (String logFileName)
	   {
	      if ((logFileName == null) || Objects.equals (logFileName, ""))
	         this.logFileName = "logger";
	         else this.logFileName = logFileName;

	      passengerState = new int [Settings.nPassengers];
	      for (int i = 0; i < Settings.nPassengers; i++)
	        passengerState[i] = Passenger.States.GOING_TO_AIRPORT;
	      
	      hostessState = Hostess.States.WAIT_FOR_NEXT_FLIGHT;
	      pilotState = Pilot.States.AT_TRANSFER_GATE;
	     	      
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
	    *     @param id customer id
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

	   /**
	    *   Set barber and customer state.
	    *
	    *     @param barberId barber id
	    *     @param barberState barber state
	    *     @param customerId customer id
	    *     @param customerState customer state
	    */
	    /*public synchronized void setBarberCustomerState (int barberId, int barberState, int customerId, int customerState)
	    {
	       this.barberState[barberId] = barberState;
	       this.customerState[customerId] = customerState;
	       reportStatus ();
	    }  */ 

	    
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
	      TextFile log = new TextFile ();                      // instantiation of a text file handler

	      if (!log.openForWriting (".", logFileName))
	         { 
	    	  	System.out.println ("The operation of creating the file " + logFileName + " failed!");
	    	  	System.exit (1);
	         }
	      log.writelnString ("                Problem of the Airlift");
	      //log.writelnString ("\nNumber of iterations = " + nIter + "\n");
	      log.writelnString (" Hostess  Pilot  Passenger 1  Passenger 2  Passenger 3  Passenger 4  Passenger 5  Passenger 6  Passenger 7  Passenger 8  Passenger 9  Passenger 10  Passenger 11  Passenger 12  Passenger 13  Passenger 14  Passenger 15  Passenger 16  Passenger 17  Passenger 18  Passenger 19  Passenger 20  Passenger 21");
	      if (!log.close ())
	         { 
	    	   	System.out.println ("The operation of closing the file " + logFileName + " failed!");
	    	   	System.exit (1);
	         }
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
	      TextFile log = new TextFile ();                      // instantiation of a text file handler

	      String lineStatus = "";                              // state line to be printed

	      if (!log.openForAppending (".", logFileName))
	         { 
	    	   System.out.println ("The operation of opening for appending the file " + logFileName + " failed!");
	           System.exit (1);
	         }
	      
	      for (int i = 0; i < Settings.nPassengers; i++)
	        switch (passengerState[i])
	        { 
	        	case Passenger.States.GOING_TO_AIRPORT:   lineStatus += " GOING_TO_AIRPORT ";
	                                        break;
	        	case Passenger.States.AT_DESTINATION: lineStatus += " AT_DESTINATION ";
	                                        break;
	        	case Passenger.States.IN_FLIGHT:   lineStatus += " IN_FLIGHT ";
                							break;
	        	case Passenger.States.IN_QUEUE: lineStatus += " IN_QUEUE ";
                							break;
	        }

	        switch (hostessState)
	        { 
	        	case Hostess.States.CHECK_PASSENGER:  lineStatus += " CHECK_PASSENGER ";
	                                             break;
	        	case Hostess.States.READY_TO_FLY: lineStatus += " READY_TO_FLY ";
	                                             break;
	        	case Hostess.States.WAIT_FOR_NEXT_FLIGHT: lineStatus += " WAIT_FOR_NEXT_FLIGHT ";
	                                             break;
	        	case Hostess.States.WAIT_FOR_PASSENGER: lineStatus += " WAIT_FOR_PASSENGER ";
	                                             break;
	        }
	        
	        switch (pilotState)
	        { 
	        	case Pilot.States.AT_TRANSFER_GATE:  lineStatus += " AT_TRANSFER_GATE ";
	                                             break;
	        	case Pilot.States.DEBOARDING: lineStatus += " DEBOARDING ";
	                                             break;
	        	case Pilot.States.FLYING_BACK: lineStatus += " FLYING_BACK ";
	                                             break;
	        	case Pilot.States.FLYING_FORWARD: lineStatus += " FLYING_FORWARD ";
	                                             break;
	        	case Pilot.States.READY_FOR_BOARDING: lineStatus += " READY_FOR_BOARDING ";
                								break;	
	        	case Pilot.States.WAIT_FOR_BOARDING: lineStatus += " WAIT_FOR_BOARDING ";
												break;               								
	        }
	        
	      log.writelnString (lineStatus);
	      if (!log.close ())
	         { 
	    	  	System.out.println ("The operation of closing the file " + logFileName + " failed!");
	    	  	System.exit (1);
	         }
	   }
	}
