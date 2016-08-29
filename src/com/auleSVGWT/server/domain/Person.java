package com.auleSVGWT.server.domain;


import com.auleSVGWT.client.dto.PersonDTO;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by FEDE on 21/03/2016.
 */
public class Person implements Serializable {
    private int id;
    private String name;
    private String surname;
    private Role role;
    private Date startWork;
    private Date endWork;
    private String email;
    private String phone;

    public Person() {
    }

    public Person(PersonDTO personDTO) {
        if (personDTO.getId() >= 0)
            this.id = personDTO.getId();
        this.name = personDTO.getName();
        this.surname = personDTO.getSurname();
        this.role = new Role(personDTO.getRole());
        this.startWork = personDTO.getStartWork();
        this.endWork = personDTO.getEndWork();
        this.email = getEmail();
        this.phone = getPhone();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getStartWork() {
        return startWork;
    }

    public void setStartWork(Date startWork) {
        this.startWork = startWork;
    }

    public Date getEndWork() {
        return endWork;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEndWork(Date endWork) {
        this.endWork = endWork;
    }
}

