package com.auleSVGWT.client.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class RoomPeopleDTO implements Serializable {

    private RoomDTO roomDTO;
    private ArrayList<PersonDTO> peopleDTO;
    private ArrayList<Long> occId;

    public RoomPeopleDTO() {
        this.peopleDTO = new ArrayList<>();
        this.occId = new ArrayList<>();
    }

    public RoomPeopleDTO(RoomDTO roomDTO, ArrayList<PersonDTO> peopleDTO, ArrayList<Long> occId) {
        this.peopleDTO = new ArrayList<>();
        this.occId = new ArrayList<>();//servono altrimenti nonho id della relazione
        this.roomDTO = roomDTO;
        this.peopleDTO.addAll(peopleDTO);
        this.occId.addAll(occId);

    }

    public RoomDTO getRoomDTO() {
        return roomDTO;
    }

    public void setRoomDTO(RoomDTO roomDTO) {
        this.roomDTO = roomDTO;
    }

    public ArrayList<PersonDTO> getPeopleDTO() {
        return peopleDTO;
    }

    public void setPeopleDTO(ArrayList<PersonDTO> peopleDTO) {
        this.peopleDTO = peopleDTO;
    }

    public ArrayList<Long> getOccId() {
        return occId;
    }

    public void setOccId(ArrayList<Long> occId) {
        this.occId = occId;
    }
}