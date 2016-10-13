package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.JaxRestImage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.HashMap;


public class ImageRoomsListToJson extends CommonVar implements JsonConverter<ArrayList<String>,JSONObject> {
    String buildingFloor;
    public ImageRoomsListToJson(String buildingFloor) {
        this.buildingFloor = buildingFloor;
    }

    @Override
    public JSONObject convert(ArrayList<String> rooms) {
        JSONObject image = new JSONObject();
        image.put("buildingFloor", "" + buildingFloor);
        JSONArray link = new JSONArray();


        for(String room : rooms){
            String buildFloor = room.substring(0,room.lastIndexOf('-'));
            buildFloor = buildFloor.replace(' ', '_');
            String number = room.substring(room.lastIndexOf('-')+1);

            //--------------------------------
            s= UriBuilder.fromResource(JaxRestImage.class).toString();
            u =UriBuilder.fromMethod(JaxRestImage.class, "getRoomImage");
            m = new HashMap<>();
            m.put("buildingFloor",buildFloor);
            m.put("numRoom",number);
            s+=u.buildFromEncodedMap(m).toString();
            //System.out.println("mi da ................" + s );
            //link.add("/rest/immagini/edifici/"+buildFloor+"/stanze/"+number);
            link.add(REST+s);
        }
        image.put("link",link);

        return image;

    }
}
