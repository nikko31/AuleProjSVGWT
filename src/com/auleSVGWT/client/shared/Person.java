package com.auleSVGWT.client.shared;

import java.io.Serializable;

/**
 * Created by darklinux on 14/03/16.
 */
public class Person implements Serializable {
    private String firstName;
    private String lastName;
    private String pRole;
    private String id;

    public Person(){

    }
    public Person(String id, String pRole, String lastName, String firstName) {
        this.id = id;
        this.pRole = pRole;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public PersonDetails getLightWeightPerson() {
        return new PersonDetails(id, getFullName());
    }

    public String getName() {
        return firstName;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return lastName;
    }

    public void setSurname(String lastName) {
        this.lastName = lastName;
    }

    public String getpRole() {
        return pRole;
    }

    public void setpRole(String pRole) {
        this.pRole = pRole;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
