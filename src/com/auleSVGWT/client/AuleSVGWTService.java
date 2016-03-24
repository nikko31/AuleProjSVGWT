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
    RoomPeopleDTO getRoomPeople(String building, String floor, String number);
    ArrayList<String> listaAulePiano(String edificiopiano);
}
/*
@RemoteServiceRelativePath("AuleSVGWTService")
public interface AuleSVGWTService extends RemoteService {
    // Sample interface method of remote interface
    ArrayList<Stanza> getMessage(String edificio, String aula);
    ArrayList<String> getRoles();
    void addPersona(Persona persona);
    ArrayList<PersonDetails> getPersonsDetails(String edificio,String aula);

    ArrayList<PersonDetails> getInvadersDetails(String building,String floor,String number);
    Person getRoomInvader(String id);
    //aggiunte
    ArrayList<String> listaEdiPiani();
    ArrayList<String> listaAulePiano(String edipiano);



    *//**
     * Utility/Convenience class.
     * Use AuleSVGWTService.App.getInstance() to access static instance of AuleSVGWTServiceAsync
     *//*
    public static class App {
        private static AuleSVGWTServiceAsync ourInstance = GWT.create(AuleSVGWTService.class);

        public static synchronized AuleSVGWTServiceAsync getInstance() {
            return ourInstance;
        }
    }
}*/
