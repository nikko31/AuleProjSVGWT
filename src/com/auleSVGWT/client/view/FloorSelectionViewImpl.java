package com.auleSVGWT.client.view;

import com.auleSVGWT.client.common.MyListBox;
import com.auleSVGWT.client.shared.FloorDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by darklinux on 17/03/16.
 */
public class FloorSelectionViewImpl extends Composite implements FloorSelectionView<FloorDetails> {


    @UiTemplate("FloorSelectionView.ui.xml")
    interface FloorSelectionViewUiBinder extends UiBinder<Widget, FloorSelectionViewImpl> {
    }

    @UiField
    MyListBox<String> buildingLst;
    @UiField
    MyListBox<String> floorLst;
    @UiField
    MyListBox<String> mapLst;


    @UiField
    Button enterBtn;


    private static FloorSelectionViewUiBinder ourUiBinder = GWT.create(FloorSelectionViewUiBinder.class);
    private FloorSelectionView.Presenter<FloorDetails> presenter;

    public FloorSelectionViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("enterBtn")
    void onEnterButtonClicked(ClickEvent event) {
        if (presenter != null) {
            presenter.onEnterButtonClicked();
        }
    }

    @Override
    public HasValue<String> getBuilding() {
        return buildingLst;
    }

    @Override
    public HasValue<String> getFloor() {
        return floorLst;
    }

    @Override
    public HasValue<String> getMapType() {
        return mapLst;
    }

    @Override
    public void setListData(Set<String> buildings) {
        mapLst.setValue("mappa1");
        mapLst.setValue("mappa2");
        buildingLst.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                presenter.onBuildingLstSelect();
            }
        });

        for(String building : buildings) {
            buildingLst.addValue(building);
        }
        buildingLst.setSelectedIndex(0);
    }

    @Override
    public void setFloorData(ArrayList<String> listsData) {
        this.floorLst.clear();
        for(String floor:listsData)
            floorLst.addValue(floor);
    }


    @Override
    public void setPresenter(Presenter<FloorDetails> presenter) {
        this.presenter = presenter;
    }
}
