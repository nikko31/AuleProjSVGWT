package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.Resources;
import com.auleSVGWT.client.event.ShowRoomEvent;
import com.auleSVGWT.client.shared.FloorDetails;
import com.auleSVGWT.client.view.ShowFloorView;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import java.util.ArrayList;

/**
 * Created by darklinux on 18/03/16.
 */
public class ShowFloorPresenter implements Presenter, ShowFloorView.Presenter<FloorDetails> {
    private final EventBus eventBus;
    private final AuleSVGWTServiceAsync rpcService;
    ShowFloorView<FloorDetails> view;
    private String building;
    private String floor;
    private OMSVGSVGElement roomSVGElt;

    private static final String DEF_FILL = "fill:transparent";
    private static final String SEL_FILL = "fill:red";


    public ShowFloorPresenter(EventBus eventBus,
                              AuleSVGWTServiceAsync rpcService,
                              ShowFloorView<FloorDetails> view,
                              String building,
                              String floor) {
        this.building = building;
        this.floor = floor;
        this.eventBus = eventBus;
        this.rpcService = rpcService;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        mapContainer.clear();
        mapContainer.add(view.asWidget());
        getMap(building, floor);
    }

    private void getMap(final String building, final String floor) {

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "res/" + building + "-" + floor + ".svg");
        try {

            RequestCallback pendingRequest = new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                }

                public void onResponseReceived(Request request, Response response) {
                    roomSVGElt = org.vectomatic.dom.svg.utils.OMSVGParser.parse(response.getText());

                    AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(ArrayList<String> result) {
                            addHandlers(result);
                        }
                    };
                    rpcService.listaAulePiano(building + "-" + floor, callback);

                    view.setFloorName(new FloorDetails(building, floor, roomSVGElt));
                }
            };
            requestBuilder.sendRequest(null, pendingRequest);
        } catch (RequestException e) {
            Window.alert("failed file reading" + e.getMessage());
        }
    }

    private void addHandlers(ArrayList<String> strings) {
        for (String room : strings) {
            final OMElement roomEl = roomSVGElt.getElementById(room);
            HandlesAllMouseEvents handler = new HandlesAllMouseEvents() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    String style = roomEl.getAttribute("style");
                    if (style.contains("fill:green"))
                        style = style.replace("fill:green", SEL_FILL);
                    else {
                        style = style.replace(SEL_FILL, "fill:green");
                        eventBus.fireEvent(new ShowRoomEvent(building, floor, String.valueOf(Resources.SVG_ID_MAP.get(roomEl.getAttribute("id")) + 1)));
                    }
                    roomEl.setAttribute("style", style);
                }

                @Override
                public void onMouseMove(MouseMoveEvent event) {

                }

                @Override
                public void onMouseOut(MouseOutEvent event) {
                    String style = roomEl.getAttribute("style");
                    style = style.replace(SEL_FILL, DEF_FILL);
                    roomEl.setAttribute("style", style);
                }

                @Override
                public void onMouseOver(MouseOverEvent event) {
                    String style = roomEl.getAttribute("style");
                    style = style.replace(DEF_FILL, SEL_FILL);
                    roomEl.setAttribute("style", style);
                }

                @Override
                public void onMouseUp(MouseUpEvent event) {

                }

                @Override
                public void onMouseWheel(MouseWheelEvent event) {

                }
            };
            roomEl.addDomHandler(handler, MouseOverEvent.getType());
            roomEl.addDomHandler(handler, MouseDownEvent.getType());
            roomEl.addDomHandler(handler, MouseOutEvent.getType());

        }
    }
}
