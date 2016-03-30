package com.auleSVGWT.client.event;

import com.auleSVGWT.client.dto.PersonDTO;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 30/03/16.
 */
public class EditPersonEvent extends GwtEvent<EditPersonHandler> {
    public static Type<EditPersonHandler> TYPE = new Type<EditPersonHandler>();

    public PersonDTO getPersonDTO() {
        return personDTO;
    }

    private PersonDTO personDTO;

    public EditPersonEvent(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    public Type<EditPersonHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(EditPersonHandler handler) {
        handler.onEditPerson(this);
    }
}
