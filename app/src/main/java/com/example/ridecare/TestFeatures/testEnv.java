package com.example.ridecare.TestFeatures;
import com.example.ridecare.TestFeatures.VinDecoder;
public class testEnv {



    public static void main(String[] args) {
        VinDecoder validate = new VinDecoder();

        // user input
        String vin = "WBA3A5C0DF357891"; //  user adds their vin
        int yearInput = 1991; // collect user year input of vehicle
        // end user input

        System.out.println(validate.vinDecoderFinal(vin,yearInput));

        }
    }

