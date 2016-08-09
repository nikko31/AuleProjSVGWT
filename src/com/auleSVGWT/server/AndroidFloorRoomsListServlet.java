package com.auleSVGWT.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Utente on 01/08/2016.
 */
public class AndroidFloorRoomsListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String addImageAndroid = "/res/imageAndroid";

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String servlet = request.getServletPath();
        uri =  java.net.URLDecoder.decode(uri,"UTF-8");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        try{

            if(controlPath(uri,servlet,out)){

                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                out.print("ERRORE NEL PARAMETRO");
                out.close();
            }

        }catch(Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.close();

        }









    }

    private Boolean controlPath(String uri,String servlet ,PrintWriter out) throws Exception{
        ArrayList<String> rooms;
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath(addImageAndroid);

        File folder = new File(fullPath);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles != null){
            for (File file : listOfFiles) {
                String s= file.getName();
                s=servlet+"/"+s.substring(0,s.lastIndexOf("."))+".json";
                System.out.println(""+s+"........"+uri);
                if(uri.equals(s) ){
                    rooms = roomFloorList(s.substring(s.lastIndexOf("/")+1,s.lastIndexOf(".")));

                    parseOut(rooms, out);
                    return false;



                }

            }
        }

        return true;


    }


    private void parseOut(ArrayList<String> rooms, PrintWriter out) {
        int i = 0;
        JSONArray a = new JSONArray();
        JSONObject obj = new JSONObject();


        if ((rooms.size() != 0)) {

            for (String room : rooms) {
                System.out.println("VErifico ordine:::::::::::::::: "+room);
                JSONObject ro = new JSONObject();
                JSONObject link = new JSONObject();

                String buildingFloor = room.substring(0,room.lastIndexOf("-"));
                String num = room.substring(room.lastIndexOf("-")+1);

                link.put("imageSelecRoom","/Android/immagine/"+buildingFloor+"/"+num+".png");
                link.put("roomSelectInfo","/Android/stanzee/"+buildingFloor + "/" + num+".json");
                link.put("peopleInRoom","/Android/persone/"+buildingFloor + "/" + num+".json");

                ro.put("link", link);
                ro.put("room",room);

                a.add(i, ro);
                i++;

            }

        }


        obj.put("rooms", a);
        out.print(obj.toString());
        out.close();


    }


    private Boolean controlParameter(String fileSVG) {
        Boolean controlFlag = true;

        if (fileSVG == null) {
            controlFlag = false;
        }

        if (controlFlag) {
            if (fileSVG.length() > 30) {
                controlFlag = false;
            }
        }

        return controlFlag;
    }

    public ArrayList<String> roomFloorList(String text) {
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath(addImageAndroid);
        URI uri = new File(fullPath + "/" + text + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<String> room = new ArrayList<>();
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
                    room.add(((Element) n.item(i)).getAttribute("id"));

                    counter++;
                }
            }
            for (int i = 0; i < p.getLength(); i++) {
                //System.out.println(p.item(i).getTextContent());
                if (((Element) p.item(i)).getAttribute("id").contains(text)) {
                    u += ((Element) p.item(i)).getAttribute("id") + " ";
                    //System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));

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
