package com.auleSVGWT.server;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Utente on 10/08/2016.
 */
public class JaxAndroidBase {
    @GET
    public Response getHello( ) {

        JSONObject obj = new JSONObject();
        obj.put("image","/Android/immagine");
        obj.put("listRoomsFloor","/Android/listaStanzePiano");
        obj.put("people","/Android/persone");
        obj.put("rooms","/Android/stanze");
        obj.put("listBuildings","/Android/listaEdifici.json");

        try {
            String json = obj.toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

}
