package com.auleSVGWT.client.view;

import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoomDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark-Linux on 15/03/2016.
 */
public interface EditRoomView<T> {
    public interface Presenter<T> {
        void onSaveButtonClicked();

        void onCancelButtonClicked();

        void onAddPersonButtonClicked();

        void onItemClicked(PersonDTO clickedItem);

        void onItemSelected(PersonDTO selectedItem);
    }

    HasValue<String> getNumSeats();

    HasValue<String> getMtQ();

    HasValue<String> getSockets();

    void setRowData(ArrayList<PersonDTO> rowData, RoomPeopleDTO roompeopleDTO);

    void setRoomData(RoomDTO roomData);


    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}
