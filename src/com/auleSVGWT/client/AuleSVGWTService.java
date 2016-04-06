package com.auleSVGWT.client;

import com.auleSVGWT.client.dto.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("AuleSVGWTService")
public interface AuleSVGWTService
        extends RemoteService {

    Integer saveRoom(RoomDTO roomDTO);

    Integer savePerson(PersonDTO personDTO);

    Long saveOccupy(OccupyDTO occupyDTO);

    Long deleteOccupy(Long id);

    Integer deletePerson(int id);

    ArrayList<OccupyDTO> getOccupy();

    Integer updatePerson(PersonDTO personDTO);

    Integer updateRole(RoleDTO roleDTO);

    ArrayList<RoleDTO> getRoles();

    Integer saveRole(RoleDTO roleDTO);

    ArrayList<PersonDTO> getPerson();

    RoomPeopleDTO getRoomPeople(String building, String floor, String number);

    ArrayList<RoomPeopleDTO> getRoomsPeople(String building, String floorSt);

    ArrayList<String> listaAulePiano(String edificiopiano);
}
