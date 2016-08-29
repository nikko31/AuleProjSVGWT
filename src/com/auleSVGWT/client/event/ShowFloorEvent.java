package com.auleSVGWT.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 18/03/16.
 */
public class ShowFloorEvent extends GwtEvent<ShowFloorEventHandler> {
    public static Type<ShowFloorEventHandler> TYPE = new Type<ShowFloorEventHandler>();
    private final String building;
    private final String floor;
    private final String roomID;
    //aggiunta
    private final String mode;

    public ShowFloorEvent(String building, String floor, String mode, String roomID) {
        this.building = building;
        this.floor = floor;
        this.mode = mode;
        this.roomID = roomID;

    }

    public String getBuilding() {
        return building;
    }

    public String getFloor() {
        return floor;
    }

    public String getMode() {
        return mode;
    }

    public String getRoomID() {
        return roomID;
    }


    public Type<ShowFloorEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowFloorEventHandler handler) {
        handler.onShowFloor(this);
    }
}
