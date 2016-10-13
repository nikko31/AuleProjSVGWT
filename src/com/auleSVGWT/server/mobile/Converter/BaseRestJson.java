package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.mobile.JaxRestBuildings;
import com.auleSVGWT.server.mobile.JaxRestImage;
import com.auleSVGWT.server.mobile.JaxRestPeople;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;


public class BaseRestJson extends CommonVar implements JsonConverter<Integer,JSONObject>{
    @Override
    public JSONObject convert(Integer value) {
        String s;
        JSONObject obj = new JSONObject();

        s= UriBuilder.fromResource(JaxRestImage.class).toString();
        obj.put("image",REST+s);
        s= UriBuilder.fromResource(JaxRestPeople.class).toString();
        obj.put("people",REST+s);
        s= UriBuilder.fromResource(JaxRestBuildings.class).toString();
        obj.put("rooms",REST+s);

        return obj;
    }
}
