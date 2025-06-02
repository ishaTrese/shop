package com.project.shop;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String buildingStreet;
    private String barangayDistrict;
    private String cityMunicipality;
    private String postalCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public User() {
    }

    // Constructor for creating a new user (without ID, timestamps)
    public User(String firstName, String lastName, String email, String mobileNumber,
                String buildingStreet, String barangayDistrict, String cityMunicipality,
                String postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.buildingStreet = buildingStreet;
        this.barangayDistrict = barangayDistrict;
        this.cityMunicipality = cityMunicipality;
        this.postalCode = postalCode;
    }

    // Constructor for retrieving a user (with all fields)
    public User(int userId, String firstName, String lastName, String email, String mobileNumber,
                String buildingStreet, String barangayDistrict, String cityMunicipality,
                String postalCode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.buildingStreet = buildingStreet;
        this.barangayDistrict = barangayDistrict;
        this.cityMunicipality = cityMunicipality;
        this.postalCode = postalCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBuildingStreet() {
        return buildingStreet;
    }

    public void setBuildingStreet(String buildingStreet) {
        this.buildingStreet = buildingStreet;
    }

    public String getBarangayDistrict() {
        return barangayDistrict;
    }

    public void setBarangayDistrict(String barangayDistrict) {
        this.barangayDistrict = barangayDistrict;
    }

    public String getCityMunicipality() {
        return cityMunicipality;
    }

    public void setCityMunicipality(String cityMunicipality) {
        this.cityMunicipality = cityMunicipality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", buildingStreet='" + buildingStreet + '\'' +
                ", barangayDistrict='" + barangayDistrict + '\'' +
                ", cityMunicipality='" + cityMunicipality + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
