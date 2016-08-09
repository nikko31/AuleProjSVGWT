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
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Utente on 01/08/2016.
 */
public class AndroidPeopleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String addImageAndroid="/res/imageAndroid";


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String uri = req.getRequestURI();
        String servlet = req.getServletPath();
        uri =  java.net.URLDecoder.decode(uri,"UTF-8");
        resp.setContentType("application/json");
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
    private Boolean controlPath(String uri,String servlet ,PrintWriter out) throws Exception{
        ArrayList<PersonDTO> personDTOs;
        DatabaseM db;
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath(addImageAndroid);
        File folder = new File(fullPath);
        File[] listOfFiles = folder.listFiles();


        if(uri.equals(servlet)){
            db= new DatabaseM();
            personDTOs = db.getPerson();
            parseOut(out,personDTOs);
            return false;
        }
        String path = servlet+"/";
        if(uri.contains(path) && uri.endsWith(".json")){
            System.out.println(uri.substring(servlet.length() + 1,uri.lastIndexOf("."))+".........");
            if(controlOfNumberOnly(uri.substring(servlet.length() + 1,uri.lastIndexOf(".")))){
                db= new DatabaseM();
                personDTOs = db.getPersonFromId(Integer.parseInt(uri.substring(servlet.length() + 1,uri.lastIndexOf("."))));
                parseOut(out,personDTOs);
                return false;

            }
            if(controlOfLettersandSpaceOnly(uri.substring(servlet.length() + 1, uri.lastIndexOf(".")))){

                String person = uri.substring(uri.lastIndexOf('/')+1);
                System.out.println("la string della persona e :" + person + "di cui  " + person.substring(0, person.lastIndexOf('_')) + "   e...." + person.substring(person.lastIndexOf('_') + 1));
                db= new DatabaseM();
                personDTOs = db.getPerson(person.substring(0,person.lastIndexOf('_')),person.substring(person.lastIndexOf('_')+1,person.lastIndexOf(".")));
                System.out.println("la dimensione di personDTOS e  " + personDTOs.size());
                parseOut(out,personDTOs);
                return false;
            }

        }


        if(listOfFiles != null){
            for (File file : listOfFiles) {
                String s= file.getName();
                String filename = file.getName();
                filename = filename.substring(0,filename.lastIndexOf("."));
                s=servlet+"/"+s.substring(0,s.lastIndexOf("."));
                System.out.println("" + s + "........" + uri);
                if(uri.contains(servlet + "/" + filename)){

                    String build = s.substring(s.lastIndexOf("/")+1);

                    if(uri.equals(s+"/lavoro.json")){
                        System.out.println(filename.substring(0,filename.lastIndexOf("-"))+" "+filename.substring(filename.lastIndexOf("-"))+".....  ");
                        db= new DatabaseM();
                        personDTOs = db.getOccupyOfFloorwithDate(filename.substring(0,filename.lastIndexOf("-")),filename.substring(filename.lastIndexOf("-")+1));
                        parseOut(out,personDTOs);
                        return false;
                    }

                    for(String room :listRoomsOfFloor(filename)){
                        System.out.println(servlet+"/"+filename+"/"+room.substring(room.lastIndexOf("-")+1)+".json");
                        if(uri.equals(servlet+"/"+filename+"/"+room.substring(room.lastIndexOf("-")+1)+".json")){
                            db= new DatabaseM();
                            System.out.println(filename.substring(0,filename.lastIndexOf("-"))+" "+filename.substring(filename.lastIndexOf("-"))+".....  "+uri.substring(uri.lastIndexOf("/")+1));
                            personDTOs = db.getPeopleInRoom(filename.substring(0, filename.lastIndexOf("-")), filename.substring(filename.lastIndexOf("-") + 1), uri.substring(uri.lastIndexOf("/") + 1,uri.lastIndexOf(".")));
                            parseOut(out,personDTOs);
                            return false;

                        }
                    }
                }

            }
        }

        return true;


    }


    private void parseOut(PrintWriter out,ArrayList<PersonDTO> personDTOs){
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

                link.put("roomsOccPerson","/Android/stanzee/persone/"+personDTO.getName()+"_"+personDTO.getSurname()+".json");

                personJSON.put("link",link);

                arrayPersonJ.add(j,personJSON);
                j++;



            }

        }

        obj= new JSONObject();
        obj.put("people", arrayPersonJ);
        out.print(obj.toString());
    }

    private boolean controlOfLettersandSpaceOnly(String s) {

        int counter =0;

        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='_'){
                counter ++;
            }
            if (!Character.isLetter(s.charAt(i)) && s.charAt(i)!='_'){
                System.out.println("il problema e nel controllo spazio lettera");
                return false;
            }
        }

        if(counter!=1){
            return false;
        }

        return true;
    }

    private boolean controlOfNumberOnly(String s) {
        if(s.length()<=0){
            return false;
        }

        for(int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))){
                return false;
            }
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