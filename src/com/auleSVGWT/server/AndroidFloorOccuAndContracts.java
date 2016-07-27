package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.OccupyDTO;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Utente on 27/07/2016.
 */
public class AndroidFloorOccuAndContracts extends HttpServlet{

    private static final long serialVersionUID = 1L;
    private static final String addImageAndroid="/res/imageAndroid";
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String building = request.getParameter("building");
        String floor = request.getParameter("floor");
        String query = request.getParameter("query");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        ArrayList<OccupyDTO> occupyDTO = new ArrayList<>();
        ArrayList<RoomDTO> roomDTO = new ArrayList<>();


        if(controlParameter(building,floor,query)){

            if("lavoro".equals(query)){

                parseOutWork(out, building, floor);

            }
            if("occupa".equals(query)) {

                parseOutOccupy(out, building, floor);
            }

        }else{
            out.print("noResult");

        }





    }




    private void parseOutWork(PrintWriter out,String building,String floor){
        JSONArray arrayPersonJ = new JSONArray();
        JSONObject personJSON;
        JSONObject obj;

        DatabaseM db= new DatabaseM();
        ArrayList<PersonDTO> personDTOs = db.getOccupyOfFloorwithDate(building,floor);


        if(personDTOs.size()>0){

            int j = 0;
            for(PersonDTO personDTO : personDTOs){

                if(personDTO.getEndWork()!=null){
                    personJSON = new JSONObject();


                    personJSON.put("name", personDTO.getName());
                    personJSON.put("surname", personDTO.getSurname());
                    personJSON.put("role",personDTO.getRole().getName());
                    if(personDTO.getStartWork() != null){
                        String sW = personDTO.getStartWork().toString();
                        sW = sW.replaceAll("-"," ");
                        personJSON.put("startWork",sW);
                    }else{
                        personJSON.put("startWork","null");

                    }
                    if(personDTO.getEndWork() != null){
                        String eW = personDTO.getEndWork().toString();
                        eW = eW.replaceAll("-", " ");
                        personJSON.put("endWork",eW);


                    }else{
                        personJSON.put("endWork","null");

                    }

                    arrayPersonJ.add(j,personJSON);
                    j++;

                }





            }

        }

        obj= new JSONObject();
        obj.put("persons",arrayPersonJ);
        out.print(obj.toString());
    }


    private void parseOutOccupy(PrintWriter out,String building,String floor){
        JSONArray arrayRoomJ = new JSONArray();
        JSONObject roomJson;
        JSONObject obj;

        DatabaseM db= new DatabaseM();
        ArrayList<RoomDTO> roomDTOs = db.getOccupyOfFloorwithDimension(building,floor);

        if(roomDTOs.size()>0){

            int i = 0;

            for(RoomDTO roomDTO : roomDTOs){

                roomJson = new JSONObject();
                roomJson.put("building",""+roomDTO.getBuilding().getName());
                roomJson.put("floor",""+roomDTO.getFloor());
                roomJson.put("info", "" + roomDTO.getMaintenance());
                roomJson.put("personMax", "" + roomDTO.getMaxPeople());
                roomJson.put("socket", "" + roomDTO.getSocket());
                roomJson.put("dimension",""+roomDTO.getDimension());
                roomJson.put("number", "" + roomDTO.getNumber());

                arrayRoomJ.add(i, roomJson);
                i++;

            }

        }

        obj=new JSONObject();
        obj.put("rooms",arrayRoomJ);
        out.print(obj.toString());

    }


    private Boolean controlParameter(String building,String floor,String query){
        Boolean controlFlag = true;

        if(query== null) {
            controlFlag = false;
        }

        if(controlFlag){
            if(!"lavoro".equals(query) && !"occupa".equals(query)){
                controlFlag = false;
            }
            if(building == null || floor == null){
                controlFlag = false;
            }
        }

        return controlFlag;
    }



}
