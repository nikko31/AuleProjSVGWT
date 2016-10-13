package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;


public class ImageRoomToJson extends CommonVar implements JsonConverter<String,JSONObject> {
    String buildingFloor;

    public ImageRoomToJson(String buildingFloor) {
        this.buildingFloor = buildingFloor;
    }

    @Override
    public JSONObject convert(String numRoom) {
        JSONObject image = new JSONObject();
        image.put("name", ""+buildingFloor+"-"+numRoom);
        image.put("extension","png");
        image.put("height", "1280");
        image.put("width","720");


        //--------------------------------
        s= UriBuilder.fromResource(ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(ImagesHandler.class,"getRoomImage");
        m = new HashMap<>();
        m.put("buildingFloorNumb",buildingFloor+"-"+numRoom+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/stanze/"+buildingFloor+"-"+numRoom+".png");
        image.put("PNGLink",RESOURCE+s);

        //------------------------
        s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(JaxRestBuildings.class, "getRoomInfo");
        m = new HashMap<>();
        m.put("buildFloor",buildingFloor);
        m.put("room",numRoom);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("infoSelectRoom","/rest/edifici/"+buildingFloor+ "/stanze/" + numRoom);
        image.put("infoSelectRoom",REST+s);

        return image;
    }
}
