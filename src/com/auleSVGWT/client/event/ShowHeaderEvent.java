package com.auleSVGWT.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 17/03/16.
 */
public class ShowHeaderEvent extends GwtEvent<ShowHeaderEventHandler> {
    public static Type<ShowHeaderEventHandler> TYPE = new Type<ShowHeaderEventHandler>();

    public Type<ShowHeaderEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowHeaderEventHandler handler) {
        handler.onShowHeader(this);
    }
}
