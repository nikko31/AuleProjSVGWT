package com.auleSVGWT.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

public class AuleTabella extends VerticalPanel {
    private FlexTable ocpTabale;
    private FlexTable carAula;
    private HorizontalPanel zeroPnl;
    //private HorizontalPanel firstPnl;
    //private HorizontalPanel secondPnl;
    //private HorizontalPanel thirdPnl;
    private HorizontalPanel buttonPnl;
    private Label massimoPosti;
    private Label nomeAula;
    private Label metriQ;
    private TextBox prima;
    private TextBox seconda;
    private TextBox terza;
    private TextBox generale;
    private TextBox addName;
    private TextBox addSurname;
    private TextBox addMeter;
    private ListBox ruoli;
    private Stanza stanCor;//stanzacorrente
    private int id;
    private Integer index;
    private AuleSVGWTServiceAsync auleSVGWTSvc = GWT.create(AuleSVGWTService.class);

    public AuleTabella(Stanza stanza) {
        this.stanCor = stanza;
        tabellaAula(stanza);
    }

    public void tabellaAula(Stanza stanza) {
        ocpTabale = new FlexTable();
        carAula = new FlexTable();
        zeroPnl = new HorizontalPanel();
        //firstPnl = new HorizontalPanel();
        //secondPnl = new HorizontalPanel();
        //thirdPnl = new HorizontalPanel();
        buttonPnl = new HorizontalPanel();
        massimoPosti = new Label("Numenro massimo posti:");
        nomeAula = new Label("numero aula:");
        metriQ = new Label("metri quadrati:");
        Label posti = new Label(stanCor.getNum_max_posti().toString());
        Label aula = new Label(stanCor.getNumero().toString());
        Label mQ = new Label(stanCor.getMtq().toString());
        Button edit = new Button("edit");

        //firstPnl.add(massimoPosti);
        //firstPnl.add(posti);
        //secondPnl.add(nomeAula);
        //secondPnl.add(aula);
        //thirdPnl.add(metriQ);
        //thirdPnl.add(mQ);

        carAula.setText(0, 0, "Numero Aula");
        carAula.setText(1, 0, "Numero posti");
        carAula.setText(2, 0, "Metri quadrati");
        carAula.setText(0,1,stanCor.getNumero().toString());
        carAula.setText(1,1,stanCor.getNum_max_posti().toString());
        carAula.setText(2,1,stanCor.getMtq().toString());

        zeroPnl.add(carAula);




        ocpTabale.setText(0, 0, "Nome");
        ocpTabale.setText(0, 1, "Cognome");
        ocpTabale.setText(0, 2, "Ruolo");
        ocpTabale.setText(0, 3, "ID");

        int i = 1;
        for (Persona prof : stanza.getProf()) {

            ocpTabale.setText(i, 0, prof.getNome());
            ocpTabale.setText(i, 1, prof.getCognome());
            ocpTabale.setText(i, 2, prof.getRuolo());
            ocpTabale.setText(i, 3, prof.getId().toString());
            i++;


        }

        edit.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                modificaTabellaAula();
            }
        });
        buttonPnl.add(edit);

        this.add(zeroPnl);
        //this.add(firstPnl);
        //this.add(secondPnl);
        //this.add(thirdPnl);
        this.add(ocpTabale);
        this.add(buttonPnl);

    }

    public void modificaTabellaAula() {
        this.clear();
        zeroPnl.clear();
        //firstPnl.clear();
        //secondPnl.clear();
        //thirdPnl.clear();
        ocpTabale.clear();
        buttonPnl.clear();

        ocpTabale.removeAllRows();

        Button end = new Button("fine");
        prima = new TextBox();
        seconda = new TextBox();
        terza = new TextBox();
        generale = new TextBox();

        prima.setText(stanCor.getNum_max_posti().toString());
        seconda.setText(stanCor.getNumero().toString());
        terza.setText(stanCor.getMtq().toString());


        carAula.setWidget(0, 1, seconda);
        carAula.setWidget(1, 1, prima);
        carAula.setWidget(2, 1, terza);

        zeroPnl.add(carAula);

        //firstPnl.add(massimoPosti);
        //firstPnl.add(prima);
        //secondPnl.add(nomeAula);
        //secondPnl.add(seconda);
        //thirdPnl.add(metriQ);
        //thirdPnl.add(terza);


        ocpTabale.setText(0, 0, "nome");
        ocpTabale.setText(0, 1, "cognome");
        ocpTabale.setText(0, 2, "ruolo");
        ocpTabale.setText(0, 3, "ID");
        ocpTabale.setText(0, 4, "modifica");
        ocpTabale.setText(0, 5, "elimina");

        int j = 1;
        for (Persona prof : stanCor.getProf()) {
            ocpTabale.setText(j, 0, prof.getNome());
            ocpTabale.setText(j, 1, prof.getCognome());
            ocpTabale.setText(j, 2, prof.getRuolo());
            ocpTabale.setText(j, 3, prof.getId().toString());
            j++;
        }
        ocpTabale.setText(ocpTabale.getRowCount(), 0, "");
        ocpTabale.setText(ocpTabale.getRowCount() - 1, 1, "");
        ocpTabale.setText(ocpTabale.getRowCount() - 1, 2, "");
        ocpTabale.setText(ocpTabale.getRowCount() - 1, 3, "");

        for (int i = 0; i < (ocpTabale.getRowCount() - 1); i++) {
            //magari metteremo delle icone al posto dei bottoni
            Button change = new Button("X");
            Button delete = new Button("X");
            change.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    for (int i = 1; i < ocpTabale.getRowCount(); i++) {
                        if (ocpTabale.getWidget(i, 4) == event.getSource()) {
                            ocpTabale.clear();
                            modificaRigaAula(i);
                        }
                    }

                }
            });

            delete.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    for (int i = 1; i < ocpTabale.getRowCount(); i++) {
                        if (ocpTabale.getWidget(i, 5) == event.getSource()) {
                            rimuoviRigaAula(i);
                        }
                    }

                }
            });
            ocpTabale.setWidget(i + 1, 4, change);
            ocpTabale.setWidget(i + 1, 5, delete);
        }

        end.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //questo comando fa tornare alla
                clear();
                zeroPnl.clear();
                //firstPnl.clear();
                //secondPnl.clear();
                //thirdPnl.clear();
                ocpTabale.clear();
                buttonPnl.clear();
                tabellaAula(stanCor);

            }
        });
        buttonPnl.add(end);

        this.add(zeroPnl);
        //this.add(firstPnl);
        //this.add(secondPnl);
        //this.add(thirdPnl);
        this.add(ocpTabale);
        this.add(buttonPnl);
        this.setVisible(true);
    }

    public void rimuoviRigaAula(int line) {
        //TODO
        //bisonga fare opportuni controlli e aggiornare il DB
        Persona nuovaPers = new Persona();
        nuovaPers.setNome(ocpTabale.getText(line, 0));
        nuovaPers.setCognome(ocpTabale.getText(line, 1));
        nuovaPers.setRuolo(ocpTabale.getText(line, 2));
        nuovaPers.setId(Integer.parseInt(ocpTabale.getText(line, 3)));
        ArrayList<Persona> pers = new ArrayList<>();
        pers.addAll(stanCor.getProf());
        int ind = -1;
        for (Persona persona : pers)
            if (persona.getId().equals(nuovaPers.getId()))
                ind = pers.indexOf(persona);

        if (ind != -1) {
            stanCor.removeProf(ind);
        } else {
            //errore
        }

        ocpTabale.removeRow(line);
    }


    public void modificaRigaAula(int line) {
        //TODO
        //bisonga fare opportuni controlli e aggiornare il DB


        //ocpTabale.clear();
        this.clear();
        zeroPnl.clear();
        //firstPnl.clear();
        //secondPnl.clear();
        //thirdPnl.clear();
        ocpTabale.clear();
        buttonPnl.clear();

        ocpTabale.removeAllRows();
        prima.setText(stanCor.getNum_max_posti().toString());
        seconda.setText(stanCor.getNumero().toString());
        terza.setText(stanCor.getMtq().toString());

        //firstPnl.add(massimoPosti);
        //firstPnl.add(prima);
        //secondPnl.add(nomeAula);
        //secondPnl.add(seconda);
        //thirdPnl.add(metriQ);
        //thirdPnl.add(terza);

        carAula.setText(0,1,stanCor.getNumero().toString());
        carAula.setText(1,1,stanCor.getNum_max_posti().toString());
        carAula.setText(2,1,stanCor.getMtq().toString());

        zeroPnl.add(carAula);

        ocpTabale.setText(0, 0, "nome");
        ocpTabale.setText(0, 1, "cognome");
        ocpTabale.setText(0, 2, "ruolo");
        ocpTabale.setText(0, 3, "ID");

        int i = 1;
        for (Persona prof : stanCor.getProf()) {

            ocpTabale.setText(i, 0, prof.getNome());
            ocpTabale.setText(i, 1, prof.getCognome());
            ocpTabale.setText(i, 2, prof.getRuolo());
            ocpTabale.setText(i, 3, prof.getId().toString());
            i++;
        }

        ocpTabale.setText(i, 0, "");
        ocpTabale.setText(i, 1, "");
        ocpTabale.setText(i, 2, "");
        ocpTabale.setText(i, 3, "");

        //per adesso cancella e rifa tutto perche la modifica dei bottoni � da vedere meglio

        addName = new TextBox();
        addSurname = new TextBox();
        addMeter = new TextBox();
        ruoli = new ListBox();
        Button save = new Button("salva");
        Button dontSave = new Button("non salvare");

        addName.setText(ocpTabale.getText(line, 0));
        addSurname.setText(ocpTabale.getText(line, 1));
        addMeter.setText(ocpTabale.getText(line, 3));


        if(line != ocpTabale.getRowCount()-1){
            id = Integer.parseInt(ocpTabale.getText(line, 3));
        }
        else{
            id = -1;
        }

        if (auleSVGWTSvc == null) {
            auleSVGWTSvc = GWT.create(AuleSVGWTService.class);
        }
        AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                for (String ruolo : result)
                    ruoli.addItem(ruolo);
            }
        };
        auleSVGWTSvc.getRoles(callback);

        ocpTabale.setWidget(line, 0, addName);
        ocpTabale.setWidget(line, 1, addSurname);
        ocpTabale.setWidget(line, 2, ruoli);
        ocpTabale.setWidget(line, 3, addMeter);

        save.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if(!addName.getText().matches("^[a-zA-Z\\.]{1,15}$")){
                    Window.alert("ATTENZIONE: NOME NON VALIDO");
                    addName.selectAll();
                    return;
                }
                if(!addSurname.getText().matches("^[a-zA-Z\\.]{1,15}$")){
                    Window.alert("ATTENZIONE: COGNOME NON VALIDO");
                    addName.selectAll();
                    return;
                }
                if(!addMeter.getText().matches("^[0-9\\.]{1,15}$")){
                    Window.alert("ATTENZIONE: ID NON VALIDO");
                    addName.selectAll();
                    return;
                }

                if (id >= 0) {
                    for (Persona per : stanCor.getProf()) {
                        if (per.getId().equals(new Integer(id))) {
                            index = stanCor.getProf().indexOf(per);
                            stanCor.removeProf(index);
                        }
                    }
                }
                //ricorda di controllare che siano numeri e stringhe accettabili
                boolean flag = true;
                Persona nuovaPers = new Persona();
                nuovaPers.setNome(addName.getText());
                nuovaPers.setCognome(addSurname.getText());
                nuovaPers.setRuolo(ruoli.getSelectedItemText());
                nuovaPers.setId(Integer.parseInt(addMeter.getText()));




                flag = true;


                for (Persona pers : stanCor.getProf()) {
                    if (pers.getId().equals(nuovaPers.getId())) {
                        flag = false;
                    }
                }
                if (flag) {
                    for (Persona pers : stanCor.getProf()) {
                        if (pers.getCognome().equals(nuovaPers.getCognome()) && pers.getNome().equals(nuovaPers.getNome())) {
                            flag = !pers.getId().equals(nuovaPers.getId());
                        }
                    }

                }

                if (flag) {

                    stanCor.addProf(nuovaPers);

                }


                modificaTabellaAula();


            }
        });
        dontSave.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //non salvo modifiche e ritorno alla tabella iniziale con stanzaCOrrente
                ocpTabale.clear();
                modificaTabellaAula();

            }
        });

        buttonPnl.add(save);
        buttonPnl.add(dontSave);
        this.add(zeroPnl);
        //this.add(firstPnl);
        //this.add(secondPnl);
        //this.add(thirdPnl);
        this.add(ocpTabale);
        this.add(buttonPnl);
        this.setVisible(true);
    }
}
