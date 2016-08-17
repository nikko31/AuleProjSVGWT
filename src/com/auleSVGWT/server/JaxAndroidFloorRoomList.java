package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.PersonDTO;
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
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Utente on 10/08/2016.
 */
@Path("/listaStanzePiano")
public class JaxAndroidFloorRoomList {
    final String path = "/res/imageAndroid";
    DatabaseM db;
    ArrayList<PersonDTO> personDTOs;
    @Context
    ServletContext servletContext;

    @GET
    @Path("/{buildingFloor}")
    public Response getListOfRoom(@PathParam("buildingFloor") String buildingFloor){
        try {

            if(buildingFloor.endsWith(".json")){
                String build = buildingFloor.substring(0, buildingFloor.lastIndexOf("."));
                build = build.replace('_', ' ');
                if(controlBuildFloor(build)){
                    String json = parseRoomList(roomFloorList(build),build).toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }
            }



        }catch(Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();

    }
    private JSONObject parseRoomList(ArrayList<Integer> rooms, String buildingFloor) {
        int i = 0;
        JSONArray a = new JSONArray();
        JSONObject obj = new JSONObject();
        Collections.sort(rooms);


        if ((rooms.size() != 0)) {

            for (Integer room : rooms) {
               // System.out.println("VErifico ordine:::::::::::::::: "+room);
                JSONObject ro = new JSONObject();
                JSONObject link = new JSONObject();

                buildingFloor = buildingFloor.replace(' ','_');
                String num =""+ String.valueOf(room);

                link.put("imageSelecRoom","/Android/immagine/"+buildingFloor+"/"+num+".png");
                link.put("roomSelectInfo","/Android/stanze/"+buildingFloor + "/" + num+".json");
                link.put("peopleInRoom","/Android/persone/"+buildingFloor + "/" + num+".json");

                ro.put("link", link);
                ro.put("room",""+room);

                a.add(i, ro);
                i++;

            }

        }


        obj.put("rooms", a);
        return obj;


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

    public ArrayList<Integer> roomFloorList(String text) {
        String fullPath = servletContext.getRealPath(path);
        URI uri = new File(fullPath + "/" + text + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<Integer> room = new ArrayList<>();
        try {
            converter = new SVGMetaPost(uri.toString());
            Document doc = converter.getSVGDocument();

            int counter = 0;
            String u = "";
            NodeList n = doc.getElementsByTagName("rect");
            NodeList p = doc.getElementsByTagName("path");
            for (int i = 0; i < n.getLength(); i++) {
                //System.out.println(n.item(i).getTextContent());
                if (((Element) n.item(i)).getAttribute("id").contains(text)) {
                    u += ((Element) n.item(i)).getAttribute("id") + " ";
                    //System.out.println(((Element) n.item(i)).getAttribute("id"));
                    String s = ((Element) n.item(i)).getAttribute("id");
                    s = s.substring(s.lastIndexOf("-")+1);
                    room.add(Integer.parseInt(s));

                    counter++;
                }
            }
            for (int i = 0; i < p.getLength(); i++) {
                //System.out.println(p.item(i).getTextContent());
                if (((Element) p.item(i)).getAttribute("id").contains(text)) {
                    u += ((Element) p.item(i)).getAttribute("id") + " ";
                    //System.out.println(((Element) p.item(i)).getAttribute("id"));
                    String s = ((Element) p.item(i)).getAttribute("id");
                    s = s.substring(s.lastIndexOf("-")+1);
                    room.add(Integer.parseInt(s));

                    counter++;
                }
            }

            // System.out.println("RISULTATO RICERCA NEL FILE "+ u+" "+ counter);
        } catch (IOException e) {
            System.out.println("ERROR in listRoomsOfFloor");
            e.printStackTrace();
        }




        return room;

    }
}
