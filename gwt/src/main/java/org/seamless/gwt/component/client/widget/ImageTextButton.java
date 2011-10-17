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
