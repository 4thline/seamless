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
