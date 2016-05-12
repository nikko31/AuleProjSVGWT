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
    private int socket;
    private String maintenance;
    private String roomCode;


    public RoomDTO() {

    }
    public RoomDTO( int number, int floor, BuildingDTO building, int maxPeople, int dimension, String roomCode) {

        this.number = number;
        this.floor = floor;
        this.building = building;
        this.maxPeople = maxPeople;
        this.dimension = dimension;
        this.roomCode = roomCode;
    }

    public RoomDTO(int id, int number, int floor, BuildingDTO building, int maxPeople, int dimension, String roomCode) {
        this.id = id;
        this.number = number;
        this.floor = floor;
        this.building = building;
        this.maxPeople = maxPeople;
        this.dimension = dimension;
        this.roomCode =roomCode;
    }

    public RoomDTO(int id, int number, int floor, BuildingDTO building, int maxPeople, int dimension, String roomCode,String maintenance,int socket) {
        this.id = id;
        this.number = number;
        this.floor = floor;
        this.building = building;
        this.maxPeople = maxPeople;
        this.dimension = dimension;
        this.roomCode = roomCode;
        this.socket = socket;
        this.maintenance = maintenance;
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

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getSocket() {
        return socket;
    }

    public void setSocket(int socket) {
        this.socket = socket;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
}


