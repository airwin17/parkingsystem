package com.parkit.parkingsystem.service;


import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration = outHour.getTime() - inHour.getTime();
        double durationInHour=(double) duration/(double)TimeUnit.HOURS.toMillis(1);

        if(durationInHour>0.5){
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticket.setPrice(durationInHour * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(durationInHour * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }else
            ticket.setPrice(0);
    }
    public void calculateFare(Ticket ticket,boolean discount){
        calculateFare(ticket);
        if(discount){
            double currentPrice=ticket.getPrice();
            double savedMoney=currentPrice*5/100;
            ticket.setPrice(currentPrice-savedMoney);
        }
    }

}