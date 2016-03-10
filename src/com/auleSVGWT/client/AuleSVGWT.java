package com.auleSVGWT.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class AuleSVGWT implements EntryPoint, HandlerInterface {
    private AuleSVGWT main;
    private VerticalPanel mainPnl;
    private VerticalPanel dataPnl;
    private ListBox piani;
    private ListBox edifici;
    private AulaCtrl aulaCtrl;
    private Label infoLbl;
    private AuleSVGWTServiceAsync auleSVGWTSvc = GWT.create(AuleSVGWTService.class);
    private ArrayList<Stanza> stanze;

    /**
     * dataPnl is the entry point method.
     */
    public void onModuleLoad() {
        stanze = new ArrayList<>();
        main = this;
        dataPnl = new VerticalPanel();//contiene la celltable e varie labels con le info della stanza
        CellTable occupationTbl = new CellTable();
        infoLbl = new Label();
        mainPnl = new VerticalPanel();
        Button invio = new Button("invio");
        piani = new ListBox();
        edifici = new ListBox();
        ListBox mode = new ListBox();
        //piani.addItem("1");
        //piani.addItem("2");
        //edifici.addItem("fac-0");
        //edifici.addItem("fac-1");
        itemEdiPiani();
        mode.addItem("mappa 1");
        mode.addItem("mappa 2");
        invio.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get("room").clear();
                aulaCtrl = new AulaCtrl(edifici.getSelectedItemText(), piani.getSelectedItemText());
                aulaCtrl.setSize("1500", "1000");
                aulaCtrl.registerEventHeader(main);
                mainPnl.clear();
                mainPnl.add(aulaCtrl);
                mainPnl.setSize("1500", "500");

                if (auleSVGWTSvc == null) {
                    auleSVGWTSvc = GWT.create(AuleSVGWTService.class);
                }
                AsyncCallback<ArrayList<Stanza>> callback = new AsyncCallback<ArrayList<Stanza>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(ArrayList<Stanza> result) {
                        stanze = result;
                    }
                };
                auleSVGWTSvc.getMessage(edifici.getSelectedItemText(), piani.getSelectedItemText(), callback);

                RootPanel.get("room").add(mainPnl);
            }
        });
        RootPanel.get("slot1").add(edifici);
        RootPanel.get("slot2").add(piani);
        RootPanel.get("slot3").add(mode);
        RootPanel.get("slot4").add(invio);
    }


    @Override
    public void mouseDownHandler(String stanzaN) {
        int numero = Resources.SVG_ID_MAP.get(stanzaN) + 1;
        for (Stanza stanza : stanze) {
            if (stanza.getNumero() == numero) {
               // AuleTabella tabella = new AuleTabella(stanza);
                //tabella.setVisible(true);
                dataPnl.clear();
                //dataPnl.add(tabella);
                dataPnl.add(new AulaTab(stanza));
            }
        }
        dataPnl.setVisible(true);
        mainPnl.add(dataPnl);

    }


    public void itemEdiPiani (){

        if (auleSVGWTSvc == null) {
            auleSVGWTSvc = GWT.create(AuleSVGWTService.class);
        }
        AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                int index;


                for(String stringa : result){
                    index = 0;

                    for(int i=0; i < stringa.length(); i++){
                        if(stringa.charAt(i) == '-'){
                            index = i;
                        }

                    }

                    if(index != 3 && index!=0){
                        edifici.addItem(stringa.substring(0, index));
                        piani.addItem(stringa.substring(index+1, stringa.indexOf('.')));
                    }
                }

            }
        };
        auleSVGWTSvc.listaEdiPiani(callback);

    }


}
