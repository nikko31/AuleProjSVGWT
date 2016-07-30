package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.PersonDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AndroidStartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String addImageAndroid="/res/imageAndroid";

    public AndroidStartServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {


        String reque = request.getParameter("request");

        ArrayList<String> string = new ArrayList<>();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        if(reque == null){
            printBuildings(out);
        }else if("allPeople".equals(reque)){
            printPeople(out);
        }else{
            out.print("");
            out.close();
        }






    }


    private void printPeople(PrintWriter out){
        JSONObject personJSON = new JSONObject();
        JSONArray perosnARRJSON = new JSONArray();
        ArrayList<PersonDTO> personDTOs;
        DatabaseM db = new DatabaseM();
        personDTOs = db.getPerson();


        if(personDTOs.size()>0){
            int i = 0;
            for(PersonDTO personDTO : personDTOs){
                JSONObject stringNameSurname = new JSONObject();
                stringNameSurname.put("name",personDTO.getName());
                stringNameSurname.put("surname",personDTO.getSurname());
                perosnARRJSON.add(i,stringNameSurname);
                i++;
            }

        }

        personJSON.put("personsNameSur",perosnARRJSON);
        out.print(personJSON.toString());
        out.close();


    }

    private void printBuildings(PrintWriter printOut){

        ArrayList<String> string = new ArrayList<>();
        HashMap<String, ArrayList<String>> buildings = new HashMap<>();

        try {
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath(addImageAndroid);

            File folder = new File(fullPath);
            File[] listOfFiles = folder.listFiles();

            if(listOfFiles != null){
                for (File file : listOfFiles) {
                    String s= file.getName();
                    if (buildings.containsKey(s.substring(0, s.lastIndexOf('-')))) {
                        ArrayList<String> floors;
                        floors = buildings.get(s.substring(0, s.lastIndexOf('-')));
                        floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                        Collections.sort(floors);
                        buildings.replace(s.substring(0, s.lastIndexOf('-')), floors);
                    } else {
                        ArrayList<String> floors = new ArrayList<>();
                        floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                        buildings.put(s.substring(0, s.lastIndexOf('-')), floors);
                    }
                    //string.add(file.getName());
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading name of maps");
            e.printStackTrace();
        }

        printOutBuildingJSON(buildings, printOut);





    }




    private void  printOutBuildingJSON(HashMap<String, ArrayList<String>> buildings,PrintWriter out){
        int j = 0;
        JSONArray buildingFloors = new JSONArray();
        for (String buildingString : buildings.keySet()) {


            int i = 0;
            JSONObject build = new JSONObject();
            JSONArray arr = new JSONArray();


            for (String floorString : buildings.get(buildingString)) {
                arr.add(i, floorString);
                i++;
            }
            build.put("name", buildingString);

            build.put("floors", arr);
            buildingFloors.add(j, build);
            j++;
        }

        JSONObject obj = new JSONObject();
        obj.put("buildings", buildingFloors);

        out.print(obj);
        out.close();

    }
}

