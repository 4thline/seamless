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

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Christian Bauer
 */
public class ImageTextButton extends Button {

    private String text;
    private ImageResource imageResource;
    private boolean centered;

    public ImageTextButton() {
        super();
    }

    public void setImageResource(ImageResource imageResource) {
        this.imageResource = imageResource;
        Image img = new Image(imageResource);
        String definedStyles = img.getElement().getAttribute("style");
        img.getElement().setAttribute("style", definedStyles + "; vertical-align:middle;");
        DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement()));
    }

    public ImageResource getImageResource() {
        return imageResource;
    }

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        Element span = DOM.createElement(centered ? "div" : "span");
        span.setInnerText(text);
        if (centered) {
            span.setAttribute("style", "white-space:nowrap; padding-top:0.2em; vertical-align:middle;");
        } else {
            span.setAttribute("style", "white-space:nowrap; padding-left:0.5em; vertical-align:middle;");
        }
        DOM.insertChild(getElement(), span, 0);
    }

    @Override
    public String getText() {
        return this.text;
    }

}
