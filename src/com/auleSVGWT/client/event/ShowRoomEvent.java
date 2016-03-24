package com.auleSVGWT.client.event;

import com.auleSVGWT.client.shared.FloorDetails;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 14/03/16.
 */
public class ShowRoomEvent extends GwtEvent<ShowRoomEventHandler> {
    public static Type<ShowRoomEventHandler> TYPE = new Type<ShowRoomEventHandler>();
    private String building;
    private String floor;
    private String number;

    public ShowRoomEvent(String building, String floor, String number) {
        this.building = building;
        this.floor = floor;
        this.number = number;
    }

    public String getBuilding() {

        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Type<ShowRoomEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowRoomEventHandler handler) {
        handler.onShowRoom(this);
    }
}
