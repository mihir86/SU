package com.example.su.Items;

import java.util.Date;

public class CabShareRequest {

    public static final int CAMPUS_TO_AIRPORT = 100;
    public static final int AIRPORT_TO_CAMPUS = 101;

    private String documentID;
    private long requesterPhone;
    private long cabType;
    private Date flightDateWithTime;
    private double waitTime;

    public CabShareRequest(String documentID, long requesterPhone, long cabType, Date flightDateWithTime, double waitTime) {
        this.documentID = documentID;
        this.requesterPhone = requesterPhone;
        this.cabType = cabType;
        this.flightDateWithTime = flightDateWithTime;
        this.waitTime = waitTime;
    }

    public String getDocumentID() {
        return documentID;
    }

    public long getrequesterPhone() {
        return requesterPhone;
    }

    public long getCabType() {
        return cabType;
    }

    public Date getFlightDateWithTime() {
        return flightDateWithTime;
    }

    public double getWaitTime() {
        return waitTime;
    }

}
