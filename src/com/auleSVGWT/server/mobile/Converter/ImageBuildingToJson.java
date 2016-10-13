package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import com.auleSVGWT.server.mobile.JaxRestImage;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;


public class ImageBuildingToJson extends CommonVar implements JsonConverter<String,JSONObject> {
    @Override
    public JSONObject convert(String buildingFloor) {
        String build = buildingFloor.replace('_', ' ');
        JSONObject obj = new JSONObject();
        JSONObject image;
        obj.put("name",""+buildingFloor);
        obj.put("extension","png");
        obj.put("height","1280");
        obj.put("width","720");


        //----------------------------image-floor-link---------------------
        s= UriBuilder.fromResource(ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(ImagesHandler.class,"getFloorImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildingFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //obj.put("PNGLink","/risorse/immagini/edifici/"+buildingFloor+".png");
        obj.put("PNGLink",RESOURCE+s);

        //--------------------rest-occupation-floor-link-------------------------
        s=UriBuilder.fromResource(JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(JaxRestImage.class,"getOccupationImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildingFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        image = new JSONObject();
        //image.put("restLink","/rest/immagini/edifici/"+buildingFloor+ "/occupazione");
        image.put("restLink",REST+s);
        obj.put("imageFloorSpace",image);

        //----------------------------------rest-work-image-link----------------------
        s=UriBuilder.fromResource(JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(JaxRestImage.class, "getWorkImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildingFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        image = new JSONObject();
        //image.put("restLink", "/rest/immagini/edifici/" + buildingFloor + "/lavoro");
        image.put("restLink",REST+s);
        obj.put("imageFloorWork",image);

        //--------------------rest-rooms-image-link----------------------------------
        s=UriBuilder.fromResource(JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(JaxRestImage.class, "getRoomsImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildingFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        image = new JSONObject();
        //image.put("restLink", "/rest/immagini/edifici/"+buildingFloor+"/stanze");
        image.put("restLink", REST+s);
        obj.put("imagesSelectRoom",image);

        //-----------------rest---building-info-link
        s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(JaxRestBuildings.class, "getBuildingFloorInfo");
        m = new HashMap<>();
        m.put("buildingFloor",buildingFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        obj.put("infoBuildingFloor", REST + s);

        return obj;


    }
}
