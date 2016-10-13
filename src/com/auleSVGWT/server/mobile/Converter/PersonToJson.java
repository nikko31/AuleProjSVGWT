package com.auleSVGWT.server.mobile.Converter;


import com.auleSVGWT.server.domain.Person;
import com.auleSVGWT.server.mobile.JaxRestPeople;
import org.json.simple.JSONObject;

import javax.ws.rs.core.UriBuilder;
import java.util.HashMap;

public class PersonToJson extends CommonVar implements JsonConverter<Person,JSONObject> {


    @Override
    public JSONObject convert(Person person) {
        JSONObject personJSON = new JSONObject();

        personJSON.put("id",person.getId());
        personJSON.put("name",""+ person.getName());
        personJSON.put("surname",""+ person.getSurname());
        personJSON.put("role",""+ person.getRole().getName());


        if(person.getStartWork() != null){
            String sW = person.getStartWork().toString();
            sW = sW.replaceAll("-"," ");
            personJSON.put("startWork",""+sW);
        }else{
            personJSON.put("startWork",null);

        }
        if(person.getEndWork() != null){
            String eW = person.getEndWork().toString();
            eW = eW.replaceAll("-", " ");
            personJSON.put("endWork",""+eW);


        }else{
            personJSON.put("endWork",null);

        }

        if(person.getPhone() != null){
            String phone = person.getPhone();

            personJSON.put("phone",""+phone);


        }else{
            personJSON.put("phone",null);

        }
        if(person.getEmail() != null){
            String email = person.getEmail();

            personJSON.put("email",""+email);


        }else{
            personJSON.put("email",null);

        }


        s= UriBuilder.fromResource(JaxRestPeople.class).toString();
        u =UriBuilder.fromMethod(JaxRestPeople.class, "getRoomsOfPerson");
        m = new HashMap<>();
        m.put("person", "" + person.getId());
        s+=u.buildFromEncodedMap(m).toString();
        //System.out.println("mi da ................"+s);
        //personJSON.put("infoRoomsOfPerson","/rest/persone/"+person.getId()+"/stanze");
        personJSON.put("infoRoomsOfPerson",REST+s);

        return personJSON;
    }

}
