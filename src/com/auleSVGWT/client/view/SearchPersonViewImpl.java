package com.auleSVGWT.client.view;

import com.auleSVGWT.client.dto.OccupyDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utente on 19/07/2016.
 */
public class SearchPersonViewImpl extends Composite implements SearchPersonView<OccupyDTO> {

    @UiTemplate("SearchPersonView.ui.xml")
    interface SearchPersonViewImplUiBinder extends UiBinder<Widget, SearchPersonViewImpl> {
    }


    @UiField
    HorizontalPanel popupSearch;
    @UiField
    FlexTable searchPersonTable;

    private static SearchPersonViewImplUiBinder ourUiBinder = GWT.create(SearchPersonViewImplUiBinder.class);
    private SearchPersonView.Presenter<OccupyDTO> presenter;
    private List<OccupyDTO> rowData;

    @UiHandler("searchPersonTable")
    void onTableClicked(ClickEvent event) {

        if (presenter != null) {
            FlexTable.Cell cell = searchPersonTable.getCellForEvent(event);
            if (cell != null) {
                // Suppress clicks if the user is actually selecting the
                //  check box
                presenter.onItemClicked(this.rowData.get(cell.getRowIndex()-1));
            }
        }
    }

    public SearchPersonViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(Presenter<OccupyDTO> presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setRowData(ArrayList<OccupyDTO> rowData) {
        searchPersonTable.setText(0, 0, "EDIFICIO STANZA");
        searchPersonTable.setText(0, 1, "DIMENSIONI");
        searchPersonTable.setText(0, 2, "NUMERO MASSIMO DI PERSONE");
        searchPersonTable.setText(0, 3, "PRESE DI RETE");

        for (int i = 1; i < rowData.size() + 1; i++) {
            searchPersonTable.setText(i, 0, rowData.get(i - 1).getRoom().getBuilding().getName() + "-" + String.valueOf(rowData.get(i - 1).getRoom().getFloor()) + "-" + String.valueOf(rowData.get(i - 1).getRoom().getNumber()));
            searchPersonTable.setText(i, 1, String.valueOf(rowData.get(i - 1).getRoom().getDimension()));
            searchPersonTable.setText(i, 2, String.valueOf(rowData.get(i - 1).getRoom().getMaxPeople()));
            searchPersonTable.setText(i, 3, String.valueOf(rowData.get(i - 1).getRoom().getSocket()));
        }
        this.rowData=rowData;
    }

    @Override
    public void setRoomData(RoomDTO roomData) {
    }


    @Override
    public Widget asWidget() {
        return this;
    }


}