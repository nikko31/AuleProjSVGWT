package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.auleSVGWT.client.event.ShowRoomEvent;
import com.auleSVGWT.client.shared.FloorDetails;
import com.auleSVGWT.client.shared.Global;
import com.auleSVGWT.client.view.ShowFloorView;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import org.vectomatic.dom.svg.OMElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;


import java.sql.Date;
import java.util.ArrayList;


/**
 * Created by darklinux on 18/03/16.
 */
public class ShowFloorPresenter implements Presenter, ShowFloorView.Presenter<FloorDetails> {
    private final EventBus eventBus;
    private final AuleSVGWTServiceAsync rpcService;
    ShowFloorView<FloorDetails> view;
    private ArrayList<String> string;
    private String building;
    private String floor;
    private String mode;
    private String roomID;
    private OMSVGSVGElement roomSVGElt;
    private OMElement selectedRoom;


    public ShowFloorPresenter(EventBus eventBus,
                              AuleSVGWTServiceAsync rpcService,
                              ShowFloorView<FloorDetails> view,
                              String building,
                              String floor,
                              String mode,
                              String roomID) {
        this.selectedRoom = null;
        this.building = building;
        this.floor = floor;
        this.eventBus = eventBus;
        this.rpcService = rpcService;
        this.view = view;
        this.view.setPresenter(this);
        this.mode = mode;
        this.roomID = roomID;
    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        mapContainer.clear();
        infoContainer.clear();
        mapContainer.add(view.asWidget());
        getMap(building, floor);
    }

    private void getMap(final String building, final String floor) {

        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
                "res/imageGWT/" + building + "-" + floor + ".svg");
        try {

            RequestCallback pendingRequest = new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                }

                public void onResponseReceived(Request request, Response response) {
                    roomSVGElt = org.vectomatic.dom.svg.utils.OMSVGParser.parse(response.getText());
                    roomSVGElt.setWidth(Style.Unit.PX, (float) (( (float) RootPanel.get().getOffsetWidth())*0.6));
                    roomSVGElt.setHeight(Style.Unit.PX, (float) (((float) RootPanel.get().getOffsetHeight()) * 0.9));

                    AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(ArrayList<String> result) {
                            if (result == null) {
                                Window.alert("devono essere aggiunti gli handler");
                            }
                            if (mode.equals("visualizzazione")) {
                                addHandlers(result);
                                if(roomID!=null)
                                    colorRoom();
                            } else {
                                colorFloor(result);
                            }
                            view.setFloorName(new FloorDetails(building, floor, roomSVGElt));

                        }
                    };

