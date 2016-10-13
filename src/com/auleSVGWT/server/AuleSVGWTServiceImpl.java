package com.auleSVGWT.server;

import com.auleSVGWT.client.AuleSVGWTService;
import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class AuleSVGWTServiceImpl extends RemoteServiceServlet implements AuleSVGWTService {
    private static final String addImageGWT = "/res/imageGWT";



    //--------------------------ROOM

    @Override
    public Integer saveRoom(RoomDTO roomDTO) {
        Room room = new Room(roomDTO);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(room);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: saveRoom method fail");
            e.printStackTrace();
        }finally {
            session.close();
        }

        return room.getId();
    }

    @Override
    public Integer updateRoom(RoomDTO roomDTO) {
        com.auleSVGWT.server.domain.Room room = new com.auleSVGWT.server.domain.Room(roomDTO);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(room);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: updateRoom method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        return room.getId();
    }

    //---------------------PERSON

    public ArrayList<PersonDTO> getPerson() {
        ArrayList<PersonDTO> personDTO = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ArrayList<Person> persons = new ArrayList<>(session.createQuery("from Person ").list());
            Collections.sort(persons,Person.getCompByNameaftSurname());
            personDTO.addAll(persons.stream().map(this::createPersonDTO).collect(Collectors.toList()));
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getPerson method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        return personDTO;
    }

    @Override
    public Integer deletePerson(int id) {
        deleteOccupyPerson(id);//prima elimino le relazioni
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("delete Person p where p.id = :id");
            q.setInteger("id", id);
            q.executeUpdate();
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: deletePerson method fail");
            e.printStackTrace();
        }finally {
            session.close();
        }
        return id;
    }

    @Override
    public Integer savePerson(PersonDTO personDTO) {
        Person person = new Person(personDTO);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(person);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: savePerson method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }
        return person.getId();
    }

    @Override
    public Integer updatePerson(PersonDTO personDTO) {
        com.auleSVGWT.server.domain.Person person = new com.auleSVGWT.server.domain.Person(personDTO);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(person);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: updatePerson method fail");
            e.printStackTrace();
        }finally {
            session.close();
        }


        return person.getId();

    }

    //--------------------------------OCCUPY-
    /* */

    @Override
    public Long saveRoomOccupy(ArrayList<Long> ids, ArrayList<OccupyDTO> occupyDTOs) {




        for (Long id : ids) {

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query q = session.createQuery("delete Occupy o where o.id = :id");
                q.setLong("id",id);
                q.executeUpdate();
                tx.commit();
            }
            catch (Exception e) {
                if (tx!=null) tx.rollback();
                System.out.println("error: saveRoomOccupy method fail");
                e.printStackTrace();
            }finally {
                session.close();
            }

        }
        for (OccupyDTO occupyDTO : occupyDTOs) {

            Occupy occupy = new Occupy(occupyDTO);
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(occupy);
                tx.commit();
            }
            catch (Exception e) {
                if (tx!=null) tx.rollback();
                System.out.println("error: saveRoomOccupy method fail ");
                e.printStackTrace();
            }finally {
                session.close();
            }

        }


        return (long) 5;
    }


    @Override
    public Long saveOccupy(OccupyDTO occupyDTO) {
        Occupy occupy = new Occupy(occupyDTO);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(occupy);

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: saveOccupy method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        return occupy.getId();
    }

    @Override
    public Long deleteOccupy(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("delete Occupy o where o.id = :id");
            q.setLong("id",id);
            q.executeUpdate();

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: deleteOccupy method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        return id;
    }

    @Override
    public ArrayList<OccupyDTO> getOccupy() {
        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ArrayList<Occupy> occup = new ArrayList<>(session.createQuery("from Occupy ").list());

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getOccupy method fail");
            e.printStackTrace();
        }finally {
            session.close();
        }

        return occupyDTO;
    }

    //questo metodo serve perchè se elimino una persona prima devo eliminare tutte le sue relazioni con room
    private int deleteOccupyPerson(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("delete Occupy O where O.person.id = :id");
            q.setInteger("id", id);
            q.executeUpdate();

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: deleteOccupyPerson method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        return id;
    }

    //-----------------STANZE OCCUPATE------------

    @Override
    public ArrayList<RoomPeopleDTO> getRoomsPeople(String building, String floorSt) {
        int floor = Integer.parseInt(floorSt);
        ArrayList<RoomPeopleDTO> roomPeopleDTO = new ArrayList<>();
        ArrayList<Person> people;
        ArrayList<Long> occ;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("select o from Room o join fetch o.building " +
                    "where o.floor= :floor and o.building.name= :building");
            q.setString("building",building);
            q.setInteger("floor",floor);
            ArrayList<Room> rooms = new ArrayList<>(q.list());

            for (Room room : rooms) {
                q = session.createQuery("select o from Occupy o   " +
                        "where o.room.floor= :floor and o.room.number= :number  and o.room.building.name= :building");
                q.setString("building",building);
                q.setInteger("floor", floor);
                q.setInteger("number",room.getNumber());

                ArrayList<Occupy> occupies= new ArrayList<>(q.list());

                people = new ArrayList<>();
                occ = new ArrayList<>();
                for(Occupy occupy :occupies){
                    people.add(occupy.getPerson());
                    occ.add(occupy.getId());

                }
                roomPeopleDTO.add(createRoomPeopleDTO(room, people, occ));
            }

            tx.commit();

        } catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getRoomsPeople method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }
        /*
        try {
            tx = session.beginTransaction();
            ArrayList<Occupy> occupies = new ArrayList<>(session.createQuery("from Occupy ").list());
            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("from Room ").list());

            for (Room room : rooms) {

                //System.out.println(room.getFloor() + " = " + floor + " " + room.getBuilding().getName() + " = " + building + "....................................................");
                if ((room.getFloor() == floor) && (room.getBuilding().getName().equals(building))) {

                    people = new ArrayList<>();
                    occ = new ArrayList<>();
                    for (Occupy occupy : occupies) {
                        //System.out.println("sono nel cilo occupy" + occupy.getId() + " = " + room.getId() + "....................................................");
                        if (occupy.getRoom().getId() == room.getId()) {
                            people.add(occupy.getPerson());
                            occ.add(occupy.getId());
                            //System.out.println(people.get(0).getName() + "\n");
                        }
                    }
                    roomPeopleDTO.add(createRoomPeopleDTO(room, people, occ));
                }
            }

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getRoomsPeople method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }*/


        return roomPeopleDTO;
    }


    @Override
    public RoomPeopleDTO getRoomPeople(String building, String floorSt, String numberSt) {
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        Room room1;
        ArrayList<Occupy> o;
        ArrayList<Long> occ= new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();
        RoomPeopleDTO roomPeopleDTO = new RoomPeopleDTO();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query q = session.createQuery("select o from Occupy o  " +
                    "where o.room.floor= :floor and o.room.number= :number and o.room.building.name= :building");
            q.setString("building", building);
            q.setInteger("floor", floor);
            q.setInteger("number",number);
            o = new ArrayList<>(q.list());

            q = session.createQuery("select o from Room o join fetch o.building " +
                    "where o.floor= :floor and o.number= :number and o.building.name= :building");
            q.setString("building", building);
            q.setInteger("floor", floor);
            q.setInteger("number",number);
            ArrayList<Room> rooms = new ArrayList<>(q.list());


            for(Occupy occupy : o){
                people.add(occupy.getPerson());
                occ.add(occupy.getId());

            }
            if (rooms.size() == 1) {
                System.out.println("la dimensione della stanza è 1 ");
                room1 = rooms.get(0);
                roomPeopleDTO = createRoomPeopleDTO(room1, people, occ);
            } else if(rooms.size() == 0) {
                roomPeopleDTO = createRoomPeopleDTO(newRoom(building, floorSt, numberSt, session), people, occ);
            }
            tx.commit();

        }catch (Exception e) {
                if (tx!=null) tx.rollback();
                System.out.println("error: getRoomPeople method fail ");
                e.printStackTrace();
        }finally {
                session.close();
        }

        /*
        try {
            tx = session.beginTransaction();
            ArrayList<Occupy> occupies = new ArrayList<>(session.createQuery("from Occupy ").list());
            ArrayList<Room> rooms = new ArrayList<>(session.createQuery("from Room ").list());


            for (Room room : rooms) {

                // System.out.println(room.getFloor() + " = " + floor + " " + room.getBuilding().getName() + " = " + building + "....................................................");
                if ((room.getFloor() == floor) && (room.getBuilding().getName().equals(building) && (room.getNumber() == number))) {
                    check = false;

                    for (Occupy occupy : occupies) {
                        //System.out.println("sono nel ciclo occupy" + occupy.getId() + " = " + room.getId() + "....................................................");
                        if (occupy.getRoom().getId() == room.getId()) {
                            people.add(occupy.getPerson());
                            occ.add(occupy.getId());
                            //System.out.println(people.get(0).getName() + "\n");
                        }
                    }
                    room1 = room;


                }
            }

            if (check) {
                roomPeopleDTO = createRoomPeopleDTO(newRoom(building, floorSt, numberSt, session), people, occ);
            } else {
                roomPeopleDTO = createRoomPeopleDTO(room1, people, occ);
            }


            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getRoomPeople method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }*/


        return roomPeopleDTO;


    }



    //--------------------ROLES

    @Override
    public ArrayList<RoleDTO> getRoles() {
        ArrayList<RoleDTO> rolesDTO = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ArrayList<Role> roles = new ArrayList<>(session.createQuery("from Role").list());
            rolesDTO.addAll(roles.stream().map(this::createRoleDTO).collect(Collectors.toList()));

            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getRoles method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        return rolesDTO;
    }


    @Override
    public ArrayList<String> listaAulePiano(String edificiopiano) {
        ArrayList<String> aule = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader("doc.JSON"));
            JSONObject jsonObject = (JSONObject) obj;


            JSONArray list = (JSONArray) jsonObject.get(edificiopiano);

            for (String aList : (Iterable<String>) list) {
                aule.add(edificiopiano + "-" + aList);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
        return aule;
    }


    public ArrayList<String> listaAule(String edificiopiano) throws FileNotFoundException {
        ArrayList<String> aule = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("doc.JSON"));
            JSONObject jsonObject = (JSONObject) obj;


            JSONArray list = (JSONArray) jsonObject.get(edificiopiano);

            for (String aList : (Iterable<String>) list) {
                aule.add(edificiopiano + "-" + aList);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }
        return aule;
    }

    public Integer saveRole(RoleDTO roleDTO) {
        Role role = new Role(roleDTO);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(role);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: saveRole  method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        return new Integer(role.getId());

    }

    public Integer updateRole(RoleDTO roleDTO) {
        Role role = new Role(roleDTO);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(role);
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error:  updateRole method fail  ");
            e.printStackTrace();
        }finally {
            session.close();
        }


        return role.getId();

    }

    //---------------------OTHER METHOD

    public HashMap<String, ArrayList<String>> getBuildingsFileName() {
        ArrayList<String> string = new ArrayList<>();
        HashMap<String, ArrayList<String>> buildings = new HashMap<String, ArrayList<String>>();


        try {
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath(addImageGWT);


            File folder = new File(fullPath);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (file.getName().contains(".svg")) {
                    string.add(file.getName());
                }
            }


        } catch (Exception e) {
            System.out.println("Error reading name of maps");
        }

        for (String s : string) {

            if (buildings.containsKey(s.substring(0, s.lastIndexOf('-')))) {
                ArrayList<String> floors = new ArrayList<>();
                floors = buildings.get(s.substring(0, s.lastIndexOf('-')));
                floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                Collections.sort(floors);
                buildings.replace(s.substring(0, s.lastIndexOf('-')), floors);
            } else {
                ArrayList<String> floors = new ArrayList<>();
                floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                buildings.put(s.substring(0, s.lastIndexOf('-')), floors);
            }
        }

        return buildings;

    }

    //-------------------------PRIVATE METHOD

    private Room newRoom(String buildingName, String floorSt, String numberSt, org.hibernate.Session session) {
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        Room room = new Room();
        Building building = new Building();
        boolean check = true;
        try {

            ArrayList<Building> buildings = new ArrayList<Building>(session.createQuery("from Building ").list());

            for (Building build : buildings) {
                if (build.getName().equals(buildingName)) {
                    room.setBuilding(build);
                    room.setFloor(floor);
                    room.setNumber(number);
                    room.setMaxPeople(0);
                    room.setDimension(0);
                    room.setRoomCode(buildingName + "-" + floor + "-" + number);
                    room.setSocket(0);
                    room.setMaintenance("nessun particolare");
                    session.save(room);
                    check = false;
                }
            }

            if (check) {
                building.setName(buildingName);
                session.save(building);
                room.setBuilding(building);
                room.setFloor(floor);
                room.setNumber(number);
                room.setMaxPeople(0);
                room.setDimension(0);
                room.setRoomCode(buildingName + "-" + floor + "-" + number);
                room.setSocket(0);
                room.setMaintenance("nessun particolare");
                session.save(room);
            }


        } catch (Exception e) {
            System.out.println("ERROR : newRoom method fail ");

        }

        return room;

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
                createRoleDTO(person.getRole()), person.getStartWork(), person.getEndWork(), person.getEmail(), person.getPhone());
    }

    private BuildingDTO createBuildingDTO(Building building) {

        return new BuildingDTO(building.getNumber(), building.getName());
    }

    private RoomDTO createRoomDTO(Room room) {

        return new RoomDTO(room.getId(), room.getNumber(), room.getFloor(),
                createBuildingDTO(room.getBuilding()), room.getMaxPeople(), room.getDimension(), room.getRoomCode(), room.getMaintenance(), room.getSocket());
    }

    //--------------------------------------------DA APPROVARE-----------------------------

    public ArrayList<OccupyDTO> getOccupySearch(String part1, String part2) {

        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query q = session.createQuery("from Occupy where person.name= :sost1 and person.surname= :sost2");
            q.setString("sost1",part1);
            q.setString("sost2",part2);
            ArrayList<Occupy> occup = new ArrayList<>(q.list());


            if (occup.size() > 0) {
                for (Occupy occupy : occup) {
                    occupyDTO.add(createOccupyDTO(occupy));
                }


            }
            tx.commit();
        }
        catch (Exception e) {
            if (tx!=null) tx.rollback();
            System.out.println("error: getOccupySearch method fail ");
            e.printStackTrace();
        }finally {
            session.close();
        }

        return occupyDTO;
    }

    public ArrayList<OccupyDTO> getOccupySearch(ArrayList<PersonDTO> persons) {
        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();
        for (PersonDTO personDTO : persons) {
            occupyDTO.addAll(getOccupySearch(personDTO.getName(), personDTO.getSurname()));
        }
        return occupyDTO;
    }


    public ArrayList<String> listaAulePianoNewVersion(String text) {
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath(addImageGWT);
        URI uri = new File(fullPath + "/" + text + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<String> room = new ArrayList<>();
        try {
            converter = new SVGMetaPost(uri.toString());
            Document doc = converter.getSVGDocument();


            int counter = 0;
            String u = "";
            NodeList n = doc.getElementsByTagName("rect");
            NodeList p = doc.getElementsByTagName("path");
            for (int i = 0; i < n.getLength(); i++) {
                //System.out.println(n.item(i).getTextContent());
                if (((Element) n.item(i)).getAttribute("id").contains(text)) {
                    u += ((Element) n.item(i)).getAttribute("id") + " ";
                    //System.out.println(((Element) n.item(i)).getAttribute("id"));
                    room.add(((Element) n.item(i)).getAttribute("id"));

                    counter++;
                }
            }
            for (int i = 0; i < p.getLength(); i++) {
                //System.out.println(p.item(i).getTextContent());
                if (((Element) p.item(i)).getAttribute("id").contains(text)) {
                    u += ((Element) p.item(i)).getAttribute("id") + " ";
                    //System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));

                    counter++;
                }
            }

            // System.out.println("RISULTATO RICERCA NEL FILE " + u + " " + counter);
        } catch (IOException e) {
            System.out.println("ERROr in liste aule piano new");
        }


        return room;

    }

}
