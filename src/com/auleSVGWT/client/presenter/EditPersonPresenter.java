package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoleDTO;
import com.auleSVGWT.client.view.EditPersonView;
import com.auleSVGWT.client.view.EditPersonViewImpl;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import java.util.ArrayList;

/**
 * Created by darklinux on 16/03/16.
 */
public class EditPersonPresenter implements Presenter, EditPersonView.Presenter<PersonDTO> {
    private PersonDTO personDTO;
    private final AuleSVGWTServiceAsync rpcService;
    private EditPersonView<PersonDTO> view;
    private final EventBus eventBus;

    public EditPersonPresenter(EventBus eventBus, AuleSVGWTServiceAsync rpcService, EditPersonViewImpl editPersonView, PersonDTO personDTO) {
        this.rpcService = rpcService;
        this.eventBus = eventBus;
        this.personDTO = personDTO;
        this.view = editPersonView;
        this.view.setPresenter(this);

    }


    @Override
    public void onSaveButtonClicked() {
    }

    @Override
    public void onCancelButtonClicked() {

    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        infoContainer.clear();
        infoContainer.add(view.asWidget());

        fetchRoles();
        view.setPersonData(personDTO);

    }
    public void fetchRoles(){
        rpcService.getRoles(new AsyncCallback<ArrayList<RoleDTO>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ArrayList<RoleDTO> result) {
                view.setRolesData(result);
            }
        });
    }
}
