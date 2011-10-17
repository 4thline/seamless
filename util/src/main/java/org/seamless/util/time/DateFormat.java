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

package org.seamless.util.time;

import java.io.Serializable;

/**
 * Pattern for com.google.gwt.i18n.client.DateTimeFormat;
 *
 * @author Christian Bauer
 */
public class DateFormat implements Serializable {

    public static enum Preset {
        DD_MM_YYYY_DOT(new DateFormat("31.12.2010", "dd.MM.yyyy")),
        MM_DD_YYYY_DOT(new DateFormat("12.31.2010", "MM.dd.yyyy")),
        YYYY_MM_DD_DOT(new DateFormat("2010.12.31", "yyyy.MM.dd")),
        YYYY_DD_MM_DOT(new DateFormat("2010.31.12", "yyyy.dd.MM")),
        DD_MM_YYYY_SLASH(new DateFormat("31/12/2010", "dd/MM/yyyy")),
        MM_DD_YYYY_SLASH(new DateFormat("12/31/2010", "MM/dd/yyyy")),
        YYYY_MM_DD_SLASH(new DateFormat("2010/12/31", "yyyy/MM/dd")),
        YYYY_DD_MM_SLASH(new DateFormat("2010/31/12", "yyyy/dd/MM")),
        YYYY_MMM_DD(new DateFormat("2010 Dec 31", "yyyy MMM dd")),
        DD_MMM_YYYY(new DateFormat("31 Dec 2010", "dd MMM yyyy")),
        MMM_DD_YYYY(new DateFormat("Dec 31 2010", "MMM dd yyyy"));

        protected DateFormat dateFormat;

        Preset(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        public DateFormat getDateFormat() {
            return dateFormat;
        }
    }

    protected String label;
    protected String pattern;

    public DateFormat() {
    }

    DateFormat(String label, String pattern) {
        this.label = label;
        this.pattern = pattern;
    }

    public DateFormat(String pattern) {
        this.label = pattern;
        this.pattern = pattern;
    }

    public String getLabel() {
        return label;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateFormat that = (DateFormat) o;

        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pattern != null ? pattern.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getLabel() + ", Pattern: " + getPattern();
    }
}
