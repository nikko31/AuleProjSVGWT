package com.auleSVGWT.server;

import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;



public class DatabaseM {




    //----------------------------------------------------------PERSON----------------------------------------------------


    public ArrayList<Person> getPeopleInRoom(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        String correctBuilding= building.replace("'","''");

        ArrayList<Person> people = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            people = new ArrayList<Person>(session.createQuery("select o.person from Occupy o join o.person  join fetch o.person.role  where o.room.building.name='" + correctBuilding + "' and " +
                    "o.room.floor=" + floor + " and o.room.number="+number).list());

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getPeopleInRoom fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        return people;

    }




    //-----------------------------------------PersonJSON-----------------------------------
    public JSONArray getPersonJson(String part1,String part2) {
        JSONArray peopleJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        String sost1 = part1;
        String sost2 = part2;
        sost1 = sost1.replaceAll("'","''");
        sost2 = sost2.replaceAll("'", "''");

        try {
            tx = session.beginTransaction();
            ArrayList<Person> people =  new ArrayList<>(( session.createQuery("select o from Person o where (o.name='" + sost1 + "' and o.surname='" + sost2 + "') or (" +
                    "o.surname='"+sost1+"' and o.name='"+sost2+"')").list()));

            peopleJson = parsePeople(people);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getPersonJson fail");
            e.printStackTrace();
        }finally {
            session.close();
        }


        if(peopleJson != null){
            if(peopleJson.size() >0){
                return peopleJson;
            }

        }
        return null;

        /*if(nullFlag){
            return null;
        }else{
            return peopleJson;
        }*/
    }

