package com.auleSVGWT.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AndroidStartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AndroidStartServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        ArrayList<String> string = new ArrayList<>();

        response.setContentType("text/html");
        PrintWriter outPrinter = response.getWriter();



        try {
            ServletContext context = getServletContext();
            String fullPath = context.getRealPath("/Images");

            File folder = new File(fullPath);
            File[] listOfFiles = folder.listFiles();

            if(listOfFiles != null){
                for (File file : listOfFiles) {
                    string.add(file.getName());
                }
            }



        } catch (Exception e) {
            System.out.println("Error reading name of maps");
            e.printStackTrace();
        }


        if(string.size()>0) {
            printBuildings(outPrinter, string);

        }


    }

    private void printBuildings(PrintWriter printOut,ArrayList<String> string){

        HashMap<String, ArrayList<String>> buildings = new HashMap<>();

        for (String s : string) {

            //System.out.println(s.substring(0,s.lastIndexOf('-')));
            //System.out.println(s.substring(s.lastIndexOf('-')+1,s.lastIndexOf('.')));


            if (buildings.containsKey(s.substring(0, s.lastIndexOf('-')))) {
                ArrayList<String> floors;
                floors = buildings.get(s.substring(0, s.lastIndexOf('-')));
                floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                Collections.sort(floors);
                buildings.replace(s.substring(0, s.lastIndexOf('-')), floors);
            } else {
                ArrayList<String> floors = new ArrayList<>();
                floors.add(s.substring(s.lastIndexOf('-') + 1, s.lastIndexOf('.')));
                buildings.put(s.substring(0, s.lastIndexOf('-')), floors);
            }
        }



        JSONObject obj = createReturnJSON(buildings);


        printOut.println(obj);

    }




    private JSONObject createReturnJSON(HashMap<String, ArrayList<String>> buildings){
        int j = 0;
        JSONArray buildingFloors = new JSONArray();
        for (String buildingString : buildings.keySet()) {


            int i = 0;
            JSONObject build = new JSONObject();
            JSONArray arr = new JSONArray();


            for (String floorString : buildings.get(buildingString)) {
                arr.add(i, floorString);
                i++;
            }
            build.put("name", buildingString);

            build.put("floors", arr);
            buildingFloors.add(j, build);
            j++;
        }

        JSONObject obj = new JSONObject();
        obj.put("buildings", buildingFloors);

        return obj;

    }
}

