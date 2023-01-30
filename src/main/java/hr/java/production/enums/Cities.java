package hr.java.production.enums;

/**
 * Holds information about city name and postal code.
 */
public enum Cities {
    CITY_ZAGREB("ZAGREB", "10000"),
    CITY_VUKOVAR("VUKOVAR", "32000"),
    CITY_DJAKOVO("ĐAKOVO", "31400");
    private String cityName, postalCode;

    Cities(String cityName, String postlaCode) {
        this.cityName = cityName;
        this.postalCode = postlaCode;
    }

    public String getCityName() {
        return cityName;
    }

    public String getPostlaCode() {
        return postalCode;
    }

}
