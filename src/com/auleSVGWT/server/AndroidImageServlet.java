package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.*;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
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
import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;


public class AndroidImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String buildingName;
        String modality;
        String numRoom ;

        resp.setContentType("image/png");
        OutputStream out = resp.getOutputStream();



        buildingName = req.getParameter("name");
        modality = req.getParameter("mod");
        numRoom = req.getParameter("numRoom");

        if(convertionParameter(buildingName,modality,numRoom)){
            String roomID = buildingName+"-"+numRoom;
            Convert(out,buildingName,modality,roomID);
        }
        out.close();





        //File file = new File("C:\\Users\\Utente\\Desktop\\provaAule\\AuleProjSVGWT\\war\\Images\\");
        //resp.setContentLength((int)file.length());

        //FileInputStream in = new FileInputStream(file);


        // Copy the contents of the file to the output stream
        /*
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }*/

        //in.close();


    }



    private Boolean convertionParameter(String name,String mod,String numRoom){
        Boolean controlFlag = true;

        if((name == null) || (mod == null)){
            controlFlag = false;
        }

        if(!("0".equals(mod)) && !("1".equals(mod)) && !("2".equals(mod)) && !("3".equals(mod)) ){
            controlFlag = false;
        }

        if("1".equals(mod)){
            if(numRoom == null){
                controlFlag = false;

            }else{
                if(numRoom.length()>10){
                    controlFlag = false;
                }
            }


        }


        return controlFlag;

    }


    public void Convert(OutputStream out,String name,String mod,String idRoom) throws IOException {


        Boolean fileExistance = true;
        String image="";
        File f;

        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, (float) 1920);
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, (float) 1080);


        try{
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/Images");
            f = (new File(fullPath+"/"+name+".svg"));
            if(!f.exists()){
                fileExistance = false;
            }else{
                image= f.toURI().toString();
            }


        }catch (Exception e){
            System.out.println("non esiste il file cercato");

        }

        if(fileExistance){
            try {

                SVGMetaPost converter = new SVGMetaPost(image);
                Document doc = converter.getSVGDocument();
                if("0".equals(mod)){
                    modalityZero(out,doc,transcoder);

                }
                if("1".equals(mod)){
                    modalityOne(out,doc,idRoom,transcoder);
                }
                else{

                    ArrayList<OccupyDTO> occupyDTOs;
                    ArrayList<String> room;
                    String build;
                    String floor;
                    DatabaseM db = new DatabaseM();


                    build = name.substring(0, name.lastIndexOf("-"));
                    floor = name.substring(name.lastIndexOf("-") + 1);

                    room = listRoomsOfFloor(name);
                    occupyDTOs = db.getOccupyOfFloor(build,floor);




                    if("2".equals(mod)){
                        modalityTwo(room, occupyDTOs, doc, out, transcoder);
                    }
                    else if("3".equals(mod)){
                        modalityThree(room, occupyDTOs, doc, out, transcoder);
                    }

                }




            } catch (IOException v) {
                v.printStackTrace();
            }

        }


    }

    public void modalityZero(OutputStream out,Document doc,PNGTranscoder transcoder){


        try{

            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput(out);
            //System.out.println("vediamo cosa passa    " + input.getURI());
            transcoder.transcode(input, output);
            out.flush();
            out.close();

        }catch(TranscoderException | IOException c){
            c.printStackTrace();
        }


    }

    public void modalityOne(OutputStream out,Document doc,String id,PNGTranscoder transcoder){


        try{
            String style = doc.getElementById(id).getAttribute("style");
            style = style.replaceFirst("fill:none","fill:green");
            doc.getElementById(id).setAttribute("style", style);


            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput(out);
            //System.out.println("vediamo cosa passa    " + input.getURI());
            transcoder.transcode(input, output);
            out.flush();
            out.close();

        }catch(TranscoderException | IOException c){
            c.printStackTrace();
        }

    }


    public void modalityTwo(ArrayList<String> strings, ArrayList<OccupyDTO> occupyDTOs,Document doc,OutputStream out,PNGTranscoder transcoder){

        int sum;
        int dim;


        for (String room : strings) {
            sum = 0;
            dim = 0;
            Boolean first = true;
            for (OccupyDTO occupyDTO : occupyDTOs) {
                String s = "";
                s += occupyDTO.getRoom().getBuilding().getName() + "-" + Integer.toString(occupyDTO.getRoom().getFloor()) +
                        "-" + Integer.toString(occupyDTO.getRoom().getNumber());

                System.out.println(""+room +"  ===  "+ s);

                if (room.equals(s)) {
                    System.out.println("\n sono uguali");
                    sum += occupyDTO.getPerson().getRole().getSqm();

                    if(occupyDTO.getRoom().getDimension()!=0 && first) {
                        System.out.println("\n dim=" + occupyDTO.getRoom().getDimension());
                        dim = occupyDTO.getRoom().getDimension();
                        first = false;
                    }




                }

            }
            if (dim == 0 || sum == 0) {
                String style = doc.getElementById(room).getAttribute("style");
                style = style.replaceFirst("fill:none", "fill:grey");
                doc.getElementById(room).setAttribute("style", style);

            } else if (sum == dim /*&& dim != 0*/) {
                String style = doc.getElementById(room).getAttribute("style");
                style = style.replaceFirst("fill:none", "fill:green");
                doc.getElementById(room).setAttribute("style", style);
            } else if (sum > dim /*&& dim != 0*/) {
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
            } else {
                if (sum < dim /*&& dim != 0*/) {
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


        }

        try{
            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput(out);
            //System.out.println("vediamo cosa passa    " + input.getURI());
            transcoder.transcode(input, output);
            out.flush();
            out.close();

        }catch(TranscoderException | IOException c){
            c.printStackTrace();
        }


    }



    public void modalityThree(ArrayList<String> strings, ArrayList<OccupyDTO> occupyDTOs,Document doc,OutputStream out,PNGTranscoder transcoder){

        for (String room : strings) {
            for (OccupyDTO occupyDTO : occupyDTOs) {
                String s = "";
                s += occupyDTO.getRoom().getBuilding().getName() + "-" + Integer.toString(occupyDTO.getRoom().getFloor()) +
                        "-" + Integer.toString(occupyDTO.getRoom().getNumber());
                if (room.equals(s)) {
                    if(occupyDTO.getPerson().getEndWork() != null){
                            if(occupyDTO.getPerson().getEndWork().before(new Date(new java.util.Date().getTime()))){
                                String style = doc.getElementById(room).getAttribute("style");
                                style = style.replaceFirst("fill:none", "fill:red");
                                doc.getElementById(room).setAttribute("style", style);

                            }
                    }




                }

            }


        }

        try{

            //System.out.println("" + (new File("C:\\Users\\Utente\\IdeaProject\\esperimenti_vari\\src\\fac-0.svg.svg")).toURI().toString());
            TranscoderInput input = new TranscoderInput(doc);
            //FileOutputStream ostream = new FileOutputStream("C:\\Users\\Utente\\IdeaProject\\esperimenti_vari\\src\\");
            TranscoderOutput output = new TranscoderOutput(out);
            System.out.println("vediamo cosa passa    " + input.getURI());
            transcoder.transcode(input, output);
            out.flush();
            out.close();

        }catch(TranscoderException | IOException c){
            c.printStackTrace();
        }

    }



    public ArrayList<String> listRoomsOfFloor(String text){
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("/imageGWT");
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
                    System.out.println(((Element) n.item(i)).getAttribute("id"));
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

            System.out.println("RISULTATO RICERCA NEL FILE " + u + " " + counter);
        }catch (IOException e){
            System.out.println("ERROr in liste aule piano new");
        }





        return room;

    }
}
