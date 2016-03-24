package com.auleSVGWT.client.dto;

import java.io.Serializable;

/**
 * Created by FEDE on 21/03/2016.
 */
public class BuildingDTO implements Serializable {
    private int number;
    private String name;

    public BuildingDTO() {
    }
    public BuildingDTO( String name) {
        this.name = name;
    }

    public BuildingDTO(int number, String name) {
        this.name = name;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
