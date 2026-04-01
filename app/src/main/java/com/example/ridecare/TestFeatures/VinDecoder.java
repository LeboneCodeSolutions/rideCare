package com.example.ridecare.TestFeatures;

import java.util.HashMap;
import java.util.Map;

public class VinDecoder {

    private static final HashMap<String, String> wmiMap = new HashMap<>();
    private final Map<Character, Integer> vinYearMapPost2009 = new HashMap<>(); // Year codes
    private  final Map<Character, Integer> vinYearMapPre2009= new HashMap<>();


    //  private Map<String, String> countryMap = new HashMap<>(); // Country codes
    public VinDecoder() {
        // 🇯🇵 Japan
        wmiMap.put("JT", "Toyota");
        wmiMap.put("JH", "Honda");
        wmiMap.put("JN", "Nissan");
        wmiMap.put("JM", "Mazda");
        wmiMap.put("JS", "Suzuki");
        // 🇩🇪 Germany
        wmiMap.put("WBA", "BMW");
        wmiMap.put("WDB", "Mercedes-Benz");
        wmiMap.put("WDC", "Mercedes-Benz");
        wmiMap.put("WAU", "Audi");
        wmiMap.put("WVW", "Volkswagen");
        // 🇺🇸 USA
        wmiMap.put("1F", "Ford");
        wmiMap.put("1G", "Chevrolet");
        wmiMap.put("5YJ", "Tesla");
        // 🇰🇷 Korea
        wmiMap.put("KMH", "Hyundai");
        wmiMap.put("KNA", "Kia");
        // 🇫🇷 France
        wmiMap.put("VF1", "Renault");
        wmiMap.put("VF3", "Peugeot");
        wmiMap.put("VF7", "Citroen");
        // 🇬🇧 UK
        wmiMap.put("SAL", "Land Rover");
        wmiMap.put("SAJ", "Jaguar");
        // 🇮🇹 Italy
        wmiMap.put("ZFF", "Ferrari");
        wmiMap.put("ZFA", "Fiat");
        // 🇸🇪 Sweden
        wmiMap.put("YV1", "Volvo");
        // 🇿🇦 South Africa (local production)
        wmiMap.put("AAV", "Volkswagen");
        wmiMap.put("ADD", "Toyota");


        ///  Vin Year  Code (Y)

        //Pre- 2009
        // 1980 - 1989
        vinYearMapPre2009.put('A', 1980);
        vinYearMapPre2009.put('B', 1981);
        vinYearMapPre2009.put('C', 1982);
        vinYearMapPre2009.put('D', 1983);
        vinYearMapPre2009.put('E', 1984);
        vinYearMapPre2009.put('F', 1985);
        vinYearMapPre2009.put('G', 1986);
        vinYearMapPre2009.put('H', 1987);
        vinYearMapPre2009.put('J', 1988); // I skipped
        vinYearMapPre2009.put('K', 1989);

// 1990 - 1999
        vinYearMapPre2009.put('L', 1990);
        vinYearMapPre2009.put('M', 1991);
        vinYearMapPre2009.put('N', 1992);
        vinYearMapPre2009.put('P', 1993); // O skipped
        vinYearMapPre2009.put('R', 1994);
        vinYearMapPre2009.put('S', 1995);
        vinYearMapPre2009.put('T', 1996);
        vinYearMapPre2009.put('V', 1997);
        vinYearMapPre2009.put('W', 1998);
        vinYearMapPre2009.put('X', 1999);

// 2000 - 2009 (numbers)
        vinYearMapPre2009.put('Y', 2000);
        vinYearMapPre2009.put('1', 2001);
        vinYearMapPre2009.put('2', 2002);
        vinYearMapPre2009.put('3', 2003);
        vinYearMapPre2009.put('4', 2004);
        vinYearMapPre2009.put('5', 2005);
        vinYearMapPre2009.put('6', 2006);
        vinYearMapPre2009.put('7', 2007);
        vinYearMapPre2009.put('8', 2008);
        vinYearMapPre2009.put('9', 2009);

        //Post 2009
        // 2010 - 2019
        vinYearMapPost2009.put('A', 2010);
        vinYearMapPost2009.put('B', 2011);
        vinYearMapPost2009.put('C', 2012);
        vinYearMapPost2009.put('D', 2013);
        vinYearMapPost2009.put('E', 2014);
        vinYearMapPost2009.put('F', 2015);
        vinYearMapPost2009.put('G', 2016);
        vinYearMapPost2009.put('H', 2017);
        vinYearMapPost2009.put('J', 2018); // I skipped
        vinYearMapPost2009.put('K', 2019);

// 2020 - 2029
        vinYearMapPost2009.put('L', 2020);
        vinYearMapPost2009.put('M', 2021);
        vinYearMapPost2009.put('N', 2022);
        vinYearMapPost2009.put('P', 2023); // O skipped
        vinYearMapPost2009.put('R', 2024);
        vinYearMapPost2009.put('S', 2025);
        vinYearMapPost2009.put('T', 2026);
        vinYearMapPost2009.put('V', 2027);
        vinYearMapPost2009.put('W', 2028);
        vinYearMapPost2009.put('X', 2029);

// 2030 - 2038
        vinYearMapPost2009.put('1', 2030);
        vinYearMapPost2009.put('2', 2031);
        vinYearMapPost2009.put('3', 2032);
        vinYearMapPost2009.put('4', 2033);
        vinYearMapPost2009.put('5', 2034);
        vinYearMapPost2009.put('6', 2035);
        vinYearMapPost2009.put('7', 2036);
        vinYearMapPost2009.put('8', 2037);
        vinYearMapPost2009.put('9', 2038);

    }

    public Integer yearChecker(String vinUpper, int year) {
        // take users input from year and proceess it
        char position = vinUpper.charAt(9);
        if (year < 2009) {
            // process it as a pre 2009 vin code
            if (vinYearMapPre2009.containsKey(position)) {
                return vinYearMapPre2009.get(position);
            }
        }
        if (year > 2009) {
            if (vinYearMapPost2009.containsKey(position )) {
                return vinYearMapPost2009.get(position);
            }
        }
        return null;
    }
    public String wmiChecker (String vin){
            String vinUpper = vin.toUpperCase();
            String wmi3 = vinUpper.substring(0, 3);
            if (wmiMap.containsKey(wmi3)) {
                return wmiMap.get(wmi3);
            }

            // Fallback to 2-character match
            String wmi2 = vinUpper.substring(0, 2);
            for (String key : wmiMap.keySet()) {
                if (key.length() == 2 && key.equals(wmi2)) {
                    return wmiMap.get(key);
                }
            }
            return "Unknown manufacturer";
        }
public String vinDecoderFinal(String vin, int yearInput){
    if (vin == null || vin.length() != 17) {
        return "Invalid VIN";
    }
    String vinUpper = vin.toUpperCase();
    int valid = yearChecker(vinUpper, yearInput);

    if(yearInput != valid){
        return "Mismatched VIN - Enquire";
    }
    else{
        return "System Verified";
    }
}




       /* public String decodeVin (String vin){
            if (vin == null || vin.length() != 17) {
                return "Invalid VIN (must be 17 characters)";
            }
            String vinUpper = vin.toUpperCase();
            yearChecker(vinUpper, 2009);
            wmiChecker(vinUpper);
            return "Unknown manufacturer";
        } */

}
