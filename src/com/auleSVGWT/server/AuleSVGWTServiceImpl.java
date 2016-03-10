package com.auleSVGWT.server;

import com.auleSVGWT.client.AuleSVGWTService;
import com.auleSVGWT.client.Persona;
import com.auleSVGWT.client.Stanza;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class AuleSVGWTServiceImpl extends RemoteServiceServlet implements AuleSVGWTService {
    // Implementation of sample interface method
    public ArrayList<Stanza> getMessage(String edificio, String piano) {
        Connection connection = null;
        ArrayList<Stanza> stanze = new ArrayList<>();
        try {
            // create a database connection
            //connection = DriverManager.getConnection("jdbc:sqlite:/home/darklinux/IdeaProjects/aulesvgwt/AuleProjSVGWT/war/aule1.db");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Federico\\IdeaProjects\\aulesvgw\\AuleProjSVGWT\\war\\aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT stanza.id as idStanza,stanza.numero,persona.id,stanza.num_max_persone,stanza.dimensione,persona.nome,persona.cognome,persona.ruolo\n" +
                            "FROM edificio,stanza,persona\n" +
                            "WHERE edificio.id=stanza.edificio and stanza.id=persona.stanza \n" +
                            "and edificio.nome='" + edificio + "' and  stanza.piano='" + piano + "'\n" +
                            "ORDER BY stanza.id"
            );

            while (rs.next()) {
                // read the result set
                Integer stanza_num = rs.getInt("numero");
                int find = -1;
                //controllo se esiste la stanza nel mio array
                for (int c = 0; c < stanze.size(); c++) {
                    if (stanza_num.equals(stanze.get(c).getNumero())) {
                        Stanza stanza = stanze.get(c);
                        stanza.addProf(new Persona(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("ruolo")));
                        stanze.set(c, stanza);
                        find = c;
                        break;
                    }
                }
                //non trovata
                if (find < 0) {
                    Stanza stanza = new Stanza(rs.getInt("idStanza"), rs.getInt("numero"), rs.getInt("num_max_persone"), rs.getInt("dimensione"));
                    stanza.addProf(new Persona(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"), rs.getString("ruolo")));
                    stanze.add(stanza);
                }


            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return stanze;
    }

    public ArrayList<String> getRoles() {
        Connection connection = null;
        ArrayList<String> roles = new ArrayList<>();
        try {
            // create a database connection
            //connection = DriverManager.getConnection("jdbc:sqlite:/home/darklinux/IdeaProjects/aulesvgwt/AuleProjSVGWT/war/aule1.db");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Federico\\IdeaProjects\\aulesvgw\\AuleProjSVGWT\\war\\aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("SELECT ruolo.nome\n" +
                            "FROM ruolo"
            );
            while (rs.next())
                roles.add(rs.getString("nome"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        return roles;
    }

    public void addPersona(Persona persona){
        Connection connection = null;
        try {
            // create a database connection
            //connection = DriverManager.getConnection("jdbc:sqlite:/home/darklinux/IdeaProjects/aulesvgwt/AuleProjSVGWT/war/aule1.db");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Federico\\IdeaProjects\\aulesvgw\\AuleProjSVGWT\\war\\aule1.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery(
                    "INSERT INTO persona (nome,cognome,ruolo)" +
                            "VALUES ('"+persona.getNome()+"','"+persona.getCognome()+"','"+persona.getRuolo()+"')"
            );
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }




    public ArrayList<String>  listaEdiPiani (){
        File folder = new File("C:\\Users\\Federico\\IdeaProjects\\aulesvgw\\AuleProjSVGWT\\war\\res");
        File[] listOfFiles =folder.listFiles();
        ArrayList<String> string = new ArrayList<String>();
        for(File file :listOfFiles){
            string.add(file.getName());
            System.out.println(file.getName());
        }

        return string;


    }
}