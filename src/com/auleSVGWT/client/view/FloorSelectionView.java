package com.auleSVGWT.client.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created by darklinux on 17/03/16.
 */
public interface FloorSelectionView<T> {
    public interface Presenter<T> {
        void onEnterButtonClicked();
    }

    HasValue<String> getBuilding();

    HasValue<String> getFloor();

    HasValue<String> getMapType();

    void setListData(List<T> listsData);

    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}