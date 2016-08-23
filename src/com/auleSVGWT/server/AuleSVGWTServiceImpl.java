package com.auleSVGWT.server;

import com.auleSVGWT.client.AuleSVGWTService;
import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;

import org.hibernate.Session;
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
    private static final String addImageGWT="/res/imageGWT";
    // Implementation of sample interface method


    //--------------------------ROOM

    @Override
    public Integer saveRoom(RoomDTO roomDTO) {
        Room room = new Room(roomDTO);

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(room);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("ERROR : saveRoom method fail ");
        }

        return room.getId();
    }
    @Override
    public Integer updateRoom(RoomDTO roomDTO) {
        com.auleSVGWT.server.domain.Room room = new com.auleSVGWT.server.domain.Room(roomDTO);

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(room);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : updatePerson method fail ");


        }

        return room.getId();

    }

    //---------------------PERSON

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

    @Override
    public Integer deletePerson(int id) {
        deleteOccupyPerson(id);//prima elimino le relazioni
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery("delete Person p where p.id = " + id);
            q.executeUpdate();
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : deletePerson method fail");

        }


        return id;
    }

    @Override
    public Integer savePerson(PersonDTO personDTO) {
        Person person = new Person(personDTO);
        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(person);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : savePerson method fail ");

        }

        return person.getId();
    }

    @Override
    public Integer updatePerson(PersonDTO personDTO) {
        com.auleSVGWT.server.domain.Person person = new com.auleSVGWT.server.domain.Person(personDTO);

        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(person);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : updatePerson method fail ");


        }

        return person.getId();

    }

    //--------------------------------OCCUPY-
    /* */

    @Override
    public Long saveRoomOccupy(ArrayList<Long> ids, ArrayList<OccupyDTO> occupyDTOs) {
        /*System.out.println("sono nel saveroom");
        for(OccupyDTO occupyDTO: occupyDTOs){
            System.out.println(occupyDTO.getPerson().getName() + occupyDTO.getPerson().getSurname());

        }*/


        for (Long id : ids) {
            //System.out.println("sto cancellando id ................" + id);
            try {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Query q = session.createQuery("delete Occupy o where o.id = " + id);
                q.executeUpdate();
                session.getTransaction().commit();


            } catch (Exception e) {
                System.out.println("ERROR : saveRoomOccupy method fail ");

            }
        }
        for (OccupyDTO occupyDTO : occupyDTOs) {

            Occupy occupy = new Occupy(occupyDTO);


            try {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                session.save(occupy);
                session.getTransaction().commit();


            } catch (Exception e) {
                System.out.println("ERROR : saveRoomOccupy method fail ");

            }
        }


        return (long) 5;
    }


    @Override
    public Long saveOccupy(OccupyDTO occupyDTO) {
        Occupy occupy = new Occupy(occupyDTO);


        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(occupy);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : saveOccupy method fail ");

        }

        return occupy.getId();
    }

    @Override
    public Long deleteOccupy(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery("delete Occupy o where o.id = " + id);
            q.executeUpdate();
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : deleteOccupy method fail ");

        }

        return id;
    }

    @Override
    public ArrayList<OccupyDTO> getOccupy() {
        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();
        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Occupy> occup = new ArrayList<>(session.createQuery("from Occupy ").list());

            for (Occupy occupy : occup) {
                occupyDTO.add(createOccupyDTO(occupy));
            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getOccupy method fail ");
            e.printStackTrace();
        }

        return occupyDTO;
    }

    //questo metodo serve perchè se elimino una persona prima devo eliminare tutte le sue relazioni con room
    private int deleteOccupyPerson(int id) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery("delete Occupy O where O.person.id = " + id);
            q.executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("ERROR : deleteOccupyPerson method fail ");
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
        try {
            org.hibernate.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
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
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("ERROR : getRoomsPeople method fail ");

        }

        return roomPeopleDTO;
    }


    @Override
    public RoomPeopleDTO getRoomPeople(String building, String floorSt, String numberSt) {
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        Room room1 = new Room();
        ArrayList<Long> occ = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();
        RoomPeopleDTO roomPeopleDTO = new RoomPeopleDTO();
        boolean check = true;

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

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


            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");
            e.printStackTrace();

        }

        return roomPeopleDTO;


    }






    /*
    @Override
    public RoomPeopleDTO getRoomPeople(String building, String floorSt, String numberSt) {
        int number=Integer.parseInt(numberSt);
        int floor=Integer.parseInt(floorSt);
        ArrayList<Long> occId = new ArrayList<>();
        Room room = new Room();
        ArrayList<Person> people = new ArrayList<>();
        RoomPeopleDTO roomPeopleDTO = new RoomPeopleDTO();
        boolean chek = true;

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Occupy> occupy = new ArrayList<>(session.createQuery("from Occupy ").list());

            for (Occupy i : occupy) {

                System.out.println(i.getRoom().getFloor() + " = " + floor + " " + i.getRoom().getBuilding().getName() + " = " + building + " " + i.getRoom().getNumber() + " " + number + "......................");
                if ((i.getRoom().getFloor() == floor) && (i.getRoom().getBuilding().getName().equals(building) && (i.getRoom().getNumber() == number))) {

                    if (chek) {
                        room = i.getRoom();
                        chek = false;
                    }
                    occId.add(i.getId());
                    people.add(i.getPerson());

                }
            }


            roomPeopleDTO = createRoomPeopleDTO(room, people, occId);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoomPeople method fail ");

        }

        return roomPeopleDTO;


    }*/

    //--------------------ROLES

    @Override
    public ArrayList<RoleDTO> getRoles() {
        ArrayList<RoleDTO> rolesDTO = new ArrayList<>();

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Role> roles = new ArrayList<>(session.createQuery("from Role").list());
            rolesDTO.addAll(roles.stream().map(this::createRoleDTO).collect(Collectors.toList()));
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getRoles method fail ");

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
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.save(role);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : saveRole  method fail ");

        }

        return new Integer(role.getId());

    }

    public Integer updateRole(RoleDTO roleDTO) {
        Role role = new Role(roleDTO);
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(role);
            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : updateRole method fail ");

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
            //System.out.println("55555555555555555....."+fullPath+".....555555555555");

            File folder = new File(fullPath);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if(file.getName().contains(".svg")){
                    string.add(file.getName());
                }
            }


        } catch (Exception e) {
            System.out.println("Error reading name of maps");
        }

        for (String s : string) {
            //System.out.println(s.substring(0,s.lastIndexOf('-')));
            //System.out.println(s.substring(s.lastIndexOf('-')+1,s.lastIndexOf('.')));
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
                createRoleDTO(person.getRole()), person.getStartWork(), person.getEndWork());
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
        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Occupy> occup = new ArrayList<>((session.createQuery("from Occupy where person.name='" + part1 + "' and person.surname='" + part2 + "'").list()));

            if (occup.size() > 0) {
                for (Occupy occupy : occup) {
                    occupyDTO.add(createOccupyDTO(occupy));
                }


            }

            session.getTransaction().commit();


        } catch (Exception e) {
            System.out.println("ERROR : getOccupySearch method fail ");
            e.printStackTrace();
        }

        return occupyDTO;
    }
    public ArrayList<OccupyDTO> getOccupySearch(ArrayList<PersonDTO>persons) {
        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();
        for(PersonDTO personDTO:persons){
            occupyDTO.addAll(getOccupySearch(personDTO.getName(),personDTO.getSurname()));
        }
        return occupyDTO;
    }


    public ArrayList<String> listaAulePianoNewVersion(String text) {
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath(addImageGWT);
        URI uri = new File(fullPath+"/" + text + ".svg").toURI();
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
