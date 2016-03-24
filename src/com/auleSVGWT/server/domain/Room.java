package com.auleSVGWT.server.domain;


import com.auleSVGWT.client.dto.RoomDTO;

import java.io.Serializable;

/**
 * Created by FEDE on 21/03/2016.
 */
public class Room implements Serializable {
    private int id;
    private int number;
    private int floor;
    private Building building;
    private int maxPeople;
    private int dimension;


    public Room() {
    }

    public Room(RoomDTO roomDTO) {
        this.id = roomDTO.getId();
        this.number = roomDTO.getNumber();
        this. floor = roomDTO.getFloor();
        this.building = new Building(roomDTO.getBuilding());
        this.dimension = roomDTO.getDimension();
        this.maxPeople = roomDTO.getMaxPerson();
        this.dimension = roomDTO.getDimension();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

}