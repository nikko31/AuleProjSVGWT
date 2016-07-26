package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;
import org.hibernate.classic.Session;

import java.util.ArrayList;


public class DatabaseM {
    Session session;


    public DatabaseM() {

    }

    public Boolean createSession(){


        if(session.isOpen()){
            return true;
        }

        return false;
    }

    public void closeSession(){
        if(session.isOpen()){
            session.close();
        }
    }


    public ArrayList<OccupyDTO> getOccupyOfPerson(String part1,String part2) {
        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Occupy> occup =  new ArrayList<>(( session.createQuery("from Occupy where person.name='" + part1 + "' and person.surname='" + part2 + "'").list()));

            if(occup.size()>0){
                for (Occupy occupy : occup) {
                    occupyDTO.add(createOccupyDTO(occupy));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getOccupy method fail ");
            e.printStackTrace();
        }

        return occupyDTO;
    }


    public ArrayList<OccupyDTO> getOccupyOfRoom(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);

        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();


        try {
            org.hibernate.classic.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Occupy> occupies = new ArrayList<>(session.createQuery("from Occupy where room.building.name='" + building + "' and room.floor=" + floor + " and room.number="+number).list());

            if(occupies.size()>0){
                for (Occupy occupy : occupies) {
                    occupyDTO.add(createOccupyDTO(occupy));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return occupyDTO;

    }


    public ArrayList<OccupyDTO> getOccupyOfFloor(String building, String floorSt){

        int floor = Integer.parseInt(floorSt);

        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();


        try {
            org.hibernate.classic.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Occupy> occupies = new ArrayList<>(session.createQuery("from Occupy where room.building.name='" + building + "' and room.floor=" + floor).list());

            if(occupies.size()>0){
                for (Occupy occupy : occupies) {
                    occupyDTO.add(createOccupyDTO(occupy));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return occupyDTO;

    }






    private RoomPeopleDTO createRoomPeopleDTO(Room room, ArrayList<Person> people, ArrayList<Long> occId) {
        ArrayList<PersonDTO> peopleDTO = new ArrayList<>();

        for (Person person : people) {
            peopleDTO.add(createPersonDTO(person));
        }

        return new RoomPeopleDTO(createRoomDTO(room), peopleDTO, occId);


    }

    private OccupyDTO createOccupyDTO(Occupy occupy) {
        return new OccupyDTO(occupy.getId(), createRoomDTO(occupy.getRoom()), createPersonDTO(occupy.getPerson()));
    }

    private RoleDTO createRoleDTO(Role role) {

        return new RoleDTO(role.getId(), role.getName(), role.getSqm());
    }

    private PersonDTO createPersonDTO(Person person) {

        return new PersonDTO(person.getId(), person.getName(), person.getSurname(),
                createRoleDTO(person.getRole()),person.getStartWork(),person.getEndWork());
    }

    private BuildingDTO createBuildingDTO(Building building) {

        return new BuildingDTO(building.getNumber(), building.getName());
    }

    private RoomDTO createRoomDTO(Room room) {

        return new RoomDTO(room.getId(), room.getNumber(), room.getFloor(),
                createBuildingDTO(room.getBuilding()), room.getMaxPeople(), room.getDimension(),room.getRoomCode(),room.getMaintenance(),room.getSocket());
    }

}
