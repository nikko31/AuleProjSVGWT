package com.auleSVGWT.server.mobile;

import com.auleSVGWT.server.mobile.Converter.BaseRestJson;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class JaxRestBase {

    @GET
    public Response getBegin( ) {
        BaseRestJson baseRestJson = new BaseRestJson();
        JSONObject obj = baseRestJson.convert(0);

        try {
            String json = obj.toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }


}