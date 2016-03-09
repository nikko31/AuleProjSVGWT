package com.auleSVGWT.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface AuleSVGWTServiceAsync {
    //Map<String, ArrayList<String>> getMessage(String msg, AsyncCallback<String> async);

    // Sample interface method of remote interface
    void getMessage(String ed, String aula, AsyncCallback<ArrayList<Stanza>> async);
    void getRoles(AsyncCallback<ArrayList<String>> async);
    void addPersona(Persona persona, AsyncCallback<Void> async);
}
