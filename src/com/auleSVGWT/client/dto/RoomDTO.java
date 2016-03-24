package com.auleSVGWT.client.dto;

import java.io.Serializable;

/**
 * Created by FEDE on 21/03/2016.
 */
public class RoomDTO implements Serializable {

    private int id;
    private int number;
    private int floor;
    private BuildingDTO building;
    private int maxPeople;
    private int dimension;


    public RoomDTO() {

    }
    public RoomDTO( int number, int floor, BuildingDTO building, int maxPeople, int dimension) {

        this.number = number;
        this.floor = floor;
        this.building = building;
        this.maxPeople = maxPeople;
        this.dimension = dimension;
    }

    public RoomDTO(int id, int number, int floor, BuildingDTO building, int maxPeople, int dimension) {
        this.id = id;
        this.number = number;
        this.floor = floor;
        this.building = building;
        this.maxPeople = maxPeople;
        this.dimension = dimension;
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


    public int getMaxPerson() {
        return maxPeople;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPeople = maxPerson;
    }

    public BuildingDTO getBuilding() {
        return building;
    }

    public void setBuilding(BuildingDTO building) {
        this.building = building;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
