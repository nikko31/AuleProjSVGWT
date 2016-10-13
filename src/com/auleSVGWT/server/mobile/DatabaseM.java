package com.auleSVGWT.server.mobile;

import com.auleSVGWT.server.mobile.Converter.PeopleToJson;
import com.auleSVGWT.server.mobile.Converter.PersonToJson;
import com.auleSVGWT.server.mobile.Converter.RoomToJson;
import com.auleSVGWT.server.mobile.Converter.RoomsToJson;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.*;


public class DatabaseM {



    public DatabaseM() {
    }


    //----------------------------------------------------------PERSON----------------------------------------------------


    public ArrayList<Person> getPeopleInRoom(String building, String floorSt, String numberSt){
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);

        ArrayList<Person> people = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Query q = session.createQuery("select o.person from Occupy o join o.person  join fetch o.person.role  " +
                    "where o.room.building.name= :building  and o.room.floor= :floor and o.room.number= :number");
            q.setString("building", building);
            q.setInteger("floor", floor);
            q.setInteger("number", number);

            people=new ArrayList<>(q.list());

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

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select p from Person p join fetch p.role where (p.name = :name1 and p.surname = :surname1) or(p.name = :name2 and p.surname = :surname2)");
            q.setString("name1",part1);
            q.setString("surname1",part2);
            q.setString("name2", part2);
            q.setString("surname2", part1);

            ArrayList<Person> people = new ArrayList<>(q.list());

