package com.auleSVGWT.client.common;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class MyListBox extends ListBox implements HasValue<String> {
    @Override
    public String getValue() {
        return this.getSelectedValue();
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
