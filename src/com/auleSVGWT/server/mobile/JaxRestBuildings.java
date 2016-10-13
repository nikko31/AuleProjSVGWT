package com.auleSVGWT.server.mobile;

import com.auleSVGWT.server.mobile.Converter.BuildingImageToJson;
import com.auleSVGWT.server.mobile.Converter.BuildingsImageToJson;
import com.auleSVGWT.server.mobile.Converter.RoomsImageListToJson;
import com.auleSVGWT.server.SVGMetaPost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

@Path("/edifici")
public class JaxRestBuildings {
    private final String path = "/res/imageAndroid";
    private DatabaseM db;

    @Context
    private ServletContext servletContext;


    @GET
    public Response getAllBuildings( ) {

        try {
            BuildingsImageToJson buildingsImageToJson = new BuildingsImageToJson();
            JSONArray arr = buildingsImageToJson.convert(servletContext.getRealPath(path));
            //JSONArray arr = parseBuildings();
            if(arr != null){
                String json = arr.toString();
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            }

        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }





    @GET
    @Path("/{buildingFloor}")
    public Response getBuildingFloorInfo(@PathParam("buildingFloor") String buildingFloor) {

        try {
            String build = buildingFloor.replace('_', ' ');
            if(controlBuildFloor(build)){
                BuildingImageToJson buildingImageToJson = new BuildingImageToJson();
                JSONObject obj = buildingImageToJson.convert(build);
                //JSONObject obj = parseBuildingFloorJson(build);
                if(obj != null){
                    String json = obj.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();

                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }

        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }







    @GET
    @Path("/{buildingFloor}/stanze")
    public Response getSimpleListOfRoomOnFloor(@PathParam("buildingFloor") String buildingFloor){
        try {

            String build = buildingFloor.replace('_', ' ');
            if(controlBuildFloor(build)){
                //JSONArray arr = parseSimpleRoomsList(listRoomsOnFloor(build), build);
                RoomsImageListToJson roomsImageListToJson = new RoomsImageListToJson(build);
                JSONArray arr= roomsImageListToJson.convert(listRoomsOnFloor(build));

                if(arr != null){
                    String json = arr.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();

                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }

        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();

    }





    @GET
    @Path("/{buildFloor}/stanze/{room}")
    public Response getRoomInfo(@PathParam("buildFloor") String buildFloor,@PathParam("room") String room){
        // System.out.println("get room people con "+ buildFloor+ " "+ room);
        try {

            String build = buildFloor.replace('_', ' ');
            if(controlBuildFloorRoom(build, room)){
                db= new DatabaseM();
                JSONObject obj = db.getRoomInfoJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1), room);
                if(obj!=null){
                    String json = obj.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }


        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @GET
    @Path("/{buildFloor}/stanze/{room}/persone")
    public Response getPeopleInRoom(@PathParam("buildFloor") String buildFloor,@PathParam("room") String room){
        System.out.println("get room people con "+ buildFloor+ " "+ room);
        try {

            String build = buildFloor.replace('_', ' ');
            if(controlBuildFloorRoom(build, room)){
                db= new DatabaseM();
                JSONArray arr = db.getPeopleInRoomJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1), room);
                if(arr != null){
                    String json = arr.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }



        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @GET
    @Path("/{buildFloor}/occupazione/stanze")
    public Response getOccRoomsOnFloor(@PathParam("buildFloor") String buildFloor){

        try {

            String build = buildFloor.replace('_', ' ');
            if(controlBuildFloor(build)){
                db= new DatabaseM();
                JSONArray arr = db.getOccupyOfFloorwithDimensionJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1));
                if(arr != null){
                    String json = arr.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }

        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

    }
    @GET
    @Path("/{buildFloor}/lavoro/persone")
    public Response getEndWorkPeopleOnFloor(@PathParam("buildFloor") String buildFloor){

        try {

            String build = buildFloor.replace('_', ' ');
            if(controlBuildFloor(build)){
                db= new DatabaseM();
                JSONArray arr = db.getOccupyOfFloorwithDateJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1));

                if(arr!=null){
                    String json = arr.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();

                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    //-----------------------------------------------CONTROLS ON STRINGS---------------------------------------------------------------

    private boolean controlBuildFloor( String buildFloor)throws Exception{
        String fullPath = servletContext.getRealPath(path);
        File folder = new File(fullPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null){
            for (File file : listOfFiles) {

                String filename = file.getName();
                filename = filename.substring(0,filename.lastIndexOf("."));
                if(buildFloor.equals(filename)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean controlBuildFloorRoom(String buildFloor,String room)throws Exception{

        if(controlBuildFloor(buildFloor)){
            ArrayList<Integer> numberRooms = listRoomsOnFloor(buildFloor);
            for(Integer numberRoom : numberRooms){
                if(room.equals(numberRoom.toString())){
                    return true;
                }
            }

        }
        System.out.println("problema nel controllo delle stanze");
        return false;
    }



    private ArrayList<Integer> listRoomsOnFloor(String buildFloor) {
        String fullPath = servletContext.getRealPath(path);
        URI uri = new File(fullPath + "/" + buildFloor + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<Integer> room = new ArrayList<>();
        try {
            converter = new SVGMetaPost(uri.toString());
            Document doc = converter.getSVGDocument();


            NodeList n = doc.getElementsByTagName("rect");
            NodeList p = doc.getElementsByTagName("path");
            for (int i = 0; i < n.getLength(); i++) {
                //System.out.println(n.item(i).getTextContent());
                if (((Element) n.item(i)).getAttribute("id").contains(buildFloor)) {
                    //System.out.println(((Element) n.item(i)).getAttribute("id"));
                    String s = ((Element) n.item(i)).getAttribute("id");
                    s = s.substring(s.lastIndexOf("-")+1);
                    room.add(Integer.parseInt(s));

                }
            }
            for (int i = 0; i < p.getLength(); i++) {
                //System.out.println(p.item(i).getTextContent());
                if (((Element) p.item(i)).getAttribute("id").contains(buildFloor)) {
                    //System.out.println(((Element) p.item(i)).getAttribute("id"));
                    String s = ((Element) p.item(i)).getAttribute("id");
                    s = s.substring(s.lastIndexOf("-")+1);
                    room.add(Integer.parseInt(s));

                }
            }

        } catch (IOException e) {
            System.out.println("error in listRoomsOfFloor");
            e.printStackTrace();
        }




        return room;

    }




}
