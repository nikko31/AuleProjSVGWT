package com.auleSVGWT.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by Utente on 19/07/2016.
 */
public class SearchPersonEvent  extends GwtEvent<SearchPersonEventHandler> {
    public static Type<SearchPersonEventHandler> TYPE = new Type<SearchPersonEventHandler>();
    private final String name;
    private final String surname;


    public SearchPersonEvent(String name,String surname) {
        this.name = name;
        this.surname = surname;

    }

    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }



    public Type<SearchPersonEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SearchPersonEventHandler handler) {handler.onSearchPerson(this);}
}

