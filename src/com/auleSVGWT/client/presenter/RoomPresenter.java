package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.auleSVGWT.client.event.EditRoomEvent;
import com.auleSVGWT.client.event.ShowPersonEvent;
import com.auleSVGWT.client.view.RoomView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Created by darklinux on 16/03/16.
 */
public class RoomPresenter implements Presenter, RoomView.Presenter<PersonDTO> {
    private final EventBus eventBus;
    private RoomPeopleDTO selRoomDetails;
    private final AuleSVGWTServiceAsync rpcService;
    RoomView<PersonDTO> view;
    private String building;
    private String floor;
    private String number;

    public RoomPresenter(EventBus eventBus,
                         AuleSVGWTServiceAsync rpcService,
                         RoomView<PersonDTO> view,
                         String building,
                         String floor,
                         String number) {
        this.floor = floor;
        this.building = building;
        this.eventBus = eventBus;
        this.rpcService = rpcService;
        this.number = number;
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        infoContainer.clear();
        infoContainer.add(view.asWidget());
        fetchPersonDetails();
    }

    private void fetchPersonDetails() {

        rpcService.getRoomPeople(building, floor.replaceAll("\\s+",""), number.replaceAll("\\s+",""), new AsyncCallback<RoomPeopleDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error fetching contact details");
            }

            @Override
            public void onSuccess(RoomPeopleDTO result) {
                selRoomDetails = result;
                view.setRowData(selRoomDetails.getPeopleDTO());
                view.setRoomData(selRoomDetails.getRoomDTO());
            }
        });

    }

    @Override
    public void onEditButtonClicked() {
        eventBus.fireEvent(new EditRoomEvent());
    }

    @Override
    public void onItemClicked(PersonDTO clickedItem) {
        eventBus.fireEvent(new ShowPersonEvent(clickedItem));
    }
}
