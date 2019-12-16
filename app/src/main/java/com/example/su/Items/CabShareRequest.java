package com.example.su.Items;

import java.util.Date;

public class CabShareRequest {

    public static final int CAMPUS_TO_AIRPORT = 100;
    public static final int AIRPORT_TO_CAMPUS = 101;

    private String requesterID; //could be email or phone number
    private int cabType;
    private Date flightDateWithTime;
    private double waitTime;

    public CabShareRequest(String requesterID, int cabType, Date flightDateWithTime, double waitTime) {
        this.requesterID = requesterID;
        this.cabType = cabType;
        this.flightDateWithTime = flightDateWithTime;
        this.waitTime = waitTime;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public int getCabType() {
        return cabType;
    }

    public Date getFlightDateWithTime() {
        return flightDateWithTime;
    }

    public double getWaitTime() {
        return waitTime;
    }
}
