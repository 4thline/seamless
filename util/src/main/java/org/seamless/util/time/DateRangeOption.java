/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.seamless.util.time;

import java.io.Serializable;

/**
 * @author Christian Bauer
 */
public enum DateRangeOption implements Serializable {

    ALL("All dates", DateRange.Preset.ALL.getDateRange()),
    MONTH_TO_DATE("Month to date", DateRange.Preset.MONTH_TO_DATE.getDateRange()),
    YEAR_TO_DATE("Year to date", DateRange.Preset.YEAR_TO_DATE.getDateRange()),
    LAST_MONTH("Last month", DateRange.Preset.LAST_MONTH.getDateRange()),
    LAST_YEAR("Last year", DateRange.Preset.LAST_YEAR.getDateRange()),
    CUSTOM("Custom dates", null);

    String label;
    DateRange dateRange;

    DateRangeOption(String label, DateRange dateRange) {
        this.label = label;
        this.dateRange = dateRange;
    }

    public String getLabel() {
        return label;
    }

    public DateRange getDateRange() {
        return dateRange;
    }
}
