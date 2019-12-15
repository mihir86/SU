package com.example.su.Items;

import java.util.Date;

public class CabShareRequest {

    private String requesterID;
    private int cabType;
    private Date flightDateWithTime;
    private Date flexibility;

    public CabShareRequest(String requesterID, int cabType, Date flightDateWithTime, Date flexibility) {
        this.requesterID = requesterID;
        this.cabType = cabType;
        this.flightDateWithTime = flightDateWithTime;
        this.flexibility = flexibility;
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

    public Date getFlexibility() {
        return flexibility;
    }
}
