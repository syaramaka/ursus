package com.doj.ursus.service.impl;

import java.util.Objects;

public class Employee {

    private int empId;
    private String empName;
    private String country;
    private String zip;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return empId == employee.empId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", empName='" + empName + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }

    public Employee(int empId, String empName, String country, String zip) {
        this.empId = empId;
        this.empName = empName;
        this.country = country;
        this.zip = zip;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
