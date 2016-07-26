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

public class AndroidPersonRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String building;
        String floor;
        String numRoom;
        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        building = req.getParameter("building");
        floor = req.getParameter("floor");
        numRoom = req.getParameter("numRoom");

        if(controlParameter(building,floor,numRoom)){
            DatabaseM  db= new DatabaseM();
            occupyDTO = db.getOccupyOfRoom(building, floor, numRoom);
        }


        parseOut(occupyDTO, out);
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

    private void parseOut(ArrayList<OccupyDTO> occupyDTO,PrintWriter out){
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        JSONObject per;
        JSONObject roomJSon = new JSONObject();
        PersonDTO  personDTO;

        if(occupyDTO != null ){
            int j=0;

            for(OccupyDTO occ : occupyDTO){

                personDTO = occ.getPerson();

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

            RoomDTO roomDTO = occupyDTO.get(0).getRoom();

            System.out.println("a volte mi da errore ::::::::::::::"+roomDTO.getDimension());
            roomJSon.put("dimension", "" + roomDTO.getDimension());
            System.out.println("a volte mi da errore ::::::::::::::" + roomDTO.getDimension());

            roomJSon.put("building", "" + roomDTO.getBuilding().getName());
            roomJSon.put("floor",""+roomDTO.getFloor());
            roomJSon.put("number",""+roomDTO.getNumber());
            roomJSon.put("socket", "" + roomDTO.getSocket());
            roomJSon.put("personMax",""+roomDTO.getMaxPeople());
            if(roomDTO.getMaintenance() == null){
                roomJSon.put("info","nessuna info");
            }else{
                roomJSon.put("info",""+roomDTO.getMaintenance());
            }

            obj.put("room",roomJSon);


            obj.put("persons",arr);

        }else{
            obj.put("persons",arr);
            obj.put("room",roomJSon);


            //////
        }

        out.println(obj);
        out.close();


    }






}