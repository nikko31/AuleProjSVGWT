package com.auleSVGWT.client.shared;

import java.io.Serializable;

/**
 * Created by darklinux on 14/03/16.
 */
public class PersonDetails implements Serializable{
    private String id;
    private String displayName;

    public PersonDetails(){
    }

    public PersonDetails(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
