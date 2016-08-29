package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.dto.OccupyDTO;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
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
        selectedPersons.addAll(roomPeopleDTO.getPeopleDTO());
        this.rpcService=rpcService;
        this.eventBus=eventBus;
        this.roomPeopleDTO=roomPeopleDTO;
        this.view=editRoomView;
        this.view.setPresenter(this);

    }


    @Override
    public void onSaveButtonClicked() {

        RoomDTO roomDTO=roomPeopleDTO.getRoomDTO();
        roomDTO.setDimension(Integer.valueOf(view.getMtQ().getValue()));
        roomDTO.setMaxPerson(Integer.valueOf(view.getNumSeats().getValue()));
        rpcService.updateRoom(roomDTO, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("errore nel salvataggio");
            }
            @Override
            public void onSuccess(Integer result) {

                ArrayList<OccupyDTO> occupyDTOs = new ArrayList<>();
                for(PersonDTO personDTO : selectedPersons){
                    occupyDTOs.add(new OccupyDTO(roomPeopleDTO.getRoomDTO(),personDTO));
                }
                rpcService.saveRoomOccupy(roomPeopleDTO.getOccId(), occupyDTOs, new AsyncCallback<Long>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("errore nel salvataggio");
                    }
                    @Override
                    public void onSuccess(Long result) {
                        eventBus.fireEvent(new ShowRoomEvent(roomPeopleDTO.getRoomDTO().getBuilding().getName(), new Integer(roomPeopleDTO.getRoomDTO().getFloor()).toString(),
                                new Integer(roomPeopleDTO.getRoomDTO().getNumber()).toString()));

                    }
                });
            }
        });



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
        boolean flag = true;
        for(PersonDTO personDTO : selectedPersons){
            if(selectedItem.getId() == personDTO.getId()){
                //Window.alert("cancello");
                selectedPersons.remove(personDTO);
                flag = false;
            }

        }
        if(flag){
            selectedPersons.add(selectedItem);
        }

        /*
        if (selectedPersons.contains(selectedItem)){
            Window.alert("contiene un nome");
            selectedPersons.remove(selectedItem);}
        else{
            Window.alert("contiene un nome");
            selectedPersons.add(selectedItem);
        }*/


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
                view.setRowData(personsDetails,roomPeopleDTO);
            }
        });
    }
}
