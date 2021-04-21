package SharedRegions;

public class Plane
{

    private int state; //0 - dep / 1 - flight / 2 - dest

    public Plane() {
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
