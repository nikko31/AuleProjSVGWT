package com.auleSVGWT.client.dto;

import java.io.Serializable;

/**
 * Created by FEDE on 21/03/2016.
 */
public class RoleDTO implements Serializable {
    private int id;
    private String name;
    private int sqm;

    public RoleDTO() {
    }

    public RoleDTO(int id, String name, int sqm){
        this.id = id;
        this.name = name;
        this.sqm = sqm;

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

    public int getSqm() {
        return sqm;
    }

    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

}
