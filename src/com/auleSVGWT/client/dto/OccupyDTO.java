package com.auleSVGWT.client.dto;

import java.io.Serializable;

/**
 * Created by FEDE on 22/03/2016.
 */
public class OccupyDTO implements Serializable {
    private long id;
    private RoomDTO room;
    private PersonDTO person;

    public OccupyDTO() {
    }

    public OccupyDTO(RoomDTO room, PersonDTO person) {

        this.room = room;
        this.person = person;
    }

    public OccupyDTO(long id, RoomDTO room, PersonDTO person) {
        this.id = id;
        this.room = room;
        this.person = person;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }
}
