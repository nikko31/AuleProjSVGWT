package com.auleSVGWT.client.shared;

import org.vectomatic.dom.svg.OMSVGElement;

import java.io.Serializable;

/**
 * Created by darklinux on 17/03/16.
 */
public class FloorDetails implements Serializable {
    private String building;
    private String floor;
    private OMSVGElement roomSvg;

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    private String mapType;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public FloorDetails(String building, String floor) {

        this.building = building;
        this.floor = floor;
    }
    public FloorDetails(String building, String floor,OMSVGElement roomSvg) {

        this(building,floor);
        this.roomSvg=roomSvg;
    }

    public OMSVGElement getRoomSvg() {
        return roomSvg;
    }

    public void setRoomSvg(OMSVGElement roomSvg) {
        this.roomSvg = roomSvg;
    }

    public FloorDetails() {
    }
}
