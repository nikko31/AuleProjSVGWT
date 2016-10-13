package com.auleSVGWT.server.mobile;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/persone")
public class JaxRestPeople {
    private DatabaseM db;
    @Context
    private ServletContext servletContext;


    @GET
    public Response getAllPeople(@QueryParam("number") int number, @QueryParam("search") String search){
        System.out.println("il valore di number è"+ number);



        if(number!=0 && search!=null){
            String result=null;
            try{
                result=java.net.URLDecoder.decode(search, "UTF-8");
            }catch(Exception e){
                e.printStackTrace();
            }

            System.out.println("ottengo :"+result);

            result= search.replace('_',' ');
            if(controlOfLettersandSpaces(result)){
                db= new DatabaseM();
                JSONArray arr = db.getPersonSearchJson(number,result);
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
        System.out.println("persona "+person);

        try {

            String per = person.replace('_',' ');

            //System.out.println("persona...."+ per+"  "+person);
            if(controlOfLettersandSpaceOnly(per)){
                System.out.println("passato controllo");
                db= new DatabaseM();

                String[] persSplit= per.split(" ");
                System.out.println(persSplit.length+"lunghezza");
                JSONArray arr= null;

                if(persSplit.length==2){
                   arr = db.getPersonJson(persSplit[0], persSplit[1]);
                }
                if(persSplit.length==3){
                    arr = db.getPersonJson(persSplit[0]+" "+persSplit[1], persSplit[2]);
                    if(arr==null){
                        arr = db.getPersonJson(persSplit[0],persSplit[1]+" "+persSplit[2]);
                    }
                    if(arr==null){
                        arr = db.getPersonJson(persSplit[0],persSplit[2]+" "+persSplit[1]);
                    }
                    if(arr==null){
                        arr = db.getPersonJson(persSplit[1]+" "+persSplit[0], persSplit[2]);
                    }

                }
                if(persSplit.length==4){
                    arr=db.getPersonJson(persSplit[0] + " " + persSplit[1], persSplit[2]+" "+ persSplit[3]);
                    if(arr==null){
                        arr=db.getPersonJson(persSplit[0] + " " + persSplit[1], persSplit[3]+" "+ persSplit[2]);
                    }
                    if(arr==null){
                        arr=db.getPersonJson(persSplit[1] + " " + persSplit[0], persSplit[2]+" "+ persSplit[3]);
                    }
                    if(arr==null){
                        arr=db.getPersonJson(persSplit[1] + " " + persSplit[0], persSplit[3]+" "+ persSplit[2]);
                    }
                    if(arr==null){
                        arr=db.getPersonJson(persSplit[0] ,persSplit[1]+ " " +  persSplit[2]+" "+ persSplit[3]);
                    }
                    if(arr==null){
                        arr=db.getPersonJson(persSplit[0]+" "+persSplit[1]+ " " +  persSplit[2], persSplit[3]);
                    }
                }

                //JSONObject obj = db.getPersonJson(per.substring(0, per.lastIndexOf(' ')), per.substring(per.lastIndexOf(' ') + 1));
                if(arr != null){
                    String json = arr.toString();
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

                db = new DatabaseM();
                String[] persSplit= per.split(" ");
                System.out.println(persSplit.length+"lunghezza");
                JSONArray arr= null;

                if(persSplit.length==2){
                    arr = db.getOccupedRoomOfPersonJson(persSplit[0], persSplit[1]);
                }
                if(persSplit.length==3){
                    arr = db.getOccupedRoomOfPersonJson(persSplit[0] + " " + persSplit[1], persSplit[2]);
                    if(arr==null){
                        arr = db.getOccupedRoomOfPersonJson(persSplit[0], persSplit[1] + " " + persSplit[2]);
                    }
                    if(arr==null){
                        arr = db.getOccupedRoomOfPersonJson(persSplit[0], persSplit[2] + " " + persSplit[1]);
                    }
                    if(arr==null){
                        arr = db.getOccupedRoomOfPersonJson(persSplit[1] + " " + persSplit[0], persSplit[2]);
                    }

                }
                if(persSplit.length==4){
                    arr=db.getOccupedRoomOfPersonJson(persSplit[0] + " " + persSplit[1], persSplit[2] + " " + persSplit[3]);
                    if(arr==null){
                        arr=db.getOccupedRoomOfPersonJson(persSplit[0] + " " + persSplit[1], persSplit[3] + " " + persSplit[2]);
                    }
                    if(arr==null){
                        arr=db.getOccupedRoomOfPersonJson(persSplit[1] + " " + persSplit[0], persSplit[2] + " " + persSplit[3]);
                    }
                    if(arr==null){
                        arr=db.getOccupedRoomOfPersonJson(persSplit[1] + " " + persSplit[0], persSplit[3] + " " + persSplit[2]);
                    }
                    if(arr==null){
                        arr=db.getOccupedRoomOfPersonJson(persSplit[0], persSplit[1] + " " + persSplit[2] + " " + persSplit[3]);
                    }
                    if(arr==null){
                        arr=db.getOccupedRoomOfPersonJson(persSplit[0]+" "+persSplit[1]+ " " +  persSplit[2], persSplit[3]);
                    }
                }
                if(arr != null){
                    String json = arr.toString();
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }else{
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

            }else if(controlDigitOnly(person)){
                db = new DatabaseM();
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



    private boolean controlOfLettersandSpaceOnly(String s) {
        System.out.println("control letter space only");

        int counter =0;

        for(int i = 0; i < s.length(); i++) {

            if(s.charAt(i)==' '){
                counter ++;
            }
            if (!Character.isLetter(s.charAt(i)) && s.charAt(i)!=' ' && s.charAt(i)!='`' && s.charAt(i)!='\''){
                //System.out.println(s.charAt(i)+" == non lo so");
                System.out.println("controlOfLettersandSpaceOnly non superato");
                return false;

            }
        }

        return counter <= 3 && counter >= 1;



    }
    private boolean controlOfLettersandSpaces(String s) {
        System.out.println("control letter spaces");

        for(int i = 0; i < s.length(); i++) {
            if (!Character.isLetter(s.charAt(i)) && s.charAt(i)!=' ' && s.charAt(i)!='`' && s.charAt(i)!='\''){
                System.out.println("controlOfLettersandSpaces non superato");
                return false;
            }
        }

       return true;

    }



    private boolean controlDigitOnly(String s) {
        System.out.println("control digit only");


        for(int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))){
                System.out.println("controlDigitOnly non superato");
                return false;
            }
        }

        return true;

    }

}
