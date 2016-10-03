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

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by darklinux on 16/03/16.
 */
public class EditPersonPresenter implements Presenter, EditPersonView.Presenter<PersonDTO> {
    private PersonDTO personDTO;
    private final AuleSVGWTServiceAsync rpcService;
    private EditPersonView<PersonDTO> view;
    private final EventBus eventBus;
    private boolean flag;

    public EditPersonPresenter(EventBus eventBus, AuleSVGWTServiceAsync rpcService, EditPersonViewImpl editPersonView, PersonDTO personDTO) {
        this.rpcService = rpcService;
        this.eventBus = eventBus;
        this.personDTO = personDTO;
        this.view = editPersonView;
        this.view.setPresenter(this);


    }


    @Override
    public void onSaveButtonClicked() {


        String name = view.getFirstName().getValue();
        String surname = view.getLastName().getValue();
        String email = view.getEmailAddress().getValue();
        String phone = view.getPhone().getValue();
        boolean flag2 = false;

        String msg1 = "Questi parametri sono stati inseriti in modo errato:\n";

        if(name != null){
            if(checkPersonNameOrSurname(name) && name.length()>2 && name.length()<30){
                personDTO.setName(name);
            }else{
                msg1+=" nome,";
                flag2 = true;
            }
        }else{
            msg1+=" nome,";
            flag2 = true;
        }
        //-----------------------------
        if(surname != null){
            if(checkPersonNameOrSurname(surname) && surname.length()>2 && surname.length()<30){
                personDTO.setSurname(surname);
            }else{
                msg1+=" cognome,";
                flag2 = true;
            }
        }else{
            msg1+=" cognome,";
            flag2 = true;
        }
        //----------------------------
        if(view.getRole().getValue() != null) {
            personDTO.setRole(view.getRole().getValue());
        }else{
            msg1+=" ruolo,";
            flag2 = true;
        }
        //------------------------------
        if(email != null){
            email = email.replace(" ","");
            if(!email.equals("") ){
                if(checkEmail(email) && email.contains("@") && email.length()<255){
                    personDTO.setEmail(email);
                }else{
                    msg1+=" email,";
                    flag2 = true;

                }
            }


        }
        //--------------------------------------------
        if(phone != null){
            phone=phone.replace(" ","");
            phone=phone.replace("-","");
            if(!phone.equals("")){
                if(checkPhoneNumber(phone)  && phone.length()>5 && phone.length()<15){
                    personDTO.setPhone(phone);
                }
                else{
                    flag2 = true;
                    msg1+=" numero di telefono,";
                }

            }

        }
        //--------------------------------------
        if (view.getStartWork().getValue() != null){
            personDTO.setStartWork(new Date(view.getStartWork().getValue().getTime()));
        }
        //-------------------------------------
        if (view.getEndWork().getValue() != null){
            personDTO.setEndWork(new Date(view.getEndWork().getValue().getTime()));
        }
        //--------------------------------------

        if (flag2){
            Window.alert(msg1);
        }

        if(!flag2){
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

    }

    public boolean checkPhoneNumber(String phone){

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

    public boolean checkPersonNameOrSurname(String string){
        for(int c=0;c<string.length();c++){
            if(!Character.isLetter(string.charAt(c)) && string.charAt(c)!='\'' && string.charAt(c) !=' ' ){
                return false;
            }
        }
        return true;

    }
    public boolean checkEmail(String string){
        for(int c=0;c<string.length();c++){
            if(!Character.isLetter(string.charAt(c)) && !Character.isDigit(string.charAt(c))  &&  string.charAt(c)!='\''
                    && string.charAt(c) !=' ' &&  string.charAt(c)!='.'  &&  string.charAt(c)!='@'){
                return false;
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
