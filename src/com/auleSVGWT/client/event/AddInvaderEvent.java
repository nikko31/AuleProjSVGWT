package com.auleSVGWT.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 14/03/16.
 */
public class AddInvaderEvent extends GwtEvent<AddInvaderEventHandler> {
    public static Type<AddInvaderEventHandler> TYPE = new Type<AddInvaderEventHandler>();

    public Type<AddInvaderEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(AddInvaderEventHandler handler) {
        handler.onAddInvader(this);
    }
}
