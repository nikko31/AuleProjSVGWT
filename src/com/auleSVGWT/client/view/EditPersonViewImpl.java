package com.auleSVGWT.client.view;

import com.auleSVGWT.client.common.MyListBox;
import com.auleSVGWT.client.shared.Person;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

/**
 * Created by Dark-Linux on 15/03/2016.
 */
public class EditPersonViewImpl extends Composite implements EditPersonView<Person> {

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
    MyListBox rolesList;

    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    private static EditPersonViewUiBinder ourUiBinder = GWT.create(EditPersonViewUiBinder.class);
    private EditPersonView.Presenter<Person> presenter;

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
    public HasValue<String> getRole() {
        return rolesList;
    }

    @Override
    public void setPresenter(Presenter<Person> presenter) {
        this.presenter = presenter;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
