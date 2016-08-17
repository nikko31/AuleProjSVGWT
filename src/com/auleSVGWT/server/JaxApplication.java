package com.auleSVGWT.server;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Utente on 10/08/2016.
 */
public class JaxApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(JaxAndroidBuildings.class);
        s.add(JaxAndroidPeople.class);
        s.add(JaxAndroidRoom.class);
        s.add(JaxAndroidFloorRoomList.class);
        s.add(JaxAndroidImage.class);
        s.add(JaxAndroidBase.class);
        return s;
    }




}