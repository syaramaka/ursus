package com.doj.ursus.service.impl;

import com.doj.ursus.model.Civilians;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyEmpTest {

    public static void main(String[] args) {

        List<Employee> originalEmpList = getEmployeeList();
        System.out.println("originalEmplist---:"+originalEmpList);

        List<Employee> updatedEmpList = getUpdatedEmpList();
        System.out.println("updatedEmplist----:"+updatedEmpList);
        List<Employee> requiredEmpList = new ArrayList<>();

//        requiredEmpList = Stream.concat(updatedEmpList.stream(), originalEmpList.stream())
//                .distinct()
//                .collect(Collectors.toList());
//        System.out.println(" required emp list----:"+requiredEmpList);

        List<Employee> newList = originalEmpList.stream()
                .map(person -> updatedEmpList.stream()                                       // map Person to
                        .filter(i -> i.getEmpId()==(person.getEmpId()))  // .. the found Id
                        .findFirst().orElse(person))                    // .. or else to self
                .collect(Collectors.toList());

        System.out.println("newList--------:"+newList);
    }

    public static List<Employee> getEmployeeList()
    {
        List<Employee> employeeList = new ArrayList<>();
        Employee emp1 = new Employee(101,"SuneelKumar","Ind","524131");
        Employee emp2 = new Employee(102,"Koti","Ind","600100");
        Employee emp3 = new Employee(103,"Akhil","Ind","600113");
        employeeList.add(emp1);
        employeeList.add(emp2);
        employeeList.add(emp3);
        return employeeList;
    }

    public static List<Employee> getUpdatedEmpList()
    {
        List<Employee> employeeList = new ArrayList<>();
        Employee emp1 = new Employee(101,"SuneelReddy","Ind","524131");
        Employee emp2 = new Employee(103,"AkhilSai","Ind","600113");
        employeeList.add(emp1);
        employeeList.add(emp2);
        return employeeList;
    }
}
