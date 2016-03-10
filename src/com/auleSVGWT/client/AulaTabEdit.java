package com.auleSVGWT.client;

import com.auleSVGWT.client.presenter.Presenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Created by darklinux on 09/03/16.
 */
public class AulaTabEdit extends Composite {
    interface AulaTabEditUiBinder extends UiBinder<Widget, AulaTabEdit> {
    }

    @UiField
    Button addButton;
    @UiField
    FlexTable table;
    @UiField
    TextBox numAula;
    @UiField
    TextBox numPosti;
    @UiField
    TextBox mtQ;

    @UiHandler("addButton")
    void onAddButtonClicked(ClickEvent evt) {
        if(presenter!=null){
            this.presenter.onAddButtonClicked();
        }
    }

    private Presenter presenter;
    private static AulaTabEditUiBinder ourUiBinder = GWT.create(AulaTabEditUiBinder.class);

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }


    public AulaTabEdit() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}