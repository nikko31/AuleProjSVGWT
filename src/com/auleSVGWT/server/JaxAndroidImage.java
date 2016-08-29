package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.OccupyDTO;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import com.auleSVGWT.client.shared.Global;
import com.auleSVGWT.server.domain.Person;
import com.auleSVGWT.server.domain.Room;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Utente on 10/08/2016.
 */
@Path("/immagine")
public class JaxAndroidImage {
    final String path = "/res/imageAndroid";
    final String tmpPath = "/res/imagePng";
    DatabaseM db;
    ArrayList<PersonDTO> personDTOs;
    @Context
    ServletContext servletContext;


    @GET
    @Path("/{buildingFloor}")
    public Response getFloorImage(@PathParam("buildingFloor")String buildingFloor) {

        try {
            if (buildingFloor.endsWith(".png")) {
                String build = buildingFloor.substring(0, buildingFloor.lastIndexOf("."));
                build = build.replace('_', ' ');
                if (controlBuildFloor(build)) {
                    String pngPathOut = servletContext.getRealPath(tmpPath);


                    Convert(build,"map","",buildingFloor);

                    File f = new File(pngPathOut+"/"+buildingFloor);
                    //System.out.println(pngPathOut+"/"+buildingFloor);

                    if (!f.exists()) {
                        throw new WebApplicationException(404);
                    }else{
                        String mt = new MimetypesFileTypeMap().getContentType(f);
                        return Response.ok(f, mt).build();


                    }

                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }



    @GET
    @Path("/occupazione/{buildingFloor}")
    public Response getOccupationImage(@PathParam("buildingFloor")String buildingFloor) {

        try {
            if (buildingFloor.endsWith(".png")) {
                String build = buildingFloor.substring(0, buildingFloor.lastIndexOf("."));
                build = build.replace('_', ' ');
                if (controlBuildFloor(build)) {
                    String pngPathOut = servletContext.getRealPath(tmpPath);


                    Convert(build,"occupation","","occu_"+buildingFloor);

                    File f = new File(pngPathOut+"/"+"occu_"+buildingFloor);
                   // System.out.println(pngPathOut+"/"+"occu_"+buildingFloor);

                    if (!f.exists()) {
                        throw new WebApplicationException(404);
                    }else{
                        String mt = new MimetypesFileTypeMap().getContentType(f);
                        return Response.ok(f, mt).build();


                    }



                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


        return Response.status(Response.Status.NOT_FOUND).build();

    }



    @GET
    @Path("/lavoro/{buildingFloor}")
    public Response getWorkImage(@PathParam("buildingFloor")String buildingFloor) {

        try {
            if (buildingFloor.endsWith(".png")) {
                String build = buildingFloor.substring(0, buildingFloor.lastIndexOf("."));
                build = build.replace('_', ' ');
                if (controlBuildFloor(build)) {


                    String pngPathOut = servletContext.getRealPath(tmpPath);


                    Convert(build,"work","","work_"+buildingFloor);

                    File f = new File(pngPathOut+"/"+"work_"+buildingFloor);
                   // System.out.println(pngPathOut+"/"+"work_"+buildingFloor);

                    if (!f.exists()) {
                        throw new WebApplicationException(404);
                    }else{
                        String mt = new MimetypesFileTypeMap().getContentType(f);
                        return Response.ok(f, mt).build();


                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }


    @GET
    @Path("/{buildingFloor}/{numRoom}")
    public Response getRoomImage(@PathParam("buildingFloor")String buildingFloor,@PathParam("numRoom")String numRoom) {

        try{
            if(numRoom.endsWith(".png")){
                String build = buildingFloor.replace('_', ' ');
                String room = numRoom.substring(0,numRoom.lastIndexOf("."));
                if(controlBuildFloorRoom(build, room)){
                    Convert(build,"selection",build+"-"+room,buildingFloor+"-"+numRoom);
                    String pngPathOut = servletContext.getRealPath(tmpPath);

                    File f = new File(pngPathOut+"/"+buildingFloor+"-"+numRoom);
                   // System.out.println(pngPathOut+"/"+buildingFloor+"-"+numRoom);

                    if (!f.exists()) {
                        throw new WebApplicationException(404);
                    }else{
                        String mt = new MimetypesFileTypeMap().getContentType(f);
                        return Response.ok(f, mt).build();


                    }

                }

            }


        }catch (Exception e){
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
                if(numberRoom.equals(buildFloor + "-" + room)){
                    return true;
                }
            }

        }
        return false;
    }





    public void Convert(String name,String mod,String idRoom,String fileName) throws IOException {
        System.out.println("sono in convert   "+fileName);

        Boolean fileExistance = true;
        String image="";
        File f;


        /*
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, (float) 1280);
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, (float) 720);*/


        try{
            String fullPath = servletContext.getRealPath(path);
            f = (new File(fullPath+"/"+name+".svg"));
            if(!f.exists()){
                fileExistance = false;
                System.out.println("il file svg non esiste");
            }else{
                System.out.println("il file svg esiste");
                image= f.toURI().toString();

            }


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("non esiste il file cercato");

        }

        if(fileExistance){
            try {

                File file;
                String pngPathOut = servletContext.getRealPath(tmpPath);
                file = new File(pngPathOut+"/"+fileName);
                SVGMetaPost converter = new SVGMetaPost(image);
                Document doc = converter.getSVGDocument();


                if("map".equals(mod)){

                    if(file.exists()) {
                        System.out.println("il file esiste modalita 0");
                        return;
                    }else{
                        System.out.println("sono in modalita  0  ed il file non esiste"+ fileName );
                        modeZero(doc,fileName);


                    }


                }
                if("selection".equals(mod)){

                    if(file.exists()) {
                        System.out.println("il file esiste modalita 1");
                        return;
                    }else {
                        System.out.println("sono in modalita 1 ed il file esiste " + fileName);
                        modeOne(doc, idRoom, fileName);
                    }
                }
                else{
                    System.out.println("sono in modalita 2/3 "+fileName);

                    ArrayList<OccupyDTO> occupyDTOs;
                    ArrayList<String> room;
                    String build;
                    String floor;
                    DatabaseM db = new DatabaseM();


                    build = name.substring(0, name.lastIndexOf("-"));
                    floor = name.substring(name.lastIndexOf("-") + 1);

                    room = listRoomsOfFloor(name);
                   // System.out.println("get occupy of floor"+ build +" "+floor);
                    occupyDTOs = db.getOccupyOfFloor(build,floor);

                    if("occupation".equals(mod)){

                        if(file.exists()) {
                            java.util.Date date = new java.util.Date();
                            long diff = date.getTime() - file.lastModified();

                            if (diff >   2* 60 * 1000) {
                                file.delete();
                                System.out.println("sono passati i due minuti lo cancello modalita occupation");
                                try{
                                    modeTwo(room, occupyDTOs, doc,fileName);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            else{
                                System.out.println("sono in modalita occupation non cacello");
                                return;

                            }

                        }else {
                            System.out.println("sono in modalita occupation non esiste");
                            modeTwo(room, occupyDTOs, doc,fileName);
                        }

                    System.out.println("sono nell mod 2");

                    }
                    else if("work".equals(mod)){

                        if(file.exists()) {
                            java.util.Date date = new java.util.Date();
                            long diff = date.getTime() - file.lastModified();

                            if (diff >   2* 60 * 1000) {
                                file.delete();
                                System.out.println("sono passati i due minuti lo cancello modalita lavoro");
                                try{
                                    modeThree(room, occupyDTOs, doc, fileName);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            else{
                                System.out.println("sono in modalita lavoro non cacello");
                                return;

                            }

                        }else {
                            System.out.println("sono in modalita occupation non esiste");
                            modeThree(room, occupyDTOs, doc, fileName);
                        }

                    }

                }




            } catch (IOException v) {
                v.printStackTrace();
            }

        }


    }

    public void modeZero(Document doc,String fileName){
        System.out.println("sono in modalita 0 "+fileName);
        File file;
        String pngPathOut = servletContext.getRealPath(tmpPath);
        file = new File(pngPathOut+"/"+fileName);
        System.out.println("sono in modalita 0 " + fileName);

        try{
            System.out.println("il file non esiste");

            transcode(file,doc);

        }catch(Exception c){
            c.printStackTrace();
        }






    }

    public void modeOne(Document doc,String id,String fileName){
        System.out.println("sono in modalita  "+fileName+" e id "+id);

        File file;
        String pngPathOut = servletContext.getRealPath(tmpPath);
        file = new File(pngPathOut+"/"+fileName);

        try {
            System.out.println("il file non esiste");
            String style = doc.getElementById(id).getAttribute("style");
            style = style.replaceFirst("fill:none", Global.GREEN_FILL);
            doc.getElementById(id).setAttribute("style", style);

            transcode(file, doc);

        } catch (Exception c) {
            c.printStackTrace();
        }

    }


    public void modeTwo(ArrayList<String> strings, ArrayList<OccupyDTO> occupyDTOs,Document doc,String fileName){

        int sum;
        int dim;
        DatabaseM db = new DatabaseM();


        for (String room : strings) {
            sum = 0;
            dim = 0;

            String numRoom = room.substring(room.lastIndexOf("-")+1);
            String buildFLoor = room.substring(0, room.lastIndexOf("-"));
            /*ArrayList<RoomDTO> rooms = db.getRoomInfo(buildFLoor.substring(0,buildFLoor.lastIndexOf("-")),buildFLoor.substring(buildFLoor.lastIndexOf("-")+1),numRoom);
            if(rooms.size()!=0){
                dim = rooms.get(0).getDimension();
                System.out.println("la dim e:::."+dim);

            }
            ArrayList<OccupyDTO> occ = db.getOccupyOfRoom(buildFLoor.substring(0,buildFLoor.lastIndexOf("-")),buildFLoor.substring(buildFLoor.lastIndexOf("-")+1),numRoom);
            for(OccupyDTO occupyDTO :occ){
                sum += occupyDTO.getPerson().getRole().getSqm();
            }*/
            ArrayList<Room> rooms = db.getRoomInfoNotDTO(buildFLoor.substring(0, buildFLoor.lastIndexOf("-")), buildFLoor.substring(buildFLoor.lastIndexOf("-") + 1), numRoom);
            if(rooms.size()!=0){
                dim = rooms.get(0).getDimension();
                //System.out.println("la dim e:::."+dim);

            }
            ArrayList<Person> persons = db.getPeopleInRoomNotDTO(buildFLoor.substring(0,buildFLoor.lastIndexOf("-")),buildFLoor.substring(buildFLoor.lastIndexOf("-")+1),numRoom);
            for(Person person :persons){
                sum += person.getRole().getSqm();
            }



            if (dim == 0 && sum != 0 ) {
                String style = doc.getElementById(room).getAttribute("style");
                style = style.replaceFirst("fill:none",Global.RED_FILL);
                doc.getElementById(room).setAttribute("style", style);

            } else if (sum == dim && sum!=0) {
                String style = doc.getElementById(room).getAttribute("style");
                style = style.replaceFirst("fill:none",Global.GREEN_FILL);
                doc.getElementById(room).setAttribute("style", style);
            } else if (sum > dim ) {
                //Integer value = new Integer ((1-(sum/Max))*200);
                //Integer value = new Integer (((double)dim/sum)*200);
                Double value1 = ((double) dim / sum) * 200;
                Integer value = value1.intValue();

                //varia da 0 a 200
                String str = "#FF";
                if (Integer.toHexString(value).length() <= 1) {
                    str += "0" + Integer.toHexString(value) + "0" + Integer.toHexString(value);
                } else {
                    str += Integer.toHexString(value) + Integer.toHexString(value);
                }
                //Window.alert(str);
                String style = doc.getElementById(room).getAttribute("style");
                style = style.replaceFirst("fill:none", "fill:"+str);
                doc.getElementById(room).setAttribute("style", style);
            } else if (sum < dim /*&& dim != 0*/) {
                //System.out.println("la dim e maggiore");
                String str = "#";
                //Integer value = new Integer (((sum*200)/dim));

                Double value1 = ((double) sum / dim) * 200;
                Integer value = value1.intValue();

                //varia da 0 a 200

                if (Integer.toHexString(value).length() <= 1) {
                    str += "0" + Integer.toHexString(value) + "0" + Integer.toHexString(value);
                } else {
                    str += Integer.toHexString(value) + Integer.toHexString(value);
                }
                str += "FF";
                String style = doc.getElementById(room).getAttribute("style");
                style = style.replaceFirst("fill:none", "fill:" + str);
                doc.getElementById(room).setAttribute("style", style);

            }


        }


        File file;
        String pngPathOut = servletContext.getRealPath(tmpPath);
        file = new File(pngPathOut+"/"+fileName);

        try {

            transcode(file, doc);

        } catch (Exception c) {
            c.printStackTrace();
        }


    }



    public void transcode(File file, Document doc)throws Exception{
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, (float) 1280);
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, (float) 720);
        OutputStream ostream = new FileOutputStream(file);

        TranscoderInput input = new TranscoderInput(doc);
        TranscoderOutput output = new TranscoderOutput(ostream);
        System.out.println("AllocatedMemory: \t" + (Runtime.getRuntime().totalMemory() / 1024) + " Kb");
        transcoder.transcode(input, output);
        System.out.println("AllocatedMemory: \t" + (Runtime.getRuntime().totalMemory() / 1024) + " Kb");
        ostream.flush();
        ostream.close();

    }



    public void modeThree(ArrayList<String> strings, ArrayList<OccupyDTO> occupyDTOs,Document doc,String fileName){
        DatabaseM db = new DatabaseM();


        File file;
        String pngPathOut = servletContext.getRealPath(tmpPath);
        file = new File(pngPathOut+"/"+fileName);


        for (String room : strings) {

            String numRoom = room.substring(room.lastIndexOf("-")+1);
            String buildFLoor = room.substring(0, room.lastIndexOf("-"));

            ArrayList<Person> persons = db.getPeopleInRoomNotDTO(buildFLoor.substring(0,buildFLoor.lastIndexOf("-")),buildFLoor.substring(buildFLoor.lastIndexOf("-")+1),numRoom);
            for(Person person :persons){
                if(person.getEndWork()!= null){

                    if(person.getEndWork().before(new Date(new java.util.Date().getTime()))){

                        String style = doc.getElementById(room).getAttribute("style");
                        style = style.replaceFirst("fill:none", Global.RED_FILL);
                        doc.getElementById(room).setAttribute("style", style);

                    }



                }

            }


            /*

            for (OccupyDTO occupyDTO : occupyDTOs) {
                String s = "";
                s += occupyDTO.getRoom().getBuilding().getName() + "-" + Integer.toString(occupyDTO.getRoom().getFloor()) +
                        "-" + Integer.toString(occupyDTO.getRoom().getNumber());
                if (room.equals(s)) {
                    if(occupyDTO.getPerson().getEndWork() != null){
                        if(occupyDTO.getPerson().getEndWork().before(new Date(new java.util.Date().getTime()))){
                            String style = doc.getElementById(room).getAttribute("style");
                            style = style.replaceFirst("fill:none", Global.RED_FILL);
                            doc.getElementById(room).setAttribute("style", style);

                        }
                    }




                }

            }*/


        }

        try {
            transcode(file,doc);



        } catch (Exception c) {
            c.printStackTrace();
        }

    }



    public ArrayList<String> listRoomsOfFloor(String text){
        String fullPath = servletContext.getRealPath(path);

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
                   // System.out.println(((Element) p.item(i)).getAttribute("id"));
                    room.add(((Element) p.item(i)).getAttribute("id"));

                    counter ++;
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