                    rpcService.listaAulePianoNewVersion(building + "-" + floor, callback);


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
                    if (style.contains(Global.GREEN_FILL)) {
                        style = style.replace(Global.GREEN_FILL, Global.DEF_FILL);
                        selectedRoom = null;
                    } else {
                        if (selectedRoom != null) {
                            String selStyle = selectedRoom.getAttribute("style");
                            selStyle = selStyle.replace(Global.GREEN_FILL, Global.DEF_FILL);
                            selectedRoom.setAttribute("style", selStyle);
                        }
                        style = style.replace(Global.DEF_FILL, Global.GREEN_FILL);
                        style = style.replace(Global.SEL_FILL, Global.GREEN_FILL);
                        selectedRoom = roomEl;
                        //eventBus.fireEvent(new ShowRoomEvent(building, floor, String.valueOf(Resources.SVG_ID_MAP.get(roomEl.getAttribute("id")) + 1)));
                        eventBus.fireEvent(new ShowRoomEvent(building, floor,
                                String.valueOf(roomEl.getAttribute("id").substring(roomEl.getAttribute("id").lastIndexOf('-') + 1))));
                    }
                    roomEl.setAttribute("style", style);
                }

                @Override
                public void onMouseMove(MouseMoveEvent event) {

                }

                @Override
                public void onMouseOut(MouseOutEvent event) {
                    String style = roomEl.getAttribute("style");
                    style = style.replace(Global.SEL_FILL, Global.DEF_FILL);
                    roomEl.setAttribute("style", style);
                }

                @Override
                public void onMouseOver(MouseOverEvent event) {
                    String style = roomEl.getAttribute("style");
                    style = style.replace(Global.DEF_FILL, Global.SEL_FILL);
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

    private void colorFloor(ArrayList<String> strings) {
        string = strings;


        rpcService.getRoomsPeople(building, floor.replaceAll("\\s+", ""), new AsyncCallback<ArrayList<RoomPeopleDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error fetching contact details");
            }

            @Override
            public void onSuccess(ArrayList<RoomPeopleDTO> result) {
                if (mode.equals("distribuzione spazi")) {
                    colorRoom(string, result);
                } else {
                    //Window.alert("entro in modalita mappa 3");
                    colorRoomEndWork(string, result);
                }


            }
        });


    }

    private void colorRoom(){
        final OMElement roomEl = roomSVGElt.getElementById(building+"-"+floor+"-"+roomID);
        selectedRoom=roomEl;
        String style = roomEl.getAttribute("style");
        style = style.replace(Global.DEF_FILL, Global.GREEN_FILL);
        roomEl.setAttribute("style", style);
        eventBus.fireEvent(new ShowRoomEvent(building, floor,
                String.valueOf(roomEl.getAttribute("id").substring(roomEl.getAttribute("id").lastIndexOf('-') + 1))));

    }
    private void colorRoom(ArrayList<String> strings, ArrayList<RoomPeopleDTO> roomPeopleDTOs) {

        int sum;
        int dim;
        for (String room : strings) {
            sum = 0;
            dim = 0;
            for (RoomPeopleDTO roomPeopleDTO : roomPeopleDTOs) {
                String s = "";
                s += roomPeopleDTO.getRoomDTO().getBuilding().getName() + "-" + new Integer(roomPeopleDTO.getRoomDTO().getFloor()).toString() +
                        "-" + new Integer(roomPeopleDTO.getRoomDTO().getNumber()).toString();
                if (room.equals(s)) {
                    for (PersonDTO personDTO : roomPeopleDTO.getPeopleDTO()) {
                        sum += personDTO.getRole().getSqm();
                    }
                    dim = roomPeopleDTO.getRoomDTO().getDimension();
                }

            }
            if (dim == 0 && sum!=0) {
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(Global.DEF_FILL, Global.RED_FILL);
                roomEl.setAttribute("style", style);

            } else if (sum == dim && dim != 0) {
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(Global.DEF_FILL, Global.GREEN_FILL);
                roomEl.setAttribute("style", style);
            } else if (sum > dim && dim != 0) {
                //Integer value = new Integer ((1-(sum/Max))*200);
                //Integer value = new Integer (((double)dim/sum)*200);
                Double value1 = new Double(((double) dim / sum) * 200);
                Integer value = new Integer(value1.intValue());

                //varia da 0 a 200
                String str = "#FF";
                if (Integer.toHexString(value).length() <= 1) {
                    str += "0" + Integer.toHexString(value) + "0" + Integer.toHexString(value);
                } else {
                    str += Integer.toHexString(value) + Integer.toHexString(value);
                }
                //Window.alert(str);
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(Global.DEF_FILL, "fill:" + str);
                roomEl.setAttribute("style", style);
            } else if (sum < dim && dim != 0) {
                String str = "#";
                //Integer value = new Integer (((sum*200)/dim));

                Double value1 = new Double(((double) sum / dim) * 200);
                Integer value = new Integer(value1.intValue());

                //varia da 0 a 200

                if (Integer.toHexString(value).length() <= 1) {
                    str += "0" + Integer.toHexString(value) + "0" + Integer.toHexString(value);
                } else {
                    str += Integer.toHexString(value) + Integer.toHexString(value);
                }
                str += "FF";
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(Global.DEF_FILL, "fill:" + str);
                roomEl.setAttribute("style", style);
            }


        }

    }


    private void colorRoomEndWork(ArrayList<String> strings, ArrayList<RoomPeopleDTO> roomPeopleDTOs) {
        for (String room : strings) {

            for (RoomPeopleDTO roomPeopleDTO : roomPeopleDTOs) {
                String s = "";
                s += roomPeopleDTO.getRoomDTO().getBuilding().getName() + "-" + new Integer(roomPeopleDTO.getRoomDTO().getFloor()).toString() +
                        "-" + new Integer(roomPeopleDTO.getRoomDTO().getNumber()).toString();

                if (room.equals(s)) {

                    ArrayList<PersonDTO> people = roomPeopleDTO.getPeopleDTO();
                    for (PersonDTO personDTO : people) {
                        //Window.alert(personDTO.getName());

                        if(personDTO.getEndWork()!=null){
                            //Window.alert("sono nel controllo null");
                            if (personDTO.getEndWork().before(new Date(new java.util.Date().getTime()))) {
                                //Window.alert("sono nel controllo end work");
                                final OMElement roomEl = roomSVGElt.getElementById(room);
                                String style = roomEl.getAttribute("style");
                                style = style.replace(Global.DEF_FILL, Global.RED_FILL);
                                roomEl.setAttribute("style", style);

                            }

                        }

                    }

                }

            }


        }

    }
}
