package com.auleSVGWT.server.mobile;

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
        s.add(JaxRestBuildings.class);
        s.add(JaxRestPeople.class);
        s.add(JaxRestImage.class);
        s.add(JaxRestBase.class);
        return s;
    }




}