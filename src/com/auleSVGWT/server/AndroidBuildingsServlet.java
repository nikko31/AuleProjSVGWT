package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.PersonDTO;
import jdk.nashorn.internal.ir.ObjectNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Utente on 01/08/2016.
 */
public class AndroidBuildingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String addImageAndroid="/res/imageAndroid";


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try{
            printBuildings(out);

        }catch (Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.close();
        }


    }




    private void printBuildings(PrintWriter printOut)throws Exception{


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
                JSONObject floor = new JSONObject();
                JSONObject link = new JSONObject();

                link.put("imageRoom", "/Android/immagine/" + buildingString + "-" + floorString + ".png");
                link.put("imageRoomSpace","/Android/immagine/occupazione/" + buildingString + "-" + floorString+".png");
                link.put("imageRoomWork", "/Android/immagine/lavoro/" + buildingString + "-" + floorString +".png");
                link.put("roomsList","/Android/listaStanzePiano/"+buildingString+"-"+floorString+".json");
                link.put("peopleWork","/Android/persone/lavoro/"+buildingString+"-"+floorString+".json");
                link.put("roomsOccu","/Android/stanze/occupazione/"+buildingString+"-"+floorString+".json");
                floor.put("floor", floorString);
                floor.put("link",link);
                arr.add(i, floor);
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

