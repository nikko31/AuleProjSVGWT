package com.auleSVGWT.client.event;

import com.auleSVGWT.client.dto.RoomPeopleDTO;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by darklinux on 14/03/16.
 */
public class EditRoomEvent extends GwtEvent<EditRoomEventHandler> {
    public static Type<EditRoomEventHandler> TYPE = new Type<EditRoomEventHandler>();

    private RoomPeopleDTO roomPeopleDTO;

    public EditRoomEvent(RoomPeopleDTO roomPeopleDTO) {
        this.roomPeopleDTO = roomPeopleDTO;
    }

    public RoomPeopleDTO getRoomPeopleDTO(){
        return this.roomPeopleDTO;
    }

    public Type<EditRoomEventHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(EditRoomEventHandler handler) {
        handler.onEditRoom(this);
    }
}
