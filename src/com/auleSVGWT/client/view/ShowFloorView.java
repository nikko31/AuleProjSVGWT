package com.auleSVGWT.client.view;

import com.auleSVGWT.client.shared.FloorDetails;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created by darklinux on 18/03/16.
 */
public interface ShowFloorView<T> {
    public interface Presenter<T> {

    }

    void setFloorName(T floorDetails);

    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}
