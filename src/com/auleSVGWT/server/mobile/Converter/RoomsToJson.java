package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.domain.Room;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;


public class RoomsToJson implements JsonConverter<ArrayList<Room>,JSONArray> {
    @Override
    public JSONArray convert(ArrayList<Room> rooms) {
        JSONArray arrayRoomsJson = new JSONArray();

        if(rooms.size()>0){

            for(Room room : rooms){
                JSONObject obj;
                RoomToJson roomToJson = new RoomToJson();
                obj = roomToJson.convert(room);
                arrayRoomsJson.add(obj);
            }

            return arrayRoomsJson;

        }else{
            return null;
        }

    }
}
