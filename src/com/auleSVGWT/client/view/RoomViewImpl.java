package com.auleSVGWT.client.view;

import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.auleSVGWT.client.shared.PersonDetails;
import com.auleSVGWT.client.presenter.Presenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darklinux on 14/03/16.
 */
public class RoomViewImpl extends Composite implements RoomView<PersonDTO> {

    @UiTemplate("RoomView.ui.xml")
    interface RoomViewUiBinder extends UiBinder<Widget, RoomViewImpl> {
    }

    @UiField
    Label roomNumber;
    @UiField
    Label numSeats;
    @UiField
    Label mtQ;
    @UiField
    Button editButton;

    @UiField
    FlexTable personTable;

    private static RoomViewUiBinder ourUiBinder = GWT.create(RoomViewUiBinder.class);
    private Presenter<PersonDTO> presenter;
    private List<PersonDTO> rowData;

    public RoomViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("editButton")
    void onEditButtonClicked(ClickEvent event) {
        if (presenter != null)
            presenter.onEditButtonClicked();
    }

    @UiHandler("personTable")
    void onPersonTableClicked(ClickEvent event) {
        if (presenter != null) {
            FlexTable.Cell cell = personTable.getCellForEvent(event);
            if (cell != null)
                //clicco sul nome del prof
                presenter.onItemClicked(rowData.get(cell.getRowIndex()));

        }
    }

    @Override
    public void setPresenter(Presenter<PersonDTO> presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setRowData(ArrayList<PersonDTO> rowData) {
        personTable.removeAllRows();
        for (int c = 0; c < rowData.size(); c++) {
            personTable.setText(c, 0, rowData.get(c).getDetails());
        }
        this.rowData = rowData;

    }

    @Override
    public void setRoomData(RoomDTO roomData) {
        this.roomNumber.setText(String.valueOf(roomData.getNumber()));
        this.mtQ.setText(String.valueOf(roomData.getDimension()));
        this.numSeats.setText(String.valueOf(roomData.getMaxPerson()));
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}