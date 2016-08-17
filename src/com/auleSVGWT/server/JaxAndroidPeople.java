package com.auleSVGWT.server;


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


@Path("/persone")
public class JaxAndroidPeople {
    final String path = "/res/imageAndroid";
    DatabaseM db;
    //ArrayList<PersonDTO> personDTOs;
    @Context
    ServletContext servletContext;


    @GET
    public Response getAllPeople(){
        try {
            db= new DatabaseM();
            /*personDTOs = db.getPerson();
            String json = parsePeople(personDTOs).toString();*/
            String json = db.getPeopleJson().toString();
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }
    @GET
    @Path("/{person}")
    public Response getPerson(@PathParam("person") String person){

        try {
            if(person.endsWith(".json")){
                String per = person;
                per = per.replace('_',' ');
                System.out.println("la diggerenza è "+ per+"  "+person);
                if(controlOfLettersandSpaceOnly(per.substring(0, per.lastIndexOf(".")))){
                    db= new DatabaseM();
                    /*personDTOs = db.getPerson(person.substring(0,person.lastIndexOf('_')),person.substring(person.lastIndexOf('_')+1,person.lastIndexOf(".")));
                    String json = parsePeople(personDTOs).toString();*/
                    String json = db.getPersonJson(per.substring(0,per.lastIndexOf(' ')),per.substring(per.lastIndexOf(' ')+1,per.lastIndexOf("."))).toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();

                }

            }



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
            if(room.endsWith(".json")){
                String ro = room.substring(0, room.lastIndexOf("."));
                String build = buildFloor.replace('_',' ');
                if(controlBuildFloorRoom(build, ro)){
                    db= new DatabaseM();/*
                    personDTOs = db.getPeopleInRoom(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1), ro);
                    String json = parsePeople(personDTOs).toString();*/
                    String json = db.getPeopleInRoomJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1), ro).toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();

                }

            }



        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }

    @GET
    @Path("/lavoro/{buildFloor}")
    public Response getWorkRoominFloor(@PathParam("buildFloor") String buildFloor){

        try {
            if(buildFloor.endsWith(".json")){
                String build = buildFloor.substring(0,buildFloor.lastIndexOf("."));
                build = build.replace('_', ' ');
                if(controlBuildFloor(build)){
                    db= new DatabaseM();/*
                    personDTOs = db.getOccupyOfFloorwithDate(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1));
                    String json = parsePeople(personDTOs).toString();*/
                    String json = db.getOccupyOfFloorwithDateJson(build.substring(0, build.lastIndexOf("-")), build.substring(build.lastIndexOf("-") + 1)).toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();


                }

            }



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


    /*

    private JSONObject parsePeople(ArrayList<PersonDTO> personDTOs){
        JSONArray arrayPersonJ = new JSONArray();
        JSONObject personJSON;
        JSONObject obj;

        if(personDTOs.size()>0){

            int j = 0;
            for(PersonDTO personDTO : personDTOs){
                personJSON = new JSONObject();
                JSONObject link = new JSONObject();


                personJSON.put("name", personDTO.getName());
                personJSON.put("surname", personDTO.getSurname());
                personJSON.put("role", personDTO.getRole().getName());
                if(personDTO.getStartWork() != null){
                    String sW = personDTO.getStartWork().toString();
                    sW = sW.replaceAll("-"," ");
                    personJSON.put("startWork",sW);
                }else{
                    personJSON.put("startWork","null");

                }
                if(personDTO.getEndWork() != null){
                    String eW = personDTO.getEndWork().toString();
                    eW = eW.replaceAll("-", " ");
                    personJSON.put("endWork",eW);


                }else{
                    personJSON.put("endWork","null");

                }

                link.put("roomsOccPerson","/Android/stanze/persone/"+personDTO.getName()+"_"+personDTO.getSurname()+".json");

                personJSON.put("link",link);

                arrayPersonJ.add(j,personJSON);
                j++;



            }

        }

        obj= new JSONObject();
        obj.put("people", arrayPersonJ);
        return obj;
    }*/
}
