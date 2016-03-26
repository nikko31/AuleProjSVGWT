package com.auleSVGWT.client;

import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.client.shared.Person;
import com.auleSVGWT.client.shared.PersonDetails;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface AuleSVGWTServiceAsync {
    void saveRoom(RoomDTO roomDTO,AsyncCallback<Integer> async);
    void savePerson(PersonDTO personDTO,AsyncCallback<Integer> async);
    void saveOccupy(OccupyDTO occupyDTO,AsyncCallback<Long> async);
    void deleteOccupy(Long id,AsyncCallback<Long> async);
    void deletePerson(int id,AsyncCallback<Integer> async);
    void getOccupy(AsyncCallback<ArrayList<OccupyDTO>> async);
    void updatePerson(PersonDTO personDTO,AsyncCallback<Integer> async);
    void updateRole(RoleDTO roleDTO,AsyncCallback<Integer> async);
    void getRoles(AsyncCallback<ArrayList<RoleDTO>> async);
    void saveRole(RoleDTO roleDTO,AsyncCallback<Integer> async);
    void getRoomPeople (String building, String floor,String number,AsyncCallback<RoomPeopleDTO> async);

    void listaAulePiano(String edificiopiano, AsyncCallback<ArrayList<String>> async);

    void getPerson(AsyncCallback<ArrayList<PersonDTO>> async);

/*    //Map<String, ArrayList<String>> getMessage(String msg, AsyncCallback<String> async);

    // Sample interface method of remote interface
    void getMessage(String ed, String aula, AsyncCallback<ArrayList<Stanza>> async);
    void getRoles(AsyncCallback<ArrayList<String>> async);
    void addPersona(Persona persona, AsyncCallback<Void> async);
    //aggiunte
    void  listaEdiPiani(AsyncCallback<ArrayList<String>> async);
    void listaAulePiano(String edipiano, AsyncCallback<ArrayList<String>> async);

    void getPersonsDetails(String edificio, String aula, AsyncCallback<ArrayList<PersonDetails>> async);

    void getInvadersDetails(String building, String floor, String number, AsyncCallback<ArrayList<PersonDetails>> async);

    void getRoomInvader(String id, AsyncCallback<Person> async);*/
}
