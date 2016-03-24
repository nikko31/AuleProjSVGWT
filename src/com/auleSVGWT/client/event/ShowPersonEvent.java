package com.auleSVGWT.client.event;

import com.auleSVGWT.client.dto.PersonDTO;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 16/03/16.
 */
public class ShowPersonEvent extends GwtEvent<ShowPersonEventHandler> {
    public static Type<ShowPersonEventHandler> TYPE = new Type<ShowPersonEventHandler>();
    private final PersonDTO personDTO;

    public ShowPersonEvent(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    public PersonDTO getPersonDTO() {
        return personDTO;
    }

    public Type<ShowPersonEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowPersonEventHandler handler) {
        handler.onShowPerson(this);
    }
}
