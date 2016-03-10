package com.auleSVGWT.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("AuleSVGWTService")
public interface AuleSVGWTService extends RemoteService {
    // Sample interface method of remote interface
    ArrayList<Stanza> getMessage(String msg, String aula);
    ArrayList<String> getRoles();
    ArrayList<String> listaEdiPiani ();
    void addPersona(Persona persona);


    /**
     * Utility/Convenience class.
     * Use AuleSVGWTService.App.getInstance() to access static instance of AuleSVGWTServiceAsync
     */
    public static class App {
        private static AuleSVGWTServiceAsync ourInstance = GWT.create(AuleSVGWTService.class);

        public static synchronized AuleSVGWTServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
