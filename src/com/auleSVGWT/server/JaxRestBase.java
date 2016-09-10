package com.auleSVGWT.server;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class JaxRestBase {
    @GET
    public Response getBegin( ) {

        JSONObject obj = new JSONObject();
        obj.put("image","/rest/immagini/edifici");
        obj.put("people","/rest/persone");
        obj.put("rooms","/rest/edifici");


        try {
            String json = obj.toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }


}