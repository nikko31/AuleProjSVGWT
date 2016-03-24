package com.auleSVGWT.client.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created by Dark-Linux on 15/03/2016.
 */
public interface EditRoomView<T> {
    public interface Presenter<T> {
        void onSaveButtonClicked();

        void onCancelButtonClicked();

        void onAddPersonButtonClicked();

        void onItemClicked(T clickedItem);

        void onItemSelected(T selectedItem);
    }

    HasValue<String> getNumSeats();

    HasValue<String> getMtQ();

    void setRowData(List<T> rowData);


    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}
