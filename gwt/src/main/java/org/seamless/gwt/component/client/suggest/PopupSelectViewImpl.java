/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.seamless.gwt.component.client.suggest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import org.seamless.gwt.component.client.widget.GhostedTextBox;
import org.seamless.gwt.theme.shared.client.ThemeStyle;
import org.seamless.gwt.validation.shared.ValidationError;

import javax.inject.Inject;
import java.util.List;

/**
 * Text input box with popup for selecting suggestions.
 *
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.sl-FormField</dt>
 * <dd>any field</dd>
 * <dt>.sl-FormButton</dt>
 * <dd>any button</dd>
 * <dt>.sl-FormErrorField</dt>
 * <dd>any field with invalid input</dd>
 * <dt>.sl-GhostedTextBox</dt>
 * <dd>ghosted state of text input box</dd>
 * </dl>
 *
 * @author Christian Bauer
 */
public class PopupSelectViewImpl<S extends Suggestion> extends Composite implements SuggestionSelectView<S> {

    interface UI extends UiBinder<HorizontalPanel, PopupSelectViewImpl> {
    }

    private UI ui = GWT.create(UI.class);

    @UiField(provided = true)
    GhostedTextBox textBox;
    @UiField
    Button button;

    final int suggestionDelay;
    final int widthPixel;
    final int heightPixel;
    final DecoratedPopupPanel popupPanel = new DecoratedPopupPanel(true);
    final ListBox listBox = new ListBox(true);

    Presenter presenter;
    Timer suggestionTimer;
    int preserveEnteredCharacters;

    @Inject
    public PopupSelectViewImpl() {
        this(500, 300, 200);
    }

    public PopupSelectViewImpl(int suggestionDelay, int widthPixel, int heightPixel) {
        this.suggestionDelay = suggestionDelay;
        this.widthPixel = widthPixel;
        this.heightPixel = heightPixel;

        textBox =
            new GhostedTextBox(getSelectLabel(), ThemeStyle.GhostedTextBox()) {

                @Override
                public void onKeyUp(KeyUpEvent event) {

                    int keyCode = event.getNativeKeyCode();

                    if (keyCode == KeyCodes.KEY_ENTER) {
                        hidePopup();
                        return;
                    }

                    if (keyCode == KeyCodes.KEY_ESCAPE) {
                        hidePopup();
                        if (presenter != null)
                            presenter.reset();
                        return;
                    }

                    if (listBox.getItemCount() > 0) {
                        if (keyCode == KeyCodes.KEY_DOWN) {
                            showPopup();
                            selectIndex(listBox.getSelectedIndex() + 1);
                            return;
                        } else if (keyCode == KeyCodes.KEY_UP) {
                            showPopup();
                            selectIndex(listBox.getSelectedIndex() - 1);
                            return;
                        }
                    }

                    super.onKeyUp(event);

                    final String enteredValue = textBox.getValue();
                    if (enteredValue.length() > 0) {
                        scheduleSuggestionRequest(new Timer() {
                            public void run() {
                                preserveEnteredCharacters = enteredValue.length();
                                if (presenter != null)
                                    presenter.nameEntered(enteredValue);
                            }
                        });
                    } else {
                        if (suggestionTimer != null) suggestionTimer.cancel();
                        setSuggestions(null, -1);
                        if (presenter != null)
                            presenter.deselect();
                    }
                }

                @Override
                protected void valueChanged(String text) {
                    clearValidationError();
                }
            };

        textBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                int keyCode = event.getNativeKeyCode();
                if (keyCode == KeyCodes.KEY_TAB) {
                    hidePopup();
                }
            }
        });

        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (presenter != null)
                    presenter.suggestionSelected(listBox.getSelectedIndex());
            }
        });

        listBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
                    || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                    || event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
                    hidePopup();
                }
            }
        });

        listBox.setPixelSize(widthPixel, heightPixel);
        popupPanel.setWidget(listBox);

        initWidget(ui.createAndBindUi(this));
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void reset() {
        textBox.setValue(null);
        hidePopup();
        listBox.clear();
        this.preserveEnteredCharacters = 0;
    }

    @Override
    public void setName(String name) {
        textBox.setValue(name, true);
        if (name != null && preserveEnteredCharacters > 0) {
            int selectionLength = name.length() - preserveEnteredCharacters;
            textBox.setSelectionRange(preserveEnteredCharacters, selectionLength);
            preserveEnteredCharacters = 0;
        }
    }

    @Override
    public void setSuggestions(List<S> suggestions, int selectedIndex) {

        listBox.clear();
        if (suggestions != null) {
            for (Suggestion suggestion : suggestions) {
                listBox.addItem(suggestion.getDisplayString());
            }
        }

        if (listBox.getItemCount() > 0) {
            showPopup();
            if (preserveEnteredCharacters > 0) {
                selectIndex(0);
            } else {
                listBox.setFocus(true);
                selectIndex(selectedIndex);
            }
        } else {
            hidePopup();
        }
    }

    @Override
    public void showValidationError(ValidationError error) {
        textBox.addStyleName(ThemeStyle.FormErrorField());
    }

    @Override
    public void clearValidationError() {
        textBox.removeStyleName(ThemeStyle.FormErrorField());
    }

    @UiHandler("button")
    void onClickShowAll(ClickEvent event) {
        preserveEnteredCharacters = 0;
        if (presenter != null)
            presenter.nameEntered(null);
    }

    protected void showPopup() {
        Widget source = button;
        int left = source.getAbsoluteLeft() + 10;
        int top = source.getAbsoluteTop() + 10;

        if (top + heightPixel > Window.getClientHeight() - 20) {
            top = source.getAbsoluteTop() - 10 - heightPixel;
        }

        popupPanel.setPopupPosition(left, top);
        popupPanel.show();
    }

    protected void hidePopup() {
        popupPanel.hide();
    }

    protected void selectIndex(int index) {
        if (index < 0 || index > listBox.getItemCount() - 1) {
            return;
        }

        listBox.setSelectedIndex(index);

        if (presenter != null)
            presenter.suggestionSelected(index);
    }

    protected void scheduleSuggestionRequest(Timer timer) {
        if (suggestionTimer != null) {
            suggestionTimer.cancel();
        }
        suggestionTimer = timer;
        suggestionTimer.schedule(suggestionDelay);
    }


    protected String getSelectLabel() {
        return "Select...";
    }

    public GhostedTextBox getTextBox() {
        return textBox;
    }

    public Button getButton() {
        return button;
    }
}