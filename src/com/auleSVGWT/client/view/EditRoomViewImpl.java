package com.auleSVGWT.client.view;

import com.auleSVGWT.client.shared.PersonDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

import java.util.List;

/**
 * Created by Dark-Linux on 15/03/2016.
 */
public class EditRoomViewImpl extends Composite implements EditRoomView<PersonDetails> {

    @UiTemplate("EditRoomView.ui.xml")
    interface EditRoomViewUiBinder extends UiBinder<Widget, EditRoomViewImpl> {
    }

    @UiField
    TextBox numSeatsTxt;
    @UiField
    TextBox mtQTxt;

    @UiField
    FlexTable editPersonTable;

    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;
    @UiField
    Button addPersonButton;

    private static EditRoomViewUiBinder ourUiBinder = GWT.create(EditRoomViewUiBinder.class);
    private EditRoomView.Presenter<PersonDetails> presenter;
    private List<PersonDetails> rowData;

    public EditRoomViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("editPersonTable")
    void onTableClicked(ClickEvent event) {
        if(presenter !=null){
            FlexTable.Cell cell=editPersonTable.getCellForEvent(event);
            if(cell!=null){
                // Suppress clicks if the user is actually selecting the
                //  check box
                if (cell.getCellIndex() > 0) {
                    presenter.onItemClicked(this.rowData.get(cell.getRowIndex()));
                } else {
                    presenter.onItemSelected(rowData.get(cell.getRowIndex()));
                }
            }
        }
    }

    @UiHandler("saveButton")
    void onSaveButtonClicked(ClickEvent event) {
        if (presenter != null) {
            presenter.onSaveButtonClicked();
        }
    }

    @UiHandler("cancelButton")
    void onCancelButtonClicked(ClickEvent event) {
        if (presenter != null) {
            presenter.onCancelButtonClicked();
        }
    }

    @UiHandler("addPersonButton")
    void onAddPersonButtonClicked(ClickEvent event) {
        if (presenter != null)
            presenter.onAddPersonButtonClicked();
    }

    @Override
    public HasValue<String> getNumSeats() {
        return numSeatsTxt;
    }

    @Override
    public HasValue<String> getMtQ() {
        return mtQTxt;
    }

    @Override
    public void setPresenter(EditRoomView.Presenter<PersonDetails> presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setRowData(List<PersonDetails> rowData) {
        editPersonTable.removeAllRows();
        for (int i = 0; i < rowData.size(); i++) {
            editPersonTable.setWidget(i, 0, new CheckBox());
            editPersonTable.setText(i, 1, rowData.get(i).getDisplayName());
        }
        this.rowData = rowData;
    }
}
