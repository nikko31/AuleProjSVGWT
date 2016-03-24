package com.auleSVGWT.server.domain;


import com.auleSVGWT.client.dto.BuildingDTO;

import java.io.Serializable;

/**
 * Created by FEDE on 21/03/2016.
 */
public class Building implements Serializable {
    private int number;
    private String name;


    public Building() {
    }

    public Building(BuildingDTO buildingDTO){
        this.number = buildingDTO.getNumber();
        this.name = buildingDTO.getName();

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
