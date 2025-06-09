package com.project.model;

public class PlaceOrder {
    public ShippingDetails shippingDetails;

    public static class ShippingDetails {
        public String fullName;
        public String address;
        public String email;
        public String mobileNumber;
    }
}
