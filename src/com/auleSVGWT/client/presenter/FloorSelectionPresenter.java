package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.event.ShowFloorEvent;
import com.auleSVGWT.client.shared.FloorDetails;
import com.auleSVGWT.client.view.FloorSelectionView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darklinux on 17/03/16.
 */
public class FloorSelectionPresenter implements Presenter, FloorSelectionView.Presenter<FloorDetails> {
    private final EventBus eventBus;
    private List<FloorDetails> floorDetails;
    private final AuleSVGWTServiceAsync rpcService;
    FloorSelectionView<FloorDetails> view;

    public FloorSelectionPresenter(EventBus eventBus,
                                   AuleSVGWTServiceAsync rpcService,
                                   FloorSelectionView<FloorDetails> view) {
        this.eventBus=eventBus;
        this.rpcService=rpcService;
        this.view=view;
        this.view.setPresenter(this);
    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        headerPnl.clear();
        headerPnl.add(view.asWidget());
        fetchMapsDetails();
    }

    private void fetchMapsDetails() {
        ArrayList<FloorDetails>floorDetails=new ArrayList<>();
        floorDetails.add(new FloorDetails("fac-0","1"));
        floorDetails.add(new FloorDetails("fac-0","2"));
        floorDetails.add(new FloorDetails("fac","0"));
        floorDetails.add(new FloorDetails("fac","1"));
        floorDetails.add(new FloorDetails("fac","2"));
        floorDetails.add(new FloorDetails("fac","3"));
        floorDetails.add(new FloorDetails("lab_pes","0"));
        floorDetails.add(new FloorDetails("mat","0"));
        floorDetails.add(new FloorDetails("mat","1"));
        floorDetails.add(new FloorDetails("mec_info","0"));
        floorDetails.add(new FloorDetails("mec_info","1"));
        floorDetails.add(new FloorDetails("mec_info","2"));
        floorDetails.add(new FloorDetails("mec_info","3"));
        floorDetails.add(new FloorDetails("cent_tec","0"));
        view.setListData(floorDetails);
    }

    @Override
    public void onEnterButtonClicked() {
        eventBus.fireEvent(new ShowFloorEvent(view.getBuilding().getValue(),view.getFloor().getValue(),view.getMapType().getValue()));
    }
}
