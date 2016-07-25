package com.auleSVGWT.client.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * Created by darklinux on 22/03/16.
 */
public interface ShowPersonView<T> {
    public interface Presenter<T> {
        void onBackButtonClicked();
    }

    void setPersonData(T person);

    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}
