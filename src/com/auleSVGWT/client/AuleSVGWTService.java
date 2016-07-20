package com.auleSVGWT.client;

import com.auleSVGWT.client.dto.*;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.HashMap;

@RemoteServiceRelativePath("AuleSVGWTService")
public interface AuleSVGWTService
        extends RemoteService {

    Integer saveRoom(RoomDTO roomDTO);

    Integer savePerson(PersonDTO personDTO);

    Long saveOccupy(OccupyDTO occupyDTO);

    Long deleteOccupy(Long id);

    Long saveRoomOccupy(ArrayList<Long> ids, ArrayList<OccupyDTO> occupyDTOs);

    Integer deletePerson(int id);

    ArrayList<OccupyDTO> getOccupy();

    ArrayList<OccupyDTO> getOccupySearch(String param1,String param2);

    Integer updatePerson(PersonDTO personDTO);

    Integer updateRole(RoleDTO roleDTO);

    ArrayList<RoleDTO> getRoles();

    Integer saveRole(RoleDTO roleDTO);

    ArrayList<PersonDTO> getPerson();

    RoomPeopleDTO getRoomPeople(String building, String floor, String number);

    ArrayList<RoomPeopleDTO> getRoomsPeople(String building, String floorSt);

    ArrayList<String> listaAulePiano(String text);
    ArrayList<String> listaAulePianoNewVersion(String text);

    HashMap<String,ArrayList<String>> getBuildingsFileName();
}
