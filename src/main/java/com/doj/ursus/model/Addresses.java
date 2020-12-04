package com.doj.ursus.model;

import java.util.List;

public class Addresses {
    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Addresses{" +
                "addresses=" + addresses +
                '}';
    }
}
