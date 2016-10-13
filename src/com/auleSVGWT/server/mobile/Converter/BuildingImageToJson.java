package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import com.auleSVGWT.server.mobile.JaxRestImage;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;

public class BuildingImageToJson extends CommonVar implements JsonConverter<String,JSONObject> {
    @Override
    public JSONObject convert(String buildingFloor) {
        JSONObject build = new JSONObject();
        JSONObject image;
        String buildFloor = buildingFloor.replace(' ','_');

        image = new JSONObject();
        build.put("buildingFloor",""+buildFloor);

        //--------------------------------
        s= UriBuilder.fromResource(JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(JaxRestImage.class, "getFloorImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("restLink","/rest/immagini/edifici/" + buildFloor);
        image.put("restLink",REST+s);

        //-----------------------------
        s=UriBuilder.fromResource(ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(ImagesHandler.class, "getFloorImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/" + buildFloor + ".png");
        image.put("PNGLink",RESOURCE+s);
        build.put("imageFloor",image);

        //----------------------
        image = new JSONObject();
        s=UriBuilder.fromResource(JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(JaxRestImage.class, "getOccupationImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("restLink","/rest/immagini/edifici/" + buildFloor + "/occupazione");
        image.put("restLink",REST+s);

        //--------------------------
        s=UriBuilder.fromResource(ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(ImagesHandler.class, "getOccupationImage");
        m = new HashMap<>();
        m.put("buildingFloor","occu_"+buildFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/occupazione/occu_" + buildFloor+".png");
        image.put("PNGLink",RESOURCE+s);
        build.put("imageFloorSpace",image);

        //------------------------------------------------------------------------------
        image = new JSONObject();
        s=UriBuilder.fromResource(JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(JaxRestImage.class, "getWorkImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("restLink","/rest/immagini/edifici/" + buildFloor + "/lavoro");
        image.put("restLink",REST+s);

        //-------------------------------------
        s=UriBuilder.fromResource(ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(ImagesHandler.class, "getWorkImage");
        m = new HashMap<>();
        m.put("buildingFloor","work_"+buildFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/lavoro/work_"+ buildFloor +".png");
        image.put("PNGLink",RESOURCE+s);
        build.put("imageFloorWork",image);

        //-----------------------------------------------------
        s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(JaxRestBuildings.class, "getSimpleListOfRoomOnFloor");
        m = new HashMap<>();
        m.put("buildingFloor", buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //build.put("roomsList","/rest/edifici/"+buildFloor+"/stanze");
        build.put("roomsList",REST+s);

        //-------------------
        s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(JaxRestBuildings.class, "getEndWorkPeopleOnFloor");
        m = new HashMap<>();
        m.put("buildFloor", buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //build.put("infoFloorWork","/rest/edifici/"+buildFloor+"/lavoro/persone");
        build.put("infoFloorWork",REST+s);

        //-------------------
        s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(JaxRestBuildings.class, "getOccRoomsOnFloor");
        m = new HashMap<>();
        m.put("buildFloor", buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //build.put("infoFloorSpace","/rest/edifici/"+buildFloor+"/occupazione/stanze");
        build.put("infoFloorSpace",REST+s);


        return build;
    }
}
