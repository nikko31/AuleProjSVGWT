package com.auleSVGWT.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 18/03/16.
 */
public class ShowFloorEvent extends GwtEvent<ShowFloorEventHandler> {
    public static Type<ShowFloorEventHandler> TYPE = new Type<ShowFloorEventHandler>();
    private final String building;
    private final String floor;

    public ShowFloorEvent(String building,String floor) {
        this.building = building;
        this.floor=floor;

    }

    public String getBuilding() {
        return building;
    }
    public String getFloor() {
        return floor;
    }


    public Type<ShowFloorEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowFloorEventHandler handler) {
        handler.onShowFloor(this);
    }
}
