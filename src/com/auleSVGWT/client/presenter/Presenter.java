package com.auleSVGWT.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Created by darklinux on 14/03/16.
 */
public abstract interface Presenter {
    public abstract void go(final HasWidgets mapContainer, final HasWidgets infoContainer, HasWidgets headerPnl);
}