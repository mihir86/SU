package com.example.su.Items;

import java.util.Date;

public class CabShareSimilarRequest {

    private long phoneNumber;
    private Date dateAndTimeOfFlight;
    private long flexibility;

    public CabShareSimilarRequest(long phoneNumber, Date dateAndTimeOfFlight, long flexibility) {
        this.phoneNumber = phoneNumber;
        this.dateAndTimeOfFlight = dateAndTimeOfFlight;
        this.flexibility = flexibility;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDateAndTimeOfFlight() {
        return dateAndTimeOfFlight;
    }

    public long getFlexibility() {
        return flexibility;
    }
}
