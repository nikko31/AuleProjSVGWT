package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.*;
import com.auleSVGWT.server.domain.*;
import com.auleSVGWT.util.HibernateUtil;
import org.hibernate.classic.Session;
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
public class AndroidSearchPersonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AndroidSearchPersonServlet() {
        super();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        ArrayList<OccupyDTO> occupyDTOs = new ArrayList<>();
        String part1 = req.getParameter("name1");
        String part2 = req.getParameter("name2");

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();



        if(parameterControl(part1,part2)){

            occupyDTOs = getOccupy(part1,part2);
            if((occupyDTOs == null) || (occupyDTOs.size() == 0)){
                occupyDTOs = getOccupy(part2,part1);
            }

        }


        parseOut(occupyDTOs, out);





    }

    private void parseOut(ArrayList<OccupyDTO> occupyDTOs,PrintWriter out){
        JSONArray ar;
        JSONObject obj;
        JSONObject person;


        person = new JSONObject();
        ar = new JSONArray();

        if(occupyDTOs.size() !=0 && occupyDTOs != null){
            ar = new JSONArray();


            int i=0;
            for(OccupyDTO occupyDTO : occupyDTOs){
                obj = new JSONObject();
                if(!obj.containsKey("numero")){
                    obj.put("numero",""+occupyDTO.getRoom().getNumber());

                }

                obj.put("edificio",""+occupyDTO.getRoom().getBuilding().getName());
                obj.put("piano",""+occupyDTO.getRoom().getFloor());
                obj.put("info",""+occupyDTO.getRoom().getMaintenance());
                if(obj.size()!=0){
                    ar.add(i,obj);
                    i++;
                }

            }


            person.put("name",occupyDTOs.get(0).getPerson().getName());
            person.put("surname",occupyDTOs.get(0).getPerson().getSurname());
            person.put("role",occupyDTOs.get(0).getPerson().getRole().getName());

            if(occupyDTOs.get(0).getPerson().getStartWork() == null){
                person.put("startWork","null");

            }else{
                person.put("startWork",occupyDTOs.get(0).getPerson().getStartWork().toString());
            }
            if(occupyDTOs.get(0).getPerson().getEndWork() == null){
                person.put("endWork","null");

            }else{
                person.put("endWork",occupyDTOs.get(0).getPerson().getEndWork().toString());
            }

            obj= new JSONObject();
            obj.put("stanze",ar);
            obj.put("persona",person);



        }else{
            obj= new JSONObject();
            obj.put("stanze",ar);
            obj.put("persona",person);

        }



        out.print(obj.toString());

    }

    private boolean parameterControl(String part1,String part2){

        boolean controlFlag = true;

        if(part1 == null || part2 == null){
            controlFlag = false;
        }
        if(part1 != null){
            if(!controlOfLettersOnly(part1)){
                controlFlag =false;
            }
            if(part1.length()>20){
                controlFlag =false;
            }

        }
        if(part2 != null){
            if(!controlOfLettersOnly(part2)){
                controlFlag =false;
            }
            if(part2.length()>20){
                controlFlag =false;
            }


        }

        return controlFlag;

    }

    private boolean controlOfLettersOnly(String s) {
        boolean flag= true;

        for(int i = 0; i < s.length(); i++) {
            if(flag){
                if (!Character.isLetter(s.charAt(i))){
                    flag = false;
                }
            }
        }
        return flag;
    }

    public ArrayList<OccupyDTO> getOccupy(String part1,String part2) {
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