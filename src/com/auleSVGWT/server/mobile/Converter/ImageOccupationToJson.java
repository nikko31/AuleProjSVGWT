package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;


public class ImageOccupationToJson extends CommonVar implements JsonConverter<String,JSONObject> {

    @Override
    public JSONObject convert(String buildingFloor) {
        JSONObject image = new JSONObject();
        image.put("name","occu_"+buildingFloor);
        image.put("extension","png");
        image.put("height","1280");
        image.put("width","720");

        //--------------------------------
        s= UriBuilder.fromResource(ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(ImagesHandler.class, "getOccupationImage");
        m = new HashMap<>();
        m.put("buildingFloor","occu_"+buildingFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/occupazione/occu_"+buildingFloor+".png");
        image.put("PNGLink",RESOURCE+s);

        //--------------------------------
        s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(JaxRestBuildings.class, "getOccRoomsOnFloor");
        m = new HashMap<>();
        m.put("buildFloor",buildingFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("infoFloorSpace","rest/edifici/"+buildingFloor+"/occupazione/stanze");
        image.put("infoFloorSpace",REST+s);
        return image;
    }
}
