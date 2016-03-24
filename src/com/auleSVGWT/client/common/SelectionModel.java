package com.auleSVGWT.client.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darklinux on 16/03/16.
 */
public class SelectionModel<T> {
    List<T> selectedItems = new ArrayList<T>();

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    public void addSelection(T item) {
        selectedItems.add(item);
    }

    public void removeSelection(T item) {
        selectedItems.remove(item);
    }

    public boolean isSelected(T item) {
        return selectedItems.contains(item);
    }
}