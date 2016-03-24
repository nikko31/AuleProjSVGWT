package com.auleSVGWT.client.view;

import com.auleSVGWT.client.dto.RoomDTO;
import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darklinux on 15/03/16.
 */
public interface RoomView<T> {
    public interface Presenter<T> {
        void onEditButtonClicked();

        void onItemClicked(T clickedItem);
    }

    void setPresenter(Presenter<T> presenter);

    void setRowData(ArrayList<T> rowData);

    void setRoomData(RoomDTO roomData);

    Widget asWidget();
}
