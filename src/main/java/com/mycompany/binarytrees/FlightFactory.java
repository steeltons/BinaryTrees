package com.mycompany.binarytrees;

import java.util.Random;

public class FlightFactory {

    private final static int LEFT_LIMIT = 65;
    private final static int RIGHT_LIMIT = 90;
    private final static Random RANDOM = new Random();

    public static Flight createFlight(){
        String flightCode = "";
        for(int i = 0; i < 2; i++){
            flightCode += (char) RANDOM.nextInt(LEFT_LIMIT, RIGHT_LIMIT);
        }
        int flightNumber = RANDOM.nextInt(1,20);
        return new Flight(flightCode, flightNumber);
    }
}
