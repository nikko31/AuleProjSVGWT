package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.*;

import com.auleSVGWT.client.shared.Global;
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
import java.io.*;
import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;



public class AndroidImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String addImageAndroid="/res/imageAndroid";
    private static final String addImageTMPPNG="/res/tmpPNG";

    public void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        String uri = request.getRequestURI();
        String servlet = request.getServletPath();
        uri =  java.net.URLDecoder.decode(uri,"UTF-8");


        resp.setContentType("image/png");
        OutputStream out = resp.getOutputStream();

        try{
            if(ControlPath(servlet,uri,out)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                out.close();
            }
        }catch(Exception e){
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.close();
        }







    }

    private Boolean ControlPath(String servlet,String uri ,OutputStream out)throws Exception{
        ServletContext context = getServletContext();
        String imagePath = context.getRealPath(addImageAndroid);
        File folder = new File(imagePath);
        File[] listOfFiles = folder.listFiles();


        if(listOfFiles != null){
            for (File file : listOfFiles) {
                String filename = file.getName();
                filename = filename.substring(0,filename.lastIndexOf("."));
                String s= file.getName();
                s=servlet+"/"+s.substring(0,s.lastIndexOf("."));
                System.out.println(""+filename+"........"+uri);


                if(uri.equals(servlet+"/lavoro/"+filename+".png")){

                    Convert(out,filename,"3","");
                    return false;

                }
                if(uri.equals(servlet+"/occupazione/"+filename+".png")){

                    Convert(out,filename,"2","");
                    return false;

                }

                if(uri.contains(servlet + "/" + filename)){
                    if(uri.equals(servlet+"/"+filename+".png")){
                        System.out.println("entro in 0");
                        Convert(out,filename,"0","");
                        return false;

                    }


                    for(String room :listRoomsOfFloor(filename)){
                        System.out.println(""+s+"/room........"+uri);
                        if(uri.equals(s+"/"+room.substring(room.lastIndexOf("-")+1)+".png")){

                            Convert(out,filename,"1",room);
                            return false;

                        }
                    }
                }
            }
        }



        return true;


    }




    public void Convert(OutputStream out,String name,String mod,String idRoom) throws IOException {


        Boolean fileExistance = true;
        String image="";
        File f;

        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, (float) 1280);
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, (float) 720);


        try{
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath(addImageAndroid);
            f = (new File(fullPath+"/"+name+".svg"));
            if(!f.exists()){
                fileExistance = false;
            }else{
                System.out.println("il file esiste");
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
                    System.out.println("get occupy of floor"+ build +" "+floor);
                    occupyDTOs = db.getOccupyOfFloor(build,floor);




                    if("2".equals(mod)){
                        System.out.println("sono nell mod 2");
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
        /*File file;
        InputStream imageIn;
        ServletContext context = getServletContext();
        String Path = context.getRealPath(addImageTMPPNG);
        String name= String.valueOf((int)(Math.random()*(1000)));
        name += "tmp.png";

        while(new File(Path+"/"+name).exists()){

            name= String.valueOf((int)(Math.random()*(1000)));
            name += "tmp.png";

        }*/


        try{

            //OutputStream ostream = new FileOutputStream(Path+"/"+name);

            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput(out);
            //System.out.println("vediamo cosa passa    " + input.getURI());
            System.out.println("AllocatedMemory: \t" + (Runtime.getRuntime().totalMemory() / 1024) + " Kb");
            transcoder.transcode(input, output);
            System.out.println("AllocatedMemory: \t" + (Runtime.getRuntime().totalMemory() / 1024) + " Kb");
            out.flush();
            out.close();

        }catch(TranscoderException | IOException c){
            c.printStackTrace();
        }
        /*

        try {
            file = new File(Path+"/"+name);
            imageIn = new FileInputStream(file);
            byte[] buffer = new byte[20];
            for (int length = 0; (length = imageIn.read(buffer)) > 0;) {
                out.write(buffer, 0, length);
            }

            imageIn.close();
            if(file.delete()){
                System.out.println("file"+name+ " eliminato ");
            }

            out.flush();
            out.close();

        } catch (Exception e){
            e.printStackTrace();

        }*/


    }

    public void modalityOne(OutputStream out,Document doc,String id,PNGTranscoder transcoder){


        try{
            String style = doc.getElementById(id).getAttribute("style");
            style = style.replaceFirst("fill:none", Global.GREEN_FILL);
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

                //System.out.println(""+room +"  ===  "+ s);

                if (room.equals(s)) {
                    //System.out.println("\n sono uguali");
                    sum += occupyDTO.getPerson().getRole().getSqm();

                    if(occupyDTO.getRoom().getDimension()!=0 && first) {
                        //System.out.println("\n dim=" + occupyDTO.getRoom().getDimension());
                        DatabaseM db = new DatabaseM();
                        dim = occupyDTO.getRoom().getDimension();
                        first = false;
                    }




                }
                System.out.println(sum +"  dim "+ dim  +room+" .....  "+s);
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
                                style = style.replaceFirst("fill:none", Global.RED_FILL);
                                doc.getElementById(room).setAttribute("style", style);

                            }
                    }




                }

            }


        }


        try{

            //System.out.println("" + (new File("C:\\Users\\Utente\\IdeaProject\\esperimenti_vari\\sr);
            TranscoderInput input = new TranscoderInput(doc);
            //FileOutputStream ostream = new FileOutputStream("C:\\Users\\Utente\\IdeaProject\\esperimenti_vari\\src\\");
            TranscoderOutput output = new TranscoderOutput(out);
            //System.out.println("vediamo cosa passa    " + input.getURI());
            transcoder.transcode(input, output);
            out.flush();
            out.close();

        }catch(TranscoderException | IOException c){
            c.printStackTrace();
        }

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
