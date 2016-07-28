package com.auleSVGWT.client.event;

import com.auleSVGWT.client.dto.PersonDTO;
import com.google.gwt.event.shared.GwtEvent;

import java.util.ArrayList;

/**
 * Created by Utente on 19/07/2016.
 */
public class SearchPersonEvent  extends GwtEvent<SearchPersonEventHandler> {
    public static Type<SearchPersonEventHandler> TYPE = new Type<SearchPersonEventHandler>();
    private final ArrayList<PersonDTO>personsSelected;


    public SearchPersonEvent(ArrayList<PersonDTO> personsSelected){
        this.personsSelected = personsSelected;
    }

    public ArrayList<PersonDTO> getPersonsSelected() {
        return personsSelected;
    }

    public Type<SearchPersonEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SearchPersonEventHandler handler) {handler.onSearchPerson(this);}
}

