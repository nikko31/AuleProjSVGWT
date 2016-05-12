package com.auleSVGWT.client.dto;


import java.io.Serializable;
import java.sql.Date;

/**
 * Created by FEDE on 21/03/2016.
 */
public class PersonDTO implements Serializable {
    private int id;
    private String name;
    private String surname;
    private RoleDTO role;
    private Date startWork;
    private Date endWork;


    public PersonDTO() {
        this.id = -1;
    }

    public PersonDTO(String name, String surname, RoleDTO role) {

        this.name = name;
        this.surname = surname;
        this.role = role;

    }

    public PersonDTO(int id, String name, String surname, RoleDTO role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.role = role;
        startWork = null;
        endWork = null;

    }

    public PersonDTO(int id, String name, String surname, RoleDTO role,Date startWork,Date endWork) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.startWork = startWork;
        this.endWork = endWork;

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

    public String getDetails() {
        return surname + " " + name;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
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

    public void setEndWork(Date endWork) {
        this.endWork = endWork;
    }
}

