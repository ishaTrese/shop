package com.project.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Signup {
    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    public String email;

    @JsonProperty("mobile_number")
    public String mobileNumber;

    @JsonProperty("building_street")
    public String buildingStreet;

    @JsonProperty("barangay_district")
    public String barangayDistrict;

    @JsonProperty("city_municipality")
    public String cityMunicipality;

    @JsonProperty("postal_code")
    public String postalCode;

    public String password;
}