 /*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.seamless.gwt.component.client.widget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Christian Bauer
 */
public class GhostedTextBox extends TextBox
        implements KeyUpHandler, ValueChangeHandler<String>, FocusHandler, BlurHandler {

    String ghostLabel = "";
    String ghostStyleName = "";

    public GhostedTextBox() {
        this("", "");
    }

    public GhostedTextBox(String ghostStyleName) {
        this("", ghostStyleName);
    }

    public GhostedTextBox(String ghostLabel, String ghostStyleName) {
        this.ghostLabel = ghostLabel;
        this.ghostStyleName = ghostStyleName;

        addKeyUpHandler(this);
        addValueChangeHandler(this);
        addFocusHandler(this);
        addBlurHandler(this);
    }

    public void clear() {
        setText(getGhostLabel());
        removeStyleName(getGhostStyleName());
        addStyleName(getGhostStyleName());
    }

    public String getGhostLabel() {
        return ghostLabel;
    }

    public String getGhostStyleName() {
        return ghostStyleName;
    }

    @Override
    public String getValue() {
        return super.getValue().equals(getGhostLabel()) ? "" : super.getValue();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        switchGhostedState(true);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        valueChanged(getText());
        if (switchGhostedState(false)) {
            valueEntered(getText()); 
        }
    }

    @Override
    public void onFocus(FocusEvent event) {
        // All of this "on focus select all or clear" behavior is a bit awkward
        // because Webkit click events destroy selection after focus. The current
        // behavior causes flickering when tabbing into a field (keyup) and is
        // not the same as clicking on a field. However, it's the most consistent
        // solution across platforms.
        if (getText().equals(getGhostLabel())) {
            setText("");
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (getText().length() == 0)
            clear();
    }

    protected boolean switchGhostedState(boolean selectOnReset) {

        String text = getText();
        if (text.length() == 0) {
            setValue(getGhostLabel());
            if (selectOnReset)
                selectAll();
        }

        text = getText();

        if (text.equals(getGhostLabel())) {
            removeStyleName(getGhostStyleName());
            addStyleName(getGhostStyleName());
            return false;
        } else {
            removeStyleName(getGhostStyleName());
            return true;
        }
    }

    protected void valueChanged(String text) {}

    protected void valueEntered(String text) {}

}
