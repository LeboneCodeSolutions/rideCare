// RegNumberValidation.java
package com.example.ridecare.TestFeatures;

import java.util.HashMap;

public class RegNumberValidation {

    private final HashMap<String, String> southAfricaPrefixes = new HashMap<>();

    // Constructor — this is where your .put() lines live
    public RegNumberValidation() {
        // Cape Town & Western Cape prefixes
        southAfricaPrefixes.put("CA", "Cape Town");
        southAfricaPrefixes.put("CAA", "Cape Town (overflow/new series)");
        southAfricaPrefixes.put("CAB", "Cape Town (newer expansion)");
        southAfricaPrefixes.put("CY", "Bellville");
        southAfricaPrefixes.put("CF", "Kuils River");
        southAfricaPrefixes.put("CJ", "Paarl");
        southAfricaPrefixes.put("CL", "Stellenbosch");
        southAfricaPrefixes.put("CEY", "Somerset West");
        southAfricaPrefixes.put("CK", "Malmesbury");
        southAfricaPrefixes.put("CM", "Worcester");
        southAfricaPrefixes.put("CN", "Tulbagh");
        southAfricaPrefixes.put("CR", "Caledon");
        southAfricaPrefixes.put("CS", "Bredasdorp");
        southAfricaPrefixes.put("CT", "Ceres");
        southAfricaPrefixes.put("CX", "Atlantis");
        southAfricaPrefixes.put("CZ", "Vredenburg");
        // Province codes
        southAfricaPrefixes.put("GP", "Gauteng");
        southAfricaPrefixes.put("ZN", "KwaZulu-Natal");
        southAfricaPrefixes.put("KZN", "KwaZulu-Natal");
        southAfricaPrefixes.put("EC", "Eastern Cape");
        southAfricaPrefixes.put("FS", "Free State");
        southAfricaPrefixes.put("LP", "Limpopo");
        southAfricaPrefixes.put("MP", "Mpumalanga");
        southAfricaPrefixes.put("NW", "North West");
        southAfricaPrefixes.put("NC", "Northern Cape");
    }

    // Public method your ViewModel will call
    public String identifyProvince(String plate) {
        // Input Cleaner
        String cleanPlate = plate.trim().toUpperCase().replaceAll("\\s+", "");

        //  Gauteng / ZN
        if (cleanPlate.matches("^[A-Z]{2}\\d{2}[A-Z]{2}(GP|ZN)$")) {
            String suffix = cleanPlate.substring(cleanPlate.length() - 2);
            return southAfricaPrefixes.get(suffix);
        }

        // Most provinces
        if (cleanPlate.matches("^[A-Z]{3}\\d{3}[A-Z]{2}$")) {
            String suffix = cleanPlate.substring(cleanPlate.length() - 2);
            if (southAfricaPrefixes.containsKey(suffix)) {
                return southAfricaPrefixes.get(suffix);
            }
        }

        //  Western Cape custom
        if (cleanPlate.matches("^[A-Z0-9]+WP$")) {
            return "Western Cape - Custom Plate";
        }

        //  Cape Town & surrounds: e.g. CA123
        if (cleanPlate.matches("^[A-Z]{1,3}[0-9]{1,6}$")) {
            String prefix = cleanPlate.replaceAll("[^A-Z]", "");
            if (southAfricaPrefixes.containsKey(prefix)) {
                return southAfricaPrefixes.get(prefix);
            }
        }

        return null;
    }
}