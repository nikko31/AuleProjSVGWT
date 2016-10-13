package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import com.auleSVGWT.server.mobile.JaxRestImage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class BuildingsImageToJson extends CommonVar implements JsonConverter<String,JSONArray> {

    @Override
    public JSONArray convert(String path){


        HashMap<String, ArrayList<String>> buildings = new HashMap<>();

        try {

            File folder = new File(path);
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

        return parseBuildingsJSON(buildings);





    }

    private JSONArray parseBuildingsJSON(HashMap<String, ArrayList<String>> buildings) {
        JSONArray buildingsFloors = new JSONArray();
        if(buildings.keySet().size()>0){
            for (String buildingString : buildings.keySet()) {



                JSONObject buildingFloors = new JSONObject();
                JSONArray arr = new JSONArray();




                for (String floorString : buildings.get(buildingString)) {
                    JSONObject floor = new JSONObject();
                    JSONObject image;

                    String building = buildingString.replace(' ','_');





                    //--------------------------------
                    image = new JSONObject();
                    s= UriBuilder.fromResource(JaxRestImage.class).toString();
                    u =UriBuilder.fromMethod(JaxRestImage.class, "getFloorImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //image.put("restLink","/rest/immagini/edifici/" + building + "-" + floorString);
                    image.put("restLink",REST+s);

                    //-----------------------------
                    s=UriBuilder.fromResource(ImagesHandler.class).toString();
                    u =UriBuilder.fromMethod(ImagesHandler.class, "getFloorImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString+".png");
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s + ".png");
                    //image.put("PNGLink","/risorse/immagini/edifici/" + building + "-" + floorString + ".png");
                    image.put("PNGLink",RESOURCE+s);
                    floor.put("imageFloor",image);

                    //---------------------------
                    image = new JSONObject();
                    s=UriBuilder.fromResource(JaxRestImage.class).toString();
                    u =UriBuilder.fromMethod(JaxRestImage.class, "getOccupationImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //image.put("restLink","/rest/immagini/edifici/" + building + "-" + floorString + "/occupazione");
                    image.put("restLink",REST+s);
                    //----------------------------

                    s=UriBuilder.fromResource(ImagesHandler.class).toString();
                    u =UriBuilder.fromMethod(ImagesHandler.class, "getOccupationImage");
                    m = new HashMap<>();
                    m.put("buildingFloor","occu_"+building + "-" + floorString+".png");
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s + ".png");
                    //image.put("PNGLink","/risorse/immagini/edifici/occupazione/occu_" + building + "-" + floorString+".png");
                    image.put("PNGLink",RESOURCE+s);
                    floor.put("imageFloorSpace",image);

                    //------------------------------------------
                    image = new JSONObject();
                    s=UriBuilder.fromResource(JaxRestImage.class).toString();
                    u =UriBuilder.fromMethod(JaxRestImage.class, "getWorkImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //image.put("restLink","/rest/immagini/edifici/" + building + "-" + floorString + "/lavoro");
                    image.put("restLink",REST+s);

                    //---------------------------------
                    s=UriBuilder.fromResource(ImagesHandler.class).toString();
                    u =UriBuilder.fromMethod(ImagesHandler.class, "getWorkImage");
                    m = new HashMap<>();
                    m.put("buildingFloor","work_"+building + "-" + floorString+".png");
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s + ".png");
                    //image.put("PNGLink","/risorse/immagini/edifici/lavoro/work_"+ building + "-" + floorString +".png");
                    image.put("PNGLink",RESOURCE+s);
                    floor.put("imageFloorWork",image);

                    //-------------------
                    s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                    u =UriBuilder.fromMethod(JaxRestBuildings.class, "getSimpleListOfRoomOnFloor");
                    m = new HashMap<>();
                    m.put("buildingFloor", building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //floor.put("infoRoomsList","/rest/edifici/"+building+"-"+floorString+"/stanze");
                    floor.put("infoRoomsList",REST+s);

                    //-------------------
                    s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                    u =UriBuilder.fromMethod(JaxRestBuildings.class, "getEndWorkPeopleOnFloor");
                    m = new HashMap<>();
                    m.put("buildFloor", building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //floor.put("infoFloorWork","/rest/edifici/"+building+"-"+floorString+"/lavoro/persone");
                    floor.put("infoFloorWork",REST+s);

                    //-------------------
                    s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                    u =UriBuilder.fromMethod(JaxRestBuildings.class, "getOccRoomsOnFloor");
                    m = new HashMap<>();
                    m.put("buildFloor", building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //floor.put("infoFloorSpace","/rest/edifici/"+building+"-"+floorString+"/occupazione/stanze");
                    floor.put("infoFloorSpace",REST+s);
                    //senza spazio
                    floor.put("floor", floorString);
                    arr.add(floor);

                }

                buildingFloors.put("building", buildingString);

                buildingFloors.put("floors", arr);

                buildingsFloors.add(buildingFloors);

            }

            return buildingsFloors;

        }else{
            return null;
        }
    }
}
