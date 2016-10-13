package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;


public class ImageWorkToJson extends CommonVar implements JsonConverter<String,JSONObject> {
    @Override
    public JSONObject convert(String buildingFloor) {
        JSONObject image = new JSONObject();
        image.put("name", "work_" + buildingFloor);
        image.put("extension","png");
        image.put("height","1280");
        image.put("width","720");


        //--------------------------------
        s= UriBuilder.fromResource(ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(ImagesHandler.class, "getWorkImage");
        m = new HashMap<>();
        m.put("buildingFloor","work_"+buildingFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/lavoro/work_"+buildingFloor+".png");
        image.put("PNGLink",RESOURCE+s);

        //------------------------
        s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(JaxRestBuildings.class, "getEndWorkPeopleOnFloor");
        m = new HashMap<>();
        m.put("buildFloor",buildingFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("infoFloorWork","rest/edifici/"+buildingFloor+"/lavoro/persone");
        image.put("infoFloorWork",REST+s);
        return image;

    }
}
