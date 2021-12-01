package com.mycompany.binarytrees;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Flight implements Comparable<Flight>, HashStructure<String, String>{

    private final String flightCode;
    private final char[] flightNumberCharArray = new char[4];

    public Flight(String flightCode, int flightNumber){
        this.flightCode = flightCode;
        int i = 0;
        while(flightNumber > 0) {
            flightNumberCharArray[3-i] = (char)(flightNumber % 10);
            flightNumber/= 10;
            i++;
        }
    }

    public String getFlightCode() {
        return flightCode;
    }

    public String getFlightNumber() {
        String line = "";
        for(char c : flightNumberCharArray){
            line+=(int)c;
        }
        return line;
    }

    @Override
    public int compareTo(Flight o) {
        if(flightCode.compareTo(o.flightCode) != 0)
            return flightCode.compareTo(o.flightCode);
        return Integer.compare(Integer.parseInt(this.getFlightNumber()), Integer.parseInt(o.getFlightNumber()));
    }

    @Override
    public String toString() {
        return "Flight{" + flightCode+":"+getFlightNumber()+'}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(flightCode, flight.flightCode) && Arrays.equals(flightNumberCharArray, flight.flightNumberCharArray);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(flightCode);
        result = 31 * result + Arrays.hashCode(flightNumberCharArray);
        return result;
    }

    @Override
    public String getKey() {
        return flightCode;
    }

    @Override
    public String getValue() {
        return getFlightNumber();
    }
}
