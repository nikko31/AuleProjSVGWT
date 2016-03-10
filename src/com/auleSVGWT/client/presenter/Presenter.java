package com.auleSVGWT.client.presenter;

public interface Presenter<T> {
    void onAddButtonClicked();
    void onCancelButtonClicked();
    void onItemClicked(T clickedItem);
    void onItemSelected(T selectedItem);
}