    public JSONObject getPersonWithIDJson(String id) {
        Integer i = new Integer(id);
        boolean nullFlag=true;
        JSONObject personJson = new JSONObject();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            ArrayList<Person> people =  new ArrayList<>(( session.createQuery("select o from Person o where o.id="+i).list()));
            if(people.size()==1){
                nullFlag=false;
                personJson = parsePerson(people.get(0));
            }else{
                nullFlag = true;
            }

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getPersonJson fail");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(nullFlag){
            return null;
        }else{
            return personJson;
        }
    }

    public JSONArray getPersonSearchJson(int number,String search) {
        String searchCorrect= search.replaceAll("'","''");

        System.out.println("cerco dentro alle persone.............................................................................");
        JSONArray peopleJson = new JSONArray();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ArrayList<Person> people = new ArrayList<Person>((session.createQuery("select o from Person o where o.name like '" + searchCorrect + "%' or o.surname like '" + searchCorrect + "%'").list()));

            Collections.sort(people, Person.getCompByNameaftSurname());

            if (people.size() != 0) {
                if (number > people.size()) {
                    peopleJson = parsePeople(new ArrayList<Person>(people.subList(0, people.size())));
                } else {
                    peopleJson = parsePeople(new ArrayList<Person>(people.subList(0, number)));
                }

            }


            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("error: getPersonSearchJson fail ");
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (peopleJson != null) {
            if (peopleJson.size() > 0) {
                return peopleJson;
            }

        }
        return null;
    }


    public JSONArray getPeopleInRoomWithIdJson(String id){
        Integer i = new Integer(id);
        JSONArray peopleJson = new JSONArray();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ArrayList<Person> people = new ArrayList<Person>(session.createQuery("select o.person from Occupy o where o.room.id=" + i).list());

            Collections.sort(people,Person.getCompByNameaftSurname());
            peopleJson = parsePeople(people);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getPeopleInRoom fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(peopleJson != null){
            if(peopleJson.size() >0){
                return peopleJson;
            }

        }
        return null;


    }


    public JSONArray getPeopleInRoomJson(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        String correctBuilding= building.replace("'", "''");
        JSONArray peopleJson = new JSONArray();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ArrayList<Person> people = new ArrayList<Person>(session.createQuery("select o.person from Occupy o where o.room.building.name='" + correctBuilding+ "' and " +
                    "o.room.floor=" + floor + " and o.room.number="+number).list());

            Collections.sort(people,Person.getCompByNameaftSurname());
            peopleJson = parsePeople(people);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getPeopleInRoom fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(peopleJson != null){
            if(peopleJson.size() >0){
                return peopleJson;
            }

        }
        return null;


    }


    public JSONArray getOccupyOfFloorwithDateJson(String building, String floorSt){

        int floor = Integer.parseInt(floorSt);
        String correctBuilding= building.replace("'", "''");
        JSONArray peopleJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            ArrayList<Person> people = new ArrayList<Person>(session.createQuery("select occ.person from Occupy occ where occ.room.building.name='" + correctBuilding +
                    "' and occ.room.floor=" + floor +" and occ.person.endWork <= CURRENT_DATE() group by occ.person.id").list() );

            ArrayList<Person> filteredPeople = new ArrayList<>();
            for(Person person : people){
                if(person.getEndWork()!=null){
                    // System.out.println(""+person.getSurname()+person.getName());
                    filteredPeople.add(person);
                }
            }
            Collections.sort(people,Person.getCompByNameaftSurname());
            peopleJson = parsePeople(filteredPeople);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getOccupyOfFloorwithDateJson ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(peopleJson != null){
            if(peopleJson.size() >0){
                return peopleJson;
            }

        }
        return null;


    }



    public JSONArray getPeopleJson() {
        JSONArray peopleJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            ArrayList<Person> people = new ArrayList<Person>(session.createQuery("from Person ").list());

            peopleJson = parsePeople(people);

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getPeopleJson fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(peopleJson != null){
            if(peopleJson.size() >0){
                return peopleJson;
            }

        }
        return null;

    }



    //-----------------------------------------RoomJson------------------------
    public JSONArray getOccupedRoomOfPersonJson(String part1,String part2) {
        JSONArray roomsJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        String sost1 = part1;
        String sost2 = part2;
        sost1 = sost1.replaceAll("'","''");
        sost2 = sost2.replaceAll("'", "''");

        try {
            tx = session.beginTransaction();
            ArrayList<Room> rooms =  new ArrayList<Room>(( session.createQuery("select o.room from Occupy o where (o.person.name='" + sost1 + "' and o.person.surname='" + sost2 + "')" +
                    "or(o.person.name='" + sost2 +"' and o.person.surname='" + sost1 + "')").list()));

            Collections.sort(rooms,Room.getCompByName());
            Collections.sort(rooms,Room.getCompByNumber());
            roomsJson = parseRooms(rooms);


            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getOccupedRoomOfPersonJson");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(roomsJson != null){
            if(roomsJson.size() >0){
                return roomsJson;
            }

        }
        return null;
    }

    public JSONArray getOccupedRoomOfPersonWithIdJson(String id) {
        Integer i = new Integer(id);
        JSONArray roomsJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            ArrayList<Room> rooms =  new ArrayList<Room>(( session.createQuery("select o.room from Occupy o where o.person.id=" +i).list()));

            Collections.sort(rooms,Room.getCompByName());
            Collections.sort(rooms,Room.getCompByNumber());
            roomsJson = parseRooms(rooms);




            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getOccupedRoomOfPersonJson");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(roomsJson != null){
            if(roomsJson.size() >0){
                return roomsJson;
            }

        }
        return null;
    }

    public JSONObject getRoomInfowithIdJson(String id){
        boolean nullFlag=false;
        Integer i = new Integer(id);
        JSONObject roomJson = new JSONObject();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("from Room where Room.id="+i).list());
            if(rooms.size()==1){
                roomJson = parseRoom(rooms.get(0));
            }else{
                nullFlag = true;
            }

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getRoomInfoJson fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        if(nullFlag){
            return null;
        }else{
            return roomJson;
        }

    }



    public JSONObject getRoomInfoJson(String building, String floorSt, String numberSt){
        boolean nullFlag=true;
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        String correctBuilding= building.replace("'", "''");
        JSONObject roomJson = new JSONObject();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("from Room where building.name='" + correctBuilding + "' and floor=" + floor + " and number="+number).list());
            if(rooms.size()==1){
                nullFlag= false;
                roomJson = parseRoom(rooms.get(0));
            }else{
                nullFlag = true;
            }

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getRoomInfoJson fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        if(nullFlag){
            return null;
        }else{
            return roomJson;
        }

    }



    public JSONArray getOccupyOfFloorwithDimensionJson(String building, String floorSt){

        int floor = Integer.parseInt(floorSt);
        String correctBuilding= building.replace("'", "''");
        JSONArray roomsJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            ArrayList<Room> rooms = new ArrayList<Room>(session.createQuery("select room from Occupy occ where  " +
                    "occ.room.building.name ='"+correctBuilding+"'" +"and occ.room.floor="+floor +
                    " and occ.room.dimension <(select sum(o.person.role.sqm) from Occupy o where o.room.id = occ.room.id) Group by occ.room.id ").list());

            Collections.sort(rooms,Room.getCompByNumber());
            roomsJson = parseRooms(rooms);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getOccupyFloorwithDimensionJson ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        if(roomsJson != null){
            if(roomsJson.size() >0){
                return roomsJson;
            }

        }
        return null;

    }



    //------------------------------------------Room-----------------------------------------



    public ArrayList<Room> getRoomInfo(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        String correctBuilding= building.replace("'", "''");
        ArrayList<Room> rooms = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            rooms = new ArrayList<Room>(session.createQuery("select r from Room r join fetch r.building  where r.building.name='" + correctBuilding + "' and r.floor=" + floor + " " +
                    "and r.number="+number).list());
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getRoomInfo fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        return rooms;

    }





    //------------------------------------------------------private Metod---------------------------------------------------------



    private JSONArray parsePeople(ArrayList<Person> people){
        JSONArray arrayPeopleJson = new JSONArray();



        if(people.size()>0){

            for(Person person : people ){
                JSONObject obj;
                obj = parsePerson(person);
                arrayPeopleJson.add(obj);
            }

            return arrayPeopleJson;

        }else{
            return null;
        }


    }

    private JSONArray parseRooms(ArrayList<Room> rooms){
        JSONArray arrayRoomsJson = new JSONArray();



        if(rooms.size()>0){

            for(Room room : rooms){
                JSONObject obj;
                obj = parseRoom(room);
                arrayRoomsJson.add(obj);
            }

            return arrayRoomsJson;

        }else{
            return null;
        }


    }


    private JSONObject parsePerson(Person person){

        JSONObject personJSON = new JSONObject();

        personJSON.put("id",person.getId());
        personJSON.put("name",""+ person.getName());
        personJSON.put("surname",""+ person.getSurname());
        personJSON.put("role",""+ person.getRole().getName());


        if(person.getStartWork() != null){
            String sW = person.getStartWork().toString();
            sW = sW.replaceAll("-"," ");
            personJSON.put("startWork",""+sW);
        }else{
            personJSON.put("startWork",null);

        }
        if(person.getEndWork() != null){
            String eW = person.getEndWork().toString();
            eW = eW.replaceAll("-", " ");
            personJSON.put("endWork",""+eW);


        }else{
            personJSON.put("endWork",null);

        }

        if(person.getPhone() != null){
            String phone = person.getPhone();

            personJSON.put("phone",""+phone);


        }else{
            personJSON.put("phone",null);

        }
        if(person.getEmail() != null){
            String email = person.getEmail();

            personJSON.put("email",""+email);


        }else{
            personJSON.put("email",null);

        }



        personJSON.put("infoRoomsOfPerson","/rest/persone/"+person.getId()+"/stanze");

        return personJSON;
    }


    private JSONObject parseRoom(Room room){
        JSONObject roomJson = new JSONObject();
        JSONObject image = new JSONObject();


        roomJson.put("id",room.getId());
        roomJson.put("building",""+room.getBuilding().getName());
        roomJson.put("floor",room.getFloor());
        roomJson.put("number",room.getNumber());
        if(room.getMaintenance()==null){
            roomJson.put("maintenance",null);
        }else{
            roomJson.put("maintenance",""+room.getMaintenance());
        }
        roomJson.put("personMax",room.getMaxPeople());
        roomJson.put("socket",room.getSocket());
        roomJson.put("dimension",room.getDimension());
        roomJson.put("code",""+room.getRoomCode());

        //hateoas
        String build = room.getBuilding().getName();
        build = build.replace(' ','_');
        roomJson.put("infoPeopleInRoom","/rest/edifici/"+build+"-"+room.getFloor()+"/stanze/"+room.getNumber()+"/persone");
        image.put("restLink","/rest/immagini/edifici/"+build+"-"+room.getFloor()+"/stanze/"+room.getNumber());
        image.put("PNGLink","/risorse/immagini/edifici/stanze/"+build+"-"+room.getFloor()+"-"+room.getNumber()+".png");
        roomJson.put("imageSelectRoom",image);


        return roomJson;

    }







}
