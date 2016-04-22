package com.auleSVGWT.client;

import com.auleSVGWT.server.domain.Room;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class AuleSVGWT implements EntryPoint {

    public void onModuleLoad() {

        VerticalPanel mapPnl = new VerticalPanel();
        VerticalPanel infoPnl = new VerticalPanel();
        VerticalPanel headerPnl = new VerticalPanel();
        new AppController((
                AuleSVGWTServiceAsync) GWT.create(AuleSVGWTService.class),
                new SimpleEventBus()
        ).go(mapPnl, infoPnl,headerPnl);


        RootPanel.get("header").add(headerPnl);
        RootPanel.get("room").add(mapPnl);
        RootPanel.get("info").add(infoPnl);


    }


}
