package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


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

            DatabaseM db = new DatabaseM();

            occupyDTOs = db.getOccupyOfPerson(part1,part2);
            if((occupyDTOs == null) || (occupyDTOs.size() == 0)){
                occupyDTOs = db.getOccupyOfPerson(part2,part1);
            }
            parseOut(occupyDTOs, out);

        }else{
            out.print("");
            out.close();
        }






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
                if(!obj.containsKey("number")){
                    obj.put("number",""+occupyDTO.getRoom().getNumber());

                }

                obj.put("building",""+occupyDTO.getRoom().getBuilding().getName());
                obj.put("floor",""+occupyDTO.getRoom().getFloor());
                obj.put("info",""+occupyDTO.getRoom().getMaintenance());
                obj.put("personMax",""+occupyDTO.getRoom().getMaxPeople());
                obj.put("socket",""+occupyDTO.getRoom().getSocket());
                obj.put("dimension",""+occupyDTO.getRoom().getDimension());
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
            obj.put("rooms",ar);
            obj.put("person",person);



        }else{
            obj= new JSONObject();
            obj.put("rooms",ar);
            obj.put("person",person);

        }



        out.print(obj.toString());
        out.close();

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


}