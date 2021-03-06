package com.auleSVGWT.client;

import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.auleSVGWT.client.event.*;
import com.auleSVGWT.client.presenter.*;
import com.auleSVGWT.client.view.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

import java.util.ArrayList;

public class AppController implements Presenter, ValueChangeHandler<String> {
    private final EventBus eventBus;
    private final AuleSVGWTServiceAsync rpcService;

    private HasWidgets mapContainer;
    private HasWidgets infoContainer;
    private HasWidgets headerContainer;

    private FloorSelectionViewImpl floorSelectionView = null;
    private ShowFloorViewImpl showFloorView = null;
    private ShowPersonViewImpl showPersonView = null;
    private String building;
    private String floor;
    private String roomID;
    private String number;
    private String mode;
    private PersonDTO personDTO;
    private RoomPeopleDTO roomPeopleDTO;
    private String name;
    private String surname;
    private ArrayList<PersonDTO> selectedPersons;

    public AppController(AuleSVGWTServiceAsync auleSVGWTServiceAsync, SimpleEventBus simpleEventBus) {
        this.eventBus = simpleEventBus;
        this.rpcService = auleSVGWTServiceAsync;
        bind();
    }

    private void bind() {
        History.addValueChangeHandler(this);

        //metto nel eventbus gli handler di tutte le cose
        eventBus.addHandler(AddInvaderEvent.TYPE,
                new AddInvaderEventHandler() {
                    @Override
                    public void onAddInvader(AddInvaderEvent event) {
                        doOnAddInvader();
                    }
                });

        eventBus.addHandler(AddPersonEvent.TYPE,
                new AddPersonEventHandler() {
                    @Override
                    public void onAddPerson(AddPersonEvent event) {
                        doOnAddPerson();
                    }
                });

        eventBus.addHandler(EditRoomEvent.TYPE,
                new EditRoomEventHandler() {
                    @Override
                    public void onEditRoom(EditRoomEvent event) {
                        roomPeopleDTO = event.getRoomPeopleDTO();
                        doOnEditRoom();
                    }
                });

        eventBus.addHandler(EditPersonEvent.TYPE,
                new EditPersonHandler() {
                    @Override
                    public void onEditPerson(EditPersonEvent event) {
                        personDTO = event.getPersonDTO();
                        doOnEditPerson();
                    }
                });

        eventBus.addHandler(ShowRoomEvent.TYPE,
                new ShowRoomEventHandler() {
                    @Override
                    public void onShowRoom(ShowRoomEvent event) {
                        number = event.getNumber();
                        doOnShowRoom(number);
                    }
                });

        eventBus.addHandler(ShowPersonEvent.TYPE,
                new ShowPersonEventHandler() {
                    @Override
                    public void onShowPerson(ShowPersonEvent event) {
                        personDTO = event.getPersonDTO();
                        doOnShowPerson(personDTO.getId());
                    }
                });

        eventBus.addHandler(ShowHeaderEvent.TYPE,
                new ShowHeaderEventHandler() {
                    @Override
                    public void onShowHeader(ShowHeaderEvent event) {
                        doOnShowHeader();
                    }
                });

        eventBus.addHandler(ShowFloorEvent.TYPE,
                new ShowFloorEventHandler() {
                    @Override
                    public void onShowFloor(ShowFloorEvent event) {
                        building = event.getBuilding();
                        floor = event.getFloor();
                        mode = event.getMode();
                        roomID = event.getRoomID();
                        doOnShowFloor();
                    }
                });
        eventBus.addHandler(SearchPersonEvent.TYPE,
                new SearchPersonEventHandler() {
                    @Override
                    public void onSearchPerson(SearchPersonEvent event) {
                        selectedPersons = event.getPersonsSelected();
                        doOnSearchPerson();
                    }
                });
    }

    private void doOnShowHeader() {
        History.newItem("showHeader");
    }

    private void doOnShowFloor() {
        if (roomID != null)
            History.newItem("showFloor " + building + "-" + floor + "-" + mode + "&" + roomID);
        else
            History.newItem("showFloor " + building + "-" + floor + "-" + mode);
    }

    private void doOnShowRoom(String number) {
        History.newItem("list " + number);
    }

    private void doOnShowPerson(int id) {
        History.newItem("showPerson " + id);
    }

    private void doOnAddPerson() {
        History.newItem("addPerson");
    }

    private void doOnAddInvader() {
        History.newItem("addInvader");
    }

    private void doOnEditRoom() {
        History.newItem("editRoom");
    }

    private void doOnEditPerson() {
        History.newItem("editPerson");
    }

    private void doOnSearchPerson() {
        String names = "";
        for (PersonDTO personDTO : selectedPersons)
            names += personDTO.getName() + "-" + personDTO.getSurname() + " ";
        History.newItem("searchPerson " + names);
    }


    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        final String token = event.getValue();
        if (token != null) {
            //-----------OK----------
            if (token.equals("showHeader")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new FloorSelectionPresenter(eventBus, rpcService, getFloorSelectionView()).go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }
            //-----------OK------------
            if (token.startsWith("showFloor")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new ShowFloorPresenter(eventBus, rpcService, getShowFloorView(), building, floor, mode, roomID)
                                .go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }
            //----------OK----------------
            if (token.startsWith("list")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new RoomPresenter(eventBus, rpcService, getRoomView(), building, floor, token.substring("list".length()))
                                .go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }
            //----------------OK--------------
            if (token.startsWith("showPerson")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new ShowPersonPresenter(eventBus, getShowPersonView(), building, floor, number, personDTO)
                                .go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }
            if (token.startsWith("editRoom")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new EditRoomPresenter(eventBus, rpcService, getEditRoomView(), roomPeopleDTO)
                                .go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }
            if (token.startsWith("editPerson")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new EditPersonPresenter(eventBus, rpcService, getEditPersonView(), personDTO)
                                .go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }
            if (token.startsWith("searchPerson")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        /*new ShowPersonPresenter(eventBus, getShowPersonView(), building, floor, number, personDTO)
                                .go(mapContainer, infoContainer, headerContainer);*/

                        new SearchPersonPresenter(eventBus, rpcService, getSearchPersonView(), selectedPersons)
                                .go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }
        }
    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        this.mapContainer = mapContainer;
        this.infoContainer = infoContainer;
        this.headerContainer = headerPnl;
        if ("".equals(History.getToken()))
            History.newItem("showHeader");
        else History.fireCurrentHistoryState();

    }

    public RoomViewImpl getRoomView() {
        return new RoomViewImpl();
    }

    public FloorSelectionViewImpl getFloorSelectionView() {
        if (floorSelectionView == null)
            floorSelectionView = new FloorSelectionViewImpl();
        return floorSelectionView;
    }

    public ShowFloorViewImpl getShowFloorView() {
        if (showFloorView == null)
            showFloorView = new ShowFloorViewImpl();
        return showFloorView;
    }

    public ShowPersonViewImpl getShowPersonView() {
        if (showPersonView == null)
            showPersonView = new ShowPersonViewImpl();
        return showPersonView;
    }

    public EditRoomViewImpl getEditRoomView() {
        return new EditRoomViewImpl();
    }

    public EditPersonViewImpl getEditPersonView() {
        return new EditPersonViewImpl();
    }

    public SearchPersonViewImpl getSearchPersonView() {
        return new SearchPersonViewImpl();
    }
}