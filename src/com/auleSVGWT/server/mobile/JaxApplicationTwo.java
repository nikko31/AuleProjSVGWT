package com.auleSVGWT.server.mobile;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Utente on 08/09/2016.
 */
public class JaxApplicationTwo  extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(ImagesHandler.class);
        return s;
    }




}