package com.auleSVGWT.client.presenter;

import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.client.event.ShowRoomEvent;
import com.auleSVGWT.client.view.ShowPersonView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Created by darklinux on 22/03/16.
 */
public class ShowPersonPresenter implements Presenter, ShowPersonView.Presenter<PersonDTO> {
    private final EventBus eventBus;
    ShowPersonView<PersonDTO> view;
    private String building;
    private String floor;
    private String number;
    private PersonDTO personDTO;

    public ShowPersonPresenter(EventBus eventBus,
                               ShowPersonView<PersonDTO> view,
                               String building,
                               String floor,
                               String number,
                               PersonDTO person) {
        this.number = number;
        this.floor = floor;
        this.building = building;
        this.personDTO = person;
        this.eventBus = eventBus;
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void go(HasWidgets mapContainer, HasWidgets infoContainer, HasWidgets headerPnl) {
        infoContainer.clear();
        infoContainer.add(view.asWidget());
        fetchPerson();
    }

    private void fetchPerson() {
        view.setPersonData(this.personDTO);
    }

    @Override
    public void onBackButtonClicked() {
        eventBus.fireEvent(new ShowRoomEvent(building, floor, number));
        History.back();
    }

}
