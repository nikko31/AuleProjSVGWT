package com.auleSVGWT.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.http.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by darklinux on 28/02/16.
 */
public class AulaCtrl extends Composite {

    interface AulaCtrlUiBinder extends UiBinder<Widget, AulaCtrl> {
    }

    private OMSVGSVGElement roomElt;
    private List<HandlerRegistration> roomHandlers = new ArrayList<>();
    private static final String DEF_FILL = "fill:transparent";
    private static final String SEL_FILL = "fill:red";
    private HandlerInterface handlerInterface;
    @UiField
    FlowPanel display;

    @UiField
    HTML roomContainer;

    public AulaCtrl(String piano, String aula) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "res/" + piano + "-" + aula + ".svg");
        try {

            RequestCallback pendingRequest = new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                }

                public void onResponseReceived(Request request, Response response) {
                    roomElt = org.vectomatic.dom.svg.utils.OMSVGParser.parse(response.getText());
                    roomHandlers.addAll(addHandlers(Resources.SVG_ID_MAP));
                    roomContainer.getElement().appendChild(roomElt.getElement());

                    if (roomElt instanceof OMSVGSVGElement) {
                        float fWidth = roomElt.getWidth().getBaseVal().getValue();
                        float fHeight = roomElt.getHeight().getBaseVal().getValue();
                    }

                }
            };
            requestBuilder.sendRequest(null, pendingRequest);
        } catch (RequestException e) {
            Window.alert("failed file reading" + e.getMessage());
        }
        AulaCtrlUiBinder ourUiBinder = GWT.create(AulaCtrlUiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
        display.getElement().setId("container");
        roomContainer.getElement().setId("map");
        roomContainer.getElement().getStyle().setWidth(70, Style.Unit.PCT);
    }

    public void registerEventHeader(HandlerInterface handlerInterface) {
        this.handlerInterface = handlerInterface;
    }

    private List<HandlerRegistration> addHandlers(Map<String, Integer> svgIdMap) {
        List<HandlerRegistration> handlerRegistrations = new ArrayList<>();
        for (String room : svgIdMap.keySet()) {
            final OMElement territoryEl = roomElt.getElementById(room);

            HandlesAllMouseEvents han = new HandlesAllMouseEvents() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    String style = territoryEl.getAttribute("style");
                    style = style.replace(DEF_FILL, SEL_FILL);
                    territoryEl.setAttribute("style", style);
                    handlerInterface.mouseDownHandler(territoryEl.getAttribute("id"));

                }

                @Override
                public void onMouseMove(MouseMoveEvent event) {

                }

                @Override
                public void onMouseOut(MouseOutEvent event) {

                }

                @Override
                public void onMouseOver(MouseOverEvent event) {
                    String style = territoryEl.getAttribute("style");
                    style = style.replace(DEF_FILL, SEL_FILL);
                    territoryEl.setAttribute("style", style);
                    handlerInterface.mouseDownHandler(territoryEl.getAttribute("id"));

                }

                @Override
                public void onMouseUp(MouseUpEvent event) {

                }

                @Override
                public void onMouseWheel(MouseWheelEvent event) {

                }
            };



            /*
            MouseDownHandler handler = new MouseDownHandler() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    String style = territoryEl.getAttribute("style");
                    style = style.replace(DEF_FILL, SEL_FILL);
                    territoryEl.setAttribute("style", style);
                    handlerInterface.mouseDownHandler(territoryEl.getAttribute("id"));
                }


            };*/

            handlerRegistrations.add(territoryEl.addDomHandler(han, MouseOverEvent.getType()));
        }
        return handlerRegistrations;
    }
}
