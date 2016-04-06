package com.auleSVGWT.client.view;

import com.auleSVGWT.client.common.RoleDTOListBox;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoleDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * Created by Dark-Linux on 15/03/2016.
 */
public class EditPersonViewImpl extends Composite implements EditPersonView<PersonDTO> {

    @UiTemplate("EditPersonView.ui.xml")
    interface EditPersonViewUiBinder extends UiBinder<Widget, EditPersonViewImpl> {
    }

    @UiField
    TextBox firstNameTxt;
    @UiField
    TextBox lastNameTxt;
    @UiField
    TextBox emailTxt;
    @UiField
    RoleDTOListBox rolesList;

    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    @UiHandler("saveButton")
    void onSaveButtonClicked(ClickEvent event) {
        if (presenter != null) {
            presenter.onSaveButtonClicked();
        }
    }

    @UiHandler("cancelButton")
    void onCancelButtonClicked(ClickEvent event) {
        if (presenter != null) {
            presenter.onCancelButtonClicked();
        }
    }

    private static EditPersonViewUiBinder ourUiBinder = GWT.create(EditPersonViewUiBinder.class);
    private EditPersonView.Presenter<PersonDTO> presenter;

    public EditPersonViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public HasValue<String> getFirstName() {
        return firstNameTxt;
    }

    @Override
    public HasValue<String> getLastName() {
        return lastNameTxt;
    }

    @Override
    public HasValue<String> getEmailAddress() {
        return emailTxt;
    }

    @Override
    public HasValue<RoleDTO> getRole() {
        return rolesList;
    }

    @Override
    public void setPersonData(PersonDTO personData) {
        firstNameTxt.setText(personData.getName());
        lastNameTxt.setText(personData.getSurname());
    }

    @Override
    public void setRolesData(ArrayList<RoleDTO> rolesData, RoleDTO role) {
            rolesList.setRoleDTOs(rolesData);
            rolesList.setValue(role);

    }

    @Override
    public void setPresenter(Presenter<PersonDTO> presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
