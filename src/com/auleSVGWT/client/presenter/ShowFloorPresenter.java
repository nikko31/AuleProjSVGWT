package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.Resources;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
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
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

import java.util.ArrayList;

/**
 * Created by darklinux on 18/03/16.
 */
public class ShowFloorPresenter implements Presenter, ShowFloorView.Presenter<FloorDetails> {
    private final EventBus eventBus;
    private final AuleSVGWTServiceAsync rpcService;
    ShowFloorView<FloorDetails> view;
    private ArrayList<RoomPeopleDTO>  roomPeopleDTOs;
    private ArrayList<String> string;
    private String building;
    private String floor;
    private String modality;
    private OMSVGSVGElement roomSVGElt;
    private OMElement selectedRoom;

    private static final String DEF_FILL = "fill:transparent";
    private static final String SEL_FILL = "fill:red";


    public ShowFloorPresenter(EventBus eventBus,
                              AuleSVGWTServiceAsync rpcService,
                              ShowFloorView<FloorDetails> view,
                              String building,
                              String floor,
                              String modality) {
        this.selectedRoom = null;
        this.building = building;
        this.floor = floor;
        this.eventBus = eventBus;
        this.rpcService = rpcService;
        this.view = view;
        this.view.setPresenter(this);
        this.modality = modality;
    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        mapContainer.clear();
        infoContainer.clear();
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
                            if(modality.equals("mappa1")){
                                addHandlers(result);
                            }
                            else {
                                colorFloor(result);
                            }

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
                    if (style.contains("fill:green")) {
                        style = style.replace("fill:green", DEF_FILL);
                        selectedRoom = null;
                    } else {
                        if (selectedRoom != null) {
                            String selStyle = selectedRoom.getAttribute("style");
                            selStyle = selStyle.replace("fill:green", DEF_FILL);
                            selectedRoom.setAttribute("style",selStyle);
                        }
                        style = style.replace(DEF_FILL, "fill:green");
                        style = style.replace(SEL_FILL, "fill:green");
                        selectedRoom =  roomEl;
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

    private void colorFloor(ArrayList<String> strings){
        string = strings;


        rpcService.getRoomsPeople(building, floor.replaceAll("\\s+", ""), new AsyncCallback<ArrayList<RoomPeopleDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error fetching contact details");
            }

            @Override
            public void onSuccess(ArrayList<RoomPeopleDTO> result) {
                colorRoom(string,result);

            }
        });




    }
    private void colorRoom(ArrayList<String> strings,ArrayList<RoomPeopleDTO> roomPeopleDTOs){

        //int Max = 0;
        //int Min = 0;
        int sum;
        int dim;
        /*
        for(RoomPeopleDTO roomPeopleDTO : roomPeopleDTOs){
            for(PersonDTO personDTO : roomPeopleDTO.getPeopleDTO()){
                sum += personDTO.getRole().getSqm();
            }

            if(sum > roomPeopleDTO.getRoomDTO().getDimension() && sum > Max){
                Max = sum;

            }

        }*/



        for (String room : strings) {
            sum = 0;
            dim= 0;
            //Window.alert("ciao");
            for(RoomPeopleDTO roomPeopleDTO : roomPeopleDTOs){
                String s = "";
                s += roomPeopleDTO.getRoomDTO().getBuilding().getName()+"-"+new Integer(roomPeopleDTO.getRoomDTO().getFloor()).toString()+
                        "-"+new Integer(roomPeopleDTO.getRoomDTO().getNumber()).toString();
                //Window.alert(s +"......... " + room);
                if (room.equals(s)){
                    for(PersonDTO personDTO : roomPeopleDTO.getPeopleDTO()){
                        sum += personDTO.getRole().getSqm();
                    }
                    dim = roomPeopleDTO.getRoomDTO().getDimension();
                }

            }
            if(dim == 0){
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(DEF_FILL, "fill:grey");
                roomEl.setAttribute("style", style);

            }
            else if(sum == 0 /*&& dim != 0*/){
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(DEF_FILL, "fill:yellow");
                roomEl.setAttribute("style", style);

            }
            else if(sum == dim /*&& dim != 0*/){
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(DEF_FILL, "fill:green");
                roomEl.setAttribute("style", style);
            }
            else if(sum > dim /*&& dim != 0*/){
                //Integer value = new Integer ((1-(sum/Max))*200);
                Integer value = new Integer ((dim/sum)*200);

                //varia da 0 a 200
                String str = "#FF";
                if(Integer.toHexString(value).length()<=1){
                    str +="0"+Integer.toHexString(value)+"0"+Integer.toHexString(value);
                }
                else{
                    str+=Integer.toHexString(value)+Integer.toHexString(value);
                }
                //Window.alert(str);
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(DEF_FILL, "fill:"+str);
                roomEl.setAttribute("style", style);
            }
            else if(sum < dim /*&& dim != 0*/){
                String str="#";
                //Integer value = new Integer (((sum/Max))*200);
                Integer value = new Integer ((sum/dim)*200);

                //varia da 0 a 200

                if(Integer.toHexString(value).length()<=1){
                    str +="0"+Integer.toHexString(value)+"0"+Integer.toHexString(value);
                }
                else{
                    str+=Integer.toHexString(value)+Integer.toHexString(value);
                }
                str+="FF";
                //Window.alert(str);
                final OMElement roomEl = roomSVGElt.getElementById(room);
                String style = roomEl.getAttribute("style");
                style = style.replace(DEF_FILL, "fill:"+str);
                roomEl.setAttribute("style", style);
            }


        }

    }
}
