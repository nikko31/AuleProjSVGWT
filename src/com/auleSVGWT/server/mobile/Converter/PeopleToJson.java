package com.auleSVGWT.server.mobile.Converter;

import com.auleSVGWT.server.domain.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;


public class PeopleToJson implements JsonConverter<ArrayList<Person>,JSONArray> {
    @Override
    public JSONArray convert(ArrayList<Person> people) {
        JSONArray arrayPeopleJson = new JSONArray();

        if(people.size()>0){

            for(Person person : people ){
                JSONObject obj;
                PersonToJson personToJson = new PersonToJson();
                obj = personToJson.convert(person);
                arrayPeopleJson.add(obj);
            }

            return arrayPeopleJson;

        }else{
            return null;
        }
        }
}
