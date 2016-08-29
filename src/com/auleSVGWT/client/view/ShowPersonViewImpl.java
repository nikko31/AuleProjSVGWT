package com.auleSVGWT.client.view;

import com.auleSVGWT.client.dto.PersonDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

/**
 * Created by Dark-Linux on 15/03/2016.
 */
public class ShowPersonViewImpl extends Composite implements ShowPersonView<PersonDTO> {

    @UiTemplate("ShowPersonView.ui.xml")
    interface ShowPersonViewUiBinder extends UiBinder<Widget, ShowPersonViewImpl> {
    }

    @UiField
    Label firstNameLbl;
    @UiField
    Label lastNameLbl;
    @UiField
    Label emailLbl;
    @UiField
    Label phoneLnl;
    @UiField
    Label rolesLbl;

    @UiField
    Button backButton;


    private static ShowPersonViewUiBinder ourUiBinder = GWT.create(ShowPersonViewUiBinder.class);
    private ShowPersonView.Presenter<PersonDTO> presenter;

    public ShowPersonViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @UiHandler("backButton")
    void onBackButtonClicked(ClickEvent event) {
        if (presenter != null)
            presenter.onBackButtonClicked();
    }

    @Override
    public void setPersonData(PersonDTO person) {
        this.firstNameLbl.setText(person.getName());
        this.lastNameLbl.setText(person.getSurname());
        this.emailLbl.setText(person.getEmail());
        this.phoneLnl.setText(person.getPhone());
        this.rolesLbl.setText(person.getRole().getName());
    }

    @Override
    public void setPresenter(Presenter<PersonDTO> presenter) {
        this.presenter = presenter;
        backButton.setStyleName("default-button-cancel");
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
