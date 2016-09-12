package com.example.sharathn.newnavi;

/**
 * Created by SHARATH N on 5/14/2016.
 */
public class Tenant {


    private String address, street, city, state, rooms, bathrooms, sqft, rentmax, rentmin, contact, email, description, deposit,prop;
    private String zipcode;

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setMin(String rent) {
        this.rentmin = rent;
    }

    public void setMax(String rent) {
        this.rentmax = rent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPropertytype(String prop) {
        this.prop= prop;
    }
    public String getLocation() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getPropertytype() {
        return prop;
    }

    public String getMin() {
        return rentmin;
    }

    public String getMax() {
        return rentmax;
    }

    public String getDescription() {
        return description;
    }


}



