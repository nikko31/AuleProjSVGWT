package com.auleSVGWT.client.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by darklinux on 17/03/16.
 */
public interface FloorSelectionView<T> {
    public interface Presenter<T> {
        void onEnterButtonClicked();

        void onBuildingLstSelect();

        void onSearchButtonClicked();
    }

    HasValue<String> getBuilding();

    HasValue<String> getFloor();

    HasValue<String> getMapType();

    String  getName();

    String getSurname();

    void setListData(Set<String> listsData);

    void setFloorData(ArrayList<String> listsData);

    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}