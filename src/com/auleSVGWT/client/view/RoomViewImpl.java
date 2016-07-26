package com.auleSVGWT.client.view;

import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
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
    Label numSocket;
    @UiField
    Label infoTxt;
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
            personTable.setText(c, 0,rowData.get(c).getDetails());
            personTable.setText(c,1,rowData.get(c).getRole().getName());
            if(rowData.get(c).getStartWork()==null){
                personTable.setText(c,2,"nessuna info");
            }else{
                personTable.setText(c,2,rowData.get(c).getStartWork().toString());
            }

            if(rowData.get(c).getEndWork()==null){
                personTable.setText(c,3,"nessuna info");
            }else{
                personTable.setText(c,3,rowData.get(c).getEndWork().toString());
            }
            if(c%2==0)
                personTable.getRowFormatter().addStyleName(c,"rowDColor");
            personTable.getRowFormatter().addStyleName(c,"rowColor");
        }
        this.rowData = rowData;
        editButton.setStyleName("default-button");

    }

    @Override
    public void setRoomData(RoomDTO roomData) {
        this.roomNumber.setText(String.valueOf(roomData.getNumber()));
        this.mtQ.setText(String.valueOf(roomData.getDimension()));
        this.numSeats.setText(String.valueOf(roomData.getMaxPerson()));
        numSocket.setText(String.valueOf(roomData.getSocket()));
        if(roomData.getMaintenance() == null){
            infoTxt.setText("nessuna info");
        }else{
            infoTxt.setText(roomData.getMaintenance());
        }
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}