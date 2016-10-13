package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.domain.Room;
import com.auleSVGWT.server.mobile.ImagesHandler;
import com.auleSVGWT.server.mobile.JaxRestBuildings;
import com.auleSVGWT.server.mobile.JaxRestImage;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;


public class RoomToJson extends CommonVar implements JsonConverter<Room,JSONObject> {
    @Override
    public JSONObject convert(Room room) {

        JSONObject roomJson = new JSONObject();
        JSONObject image = new JSONObject();


        roomJson.put("id", room.getId());
        roomJson.put("building", "" + room.getBuilding().getName());
        roomJson.put("floor", room.getFloor());
        roomJson.put("number", room.getNumber());
        if (room.getMaintenance() == null) {
            roomJson.put("maintenance", null);
        } else {
            roomJson.put("maintenance", "" + room.getMaintenance());
        }
        roomJson.put("personMax", room.getMaxPeople());
        roomJson.put("socket", room.getSocket());
        roomJson.put("dimension", room.getDimension());
        roomJson.put("code", "" + room.getRoomCode());

        //hateoas
        String build = room.getBuilding().getName();
        build = build.replace(' ', '_');


        //--------------------------------
        s = UriBuilder.fromResource(JaxRestBuildings.class).toString();
        u = UriBuilder.fromMethod(JaxRestBuildings.class, "getPeopleInRoom");
        m = new HashMap<>();
        m.put("buildFloor", "" + build + "-" + room.getFloor());
        m.put("room", "" + room.getNumber());
        s += u.buildFromEncodedMap(m).toString();
        //System.out.println("esece..................." + s);
        //roomJson.put("infoPeopleInRoom", "/rest/edifici/" + build + "-" + room.getFloor() + "/stanze/" + room.getNumber() + "/persone");
        roomJson.put("infoPeopleInRoom", REST + s);

        //-------------------------------
        s = UriBuilder.fromResource(JaxRestImage.class).toString();
        u = UriBuilder.fromMethod(JaxRestImage.class, "getRoomImage");
        m = new HashMap<>();
        m.put("buildingFloor", "" + build + "-" + room.getFloor());
        m.put("numRoom", "" + room.getNumber());
        s += u.buildFromEncodedMap(m).toString();
        //System.out.println("esece..................." + s);
        //image.put("restLink","/rest/immagini/edifici/"+build+"-"+room.getFloor()+"/stanze/"+room.getNumber());
        image.put("restLink", REST + s);

        //------------------------------
        s = UriBuilder.fromResource(ImagesHandler.class).toString();
        u = UriBuilder.fromMethod(ImagesHandler.class, "getRoomImage");
        m = new HashMap<>();
        m.put("buildingFloorNumb", "" + build + "-" + room.getFloor() + "-" + room.getNumber() + ".png");
        s += u.buildFromEncodedMap(m).toString();
        //System.out.println("esece..................."+s);
        //image.put("PNGLink","/risorse/immagini/edifici/stanze/"+build+"-"+room.getFloor()+"-"+room.getNumber()+".png");
        image.put("PNGLink", RESOURCE + s);
        roomJson.put("imageSelectRoom", image);


        return roomJson;
    }
}
