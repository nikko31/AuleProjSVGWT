package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import com.auleSVGWT.server.mobile.JaxRestImage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class RoomsImageListToJson extends  CommonVar implements JsonConverter<ArrayList<Integer>,JSONArray>{
    String buildingFloor;

    public RoomsImageListToJson(String buildingFloor) {
        this.buildingFloor = buildingFloor;
    }

    @Override
    public JSONArray convert(ArrayList<Integer> rooms) {
        JSONArray arr = new JSONArray();
        Collections.sort(rooms);


        if ((rooms.size() != 0)) {

            for (Integer room : rooms) {
                // System.out.println("VErifico ordine:::::::::::::::: "+room);
                JSONObject ro = new JSONObject();
                JSONObject image = new JSONObject();

                String buildFloor = buildingFloor.replace(' ','_');
                String num =""+ String.valueOf(room);



                //--------------------------------
                s= UriBuilder.fromResource(JaxRestImage.class).toString();
                u =UriBuilder.fromMethod(JaxRestImage.class, "getRoomImage");
                m = new HashMap<>();
                m.put("buildingFloor",buildFloor);
                m.put("numRoom",num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //image.put("restLink","/rest/immagini/edifici/"+buildingFloor+"/stanze/"+num);
                image.put("restLink",REST+s);

                //--------------------------------
                s=UriBuilder.fromResource(ImagesHandler.class).toString();
                u =UriBuilder.fromMethod(ImagesHandler.class, "getRoomImage");
                m = new HashMap<>();
                m.put("buildingFloorNumb",buildFloor+"-"+num+".png");
                m.put("room",num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s + ".png");
                //image.put("PNGLink","/risorse/immagini/edifici/stanze/"+buildingFloor+"-"+num+".png");
                image.put("PNGLink",RESOURCE+s);
                ro.put("imageSelectRoom",image);

                //--------------------------------
                s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(JaxRestBuildings.class, "getRoomInfo");
                m = new HashMap<>();
                m.put("buildFloor",buildFloor);
                m.put("room", num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //ro.put("infoSelectRoom","/rest/edifici/"+buildingFloor + "/stanze/" + num);
                ro.put("infoSelectRoom",REST+s);

                //--------------------------------
                s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(JaxRestBuildings.class, "getPeopleInRoom");
                m = new HashMap<>();
                m.put("buildFloor",buildFloor);
                m.put("room", num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //ro.put("infoPeopleInRoom","/rest/edifici/"+buildingFloor + "/stanze/" + num+"/persone");
                ro.put("infoPeopleInRoom",REST+s);


                ro.put("room", "" + room);

                arr.add(ro);


            }

            return arr;
        }


        return null;
    }
}
