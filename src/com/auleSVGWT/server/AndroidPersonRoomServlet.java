package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Utente on 20/07/2016.
 */
public class AndroidPersonRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String building;
        String floor;
        String numRoom;
        RoomPeopleDTO roomPeopleDTO = new RoomPeopleDTO();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        building = req.getParameter("building");
        floor = req.getParameter("floor");
        numRoom = req.getParameter("numRoom");

        if(controlParameter(building,floor,numRoom)){
            roomPeopleDTO = getRoomPeople(building,floor,numRoom);
        }


        parseOut(roomPeopleDTO, out);
    }


    private boolean controlParameter(String building ,String floor,String numRoom){

        Boolean controlFlag = true;

        if((building == null) || (floor == null) || (numRoom == null)){
            controlFlag = false;
        }


        if(controlFlag){
            if((building.length() > 10) || (floor.length() >10) || (numRoom.length()>10)){
                controlFlag = false;
            }
        }




        return controlFlag;

    }

    private void parseOut(RoomPeopleDTO roomPeopleDTO,PrintWriter out){
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        JSONObject per;

        if(roomPeopleDTO != null ){
            int j=0;

            for(PersonDTO personDTO : roomPeopleDTO.getPeopleDTO()){

                per = new JSONObject();

                per.put("name", personDTO.getName());
                per.put("surname", personDTO.getSurname());
                per.put("role",personDTO.getRole().getName());
                if(personDTO.getStartWork() != null){
                    String sW = personDTO.getStartWork().toString();
                    sW = sW.replaceAll("-"," ");
                    per.put("startWork",sW);
                }else{
                    per.put("startWork","null");

                }
                if(personDTO.getEndWork() != null){
                    String eW = personDTO.getEndWork().toString();
                    eW = eW.replaceAll("-", " ");
                    per.put("endWork",eW);


                }else{
                    per.put("endWork","null");

                }


                arr.add(j, per);
                j++;


            }


            obj.put("dimension",roomPeopleDTO.getRoomDTO().getDimension());
            obj.put("info",roomPeopleDTO.getRoomDTO().getMaintenance());
            obj.put("persons",arr);

        }else{
            obj.put("persons","arr");


        }

        out.println(obj);
        out.close();


    }


    public RoomPeopleDTO getRoomPeople(String building, String floorSt, String numberSt) {
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        Room room1 = new Room();
        ArrayList<Long> occ = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();
        RoomPeopleDTO roomPeopleDTO = new RoomPeopleDTO();
        boolean check = true;

        try {
            org.hibernate.classic.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Occupy> occupies = new ArrayList<>(session.createQuery("from Occupy ").list());
            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("from Room ").list());


            for (Room room : rooms) {

                System.out.println(room.getFloor() + " = " + floor + " " + room.getBuilding().getName() + " = " + building + "....................................................");
                if ((room.getFloor() == floor) && (room.getBuilding().getName().equals(building) && (room.getNumber() == number))) {
                    check = false;

                    for (Occupy occupy : occupies) {
                        System.out.println("sono nel ciclo occupy" + occupy.getId() + " = " + room.getId() + "....................................................");
                        if (occupy.getRoom().getId() == room.getId()) {
                            people.add(occupy.getPerson());
                            occ.add(occupy.getId());
                            System.out.println(people.get(0).getName() + "\n");
                        }
                    }
                    room1 = room;


                }
            }

            if (check) {

            } else {
                roomPeopleDTO = createRoomPeopleDTO(room1, people, occ);
            }


            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }

        return roomPeopleDTO;


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