package com.auleSVGWT.client.common;

import com.auleSVGWT.client.dto.RoleDTO;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

import java.util.ArrayList;

/**
 * Created by Federico on 06/04/2016.
 */
public class RoleDTOListBox extends ListBox implements HasValue<RoleDTO> {
    private ArrayList<RoleDTO> roleDTOs;

    public ArrayList<RoleDTO> getRoleDTOs() {
        return roleDTOs;
    }

    public void setRoleDTOs(ArrayList<RoleDTO> roleDTOs) {
        this.roleDTOs = roleDTOs;
        for (RoleDTO roleDTO : roleDTOs) {
            super.addItem(roleDTO.getName());
        }
    }

    @Override
    public RoleDTO getValue() {
        return roleDTOs.get(super.getSelectedIndex());
    }

    @Override
    public void setValue(RoleDTO value) {
        int c=0;
        for(RoleDTO roleDTO:this.roleDTOs){
            if(value.getName().equals(roleDTO.getName())) {
                super.setSelectedIndex(c);
                break;
            }
            c++;
        }

    }

    @Override
    public void setValue(RoleDTO value, boolean fireEvents) {

    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<RoleDTO> handler) {
        return null;
    }
}
