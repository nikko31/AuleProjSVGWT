package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.OccupyDTO;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Utente on 01/08/2016.
 */
public class AndroidRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String addImageAndroid="/res/imageAndroid";


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String uri = req.getRequestURI();
        String servlet = req.getServletPath();
        uri =  java.net.URLDecoder.decode(uri,"UTF-8");

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try{
            if(controlPath(uri, servlet, out)){

                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                out.close();
            }

        }catch (Exception e){
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.close();
        }

    }

    private Boolean controlPath(String uri,String servlet ,PrintWriter out)throws Exception{
        ArrayList<RoomDTO> roomDTOs;
        DatabaseM db;
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath(addImageAndroid);
        File folder = new File(fullPath);
        File[] listOfFiles = folder.listFiles();


        String searchPersonRoom= servlet+"/persone/";
        if(uri.contains(searchPersonRoom) && uri.endsWith(".json")){
            System.out.println("sono dentro a persone");
            System.out.println("il controllo e fatto su : "+ uri.substring(searchPersonRoom.length()));
            if(controlOfLettersandSpaceOnly(uri.substring(searchPersonRoom.length(),uri.lastIndexOf(".")))){

                String person = uri.substring(uri.lastIndexOf('/')+1,uri.lastIndexOf("."));
                System.out.println("la string della persona e :" + person + "di cui  " + person.substring(0, person.lastIndexOf('_')) + "   e...." + person.substring(person.lastIndexOf('_') + 1));
                db= new DatabaseM();
                roomDTOs = db.getOccupedRoomOfPerson(person.substring(0,person.lastIndexOf('_')),person.substring(person.lastIndexOf('_')+1));
                System.out.println("la dimensione di personDTOS e  " + roomDTOs.size());
                parseOut(roomDTOs,out);
                return false;
            }

        }

        if(listOfFiles != null){
            for (File file : listOfFiles) {
                String filename = file.getName();
                filename = filename.substring(0,filename.lastIndexOf("."));
                String s= file.getName();
                s=servlet+"/"+s.substring(0,s.lastIndexOf("."));
                System.out.println(""+s+"........"+uri);
                if(uri.equals(servlet+"/occupazione/"+filename+".json")){
                    System.out.println(filename.substring(0,filename.lastIndexOf("-"))+" "+filename.substring(filename.lastIndexOf("-"))+".....  ");
                    db= new DatabaseM();
                    roomDTOs = db.getOccupyOfFloorwithDimension(filename.substring(0,filename.lastIndexOf("-")),filename.substring(filename.lastIndexOf("-")+1));
                    parseOut(roomDTOs,out);
                    return false;
                }
                if(uri.contains(servlet+"/"+filename) ){
                    for(String room :listRoomsOfFloor(filename)){
                        System.out.println(""+s+"/room........"+uri);
                        if(uri.equals(servlet+"/"+filename+"/"+room.substring(room.lastIndexOf("-")+1)+".json")){
                            db= new DatabaseM();
                            System.out.println(filename.substring(0,filename.lastIndexOf("-"))+" "+filename.substring(filename.lastIndexOf("-"))+".....  "+uri.substring(uri.lastIndexOf("/")+1));
                            roomDTOs = db.getRoomInfo(filename.substring(0,filename.lastIndexOf("-")),filename.substring(filename.lastIndexOf("-")+1),uri.substring(uri.lastIndexOf("/")+1,uri.lastIndexOf(".")));
                            parseOut(roomDTOs, out);
                            return false;

                        }
                    }
                }

            }
        }




        return true;


    }








    private boolean controlOfLettersOnly(String s) {
        boolean flag= true;

        for(int i = 0; i < s.length(); i++) {
            if(flag){
                if (!Character.isLetter(s.charAt(i))){
                    flag = false;
                }
            }
        }
        return flag;
    }


    private void parseOut(ArrayList<RoomDTO> roomDTOs,PrintWriter out){
        JSONArray ar;
        JSONObject obj;



        ar = new JSONArray();

        if(roomDTOs.size() !=0){
            ar = new JSONArray();


            int i=0;
            for(RoomDTO roomDTO : roomDTOs){
                obj = new JSONObject();
                JSONObject link = new JSONObject();
                if(!obj.containsKey("number")){
                    obj.put("number",""+roomDTO.getNumber());

                }

                obj.put("building",""+roomDTO.getBuilding().getName());
                obj.put("floor",""+roomDTO.getFloor());
                obj.put("info",""+roomDTO.getMaintenance());
                obj.put("personMax",""+roomDTO.getMaxPeople());
                obj.put("socket",""+roomDTO.getSocket());
                obj.put("dimension",""+roomDTO.getDimension());
                obj.put("code",""+roomDTO.getRoomCode());
                link.put("peopleInRoom","/Android/persone/"+roomDTO.getBuilding().getName()+"-"+roomDTO.getFloor()+"/"+roomDTO.getNumber()+".json");
                link.put("imageSelecRoom","/Android/immagine/"+roomDTO.getBuilding().getName()+"-"+roomDTO.getFloor()+"/"+roomDTO.getNumber()+".png");
                obj.put("link",link);
                if(obj.size()!=0){
                    ar.add(i,obj);
                    i++;
                }

            }

            obj= new JSONObject();
            obj.put("rooms",ar);

        }else{
            obj= new JSONObject();
            obj.put("rooms",ar);


        }



        out.print(obj.toString());
        out.close();

    }

    private boolean controlOfLettersandSpaceOnly(String s) {

        int counter =0;

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='_'){
                counter ++;
            }
            if (!Character.isLetter(s.charAt(i)) && s.charAt(i)!='_'){
                System.out.println("il problema e nel controllo spazio lettera "+s.charAt(i));
                return false;
            }
        }

        if(counter!=1){
            return false;
        }

        return true;
    }



    public ArrayList<String> listRoomsOfFloor(String text){
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath(addImageAndroid);
        URI uri = new File(fullPath+"/" + text + ".svg").toURI();
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
                    System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));

                    counter ++;
                }
            }

            //System.out.println("RISULTATO RICERCA NEL FILE " + u + " " + counter);
        }catch (IOException e){
            System.out.println("ERROr in listRoomsOfFLoor");
        }







        return room;

    }




}
