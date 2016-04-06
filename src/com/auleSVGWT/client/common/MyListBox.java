package com.auleSVGWT.client.common;

import com.auleSVGWT.client.dto.RoleDTO;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class MyListBox<S> extends ListBox implements HasValue<String> {
    @Override
    public String getValue() {
        return this.getSelectedValue();
    }

    public void addValue(RoleDTO roleDTO){
        this.addItem(roleDTO.getName());
    }
    public void addValue(String string){
        this.addItem(string);
    }

    @Override
    public void setValue(String value) {
        boolean find=false;
        for(int c=0;c<super.getItemCount();c++)
            if(super.getValue(c).equals(value))
                find=true;

        if(!find)
            this.addItem(value);
    }

    @Override
    public void setValue(String value, boolean fireEvents) {

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return null;
    }
}
