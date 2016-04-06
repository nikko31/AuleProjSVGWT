package com.auleSVGWT.client;

import com.auleSVGWT.client.dto.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

public interface AuleSVGWTServiceAsync {
    void saveRoom(RoomDTO roomDTO, AsyncCallback<Integer> async);

    void savePerson(PersonDTO personDTO, AsyncCallback<Integer> async);

    void saveOccupy(OccupyDTO occupyDTO, AsyncCallback<Long> async);

    void deleteOccupy(Long id, AsyncCallback<Long> async);

    void deletePerson(int id, AsyncCallback<Integer> async);

    void getOccupy(AsyncCallback<ArrayList<OccupyDTO>> async);

    void updatePerson(PersonDTO personDTO, AsyncCallback<Integer> async);

    void updateRole(RoleDTO roleDTO, AsyncCallback<Integer> async);

    void getRoles(AsyncCallback<ArrayList<RoleDTO>> async);

    void saveRole(RoleDTO roleDTO, AsyncCallback<Integer> async);

    void getRoomPeople(String building, String floor, String number, AsyncCallback<RoomPeopleDTO> async);


    void listaAulePiano(String edificiopiano, AsyncCallback<ArrayList<String>> async);

    void getPerson(AsyncCallback<ArrayList<PersonDTO>> async);

    void getRoomsPeople(String building, String floorSt, AsyncCallback<ArrayList<RoomPeopleDTO>> async);

    void  getBuildingsFileName(AsyncCallback<HashMap<String,ArrayList<String>>> async);

}
