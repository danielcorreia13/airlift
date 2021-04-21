package SharedRegions;

public class Plane
{

    private int state; //0 - dep / 1 - flight / 2 - dest

    private final GeneralRep generalRep;

    /**
     *  Plane instantiation.
     *
     *    @param repos reference to the general repository
     */

    public Plane(GeneralRep repos) {
        generalRep = repos;
        state = 0;
    }

    //----------------------------------------
    // Hostess

    public void informPlaneReadyToTakeOff() {

    }


    //----------------------------------------
    // Passenger

    public void boardThePlane() {

    }


    public void waitForEndOfFlight() {

    }

    //----------------------------------------
    // Pilot

    public void waitForAllInBoard() {

    }

    public void flyToDestinationPoint() {

    }

    public void flyToDeparturePoint() {

    }
}
