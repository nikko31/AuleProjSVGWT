package com.auleSVGWT.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Utente on 10/08/2016.
 */
@Path("/listaEdifici.json")
public class JaxAndroidBuildings {
    final String path = "/res/imageAndroid";


    @Context
    ServletContext servletContext;

    @GET
    public Response getBuildings( ) {

        try {
            String json = printBuildings().toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    private JSONObject printBuildings()throws Exception{


        HashMap<String, ArrayList<String>> buildings = new HashMap<>();

        try {

            String fullPath = servletContext.getRealPath(path);
            System.out.println(servletContext.getContextPath()+"++++++questo è il cammino");
            System.out.println(servletContext.getServletContextName()+"context nme");

            if(fullPath!=null){
                System.out.println(fullPath+"++++++questo è il cammino completo");
            }
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

        return printOutBuildingJSON(buildings);





    }




    private JSONObject  printOutBuildingJSON(HashMap<String, ArrayList<String>> buildings){

        int j = 0;
        JSONArray buildingFloors = new JSONArray();

        for (String buildingString : buildings.keySet()) {


            int i = 0;
            JSONObject build = new JSONObject();
            JSONArray arr = new JSONArray();




            for (String floorString : buildings.get(buildingString)) {
                JSONObject floor = new JSONObject();
                JSONObject link = new JSONObject();

                String building = buildingString.replace(' ','_');
                link.put("imageRoom", "/Android/immagine/" + building + "-" + floorString + ".png");
                link.put("imageRoomSpace","/Android/immagine/occupazione/" + building + "-" + floorString+".png");
                link.put("imageRoomWork", "/Android/immagine/lavoro/" + building + "-" + floorString +".png");
                link.put("roomsList","/Android/listaStanzePiano/"+building+"-"+floorString+".json");
                link.put("peopleWork","/Android/persone/lavoro/"+building+"-"+floorString+".json");
                link.put("roomsOccu","/Android/stanze/occupazione/"+building+"-"+floorString+".json");
                floor.put("floor", floorString);
                floor.put("link", link);
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


        return obj;

    }

}
