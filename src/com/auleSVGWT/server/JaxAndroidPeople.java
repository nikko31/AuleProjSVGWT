package com.auleSVGWT.server;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


@Path("/persone")
public class JaxAndroidPeople {
    final String path = "/res/imageAndroid";
    DatabaseM db;
    //ArrayList<PersonDTO> personDTOs;
    @Context
    ServletContext servletContext;


    @GET
    public Response getAllPeople(@QueryParam("number") int number, @QueryParam("search") String search){

        if(number!=0 && search!=null){

            db= new DatabaseM();
            String json = db.getPersonSearchJson(number,search).toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();

        }else{
            try {
                db= new DatabaseM();
                String json = db.getPeopleJson().toString();
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            }catch(Exception e){
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

        }


    }
    @GET
    @Path("/{person}")
    public Response getPerson(@PathParam("person") String person){

        try {
            String per = person.replace('_',' ');
            System.out.println("la diggerenza è "+ per+"  "+person);
            if(controlOfLettersandSpaceOnly(per)){
                db= new DatabaseM();
                String json = db.getPersonJson(per.substring(0,per.lastIndexOf(' ')),per.substring(per.lastIndexOf(' ')+1)).toString();
                return Response.ok(json, MediaType.APPLICATION_JSON).build();

            }
            /*
            if(person.endsWith(".json")){
                String per = person;


            }*/



        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @GET
    @Path("/{buildFloor}/{room}")
    public Response getPeopleInRoom(@PathParam("buildFloor") String buildFloor,@PathParam("room") String room){
        System.out.println("get room people con "+ buildFloor+ " "+ room);
        try {

            String build = buildFloor.replace('_',' ');
            if(controlBuildFloorRoom(build, room)){
                db= new DatabaseM();
                String json = db.getPeopleInRoomJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1), room).toString();
                return Response.ok(json, MediaType.APPLICATION_JSON).build();

            }
            /*if(room.endsWith(".json")){
                String ro = room.substring(0, room.lastIndexOf("."));


            }*/



        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @GET
    @Path("/lavoro/{buildFloor}")
    public Response getWorkPeopleInFloor(@PathParam("buildFloor") String buildFloor){

        try {

            String build = buildFloor.replace('_', ' ');
            if(controlBuildFloor(build)){
                db= new DatabaseM();
                String json = db.getOccupyOfFloorwithDateJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1)).toString();
                return Response.ok(json, MediaType.APPLICATION_JSON).build();


            }
            /*
            if(buildFloor.endsWith(".json")){
                String build = buildFloor.substring(0, buildFloor.lastIndexOf("."));


            }*/



        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    public boolean controlBuildFloor( String buildFloor)throws Exception{
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

    public boolean controlBuildFloorRoom(String buildFloor,String room)throws Exception{

        if(controlBuildFloor(buildFloor)){
            ArrayList<String> numberRooms = listRoomsOfFloor(buildFloor);
            for(String numberRoom : numberRooms){
                if(numberRoom.equals(buildFloor+"-"+room)){
                    return true;
                }
            }

        }
        return false;
    }

    private boolean controlOfLettersandSpaceOnly(String s) {

        int counter =0;

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i)==' '){
                counter ++;
            }
            if (!Character.isLetter(s.charAt(i)) && s.charAt(i)!=' '){
                System.out.println("il problema e nel controllo spazio lettera");
                return false;
            }
        }

        return counter == 1;

    }

    private boolean controlOfLettersOnly(String s) {


        for(int i = 0; i < s.length(); i++) {

            if (!Character.isLetter(s.charAt(i))){
                System.out.println("il problema e nel controllo spazio lettera");
                return false;
            }
        }

        return true;

    }





    public ArrayList<String> listRoomsOfFloor(String text){
        String fullPath = servletContext.getRealPath(path);
        URI uri = new File(fullPath+"/" + text + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<String> room = new ArrayList<>();
        try{
            converter = new SVGMetaPost( uri.toString() );
            Document doc = converter.getSVGDocument();

            //int counter =0;
            //String u ="";
            NodeList n = doc.getElementsByTagName("rect");
            NodeList p = doc.getElementsByTagName("path");
            for(int i =0 ;i<n.getLength();i++){
                //System.out.println(n.item(i).getTextContent());
                if(((Element) n.item(i)).getAttribute("id").contains(text)){
                    //u+=((Element) n.item(i)).getAttribute("id")+" ";
                    //System.out.println(((Element) n.item(i)).getAttribute("id"));
                    room.add(((Element) n.item(i)).getAttribute("id"));

                    //counter ++;
                }
            }
            for(int i =0 ;i<p.getLength();i++) {
                //System.out.println(p.item(i).getTextContent());
                if(((Element) p.item(i)).getAttribute("id").contains(text)){
                    //u+=((Element) p.item(i)).getAttribute("id")+" ";
                    System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));

                    //counter ++;
                }
            }

            //System.out.println("RISULTATO RICERCA NEL FILE " + u + " " + counter);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("ERROr in listRoomsOfFLoor");
        }





        return room;

    }






}
