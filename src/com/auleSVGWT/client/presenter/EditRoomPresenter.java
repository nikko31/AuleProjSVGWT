package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.auleSVGWT.client.event.EditPersonEvent;
import com.auleSVGWT.client.event.ShowRoomEvent;
import com.auleSVGWT.client.view.EditRoomView;
import com.auleSVGWT.client.view.EditRoomViewImpl;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import java.util.ArrayList;

/**
 * Created by darklinux on 16/03/16.
 */
public class EditRoomPresenter implements Presenter, EditRoomView.Presenter<RoomPeopleDTO> {
    private ArrayList<PersonDTO> personsDetails;
    private ArrayList<PersonDTO> selectedPersons;
    private RoomPeopleDTO roomPeopleDTO;
    private final AuleSVGWTServiceAsync rpcService;
    private EditRoomView<RoomPeopleDTO> view;
    private final EventBus eventBus;

    public EditRoomPresenter(EventBus eventBus, AuleSVGWTServiceAsync rpcService, EditRoomViewImpl editRoomView, RoomPeopleDTO roomPeopleDTO) {
        selectedPersons=new ArrayList<>();
        this.rpcService=rpcService;
        this.eventBus=eventBus;
        this.roomPeopleDTO=roomPeopleDTO;
        this.view=editRoomView;
        this.view.setPresenter(this);

    }


    @Override
    public void onSaveButtonClicked() {
        Window.alert(selectedPersons.get(0).getName()+" 2 "+selectedPersons.get(1).getName());
    }

    @Override
    public void onCancelButtonClicked() {
        /*eventBus.fireEvent(new ShowRoomEvent(roomPeopleDTO.getRoomDTO().getBuilding().getName(),
                String.valueOf(roomPeopleDTO.getRoomDTO().getFloor()),
                String.valueOf(roomPeopleDTO.getRoomDTO().getNumber()))
        );*/
        History.back();
    }

    @Override
    public void onAddPersonButtonClicked() {
        eventBus.fireEvent(new EditPersonEvent(new PersonDTO()));
    }

    @Override
    public void onItemClicked(PersonDTO clickedItem) {
        eventBus.fireEvent(new EditPersonEvent(clickedItem));
    }

    @Override
    public void onItemSelected(PersonDTO selectedItem) {
        if(selectedPersons.contains(selectedItem))
            selectedPersons.remove(selectedItem);
        else
            selectedPersons.add(selectedItem);
    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        infoContainer.clear();
        infoContainer.add(view.asWidget());
        view.setRoomData(this.roomPeopleDTO.getRoomDTO());
        fetchPersonDetails();
    }

    private void fetchPersonDetails() {
        rpcService.getPerson(new AsyncCallback<ArrayList<PersonDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error fetching contact details");
            }

            @Override
            public void onSuccess(ArrayList<PersonDTO> result) {
                personsDetails=result;
                view.setRowData(personsDetails);
            }
        });
    }
}
