package com.auleSVGWT.server;

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
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Path("/edifici")
public class JaxRestBuildings {
    private final String path = "/res/imageAndroid";
    private DatabaseM db;
    public static String REST="/rest";
    public static String RESOURCE ="/risorse/immagini";
    private String s;
    private UriBuilder u;
    private Map<String,String> m;
    @Context
    private ServletContext servletContext;


    @GET
    public Response getAllBuildings( ) {

        try {
            JSONArray arr = parseBuildings();
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
                JSONObject obj = parseBuildingFloorJson(build);
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
                JSONArray arr = parseSimpleRoomsList(listRoomsOnFloor(build), build);
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


    //---------------------------------------------------METHOD FOR SIMPLE ROOM LIST-----------------------------------------

    private JSONArray parseSimpleRoomsList(ArrayList<Integer> rooms, String buildingFloor) {


        JSONArray arr = new JSONArray();
        Collections.sort(rooms);


        if ((rooms.size() != 0)) {

            for (Integer room : rooms) {
                // System.out.println("VErifico ordine:::::::::::::::: "+room);
                JSONObject ro = new JSONObject();
                JSONObject image = new JSONObject();

                buildingFloor = buildingFloor.replace(' ','_');
                String num =""+ String.valueOf(room);



                //--------------------------------
                s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
                u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestImage.class, "getRoomImage");
                m = new HashMap<>();
                m.put("buildingFloor",buildingFloor);
                m.put("numRoom",num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //image.put("restLink","/rest/immagini/edifici/"+buildingFloor+"/stanze/"+num);
                image.put("restLink",REST+s);

                //--------------------------------
                s=UriBuilder.fromResource(com.auleSVGWT.server.ImagesHandler.class).toString();
                u =UriBuilder.fromMethod(com.auleSVGWT.server.ImagesHandler.class, "getRoomImage");
                m = new HashMap<>();
                m.put("buildingFloorNumb",buildingFloor+"-"+num+".png");
                m.put("room",num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s + ".png");
                //image.put("PNGLink","/risorse/immagini/edifici/stanze/"+buildingFloor+"-"+num+".png");
                image.put("PNGLink",RESOURCE+s);
                ro.put("imageSelectRoom",image);

                //--------------------------------
                s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getRoomInfo");
                m = new HashMap<>();
                m.put("buildFloor",buildingFloor);
                m.put("room", num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //ro.put("infoSelectRoom","/rest/edifici/"+buildingFloor + "/stanze/" + num);
                ro.put("infoSelectRoom",REST+s);

                //--------------------------------
                s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getPeopleInRoom");
                m = new HashMap<>();
                m.put("buildFloor",buildingFloor);
                m.put("room", num);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //ro.put("infoPeopleInRoom","/rest/edifici/"+buildingFloor + "/stanze/" + num+"/persone");
                ro.put("infoPeopleInRoom",REST+s);


                ro.put("room", "" + room);

                arr.add(ro);


            }

            return arr;
        }


        return null;


    }

    //--------------------------------------------------------------METHODS FOR RETURN BUILDINGS LIST-----------------------------------
    private JSONArray parseBuildings()throws Exception{


        HashMap<String, ArrayList<String>> buildings = new HashMap<>();

        try {

            String fullPath = servletContext.getRealPath(path);
            //System.out.println(servletContext.getContextPath()+"++++++questo è il cammino");
            //System.out.println(servletContext.getServletContextName()+"context nme");

            if(fullPath!=null){
                System.out.println(fullPath+"++++++questo è il cammino completo");
            }
            File folder = new File(fullPath);
            File[] listOfFiles = folder.listFiles();

            if(listOfFiles != null){
                for (File file : listOfFiles) {
                    String s= file.getName();
                    if (buildings.containsKey(s.substring(0, s.lastIndexOf('-')))) {
                        ArrayList<String> floors;
                        floors = buildings.get(s.substring(0, s.lastIndexOf('-')));
                        floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                        Collections.sort(floors);
                        buildings.replace(s.substring(0, s.lastIndexOf('-')), floors);
                    } else {
                        ArrayList<String> floors = new ArrayList<>();
                        floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                        buildings.put(s.substring(0, s.lastIndexOf('-')), floors);
                    }
                    //string.add(file.getName());
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading name of maps");
            e.printStackTrace();
        }

        return parseBuildingsJSON(buildings);





    }




    private JSONArray  parseBuildingsJSON(HashMap<String, ArrayList<String>> buildings){



        JSONArray buildingsFloors = new JSONArray();
        if(buildings.keySet().size()>0){
            for (String buildingString : buildings.keySet()) {



                JSONObject buildingFloors = new JSONObject();
                JSONArray arr = new JSONArray();




                for (String floorString : buildings.get(buildingString)) {
                    JSONObject floor = new JSONObject();
                    JSONObject image;

                    String building = buildingString.replace(' ','_');





                    //--------------------------------
                    image = new JSONObject();
                    s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestImage.class, "getFloorImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //image.put("restLink","/rest/immagini/edifici/" + building + "-" + floorString);
                    image.put("restLink",REST+s);

                    //-----------------------------
                    s=UriBuilder.fromResource(com.auleSVGWT.server.ImagesHandler.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.ImagesHandler.class, "getFloorImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString+".png");
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s + ".png");
                    //image.put("PNGLink","/risorse/immagini/edifici/" + building + "-" + floorString + ".png");
                    image.put("PNGLink",RESOURCE+s);
                    floor.put("imageFloor",image);

                    //---------------------------
                    image = new JSONObject();
                    s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestImage.class, "getOccupationImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //image.put("restLink","/rest/immagini/edifici/" + building + "-" + floorString + "/occupazione");
                    image.put("restLink",REST+s);
                    //----------------------------

                    s=UriBuilder.fromResource(com.auleSVGWT.server.ImagesHandler.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.ImagesHandler.class, "getOccupationImage");
                    m = new HashMap<>();
                    m.put("buildingFloor","occu_"+building + "-" + floorString+".png");
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s + ".png");
                    //image.put("PNGLink","/risorse/immagini/edifici/occupazione/occu_" + building + "-" + floorString+".png");
                    image.put("PNGLink",RESOURCE+s);
                    floor.put("imageFloorSpace",image);

                    //------------------------------------------
                    image = new JSONObject();
                    s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestImage.class, "getWorkImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //image.put("restLink","/rest/immagini/edifici/" + building + "-" + floorString + "/lavoro");
                    image.put("restLink",REST+s);

                    //---------------------------------
                    s=UriBuilder.fromResource(com.auleSVGWT.server.ImagesHandler.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.ImagesHandler.class, "getWorkImage");
                    m = new HashMap<>();
                    m.put("buildingFloor","work_"+building + "-" + floorString+".png");
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s + ".png");
                    //image.put("PNGLink","/risorse/immagini/edifici/lavoro/work_"+ building + "-" + floorString +".png");
                    image.put("PNGLink",RESOURCE+s);
                    floor.put("imageFloorWork",image);

                    //-------------------
                    s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getSimpleListOfRoomOnFloor");
                    m = new HashMap<>();
                    m.put("buildingFloor", building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //floor.put("infoRoomsList","/rest/edifici/"+building+"-"+floorString+"/stanze");
                    floor.put("infoRoomsList",REST+s);

                    //-------------------
                    s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getEndWorkPeopleOnFloor");
                    m = new HashMap<>();
                    m.put("buildFloor", building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //floor.put("infoFloorWork","/rest/edifici/"+building+"-"+floorString+"/lavoro/persone");
                    floor.put("infoFloorWork",REST+s);

                    //-------------------
                    s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
                    u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getOccRoomsOnFloor");
                    m = new HashMap<>();
                    m.put("buildFloor", building + "-" + floorString);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s);
                    //floor.put("infoFloorSpace","/rest/edifici/"+building+"-"+floorString+"/occupazione/stanze");
                    floor.put("infoFloorSpace",REST+s);
                    //senza spazio
                    floor.put("floor", floorString);
                    arr.add(floor);

                }

                buildingFloors.put("building", buildingString);

                buildingFloors.put("floors", arr);

                buildingsFloors.add(buildingFloors);

            }

            return buildingsFloors;

        }else{
            return null;
        }



    }

    private JSONObject parseBuildingFloorJson(String buildingFloor){

        JSONObject build = new JSONObject();
        JSONObject image;
        String buildFloor = buildingFloor.replace(' ','_');

        image = new JSONObject();
        build.put("buildingFloor",""+buildFloor);

        //--------------------------------
        s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestImage.class, "getFloorImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("restLink","/rest/immagini/edifici/" + buildFloor);
        image.put("restLink",REST+s);

        //-----------------------------
        s=UriBuilder.fromResource(com.auleSVGWT.server.ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.ImagesHandler.class, "getFloorImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/" + buildFloor + ".png");
        image.put("PNGLink",RESOURCE+s);
        build.put("imageFloor",image);

        //----------------------
        image = new JSONObject();
        s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestImage.class, "getOccupationImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("restLink","/rest/immagini/edifici/" + buildFloor + "/occupazione");
        image.put("restLink",REST+s);

        //--------------------------
        s=UriBuilder.fromResource(com.auleSVGWT.server.ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.ImagesHandler.class, "getOccupationImage");
        m = new HashMap<>();
        m.put("buildingFloor","occu_"+buildFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/occupazione/occu_" + buildFloor+".png");
        image.put("PNGLink",RESOURCE+s);
        build.put("imageFloorSpace",image);

        //------------------------------------------------------------------------------
        image = new JSONObject();
        s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestImage.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestImage.class, "getWorkImage");
        m = new HashMap<>();
        m.put("buildingFloor",buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //image.put("restLink","/rest/immagini/edifici/" + buildFloor + "/lavoro");
        image.put("restLink",REST+s);

        //-------------------------------------
        s=UriBuilder.fromResource(com.auleSVGWT.server.ImagesHandler.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.ImagesHandler.class, "getWorkImage");
        m = new HashMap<>();
        m.put("buildingFloor","work_"+buildFloor+".png");
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s + ".png");
        //image.put("PNGLink","/risorse/immagini/edifici/lavoro/work_"+ buildFloor +".png");
        image.put("PNGLink",RESOURCE+s);
        build.put("imageFloorWork",image);

        //-----------------------------------------------------
        s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getSimpleListOfRoomOnFloor");
        m = new HashMap<>();
        m.put("buildingFloor", buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //build.put("roomsList","/rest/edifici/"+buildFloor+"/stanze");
        build.put("roomsList",REST+s);

        //-------------------
        s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getEndWorkPeopleOnFloor");
        m = new HashMap<>();
        m.put("buildFloor", buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //build.put("infoFloorWork","/rest/edifici/"+buildFloor+"/lavoro/persone");
        build.put("infoFloorWork",REST+s);

        //-------------------
        s=UriBuilder.fromResource(com.auleSVGWT.server.JaxRestBuildings.class).toString();
        u =UriBuilder.fromMethod(com.auleSVGWT.server.JaxRestBuildings.class, "getOccRoomsOnFloor");
        m = new HashMap<>();
        m.put("buildFloor", buildFloor);
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................" + s);
        //build.put("infoFloorSpace","/rest/edifici/"+buildFloor+"/occupazione/stanze");
        build.put("infoFloorSpace",REST+s);


        return build;

    }


}
