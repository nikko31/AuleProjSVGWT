package com.auleSVGWT.client.common;

/**
 * Created by darklinux on 27/07/16.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.auleSVGWT.client.dto.PersonDTO;
import com.auleSVGWT.server.domain.Person;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;

public class AutoSuggestPage extends DialogBox {
    HashMap<String,PersonDTO>personsMap;
    FlowPanel container;
    MultiWordSuggestOracle oracle;
    public List<String> itemsSelected;
    private AutoSuggestForm autoSuggestForm;

    public AutoSuggestPage(ArrayList<PersonDTO>persons) {
        super(false, true);
        personsMap=new HashMap<>();
        for(PersonDTO person:persons){
            personsMap.put(person.getName() + " " + person.getSurname(), person);
        }

        container = new FlowPanel();
        autoSuggestForm=new AutoSuggestForm();
        container.add(autoSuggestForm);
        add(container);
        center();
        show();
    }
    public ArrayList<PersonDTO> getSelected(){
        ArrayList<PersonDTO> selectedPers=new ArrayList<>();
        List<String> items=autoSuggestForm.getItemsSelected();

        for(String name:items) {
            selectedPers.add(personsMap.get(name));
        }
        return selectedPers;
    }
    private class AutoSuggestForm extends Composite {
        FlowPanel form;
        InputListWidget inputListWidget;

        protected AutoSuggestForm() {
            form = new FlowPanel();
            initWidget(form);
            inputListWidget=new InputListWidget();
            form.add(inputListWidget);
        }

        public List<String> getItemsSelected(){
            return inputListWidget.getItemsSelected();
        }
        public void onSubmit(DomEvent<EventHandler> event) {
            // no-op
        }
    }

    /**
     * Facebook Style Autocompleter.
     * CSS and DIV structure from http://loopj.com/tokeninput/demo.html:
     */
    private class InputListWidget extends Composite {

        public InputListWidget() {
            itemsSelected = new ArrayList<String>();
            FlowPanel panel = new FlowPanel();
            initWidget(panel);
            // 2. Show the following element structure and set the last <div> to display: block
            /*
            <ul class="token-input-list-facebook">
                <li class="token-input-input-token-facebook">
                    <input type="text" style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/>
                </li>
            </ul>
            <div class="token-input-dropdown-facebook" style="display: none;"/>
             */
            final BulletList list = new BulletList();
            super.setStylePrimaryName("container");
            list.setStyleName("token-input-list-facebook");
            final ListItem item = new ListItem();
            item.setStyleName("token-input-input-token-facebook");
            final TextBox itemBox = new TextBox();
            oracle=new MultiWordSuggestOracle();
            for(String name:personsMap.keySet()){
                oracle.add(name);
            }
            final SuggestBox box = new SuggestBox(oracle, itemBox);
            box.getElement().setId("suggestion_box");
            item.add(box);
            list.add(item);

            // this needs to be on the itemBox rather than box, or backspace will get executed twice
            itemBox.addKeyDownHandler(new KeyDownHandler() {
                public void onKeyDown(KeyDownEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        // only allow manual entries with @ signs (assumed email addresses)
                        if (itemBox.getValue().contains("@"))
                            deselectItem(itemBox, list);
                    }
                    // handle backspace
                    if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                        if ("".equals(itemBox.getValue().trim())) {
                            ListItem li = (ListItem) list.getWidget(list.getWidgetCount() - 2);
                            Paragraph p = (Paragraph) li.getWidget(0);
                            if (itemsSelected.contains(p.getText())) {
                                itemsSelected.remove(p.getText());
                            }
                            list.remove(li);
                            itemBox.setFocus(true);
                        }
                    }
                }
            });

            box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
                public void onSelection(SelectionEvent selectionEvent) {
                    deselectItem(itemBox, list);
                }
            });

            panel.add(list);

            panel.getElement().setAttribute("onclick", "document.getElementById('suggestion_box').focus()");
            box.setFocus(true);
            /* Div structure after a few elements have been added:
                <ul class="token-input-list-facebook">
                    <li class="token-input-token-facebook">
                        <p>What's New Scooby-Doo?</p>
                        <span class="token-input-delete-token-facebook">x</span>
                    </li>
                    <li class="token-input-token-facebook">
                        <p>Fear Factor</p>
                        <span class="token-input-delete-token-facebook">x</span>
                     </li>
                     <li class="token-input-input-token-facebook">
                         <input type="text" style="outline-color: -moz-use-text-color; outline-style: none; outline-width: medium;"/>
                     </li>
                </ul>
             */
        }

        private void deselectItem(final TextBox itemBox, final BulletList list) {
            if (itemBox.getValue() != null && !"".equals(itemBox.getValue().trim())) {
                /** Change to the following structure:
                 * <li class="token-input-token-facebook">
                 * <p>What's New Scooby-Doo?</p>
                 * <span class="token-input-delete-token-facebook">x</span>
                 * </li>
                 */

                final ListItem displayItem = new ListItem();
                displayItem.setStyleName("token-input-token-facebook");
                Paragraph p = new Paragraph(itemBox.getValue());

                displayItem.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent clickEvent) {
                        displayItem.addStyleName("token-input-selected-token-facebook");
                    }
                });

                Span span = new Span("x");
                span.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent clickEvent) {
                        removeListItem(displayItem, list);
                    }
                });

                displayItem.add(p);
                displayItem.add(span);
                // hold the original value of the item selected
                itemsSelected.add(itemBox.getValue());

                list.insert(displayItem, list.getWidgetCount() - 1);
                itemBox.setValue("");
                itemBox.setFocus(true);
            }
        }

        private void removeListItem(ListItem displayItem, BulletList list) {
            itemsSelected.remove(displayItem.getWidget(0).getElement().getInnerHTML());
            list.remove(displayItem);
        }
        public List<String> getItemsSelected(){
            return itemsSelected;
        }
    }

    /**
     * To make this return a DTO that allows you to grab multiple values, see
     * the following tutorial:
     * <p/>
     * http://eggsylife.blogspot.com/2008/08/gwt-suggestbox-backed-by-dto-model.html
     *
     * @return names of possible contacts
     */
    public void setSuggestions(ArrayList<PersonDTO>persons){
        personsMap=new HashMap<>();
        for(PersonDTO person:persons){
            personsMap.put(person.getName()+" "+person.getSurname(),person);
        }
    }


}