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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import org.seamless.util.time.DateFormat;

import java.util.Date;

/**
 * @author Christian Bauer
 */
public class AutocompleteDateTextBox extends DateBox {

    final protected boolean sane;
    final protected String separator;

    protected DateTimeFormat fmt = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM);
    protected String fmtPattern;
    protected boolean defaultToday;

    public AutocompleteDateTextBox() {
        this(null);
    }

    public AutocompleteDateTextBox(DateFormat df) {
        this(true, true, ".");
        setDateFormat(df);
    }

    public AutocompleteDateTextBox(boolean defaultToday, boolean sane, String separator) {
        this.defaultToday = defaultToday;
        this.sane = sane;
        this.separator = separator;

        setFormat(new DateBox.DefaultFormat(fmt));

        getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                parseValueAndSetDate();
            }
        });
    }

    public void setDefaultToday(boolean defaultToday) {
        this.defaultToday = defaultToday;
    }

    public void selectDay() {
        if (fmtPattern != null && fmtPattern.contains("d")) {
            getTextBox().setSelectionRange(fmtPattern.indexOf('d'), 2);
        } else {
            getTextBox().selectAll();
        }
    }

    public void reset() {
        if (defaultToday) {
            setValue(new Date());
        } else {
            setValue(null);
        }
    }

    public void setDateFormat(DateFormat df ) {
        if (df == null) return;
        this.fmt = DateTimeFormat.getFormat(df.getPattern());
        this.fmtPattern = df.getPattern();
        setFormat(new DateBox.DefaultFormat(fmt));
    }

    protected void parseValueAndSetDate() {
        Date today = new Date();

        // This text is already "parsed", that is, the superclass with all its private
        // crap has already destroyed the raw text with its "lenient" parsing of dates.
        // This is basically the user's input mangled through fmt.parse(text).
        String text = getTextBox().getText();

        if (text.length() == 0) {
            if (defaultToday)
                setValue(today);
            return;
        }

        // Well we need to try again to see if it was kosher - if it was we have
        // to rely on it being right. Even if it isn't, for example the pattern "dd.mm.yyyy"
        // will accept "31.12.2010" and turn it into a garbage date.
        // TODO: Maybe the GWT interns will fix this in the next release
        try {
            setValue(fmt.parse(text));
            return;
        } catch (IllegalArgumentException ex) {
            // Try our custom parsing routines below
        }

        // Sorry folks, no Calendar or regex Matcher API in GWT
        int year = today.getYear();
        int month = today.getMonth();
        int date = today.getDate();

        if (text.matches("[0-9]{1,2}(\\" + separator + ")?")) {
            // Matches "11" and "01" for day or month, optional separator at the end
            String[] strings = text.split("\\" + separator);
            String paddedOne = strings[0].length() == 1 ? "0" + strings[0] : strings[0];
            if (sane) {
                date = Integer.valueOf(paddedOne);
            } else {
                month = Integer.valueOf(paddedOne) - 1;
            }
        } else if (text.matches("[0-9]{1,2}\\" + separator + "[0-9]{1,2}(\\" + separator + ")?")) {
            // Day/month with separator and optional separator at the end
            String[] strings = text.split("\\" + separator);
            String paddedOne = strings[0].length() == 1 ? "0" + strings[0] : strings[0];
            String paddedTwo = strings[1].length() == 1 ? "0" + strings[1] : strings[1];
            if (sane) {
                date = Integer.valueOf(paddedOne);
                month = Integer.valueOf(paddedTwo) - 1;
            } else {
                date = Integer.valueOf(paddedTwo);
                month = Integer.valueOf(paddedOne) - 1;
            }
        } else if (text.matches("[0-9]{1,2}\\" + separator + "[0-9]{1,2}\\" + separator + "[0-9]{1,4}")) {
            // Day/month/year
            String[] strings = text.split("\\" + separator);
            String paddedOne = strings[0].length() == 1 ? "0" + strings[0] : strings[0];
            String paddedTwo = strings[1].length() == 1 ? "0" + strings[1] : strings[1];
            String paddedThree = strings[2];
            if (paddedThree.length() == 1) {
                paddedThree = Integer.toString(today.getYear() + 1900).substring(0, 3) + paddedThree;
            } else if (paddedThree.length() == 2) {
                paddedThree = Integer.toString(today.getYear() + 1900).substring(0, 2) + paddedThree;
            } else if (paddedThree.length() == 3) {
                paddedThree = Integer.toString(today.getYear() + 1900).substring(0, 1) + paddedThree;
            }
            if (sane) {
                date = Integer.valueOf(paddedOne);
                month = Integer.valueOf(paddedTwo) - 1;
            } else {
                date = Integer.valueOf(paddedTwo);
                month = Integer.valueOf(paddedOne) - 1;
            }
            year = Integer.valueOf(paddedThree) - 1900;
        } else if (!defaultToday) {
            setValue(null);
            return;
        }

        setValue(new Date(year, month, date));
    }
}
