package com.auleSVGWT.server.domain;


import com.auleSVGWT.client.dto.RoleDTO;

import java.io.Serializable;


/**
 * Created by FEDE on 21/03/2016.
 */
public class Role implements Serializable {
    private int id;
    private String name;
    private int sqm;



    public Role() {
    }

    public Role(RoleDTO roleDTO) {
        this.id = roleDTO.getId();
        this.name = roleDTO.getName();
        this.sqm = roleDTO.getSqm();
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
