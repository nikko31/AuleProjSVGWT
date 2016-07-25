package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.event.SearchPersonEvent;
import com.auleSVGWT.client.event.ShowFloorEvent;
import com.auleSVGWT.client.shared.FloorDetails;
import com.auleSVGWT.client.view.FloorSelectionView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darklinux on 17/03/16.
 */
public class FloorSelectionPresenter implements Presenter, FloorSelectionView.Presenter<FloorDetails> {
    private final EventBus eventBus;
    private List<FloorDetails> floorDetails;
    private final AuleSVGWTServiceAsync rpcService;
    private HashMap<String, ArrayList<String>> buildingFloor;
    FloorSelectionView<FloorDetails> view;

    public FloorSelectionPresenter(EventBus eventBus,
                                   AuleSVGWTServiceAsync rpcService,
                                   FloorSelectionView<FloorDetails> view) {
        this.eventBus = eventBus;
        this.rpcService = rpcService;
        this.view = view;
        this.view.setPresenter(this);


    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        headerPnl.clear();
        headerPnl.add(view.asWidget());
        fetchMapsDetails();
    }

    private void fetchMapsDetails() {
        ArrayList<FloorDetails> floorDetails = new ArrayList<>();
        rpcService.getBuildingsFileName(new AsyncCallback<HashMap<String, ArrayList<String>>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(HashMap<String, ArrayList<String>> result) {
                buildingFloor = new HashMap<>(result);
                view.setListData(result.keySet());
            }
        });
    }

    @Override
    public void onEnterButtonClicked() {
        eventBus.fireEvent(new ShowFloorEvent(view.getBuilding().getValue(), view.getFloor().getValue(), view.getMapType().getValue(),null));
    }

    @Override
    public void onBuildingLstSelect() {
        view.setFloorData(buildingFloor.get(this.view.getBuilding().getValue()));
    }

    @Override
    public void onSearchButtonClicked() {
        eventBus.fireEvent(new SearchPersonEvent(view.getName(), view.getSurname()));
    }
}
