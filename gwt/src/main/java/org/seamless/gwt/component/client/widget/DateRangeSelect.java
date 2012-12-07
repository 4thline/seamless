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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import org.seamless.util.time.DateFormat;
import org.seamless.util.time.DateRange;
import org.seamless.util.time.DateRangeOption;

import javax.inject.Inject;
import java.util.Date;

/**
 * Drop-down with presets or custom from/to date selection input fields.
 *
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.sl-FormField</dt>
 * <dd>any field</dd>
 * </dl>
 *
 * @author Christian Bauer
 */
public class DateRangeSelect extends Composite implements HasValue<DateRange> {

    interface UI extends UiBinder<HTMLPanel, DateRangeSelect> {
    }

    private UI ui = GWT.create(UI.class);

    @UiField
    ListBox optionListBox;
    @UiField
    AutocompleteDateTextBox startDateBox;
    @UiField
    AutocompleteDateTextBox endDateBox;

    @Inject
    public DateRangeSelect() {
        initWidget(ui.createAndBindUi(this));

        for (DateRangeOption option : DateRangeOption.values()) {
            optionListBox.addItem(option.getLabel());
        }

        optionListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (isCustomSelected()) {
                    // Preselect "month to date" so the fields don't appear empty
                    startDateBox.setValue(DateRangeOption.MONTH_TO_DATE.getDateRange().getStart());
                    endDateBox.setValue(new Date());
                    startDateBox.setVisible(true);
                    endDateBox.setVisible(true);
                } else {
                    startDateBox.setVisible(false);
                    endDateBox.setVisible(false);
                }
                ValueChangeEvent.fire(DateRangeSelect.this, getValue());
            }
        });

        ValueChangeHandler fromToDateFilterChangeHandler = new ValueChangeHandler() {
            @Override
            public void onValueChange(ValueChangeEvent valueChangeEvent) {
                DateRange value = getValue();
                if (value.isValid())
                    ValueChangeEvent.fire(DateRangeSelect.this, getValue());
            }
        };

        startDateBox.addValueChangeHandler(fromToDateFilterChangeHandler);
        startDateBox.getTextBox().addValueChangeHandler(fromToDateFilterChangeHandler);
        endDateBox.addValueChangeHandler(fromToDateFilterChangeHandler);
        endDateBox.getTextBox().addValueChangeHandler(fromToDateFilterChangeHandler);

        reset();
    }

    @Override
    public DateRange getValue() {
        return isCustomSelected()
            ? new DateRange(startDateBox.getValue(), endDateBox.getValue())
            : DateRangeOption.values()[optionListBox.getSelectedIndex()].getDateRange();
    }

    @Override
    public void setValue(DateRange value) {
        if (value == null || !value.hasStartOrEnd()) {
            reset();
            return;
        }
        boolean custom = true;
        for (int i = 0; i < DateRangeOption.values().length; i++) {
            DateRangeOption option = DateRangeOption.values()[i];
            if (option.getDateRange() != null && option.getDateRange().equals(value)) {
                optionListBox.setSelectedIndex(i);
                custom = false;
                break;
            }
        }
        if (custom) {
            optionListBox.setSelectedIndex(DateRangeOption.values().length - 1);
            startDateBox.setVisible(true);
            endDateBox.setVisible(true);
            startDateBox.setValue(value.getStart());
            endDateBox.setValue(value.getEnd());
        } else {
            startDateBox.setVisible(false);
            endDateBox.setVisible(false);
        }
    }

    @Override
    public void setValue(DateRange value, boolean fireEvents) {
        setValue(value);
        if (fireEvents)
            ValueChangeEvent.fire(this, value);
    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addValueChangeHandler(ValueChangeHandler<DateRange> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void setDateFormat(DateFormat df) {
        startDateBox.setDateFormat(df);
        endDateBox.setDateFormat(df);
    }

    public void reset() {
        optionListBox.setSelectedIndex(0);
        startDateBox.setValue(null);
        startDateBox.setVisible(false);
        endDateBox.setValue(null);
        endDateBox.setVisible(false);
    }

    public boolean isAllSelected() {
        return optionListBox.getSelectedIndex() == 0;
    }

    public boolean isCustomSelected() {
        return optionListBox.getSelectedIndex() == DateRangeOption.values().length - 1;
    }

}
