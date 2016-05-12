package com.auleSVGWT.client.view;

import com.auleSVGWT.client.shared.FloorDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by darklinux on 18/03/16.
 */
public class ShowFloorViewImpl extends Composite implements ShowFloorView<FloorDetails> {
    @UiTemplate("ShowFloorView.ui.xml")
    interface ShowFloorViewUiBinder extends UiBinder<Widget, ShowFloorViewImpl> {
    }
    @UiField
    HTML roomContainer;

    private static ShowFloorViewUiBinder ourUiBinder = GWT.create(ShowFloorViewUiBinder.class);
    private ShowFloorView.Presenter<FloorDetails> presenter;
    private FloorDetails floorDetail;

    public ShowFloorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setFloorName(FloorDetails floorDetails) {
        roomContainer.getElement().removeAllChildren();
        roomContainer.getElement().appendChild(floorDetails.getRoomSvg().getElement());
        roomContainer.setSize("1000","800");
    }

    @Override
    public void setPresenter(Presenter<FloorDetails> presenter) {
        this.presenter=presenter;
    }
}
