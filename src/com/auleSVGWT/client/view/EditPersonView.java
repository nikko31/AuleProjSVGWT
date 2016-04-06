package com.auleSVGWT.client.view;

import com.auleSVGWT.client.common.MyListBox;
import com.auleSVGWT.client.common.RoleDTOListBox;
import com.auleSVGWT.client.dto.RoleDTO;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

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

    HasValue<RoleDTO> getRole();

    void setPersonData(T personData);

    void setRolesData(ArrayList<RoleDTO> rolesData, RoleDTO role);

    void setPresenter(Presenter<T> presenter);

    Widget asWidget();
}
