package com.auleSVGWT.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 14/03/16.
 */
public class AddPersonEvent extends GwtEvent<AddPersonEventHandler> {
    public static Type<AddPersonEventHandler> TYPE = new Type<AddPersonEventHandler>();

    public Type<AddPersonEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AddPersonEventHandler handler) {
        handler.onAddPerson(this);
    }
}
