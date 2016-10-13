package com.auleSVGWT.server.mobile;

import com.auleSVGWT.server.SVGMetaPost;
import com.auleSVGWT.server.mobile.Converter.*;
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

@Path("/immagini/edifici")
public class JaxRestImage {
    private final String path = "/res/imageAndroid";
    public static String REST="/rest";
    public static String RESOURCE ="/risorse/immagini";


    @Context
    private ServletContext servletContext;


    @GET

    public Response getBuildingsFloor() {

        try {
                ImageBuildingsToJson imageBuildingsToJson = new ImageBuildingsToJson();
                JSONArray arr = imageBuildingsToJson.convert(servletContext.getRealPath(path));
                String buildings = arr.toString();
                return Response.ok(buildings, MediaType.APPLICATION_JSON).build();


        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }



    }

    @GET
    @Path("/{buildingFloor}")
    public Response getFloorImage(@PathParam("buildingFloor")String buildingFloor) {

        try {
            String build = buildingFloor.replace('_', ' ');
            if (controlBuildFloor(build)) {
                ImageBuildingToJson imageBuildingToJson = new ImageBuildingToJson();

                JSONObject obj = imageBuildingToJson.convert(buildingFloor);
                /*JSONObject image;
                obj.put("name",""+buildingFloor);
                obj.put("extension","png");
                obj.put("height","1280");
                obj.put("width","720");


                //----------------------------image-floor-link---------------------
                s=UriBuilder.fromResource(ImagesHandler.class).toString();
                u =UriBuilder.fromMethod(ImagesHandler.class,"getFloorImage");
                m = new HashMap<>();
                m.put("buildingFloor",buildingFloor+".png");
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s + ".png");
                //obj.put("PNGLink","/risorse/immagini/edifici/"+buildingFloor+".png");
                obj.put("PNGLink",RESOURCE+s);

                //--------------------rest-occupation-floor-link-------------------------
                s=UriBuilder.fromResource(JaxRestImage.class).toString();
                u =UriBuilder.fromMethod(JaxRestImage.class,"getOccupationImage");
                m = new HashMap<>();
                m.put("buildingFloor",buildingFloor);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                image = new JSONObject();
                //image.put("restLink","/rest/immagini/edifici/"+buildingFloor+ "/occupazione");
                image.put("restLink",REST+s);
                obj.put("imageFloorSpace",image);

                //----------------------------------rest-work-image-link----------------------
                s=UriBuilder.fromResource(JaxRestImage.class).toString();
                u =UriBuilder.fromMethod(JaxRestImage.class, "getWorkImage");
                m = new HashMap<>();
                m.put("buildingFloor",buildingFloor);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                image = new JSONObject();
                //image.put("restLink", "/rest/immagini/edifici/" + buildingFloor + "/lavoro");
                image.put("restLink",REST+s);
                obj.put("imageFloorWork",image);

                //--------------------rest-rooms-image-link----------------------------------
                s=UriBuilder.fromResource(JaxRestImage.class).toString();
                u =UriBuilder.fromMethod(JaxRestImage.class, "getRoomsImage");
                m = new HashMap<>();
                m.put("buildingFloor",buildingFloor);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                image = new JSONObject();
                //image.put("restLink", "/rest/immagini/edifici/"+buildingFloor+"/stanze");
                image.put("restLink", REST+s);
                obj.put("imagesSelectRoom",image);

                //-----------------rest---building-info-link
                s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(JaxRestBuildings.class, "getBuildingFloorInfo");
                m = new HashMap<>();
                m.put("buildingFloor",buildingFloor);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                obj.put("infoBuildingFloor", REST + s);
                //obj.put("infoBuildingFloor","/rest/edifici/"+buildingFloor);*/

                return Response.ok(obj.toString(), MediaType.APPLICATION_JSON).build();

            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }



    @GET
    @Path("/{buildingFloor}/occupazione")
    public Response getOccupationImage(@PathParam("buildingFloor")String buildingFloor) {

        try {
            String build = buildingFloor.replace('_', ' ');
            if (controlBuildFloor(build)) {
                ImageOccupationToJson imageOccupationToJson = new ImageOccupationToJson();
                JSONObject image = imageOccupationToJson.convert(buildingFloor);
                /*
                image.put("name","occu_"+buildingFloor);
                image.put("extension","png");
                image.put("height","1280");
                image.put("width","720");

                //--------------------------------
                s=UriBuilder.fromResource(ImagesHandler.class).toString();
                u =UriBuilder.fromMethod(ImagesHandler.class, "getOccupationImage");
                m = new HashMap<>();
                m.put("buildingFloor","occu_"+buildingFloor+".png");
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s + ".png");
                //image.put("PNGLink","/risorse/immagini/edifici/occupazione/occu_"+buildingFloor+".png");
                image.put("PNGLink",RESOURCE+s);

                //--------------------------------
                s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(JaxRestBuildings.class, "getOccRoomsOnFloor");
                m = new HashMap<>();
                m.put("buildFloor",buildingFloor);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //image.put("infoFloorSpace","rest/edifici/"+buildingFloor+"/occupazione/stanze");
                image.put("infoFloorSpace",REST+s);*/


                return Response.ok(image.toString(), MediaType.APPLICATION_JSON).build();



            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


        return Response.status(Response.Status.NOT_FOUND).build();

    }



    @GET
    @Path("/{buildingFloor}/lavoro")
    public Response getWorkImage(@PathParam("buildingFloor")String buildingFloor) {

        try {
            String build = buildingFloor.replace('_', ' ');
            if (controlBuildFloor(build)) {

                ImageWorkToJson imageWorkToJson = new ImageWorkToJson();
                JSONObject image = imageWorkToJson.convert(buildingFloor);
                /*
                image.put("name", "work_" + buildingFloor);
                image.put("extension","png");
                image.put("height","1280");
                image.put("width","720");


                //--------------------------------
                s=UriBuilder.fromResource(ImagesHandler.class).toString();
                u =UriBuilder.fromMethod(ImagesHandler.class, "getWorkImage");
                m = new HashMap<>();
                m.put("buildingFloor","work_"+buildingFloor+".png");
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s + ".png");
                //image.put("PNGLink","/risorse/immagini/edifici/lavoro/work_"+buildingFloor+".png");
                image.put("PNGLink",RESOURCE+s);

                //------------------------
                s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(JaxRestBuildings.class, "getEndWorkPeopleOnFloor");
                m = new HashMap<>();
                m.put("buildFloor",buildingFloor);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //image.put("infoFloorWork","rest/edifici/"+buildingFloor+"/lavoro/persone");
                image.put("infoFloorWork",REST+s);*/
                return Response.ok(image.toString(), MediaType.APPLICATION_JSON).build();

            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }


    @GET
    @Path("/{buildingFloor}/stanze")
    public Response getRoomsImage(@PathParam("buildingFloor")String buildingFloor) {

        try{


            String build = buildingFloor.replace('_', ' ');
            if(controlBuildFloor(build)){
                ImageRoomsListToJson imageRoomsListToJson = new ImageRoomsListToJson(buildingFloor);
                JSONObject image = imageRoomsListToJson.convert(listRoomsOnFloor(build));
                /*
                JSONObject image = new JSONObject();
                image.put("buildingFloor", "" + buildingFloor);
                JSONArray link = new JSONArray();
                ArrayList<String> rooms = listRoomsOnFloor(build);

                for(String room : rooms){
                    String buildFloor = room.substring(0,room.lastIndexOf('-'));
                    buildFloor = buildFloor.replace(' ', '_');
                    String number = room.substring(room.lastIndexOf('-')+1);

                    //--------------------------------
                    s=UriBuilder.fromResource(JaxRestImage.class).toString();
                    u =UriBuilder.fromMethod(JaxRestImage.class, "getRoomImage");
                    m = new HashMap<>();
                    m.put("buildingFloor",buildFloor);
                    m.put("numRoom",number);
                    s+=u.buildFromEncodedMap(m).toString();
                    //System.out.println("mi da ................" + s );
                    //link.add("/rest/immagini/edifici/"+buildFloor+"/stanze/"+number);
                    link.add(REST+s);
                }
                image.put("link",link);*/

                return Response.ok(image.toString(), MediaType.APPLICATION_JSON).build();

            }


        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }

        return Response.status(Response.Status.NOT_FOUND).build();



    }

    @GET
    @Path("/{buildingFloor}/stanze/{numRoom}")
    public Response getRoomImage(@PathParam("buildingFloor")String buildingFloor,@PathParam("numRoom")String numRoom) {

        try{
            String build = buildingFloor.replace('_', ' ');
            if(controlBuildFloorRoom(build, numRoom)){

                ImageRoomToJson imageRoomToJson = new ImageRoomToJson(buildingFloor);
                JSONObject image = imageRoomToJson.convert(numRoom);
                /*
                image.put("name", ""+buildingFloor+"-"+numRoom);
                image.put("extension","png");
                image.put("height", "1280");
                image.put("width","720");


                //--------------------------------
                s=UriBuilder.fromResource(ImagesHandler.class).toString();
                u =UriBuilder.fromMethod(ImagesHandler.class,"getRoomImage");
                m = new HashMap<>();
                m.put("buildingFloorNumb",buildingFloor+"-"+numRoom+".png");
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s + ".png");
                //image.put("PNGLink","/risorse/immagini/edifici/stanze/"+buildingFloor+"-"+numRoom+".png");
                image.put("PNGLink",RESOURCE+s);

                //------------------------
                s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(JaxRestBuildings.class, "getRoomInfo");
                m = new HashMap<>();
                m.put("buildFloor",buildingFloor);
                m.put("room",numRoom);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //image.put("infoSelectRoom","/rest/edifici/"+buildingFloor+ "/stanze/" + numRoom);
                image.put("infoSelectRoom",REST+s);*/
                return Response.ok(image.toString(), MediaType.APPLICATION_JSON).build();

            }


        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }

        return Response.status(Response.Status.NOT_FOUND).build();



    }


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
            ArrayList<String> numberRooms = listRoomsOnFloor(buildFloor);
            for(String numberRoom : numberRooms){
                if(numberRoom.equals(buildFloor + "-" + room)){
                    return true;
                }
            }

        }
        return false;
    }

    private ArrayList<String> listRoomsOnFloor(String buildFloor){
        String fullPath = servletContext.getRealPath(path);

        URI uri = new File(fullPath+"/" + buildFloor + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<String> room = new ArrayList<>();
        try{
            converter = new SVGMetaPost( uri.toString() );
            Document doc = converter.getSVGDocument();


            NodeList n = doc.getElementsByTagName("rect");
            NodeList p = doc.getElementsByTagName("path");
            for(int i =0 ;i<n.getLength();i++){
                //System.out.println(n.item(i).getTextContent());
                if(((Element) n.item(i)).getAttribute("id").contains(buildFloor)){
                    //System.out.println(((Element) n.item(i)).getAttribute("id"));
                    room.add(((Element) n.item(i)).getAttribute("id"));

                }
            }
            for(int i =0 ;i<p.getLength();i++) {
                //System.out.println(p.item(i).getTextContent());
                if(((Element) p.item(i)).getAttribute("id").contains(buildFloor)){
                    // System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("error: listRoomsOnFloor fail");
        }





        return room;

    }

    /*
    private JSONArray parseBuildings()throws Exception{


        HashMap<String, ArrayList<String>> buildings = new HashMap<>();

        try {

            String fullPath = servletContext.getRealPath(path);
            System.out.println(servletContext.getContextPath()+"++++++questo è il cammino");
            System.out.println(servletContext.getServletContextName()+"context nme");

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

        return parseBuildingJSON(buildings);





    }




    private JSONArray  parseBuildingJSON(HashMap<String, ArrayList<String>> buildings){


        JSONArray buildingsFloors = new JSONArray();

        for (String buildingString : buildings.keySet()) {



            JSONObject buildingFloors = new JSONObject();
            JSONArray arr = new JSONArray();




            for (String floorString : buildings.get(buildingString)) {
                JSONObject floor = new JSONObject();
                JSONObject image;

                String building = buildingString.replace(' ','_');


                image = new JSONObject();


                //--------------------------------
                s=UriBuilder.fromResource(JaxRestImage.class).toString();
                u =UriBuilder.fromMethod(JaxRestImage.class,"getFloorImage");
                m = new HashMap<>();
                m.put("buildingFloor",building+"-"+floorString);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //image.put("restLink","/rest/immagini/edifici/" + building + "-" + floorString);
                image.put("restLink",REST+s);
                //-------
                s=UriBuilder.fromResource(ImagesHandler.class).toString();
                u =UriBuilder.fromMethod(ImagesHandler.class,"getFloorImage");
                m = new HashMap<>();
                m.put("buildingFloor",building+"-"+floorString+".png");
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s + ".png");
                //image.put("PNGLink","/risorse/immagini/edifici/" + building + "-" + floorString+ ".png");
                image.put("PNGLink",RESOURCE+s);
                floor.put("imageFloor",image);
                //-----------
                s=UriBuilder.fromResource(JaxRestBuildings.class).toString();
                u =UriBuilder.fromMethod(JaxRestBuildings.class,"getBuildingFloorInfo");
                m = new HashMap<>();
                m.put("buildingFloor", building + "-" + floorString);
                s+=u.buildFromEncodedMap(m).toString();
                //System.out.println("mi da ................" + s);
                //floor.put("infoBuildingFloor","/rest/edifici/"+building+"-"+floorString);
                floor.put("infoBuildingFloor",REST+s);

                //floor.put("buildingFloor", "" + building + "-" + floorString);
                floor.put("floor", floorString);

                arr.add( floor);

            }
            //con spazio

            buildingFloors.put("building", buildingString);

            buildingFloors.put("floors", arr);

            buildingsFloors.add( buildingFloors);

        }

        return buildingsFloors;
    }*/
}

