package com.auleSVGWT.server;

import com.auleSVGWT.client.dto.RoomDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Utente on 04/08/2016.
 */
public class AndroidStart extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String servlet;
        servlet = req.getServletPath();
        System.out.println(servlet+".........");


        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        JSONObject obj;

        obj = new JSONObject();
        obj.put("image",servlet+"/immagine");
        obj.put("listRoomsFloor",servlet+"/listaStanzePiano");
        obj.put("people",servlet+"/persone");
        obj.put("rooms",servlet+"/stanze");
        obj.put("listBuildings",servlet+"/listaEdifici");

        out.print(obj.toString());
        out.close();





    }






}
