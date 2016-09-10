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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

@Path("/persone")
public class JaxRestPeople {
    private final String path = "/res/imageAndroid";
    private DatabaseM db;
    @Context
    private ServletContext servletContext;


    @GET
    public Response getAllPeople(@QueryParam("number") int number, @QueryParam("search") String search){
        System.out.println("il valore di number è"+ number);

        if(number!=0 && search!=null){

            String s= search.replace('_',' ');
            if(controlOfLettersandSpaceOnly(s)){
                db= new DatabaseM();
                JSONArray arr = db.getPersonSearchJson(number,s);
                if(arr != null){
                    String json = arr.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }


            }


        }else if(number==0 && search==null){

            try {
                db= new DatabaseM();
                JSONArray arr = db.getPeopleJson();
                if(arr!=null){
                    return Response.ok(arr.toJSONString(), MediaType.APPLICATION_JSON).build();
                }else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }catch(Exception e){
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    @GET
    @Path("/{person}")
    public Response getPerson(@PathParam("person") String person){

        try {
            String per = person.replace('_',' ');
            //System.out.println("persona...."+ per+"  "+person);
            if(controlOfLettersandSpaceOnly(per)){
                db= new DatabaseM();
                JSONObject obj = db.getPersonJson(per.substring(0, per.lastIndexOf(' ')), per.substring(per.lastIndexOf(' ') + 1));
                if(obj != null){
                    String json = obj.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();

                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }else if(controlDigitOnly(person)){
                db= new DatabaseM();
                JSONObject obj = db.getPersonWithIDJson(person);
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
    @Path("/{person}/stanze")
    public Response getRoomsOfPerson(@PathParam("person") String person){

        try {

            String per = person.replace('_',' ');
            if(controlOfLettersandSpaceOnly(per)){
                db= new DatabaseM();
                JSONArray arr = db.getOccupedRoomOfPersonJson(per.substring(0, per.lastIndexOf(' ')), per.substring(per.lastIndexOf(' ') + 1));
                if(arr != null){
                    String json = arr.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }else if(controlDigitOnly(person)){
                db= new DatabaseM();
                JSONArray arr = db.getOccupedRoomOfPersonWithIdJson(person);
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

    //-----------------------Controls------------------


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



    private boolean controlDigitOnly(String s) {


        for(int i = 0; i < s.length(); i++) {

            if (!Character.isDigit(s.charAt(i))){
                System.out.println("il problema e nel controllo spazio numero");
                return false;
            }
        }

        return true;

    }





    public ArrayList<String> listRoomsOnFloor(String text){
        String fullPath = servletContext.getRealPath(path);
        URI uri = new File(fullPath+"/" + text + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<String> room = new ArrayList<>();
        try{
            converter = new SVGMetaPost( uri.toString() );
            Document doc = converter.getSVGDocument();


            NodeList n = doc.getElementsByTagName("rect");
            NodeList p = doc.getElementsByTagName("path");
            for(int i =0 ;i<n.getLength();i++){
                //System.out.println(n.item(i).getTextContent());
                if(((Element) n.item(i)).getAttribute("id").contains(text)){

                    //System.out.println(((Element) n.item(i)).getAttribute("id"));
                    room.add(((Element) n.item(i)).getAttribute("id"));


                }
            }
            for(int i =0 ;i<p.getLength();i++) {
                //System.out.println(p.item(i).getTextContent());
                if(((Element) p.item(i)).getAttribute("id").contains(text)){

                    System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));


                }
            }


        }catch (IOException e){
            e.printStackTrace();
            System.out.println("error: listRoomsOnFloor");
        }





        return room;

    }






}
