package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;


import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;


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



    //----------------------------------------------------------PERSON----------------------------------------------------

    public ArrayList<PersonDTO> getPerson(String part1,String part2) {
        ArrayList<PersonDTO> personDTOs = new ArrayList<>();
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            session.beginTransaction();

            ArrayList<Person> person =  new ArrayList<>(( session.createQuery("select o from Person o where o.name='" + part1 + "' and o.surname='" + part2 + "'").list()));

            if(person.size() != 0){
                personDTOs.add(createPersonDTO(person.get(0)));


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getOccupy method fail ");
            e.printStackTrace();
        }

        return personDTOs;
    }


    public ArrayList<PersonDTO> getPeopleInRoom(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);

        ArrayList<PersonDTO> personDTOs = new ArrayList<>();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Person> people = new ArrayList<>(session.createQuery("select o.person from Occupy o where o.room.building.name='" + building + "' and " +
                    "o.room.floor=" + floor + " and o.room.number="+number).list());

            if(people.size()>0){
                for (Person person : people) {
                    personDTOs.add(createPersonDTO(person));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return personDTOs;

    }


    public ArrayList<Person> getPeopleInRoomNotDTO(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);

        ArrayList<Person> people = new ArrayList<>();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

             people = new ArrayList<>(session.createQuery("select o.person from Occupy o join o.person  join fetch o.person.role  where o.room.building.name='" + building + "' and " +
                    "o.room.floor=" + floor + " and o.room.number="+number).list());



            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return people;

    }


    public ArrayList<PersonDTO> getOccupyOfFloorwithDate(String building, String floorSt){

        int floor = Integer.parseInt(floorSt);

        ArrayList<PersonDTO> personDTOs = new ArrayList<>();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Person> persons = new ArrayList<>(session.createQuery("select occ.person from Occupy occ where occ.room.building.name='" + building +
                    "' and occ.room.floor=" + floor +" and occ.person.endWork < CURRENT_DATE() group by occ.person.id").list() );

            if(persons.size()>0){
                for (Person person : persons) {
                    personDTOs.add(createPersonDTO(person));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return personDTOs;

    }



    public ArrayList<PersonDTO> getPeople() {
        ArrayList<PersonDTO> personDTO = new ArrayList<>();
        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Person> persons = new ArrayList<>(session.createQuery("from Person ").list());

            personDTO.addAll(persons.stream().map(this::createPersonDTO).collect(Collectors.toList()));
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("ERROR : getPerson method fail ");
            e.printStackTrace();
        }
        return personDTO;
    }

    //-----------------------------------------PersonJSON-----------------------------------
    public JSONObject getPersonJson(String part1,String part2) {
        JSONObject peopleJson = new JSONObject();
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            session.beginTransaction();

            ArrayList<Person> people =  new ArrayList<>(( session.createQuery("select o from Person o where o.name='" + part1 + "' and o.surname='" + part2 + "'").list()));

            peopleJson = parsePeople(people);

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getOccupy method fail ");
            e.printStackTrace();
        }
        if(peopleJson!= null){
            return peopleJson;
        }else{
            peopleJson.put("people",new JSONArray());
            return peopleJson;
        }
    }


    public JSONObject getPeopleInRoomJson(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        JSONObject peopleJson = new JSONObject();




        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Person> people = new ArrayList<>(session.createQuery("select o.person from Occupy o where o.room.building.name='" + building + "' and " +
                    "o.room.floor=" + floor + " and o.room.number="+number).list());

            peopleJson = parsePeople(people);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        if(peopleJson!= null){
            return peopleJson;
        }else{
            peopleJson.put("people",new JSONArray());
            return peopleJson;
        }

    }


    public JSONObject getOccupyOfFloorwithDateJson(String building, String floorSt){

        int floor = Integer.parseInt(floorSt);

        JSONObject peopleJson = new JSONObject();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Person> people = new ArrayList<>(session.createQuery("select occ.person from Occupy occ where occ.room.building.name='" + building +
                    "' and occ.room.floor=" + floor +" and occ.person.endWork < CURRENT_DATE() group by occ.person.id").list() );

            ArrayList<Person> filteredPeople = new ArrayList<>();
            for(Person person : people){
                if(person.getEndWork()!=null){
                   // System.out.println(""+person.getSurname()+person.getName());
                    filteredPeople.add(person);
                }
            }
            peopleJson = parsePeople(filteredPeople);

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        if(peopleJson!= null){
            return peopleJson;
        }else{
            peopleJson.put("people",new JSONArray());
            return peopleJson;
        }


    }



    public JSONObject getPeopleJson() {
        JSONObject peopleJson = new JSONObject();

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Person> people = new ArrayList<>(session.createQuery("from Person ").list());

            peopleJson = parsePeople(people);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("ERROR : getPerson method fail ");
            e.printStackTrace();
        }
        if(peopleJson!= null){
            return peopleJson;
        }else{
            peopleJson.put("people",new JSONArray());
            return peopleJson;
        }

    }



    //-----------------------------------------RoomJson------------------------
    public JSONObject getOccupedRoomOfPersonJson(String part1,String part2) {
        JSONObject roomsJson = new JSONObject();
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            session.beginTransaction();

            ArrayList<Room> rooms =  new ArrayList<>(( session.createQuery("select o.room from Occupy o where o.person.name='" + part1 + "' and o.person.surname='" + part2 + "'").list()));

            roomsJson = parseRoom(rooms);

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getOccupy method fail ");
            e.printStackTrace();
        }

        if(roomsJson!= null){
            return roomsJson;
        }else{
            roomsJson.put("rooms",new JSONArray());
            return roomsJson;
        }
    }


    public JSONObject getRoomInfoJson(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        JSONObject roomsJson = new JSONObject();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("from Room where building.name='" + building + "' and floor=" + floor + " and number="+number).list());

            roomsJson = parseRoom(rooms);

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        if(roomsJson!= null){
            return roomsJson;
        }else{
            roomsJson.put("rooms",new JSONArray());
            return roomsJson;
        }

    }



    public JSONObject getOccupyOfFloorwithDimensionJson(String building, String floorSt){

        int floor = Integer.parseInt(floorSt);

        JSONObject roomsJson = new JSONObject();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("select room from Occupy occ where  " +
                    "occ.room.building.name ='"+building+"'" +"and occ.room.floor="+floor +
                    " and occ.room.dimension <(select sum(o.person.role.sqm) from Occupy o where o.room.id = occ.room.id) Group by occ.room.id ").list());

           roomsJson = parseRoom(rooms);

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        if(roomsJson!= null){
            return roomsJson;
        }else{
            roomsJson.put("rooms",new JSONArray());
            return roomsJson;
        }

    }



    //------------------------------------------Room-----------------------------------------




    public ArrayList<RoomDTO> getOccupedRoomOfPerson(String part1,String part2) {
        ArrayList<RoomDTO> roomDTOs = new ArrayList<>();
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            session.beginTransaction();

            ArrayList<Room> rooms =  new ArrayList<>(( session.createQuery("select o.room from Occupy o where o.person.name='" + part1 + "' and o.person.surname='" + part2 + "'").list()));

            if(rooms.size()>0){
                for (Room room : rooms) {
                    roomDTOs.add(createRoomDTO(room));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getOccupy method fail ");
            e.printStackTrace();
        }

        return roomDTOs;
    }


    public ArrayList<RoomDTO> getRoomInfo(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);

        ArrayList<RoomDTO> roomDTOs = new ArrayList<>();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("from Room where building.name='" + building + "' and floor=" + floor + " and number="+number).list());

            if(rooms.size()>0){
                for (Room room : rooms) {
                    roomDTOs.add(createRoomDTO(room));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return roomDTOs;

    }

    public ArrayList<Room> getRoomInfoNotDTO(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);

        ArrayList<Room> rooms = new ArrayList<>();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            rooms = new ArrayList<>(session.createQuery("select r from Room r join fetch r.building  where r.building.name='" + building + "' and r.floor=" + floor + " " +
                    "and r.number="+number).list());



            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return rooms;

    }



    public ArrayList<RoomDTO> getOccupyOfFloorwithDimension(String building, String floorSt){

        int floor = Integer.parseInt(floorSt);

        ArrayList<RoomDTO> roomDTOs = new ArrayList<>();


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("select room from Occupy occ where  " +
                    "occ.room.building.name ='"+building+"'" +"and occ.room.floor="+floor +
                    " and occ.room.dimension <(select sum(o.person.role.sqm) from Occupy o where o.room.id = occ.room.id) Group by occ.room.id ").list());

            if(rooms.size()>0){
                for (Room room : rooms) {
                    roomDTOs.add(createRoomDTO(room));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }
        return roomDTOs;

    }



    //----------------------------Occupy-----------------


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
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
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
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
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


    public ArrayList<PersonDTO> getPerson() {
        ArrayList<PersonDTO> personDTO = new ArrayList<>();
        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Person> persons = new ArrayList<>(session.createQuery("from Person ").list());

            personDTO.addAll(persons.stream().map(this::createPersonDTO).collect(Collectors.toList()));
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("ERROR : getPerson method fail ");
            e.printStackTrace();
        }
        return personDTO;
    }


    //------------------------------------------------------private Metod---------------------------------------------------------

    private JSONObject parsePeople(ArrayList<Person> people){
        JSONArray arrayPersonJ = new JSONArray();
        JSONObject personJSON;
        JSONObject obj;

        if(people.size()>0){

            int j = 0;
            for(Person person : people){
                personJSON = new JSONObject();
                JSONObject link = new JSONObject();

                personJSON.put("id",person.getId());
                personJSON.put("name", person.getName());
                personJSON.put("surname", person.getSurname());
                personJSON.put("role", person.getRole().getName());
                if(person.getStartWork() != null){
                    String sW = person.getStartWork().toString();
                    sW = sW.replaceAll("-"," ");
                    personJSON.put("startWork",sW);
                }else{
                    personJSON.put("startWork","null");

                }
                if(person.getEndWork() != null){
                    String eW = person.getEndWork().toString();
                    eW = eW.replaceAll("-", " ");
                    personJSON.put("endWork",eW);


                }else{
                    personJSON.put("endWork","null");

                }

                link.put("roomsOccPerson","/Android/stanze/persone/"+person.getName()+"_"+person.getSurname()+".json");

                personJSON.put("link",link);

                arrayPersonJ.add(j,personJSON);
                j++;



            }

        }

        obj= new JSONObject();
        obj.put("people", arrayPersonJ);
        return obj;
    }


    private JSONObject parseRoom(ArrayList<Room> rooms){
        JSONArray ar;
        JSONObject obj;



        ar = new JSONArray();

        if(rooms.size() !=0){
            ar = new JSONArray();


            int i=0;
            for(Room room : rooms){
                obj = new JSONObject();
                JSONObject link = new JSONObject();
                if(!obj.containsKey("number")){
                    obj.put("number",""+room.getNumber());

                }

                obj.put("building",""+room.getBuilding().getName());
                obj.put("floor",""+room.getFloor());
                if(room.getMaintenance()==null){
                    obj.put("info","null");
                }else{
                    obj.put("info",""+room.getMaintenance());
                }

                obj.put("personMax",""+room.getMaxPeople());
                obj.put("socket",""+room.getSocket());
                obj.put("dimension",""+room.getDimension());
                obj.put("code",""+room.getRoomCode());
                String build = room.getBuilding().getName();
                build = build.replace(' ','_');
                link.put("peopleInRoom","/Android/persone/"+build+"-"+room.getFloor()+"/"+room.getNumber()+".json");
                link.put("imageSelecRoom","/Android/immagine/"+build+"-"+room.getFloor()+"/"+room.getNumber()+".png");
                obj.put("link",link);
                ar.add(i,obj);
                i++;


            }

            obj= new JSONObject();
            obj.put("rooms",ar);

        }else{
            obj= new JSONObject();
            obj.put("rooms",ar);


        }


        return obj;

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
