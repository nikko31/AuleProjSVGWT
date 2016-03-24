package com.auleSVGWT.client;


import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.event.*;
import com.auleSVGWT.client.presenter.*;
import com.auleSVGWT.client.view.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Presenter, ValueChangeHandler<String> {
    private final EventBus eventBus;
    private final AuleSVGWTServiceAsync rpcService;
    private HasWidgets mapContainer;
    private HasWidgets infoContainer;
    private HasWidgets headerContainer;

    private RoomViewImpl roomView = null;
    private FloorSelectionViewImpl floorSelectionView = null;
    private EditRoomViewImpl editRoomView = null;
    private ShowFloorViewImpl showFloorView = null;
    private ShowPersonViewImpl showPersonView = null;
    private String building;
    private String floor;
    private String number;
    private PersonDTO personDTO;

    public AppController(AuleSVGWTServiceAsync auleSVGWTServiceAsync, SimpleEventBus simpleEventBus) {
        this.eventBus = simpleEventBus;
        this.rpcService = auleSVGWTServiceAsync;
        bind();
    }

    private void bind() {
        History.addValueChangeHandler(this);

        //metto nel eventbus gli handler di tutte le cose
        eventBus.addHandler(AddInvaderEvent.TYPE, new AddInvaderEventHandler() {
            @Override
            public void onAddInvader(AddInvaderEvent event) {
                doOnAddInvader();
            }
        });

        eventBus.addHandler(AddPersonEvent.TYPE, new AddPersonEventHandler() {
            @Override
            public void onAddPerson(AddPersonEvent event) {
                doOnAddPerson();
            }
        });

        eventBus.addHandler(EditRoomEvent.TYPE, new EditRoomEventHandler() {
            @Override
            public void onEditRoom(EditRoomEvent event) {
                doOnEditRoom();
            }
        });

        eventBus.addHandler(ShowRoomEvent.TYPE, new ShowRoomEventHandler() {
            @Override
            public void onShowRoom(ShowRoomEvent event) {
                number = event.getNumber();
                doOnShowRoom(number);
            }
        });

        eventBus.addHandler(ShowPersonEvent.TYPE, new ShowPersonEventHandler() {
            @Override
            public void onShowPerson(ShowPersonEvent event) {
                personDTO=event.getPersonDTO();
                doOnShowPerson(personDTO.getId());
            }
        });

        eventBus.addHandler(ShowHeaderEvent.TYPE, new ShowHeaderEventHandler() {
            @Override
            public void onShowHeader(ShowHeaderEvent event) {
                doOnShowHeader();
            }
        });

        eventBus.addHandler(ShowFloorEvent.TYPE, new ShowFloorEventHandler() {
            @Override
            public void onShowFloor(ShowFloorEvent event) {
                building = event.getBuilding();
                floor = event.getFloor();
                doOnShowFloor();
            }
        });
    }

    private void doOnShowHeader() {
        History.newItem("showHeader");
    }

    private void doOnShowFloor() {
        History.newItem("showFloor " + building + "-" +floor);
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

            if (token.startsWith("showFloor")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new ShowFloorPresenter(eventBus, rpcService, getShowFloorView(), building, floor)
                                .go(mapContainer, infoContainer, headerContainer);
                    }
                });
            }

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

            if (token.startsWith("showPerson")) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {
                        Window.alert(reason.getMessage());
                    }

                    @Override
                    public void onSuccess() {
                        new ShowPersonPresenter(eventBus, rpcService, getShowPersonView(), building, floor, number, personDTO)
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
        if (roomView == null) {
            roomView = new RoomViewImpl();
        }
        return roomView;
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
}