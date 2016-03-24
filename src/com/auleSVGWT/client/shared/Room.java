package com.auleSVGWT.client.shared;

import java.io.Serializable;

/**
 * Created by darklinux on 14/03/16.
 */
public class Room implements Serializable {
    private String number;
    private String id;
    private Integer maxSeats;
    private Integer mtQ;

    public Room(String number, String id, Integer maxSeats, Integer mtQ) {

        this.number = number;
        this.id = id;
        this.maxSeats = maxSeats;
        this.mtQ = mtQ;
    }

    public Room() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }

    public Integer getMtQ() {
        return mtQ;
    }

    public void setMtQ(Integer mtQ) {
        this.mtQ = mtQ;
    }
}
