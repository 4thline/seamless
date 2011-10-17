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

import java.math.BigDecimal;

/**
 * @author Christian Bauer
 */
public class BigDecimalGhostedTextBox extends GhostedTextBox {

    public BigDecimalGhostedTextBox() {
    }

    public BigDecimalGhostedTextBox(String ghostStyleName) {
        super(ghostStyleName);
    }

    public BigDecimalGhostedTextBox(String ghostLabel, String ghostStyleName) {
        super(ghostLabel, ghostStyleName);
    }

    public void setBigDecimalValue(BigDecimal value) {
        setValue(value == null || value.signum() == 0 ? null : value.toString(), true);
    }

    public BigDecimal getBigDecimalValue() {
        if (getValue().length() == 0) return null;
        try {
            return new BigDecimal(getValue());
        } catch (Exception ex) {
            // Well...
        }
        return null;
    }
}
