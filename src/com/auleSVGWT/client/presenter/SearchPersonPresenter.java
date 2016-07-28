package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.dto.OccupyDTO;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.event.ShowFloorEvent;
import com.auleSVGWT.client.view.SearchPersonView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import java.util.ArrayList;

/**
 * Created by Utente on 19/07/2016.
 */
public class SearchPersonPresenter implements Presenter, SearchPersonView.Presenter<OccupyDTO> {
    private  HasWidgets mapContainer;

    private final EventBus eventBus;
    private ArrayList<OccupyDTO> occupyDTO;
    private final AuleSVGWTServiceAsync rpcService;
    SearchPersonView<OccupyDTO> view;
    private ArrayList<PersonDTO>selectedPersons;


    public SearchPersonPresenter(EventBus eventBus,
                                 AuleSVGWTServiceAsync rpcService,
                                 SearchPersonView<OccupyDTO> view,
                                 ArrayList<PersonDTO>selectedPersons) {

        this.selectedPersons=selectedPersons;
        this.eventBus = eventBus;
        this.rpcService = rpcService;
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        //Window.alert("sono nel go");
        //this.mapContainer = mapContainer;

        mapContainer.clear();

        mapContainer.add(view.asWidget());

        //Window.alert("sono nello show");
        //mapContainer.add(view.asWidget());
        fetchOccupyDetails();



    }

    private void fetchOccupyDetails() {

        rpcService.getOccupySearch(selectedPersons,new AsyncCallback<ArrayList<OccupyDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error fetching contact details");
            }

            @Override
            public void onSuccess(ArrayList<OccupyDTO> result) {
                occupyDTO = result;

                view.setRowData(result);
                //view.show();
                //mapContainer.add(view.asWidget());
                //view.show();

            }
        });

    }


    @Override
    public void onItemClicked(OccupyDTO clickedItem) {
        eventBus.fireEvent(new ShowFloorEvent(clickedItem.getRoom().getBuilding().getName(),
                String.valueOf(clickedItem.getRoom().getFloor()),
                "mappa1",
                String.valueOf(clickedItem.getRoom().getId())));
    }
}