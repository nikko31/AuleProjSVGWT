package com.auleSVGWT.client.view;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * Created by Dark-Linux on 15/03/2016.
 */
public interface EditPersonView<T> {
    public interface Presenter<T> {
        void onSaveButtonClicked();

        void onCancelButtonClicked();

    }

    HasValue<String> getFirstName();

    HasValue<String> getLastName();

    HasValue<String> getEmailAddress();

    HasValue<String> getRole();

    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}
