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


public class AndroidFloorRoomsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String fileSVG = request.getParameter("fileSVG");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        ArrayList<String> rooms = new ArrayList<>();


        if(controlParameter(fileSVG)){
            rooms = roomFloorList(fileSVG);
        }


        parseOut(rooms,out);


    }

    private void parseOut(ArrayList<String> rooms ,PrintWriter out){
        int i = 0;
        JSONArray a = new JSONArray();
        JSONObject obj = new JSONObject();

        if((rooms.size() != 0) && (rooms) != null){
            for (String room : rooms) {

                a.add(i,room);
                i++;
            }

        }


        obj.put("rooms",a);
        out.print(obj.toString());


    }


    private Boolean controlParameter(String fileSVG){
        Boolean controlFlag = true;

        if(fileSVG == null){
            controlFlag = false;
        }

        if(controlFlag){
            if(fileSVG.length()>30){
                controlFlag = false;
            }
        }

        return controlFlag;
    }

    public ArrayList<String> roomFloorList(String text){
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("/Images");
        URI uri = new File( fullPath+"/" + text + ".svg").toURI();
        SVGMetaPost converter;
        ArrayList<String> room = new ArrayList<>();
        try{
            converter = new SVGMetaPost( uri.toString() );
            Document doc = converter.getSVGDocument();

            int counter =0;
            String u ="";
            NodeList n = doc.getElementsByTagName("rect");
            NodeList p = doc.getElementsByTagName("path");
            for(int i =0 ;i<n.getLength();i++){
                //System.out.println(n.item(i).getTextContent());
                if(((Element) n.item(i)).getAttribute("id").contains(text)){
                    u+=((Element) n.item(i)).getAttribute("id")+" ";
                    //System.out.println(((Element) n.item(i)).getAttribute("id"));
                    room.add(((Element) n.item(i)).getAttribute("id"));

                    counter ++;
                }
            }
            for(int i =0 ;i<p.getLength();i++) {
                //System.out.println(p.item(i).getTextContent());
                if(((Element) p.item(i)).getAttribute("id").contains(text)){
                    u+=((Element) p.item(i)).getAttribute("id")+" ";
                    //System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));

                    counter ++;
                }
            }

           // System.out.println("RISULTATO RICERCA NEL FILE "+ u+" "+ counter);
        }catch (IOException e){
            System.out.println("ERROR in listRoomsOfFloor");
            e.printStackTrace();
        }





        return room;

    }
}