            PeopleToJson peopleToJson = new PeopleToJson();
            peopleJson = peopleToJson.convert(people);
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

    }

    public JSONObject getPersonWithIDJson(String id) {
        Integer i = new Integer(id);
        boolean nullFlag=true;
        JSONObject personJson = new JSONObject();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select p from Person p join fetch p.role where p.id= :id");
            q.setInteger("id", i);
            ArrayList<Person> people =  new ArrayList<>(q.list());


            if(people.size()==1){
                nullFlag=false;
                PersonToJson personToJson = new PersonToJson();
                personJson = personToJson.convert(people.get(0));
                //personJson = parsePerson(people.get(0));
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

        JSONArray peopleJson = new JSONArray();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select o from Person o where o.name like :search1  or o.surname like :search2");
            q.setString("search1",search+"%");
            q.setString("search2", search + "%");
            ArrayList<Person> people = new ArrayList<Person>(q.list());
            Collections.sort(people, Person.getCompByNameaftSurname());

            if (people.size() != 0) {
                PeopleToJson peopleToJson = new PeopleToJson();
                if (number > people.size()) {
                    peopleJson = peopleToJson.convert(new ArrayList<>(people.subList(0, people.size())));
                    //peopleJson = parsePeople(new ArrayList<Person>(people.subList(0, people.size())));
                } else {
                    peopleJson = peopleToJson.convert(new ArrayList<>(people.subList(0, number)));
                    //peopleJson = parsePeople(new ArrayList<Person>(people.subList(0, number)));
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
            Query q = session.createQuery("select o.person from Occupy o where o.room.id= :i");
            q.setInteger("i", i);
            ArrayList<Person> people = new ArrayList<Person>(q.list());


            Collections.sort(people,Person.getCompByNameaftSurname());
            PeopleToJson peopleToJson = new PeopleToJson();
            peopleJson = peopleToJson.convert(people);
            //peopleJson = parsePeople(people);
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
        JSONArray peopleJson = new JSONArray();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select o.person from Occupy o join o.person join fetch o.person.role " +
                    "where o.room.building.name= :building and  o.room.floor= :floor and o.room.number= :number");
            q.setInteger("number", number);
            q.setInteger("floor", floor);
            q.setString("building", building);

            ArrayList<Person> people = new ArrayList<Person>(q.list());

            Collections.sort(people,Person.getCompByNameaftSurname());
            PeopleToJson peopleToJson = new PeopleToJson();
            peopleJson = peopleToJson.convert(people);
            //peopleJson = parsePeople(people);
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

        JSONArray peopleJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select distinct occ.person from Occupy occ " +
                    "where occ.room.building.name= :building and occ.room.floor= :floor and occ.person.endWork <= CURRENT_DATE()");
            q.setInteger("floor",floor);
            q.setString("building", building);
            ArrayList<Person> people = new ArrayList<>(q.list());


            ArrayList<Person> filteredPeople = new ArrayList<>();
            for(Person person : people){
                if(person.getEndWork()!=null){
                    // System.out.println(""+person.getSurname()+person.getName());
                    filteredPeople.add(person);
                }
            }
            Collections.sort(people,Person.getCompByNameaftSurname());
            PeopleToJson peopleToJson = new PeopleToJson();
            peopleJson = peopleToJson.convert(filteredPeople);
            //peopleJson = parsePeople(filteredPeople);
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
            ArrayList<Person> people = new ArrayList<>(session.createQuery("from Person ").list());

            PeopleToJson peopleToJson = new PeopleToJson();
            peopleJson = peopleToJson.convert(people);
            //peopleJson = parsePeople(people);

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

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select o.room from Occupy o " +
                    "where (o.person.name= :name1 and o.person.surname=:surname1) or(o.person.name= :name2 and o.person.surname= :surname2)");
            q.setString("name1", part1);
            q.setString("surname1", part2);
            q.setString("name2", part2);
            q.setString("surname2", part1);
            ArrayList<Room> rooms =  new ArrayList<Room>(q.list());

            Collections.sort(rooms,Room.getCompByName());
            Collections.sort(rooms,Room.getCompByNumber());
            RoomsToJson roomsToJson = new RoomsToJson();
            roomsJson = roomsToJson.convert(rooms);
            //roomsJson = parseRooms(rooms);


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
            Query q = session.createQuery("select o.room from Occupy o where o.person.id= :i");
            q.setInteger("i", i);
            ArrayList<Room> rooms =  new ArrayList<Room>(q.list());


            Collections.sort(rooms,Room.getCompByName());
            Collections.sort(rooms,Room.getCompByNumber());

            RoomsToJson roomsToJson = new RoomsToJson();
            roomsJson = roomsToJson.convert(rooms);
            //roomsJson = parseRooms(rooms);




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
            Query q = session.createQuery("select o.room from Occupy o where o.id= :i");
            q.setInteger("i", i);
            ArrayList<Room> rooms =  new ArrayList<Room>(q.list());

            if(rooms.size()==1){

                RoomToJson roomToJson = new RoomToJson();
                roomJson = roomToJson.convert(rooms.get(0));
                //roomJson = parseRoom(rooms.get(0));
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
        JSONObject roomJson = new JSONObject();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Room where building.name= :building and floor= :floor and number= :number");
            q.setInteger("number",number);
            q.setInteger("floor",floor);
            q.setString("building", building);
            ArrayList<Room> rooms = new ArrayList<>(q.list());

            if(rooms.size()==1){
                nullFlag= false;
                RoomToJson roomToJson = new RoomToJson();
                roomJson = roomToJson.convert(rooms.get(0));
                //roomJson = parseRoom(rooms.get(0));
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
        JSONArray roomsJson = new JSONArray();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query q = session.createQuery("select room from Occupy occ " +
                    "where occ.room.building.name = :building and occ.room.floor = :floor" +
                    " and occ.room.dimension <(select sum(o.person.role.sqm) from Occupy o where o.room.id = occ.room.id) Group by occ.room.id ");
            q.setInteger("floor", floor);
            q.setString("building", building);
            ArrayList<Room> rooms = new ArrayList<Room>(q.list());

            Collections.sort(rooms,Room.getCompByNumber());
            RoomsToJson roomsToJson = new RoomsToJson();
            roomsJson = roomsToJson.convert(rooms);
            //roomsJson = parseRooms(rooms);
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
        ArrayList<Room> rooms = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select r from Room r join fetch r.building  " +
                            "where r.building.name= :building  and r.floor= :floor and r.number= :number");
            q.setInteger("number",number);
            q.setInteger("floor",floor);
            q.setString("building",building);
            rooms = new ArrayList<Room>(q.list());

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






}
