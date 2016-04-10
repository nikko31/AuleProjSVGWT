package com.auleSVGWT.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 18/03/16.
 */
public class ShowFloorEvent extends GwtEvent<ShowFloorEventHandler> {
    public static Type<ShowFloorEventHandler> TYPE = new Type<ShowFloorEventHandler>();
    private final String building;
    private final String floor;
    //aggiunta
    private final String modality;

    public ShowFloorEvent(String building,String floor,String modality) {
        this.building = building;
        this.floor = floor;
        this.modality = modality;

    }

    public String getBuilding() {
        return building;
    }
    public String getFloor() {
        return floor;
    }
    public String getModality(){ return modality;}



    public Type<ShowFloorEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(ShowFloorEventHandler handler) {
        handler.onShowFloor(this);
    }
}
