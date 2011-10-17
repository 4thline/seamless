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

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Christian Bauer
 */
public abstract class ClickableTextColumn<T> extends Column<T, String> {

    protected String styleName;

    protected ClickableTextColumn(String styleName) {
        super(new ClickableTextCell() {
/*
            @Override
            public boolean handlesSelection() {
                return true;
            }
*/
        });
        this.styleName = styleName;
        setFieldUpdater(
                new FieldUpdater<T, String>() {
                    public void update(int index, T object, String value) {
                        onClick(index, object, value);
                    }
                }
        );
    }

    protected ClickableTextColumn() {
        this(null);
    }

    public String getStyleName() {
        return styleName;
    }

    // Can't access the real column value in ClickableTextCell.render(), so we have to
    // override rendering here. Evil new API in GWT 2.2 written by propellerheads who
    // prefer "clean" over "functional".

    @Override
    public void render(Cell.Context context, T object, SafeHtmlBuilder sb) {
        String value = getValue(object);
        if (value != null) {
            if (isStyled(object) && getStyleName() != null) {
                sb.appendHtmlConstant("<div class=\"" + getStyleName() + "\">");
            } else {
                sb.appendHtmlConstant("<div>");
            }
            super.render(context, object, sb);
            sb.appendHtmlConstant("</div>");
        }
    }

    abstract protected void onClick(int index, T object, String value);

    protected boolean isStyled(T object) {
        return true;
    }

}
