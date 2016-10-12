package com.auleSVGWT.server;

import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/")
public class JaxRestBase {
    public static String REST="/rest";

    @GET
    public Response getBegin( ) {
        String s;
        JSONObject obj = new JSONObject();


        s= UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
        obj.put("image",REST+s);
        s= UriBuilder.fromResource(com.auleSVGWT.server.JaxRestPeople.class).toString();
        obj.put("people",REST+s);
        s= UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
        obj.put("rooms",REST+s);


        try {
            String json = obj.toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }


}