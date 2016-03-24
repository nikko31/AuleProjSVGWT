package com.auleSVGWT.server.domain;



import com.auleSVGWT.client.dto.OccupyDTO;

import java.io.Serializable;

/**
 * Created by FEDE on 22/03/2016.
 */
public class Occupy implements Serializable {
    private long id;
    private Room room;
    private Person person;

    public Occupy() {
    }

    public Occupy(OccupyDTO occupyDTO) {
        this.id = occupyDTO.getId();
        this.room = new Room(occupyDTO.getRoom());
        this.person = new Person(occupyDTO.getPerson());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}