package com.auleSVGWT.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * Created by darklinux on 09/03/16.
 */
public class AulaTab extends Composite {
    interface AulaTabUiBinder extends UiBinder<Widget, AulaTab> {
    }
    @UiField
    Button addButton;
    @UiField
    FlexTable table;
    @UiField
    Label numAula;
    @UiField
    Label numPosti;
    @UiField
    Label mtQ;
    private Stanza stanzak;

    private static AulaTabUiBinder ourUiBinder = GWT.create(AulaTabUiBinder.class);

    public AulaTab(Stanza stanza) {
        initWidget(ourUiBinder.createAndBindUi(this));
        table.setText(0, 0, "NOME");
        table.setText(0,1,"COGNOME");
        table.setText(0,2,"RUOLO");
        numAula.setText(stanza.getNumero().toString());
        numPosti.setText(stanza.getNum_max_posti().toString());
        mtQ.setText(stanza.getMtq().toString());
        setProf(stanza.getProf());


    }
    private void setProf(ArrayList<Persona> profArr){
        int c=1;
        for(Persona prof:profArr){
            table.setText(c,0,prof.getNome());
            table.setText(c,1,prof.getCognome());
            table.setText(c,2,prof.getRuolo());
            c++;
        }
    }


}