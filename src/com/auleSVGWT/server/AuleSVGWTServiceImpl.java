package com.auleSVGWT.server;

import com.auleSVGWT.client.AuleSVGWTService;
import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;


public class AuleSVGWTServiceImpl extends RemoteServiceServlet implements AuleSVGWTService {
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

    //---------------------PERSON

    public ArrayList<PersonDTO> getPerson() {
        ArrayList<PersonDTO> personDTO = new ArrayList<>();
        try {

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Person> persons = new ArrayList<>(session.createQuery("from Person ").list());

            for (Person person : persons) {
                personDTO.add(createPersonDTO(person));
            }
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

        com.auleSVGWT.server.domain.Person person = new com.auleSVGWT.server.domain.Person(personDTO);

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

    //-----------------STANZE OCCUPATE------------

    @Override
    public ArrayList<RoomPeopleDTO> getRoomsPeople(String building, String floorSt) {
        int floor=Integer.parseInt(floorSt);
        ArrayList<RoomPeopleDTO> roomPeopleDTO = new ArrayList<RoomPeopleDTO>();
        ArrayList<Person> people;
        ArrayList<Long> occ;
        try{
            org.hibernate.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            ArrayList<Occupy> occupies = new ArrayList<Occupy>(session.createQuery("from Occupy ").list());
            ArrayList<Room> rooms = new ArrayList<Room>(session.createQuery("from Room ").list());

            for (Room room : rooms) {

                //System.out.println(room.getFloor() + " = " + floor + " " + room.getBuilding().getName() + " = " + building + "....................................................");
                if ((room.getFloor() == floor) && (room.getBuilding().getName().equals(building))) {

                    people = new ArrayList<Person>();
                    occ = new ArrayList<Long>();
                    for (Occupy occupy : occupies) {
                        //System.out.println("sono nel cilo occupy" + occupy.getId() + " = " + room.getId() + "....................................................");
                        if (occupy.getRoom().getId() == room.getId()) {
                            people.add(occupy.getPerson());
                            occ.add(occupy.getId());
                            System.out.println(people.get(0).getName() + "\n");
                        }
                    }
                    roomPeopleDTO.add(createRoomPeopleDTO(room, people, occ));
                }
            }
            session.getTransaction().commit();

        }catch(Exception e){
            System.out.println("ERROR : getRoomsPeople method fail ");

        }

        return roomPeopleDTO;
    }




    @Override
    public RoomPeopleDTO getRoomPeople(String building, String floorSt, String numberSt) {
        int number=Integer.parseInt(numberSt);
        int floor=Integer.parseInt(floorSt);
        Room room1 = new Room();
        ArrayList<Long> occ = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();
        RoomPeopleDTO roomPeopleDTO = new RoomPeopleDTO();
        boolean check = true;

        try {
            org.hibernate.classic.Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            ArrayList<Occupy> occupies = new ArrayList<Occupy>(session.createQuery("from Occupy ").list());
            ArrayList<Room> rooms = new ArrayList<Room>(session.createQuery("from Room ").list());


            for (Room room : rooms) {

                System.out.println(room.getFloor() + " = " + floor + " " + room.getBuilding().getName() + " = " + building + "....................................................");
                if ((room.getFloor() == floor) && (room.getBuilding().getName().equals(building) && (room.getNumber() == number))) {
                    check = false;

                    for (Occupy occupy : occupies) {
                        System.out.println("sono nel cilo occupy" + occupy.getId() + " = " + room.getId() + "....................................................");
                        if (occupy.getRoom().getId() == room.getId()) {
                            people.add(occupy.getPerson());
                            occ.add(occupy.getId());
                            System.out.println(people.get(0).getName() + "\n");
                        }
                    }
                    room1 = room;



                }
            }


            if(check){

                roomPeopleDTO = createRoomPeopleDTO(newRoom( building, floorSt,  numberSt, session),people,occ);

            }else{
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
        ArrayList<RoleDTO> rolesDTO = new ArrayList<RoleDTO>();

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

            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                aule.add(edificiopiano + "-" + iterator.next());
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

        return new Integer(role.getId());

    }

    //-------------------------PRIVATE METHOD

    private Room newRoom(String buildingName, String floorSt, String numberSt, org.hibernate.Session session) {
        int number = Integer.parseInt(numberSt);
        int floor = Integer.parseInt(floorSt);
        Room room = new Room();
        Building building = new Building();
        boolean check = true;
        try {

            ArrayList<Building> buildings= new ArrayList<Building>(session.createQuery("from Building ").list());

            for(Building build : buildings){
                if(build.getName().equals(buildingName)){
                    room.setBuilding(build);
                    room.setFloor(floor);
                    room.setNumber(number);
                    room.setMaxPeople(0);
                    room.setDimension(0);
                    session.save(room);
                    check = false;
                }
            }

            if(check){
                building.setName(buildingName);
                session.save(building);
                room.setBuilding(building);
                room.setFloor(floor);
                room.setNumber(number);
                room.setMaxPeople(0);
                room.setDimension(0);
                session.save(room);
            }


        } catch (Exception e) {
            System.out.println("ERROR : newRoom method fail ");

        }

        return room;

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

        return new Integer(id);
    }

    private RoomPeopleDTO createRoomPeopleDTO(Room room, ArrayList<com.auleSVGWT.server.domain.Person> people, ArrayList<Long> occId) {
        ArrayList<PersonDTO> peopleDTO = new ArrayList<>();

        for (com.auleSVGWT.server.domain.Person person : people) {
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

    private PersonDTO createPersonDTO(com.auleSVGWT.server.domain.Person person) {

        return new PersonDTO(person.getId(), person.getName(), person.getSurname(),
                createRoleDTO(person.getRole()));
    }

    private BuildingDTO createBuildingDTO(Building building) {

        return new BuildingDTO(building.getNumber(), building.getName());
    }

    private RoomDTO createRoomDTO(Room room) {

        return new RoomDTO(room.getId(), room.getNumber(), room.getFloor(),
                createBuildingDTO(room.getBuilding()), room.getMaxPeople(), room.getDimension());
    }
}

/*
public class AuleSVGWTServiceImpl extends RemoteServiceServlet implements AuleSVGWTService {
    @Override
    public ArrayList<Stanza> getMessage(String edificio, String piano) {
        Connection connection = null;
        ArrayList<Stanza> stanze = new ArrayList<>();
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT stanza.id as idStanza,stanza.numero,persona.id,stanza.num_max_persone,stanza.dimensione,persona.nome,persona.cognome,persona.ruolo\n" +
                            "FROM edificio,stanza,persona\n" +
                            "WHERE edificio.id=stanza.edificio and stanza.id=persona.stanza \n" +
                            "and edificio.nome='" + edificio + "' and  stanza.piano='" + piano + "'\n" +
                            "ORDER BY stanza.id"
            );

            while (rs.next()) {
                // read the result set
                Integer stanza_num = rs.getInt("numero");
                int find = -1;
                //controllo se esiste la stanza nel mio array
                for (int c = 0; c < stanze.size(); c++) {
                    if (stanza_num.equals(stanze.get(c).getNumero())) {
                        Stanza stanza = stanze.get(c);
                        stanza.addProf(new Persona(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("ruolo")));
                        stanze.set(c, stanza);
                        find = c;
                        break;
                    }
                }
                //non trovata
                if (find < 0) {
                    Stanza stanza = new Stanza(rs.getInt("idStanza"), rs.getInt("numero"), rs.getInt("num_max_persone"), rs.getInt("dimensione"));
                    stanza.addProf(new Persona(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("ruolo")));
                    stanze.add(stanza);
                }


            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return stanze;
    }

    @Override
    public ArrayList<PersonDetails> getPersonsDetails(String edificio, String piano) {
        Connection connection = null;
        ArrayList<PersonDetails> personDetails = new ArrayList<>();
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT persona.id,persona.nome,persona.cognome,\n" +
                            "FROM edificio,stanza,persona\n" +
                            "WHERE edificio.id=stanza.edificio and stanza.id=persona.stanza \n" +
                            "and edificio.nome='" + edificio + "' and  stanza.piano='" + piano + "'\n"
            );

            while (rs.next()) {
                // read the result set
                personDetails.add(new PersonDetails(rs.getString("id"), rs.getString("nome") + " " + rs.getString("cognome")));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return personDetails;
    }

    @Override
    public ArrayList<PersonDetails> getInvadersDetails(String building, String floor, String number) {
        Connection connection = null;
        ArrayList<PersonDetails> personDetails = new ArrayList<>();
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT persona.id,persona.nome,persona.cognome\n" +
                            "FROM edificio,stanza,persona\n" +
                            "WHERE edificio.id=stanza.edificio and stanza.id=persona.stanza \n" +
                            "and edificio.nome='" + building + "' and  stanza.piano='" + floor +
                            "' and stanza.numero='" + number + "'\n"
            );

            while (rs.next()) {
                // read the result set
                personDetails.add(new PersonDetails(rs.getString("id"), rs.getString("nome") + " " + rs.getString("cognome")));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return personDetails;
    }

    @Override
    public Person getRoomInvader(String id) {
        Connection connection = null;
        Person person = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT persona.id,persona.nome,persona.cognome,persona.ruolo\n" +
                            "FROM persona\n" +
                            "WHERE persona.id='" + id + "'"
            );

            while (rs.next()) {
                // read the result set
                person=new Person(rs.getString("id"), rs.getString("ruolo"), rs.getString("cognome"),rs.getString("nome"));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return person;
    }

    @Override
    public ArrayList<String> getRoles() {
        Connection connection = null;
        ArrayList<String> roles = new ArrayList<>();
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("SELECT ruolo.nome\n" +
                            "FROM ruolo"
            );
            while (rs.next())
                roles.add(rs.getString("nome"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return roles;
    }

    @Override
    public void addPersona(Persona persona) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery(
                    "INSERT INTO persona (nome,cognome,ruolo)" +
                            "VALUES ('" + persona.getNome() + "','" + persona.getCognome() + "','" + persona.getRuolo() + "')"
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    //aggiunte

    @Override
    public ArrayList<String> listaEdiPiani() {
        File folder = new File("res");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> string = new ArrayList<String>();
        for (File file : listOfFiles) {
            string.add(file.getName());
            System.out.println(file.getName());
        }

        return string;


    }

    @Override
    public ArrayList<String> listaAulePiano(String edificiopiano) {
        ArrayList<String> aule = new ArrayList<String>();
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader("doc.JSON"));
            JSONObject jsonObject = (JSONObject) obj;


            JSONArray list = (JSONArray) jsonObject.get(edificiopiano);

            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                aule.add(edificiopiano + "-" + iterator.next());
                //System.out.println(edificiopiano +"-"+iterator.next());
            }
            for (String bob : aule) {
                System.out.println(bob);
            }


        } catch (Exception e) {

            e.printStackTrace();

        }
        return aule;
    }


}*/
