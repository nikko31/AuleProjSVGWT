package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.AuleSVGWTServiceAsync;
import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.dto.RoleDTO;
import com.auleSVGWT.client.event.EditRoomEvent;
import com.auleSVGWT.client.view.EditPersonView;
import com.auleSVGWT.client.view.EditPersonViewImpl;
import com.auleSVGWT.server.domain.Person;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
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
        String phone="";
        personDTO.setName(view.getFirstName().getValue());
        personDTO.setSurname(view.getLastName().getValue());
        personDTO.setEmail(view.getEmailAddress().getValue());
        phone=view.getPhone().getValue();
        phone=phone.replace(" ","");
        phone=phone.replace("-","");
        if(phone!=""){
            if(checkPhoneNumber(phone))
                personDTO.setPhone(phone);
            else
                Window.alert("Errore inserimento numero di telefono");
        }
        else
            personDTO.setPhone("");
        personDTO.setRole(view.getRole().getValue());
        if(this.personDTO.getId()<0){
            rpcService.savePerson(personDTO, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Errore durante il salvataggio della persona.");
                }

                @Override
                public void onSuccess(Integer result) {
                    History.back();
                }
            });
        }
        else {
            rpcService.updatePerson(personDTO, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Errore nell'update della persona.");
                }

                @Override
                public void onSuccess(Integer result) {
                    History.back();
                }
            });
        }
    }

    public boolean checkPhoneNumber(String phone){
        phone=phone.replace(" ","");
        phone=phone.replace("-","");
        for(int c=0;c<phone.length();c++){
            if(c==0 && !(phone.charAt(0)=='+'||Character.isDigit(phone.charAt(0)))){
                return false;
            }
            else {
                if(!Character.isDigit(phone.charAt(c))){
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onCancelButtonClicked() {
        //eventBus.fireEvent(new EditRoomEvent());
        History.back();
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
                Window.alert("Errore durante la richiesta dei ruoli.");
            }
            @Override
            public void onSuccess(ArrayList<RoleDTO> result) {
                view.setRolesData(result,personDTO.getRole());
            }
        });
    }
}
