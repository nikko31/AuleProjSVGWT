package com.auleSVGWT.client.view;

import com.auleSVGWT.client.dto.RoomDTO;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Utente on 19/07/2016.
 */
public interface SearchPersonView <T>  {
    public interface Presenter<T> {

        void onItemClicked(T clickedItem);

    }

    void setPresenter(Presenter<T> presenter);

    void setRowData(ArrayList<T> rowData);

    void setRoomData(RoomDTO roomData);

    Widget asWidget();
}

