package com.auleSVGWT.server;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Utente on 10/08/2016.
 */
@Path("/")
public class JaxAndroidBase {
    @GET
    public Response begin( ) {

        JSONObject obj = new JSONObject();
        obj.put("image","/rest/immagini");
        obj.put("listRoomsFloor","/rest/listaStanzePiano");
        obj.put("people","/rest/persone");
        obj.put("rooms","/rest/edifici");
        obj.put("listBuildings","/rest/listaEdifici");

        try {
            String json = obj.toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

}
