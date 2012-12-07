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
package org.seamless.swing.logging;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * @author Christian Bauer
 */
public abstract class LogTableCellRenderer extends DefaultTableCellRenderer {

    // Only accessed by EDT
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {

        LogMessage message = (LogMessage) value;

        switch (column) {
            case 0:
                if (message.getLevel().equals(Level.SEVERE) ||
                        message.getLevel().equals(Level.WARNING)) {

                    return new JLabel(getWarnErrorIcon());

                } else if (message.getLevel().equals(Level.FINE)) {

                    return new JLabel(getDebugIcon());

                } else if (message.getLevel().equals(Level.FINER) ||
                        message.getLevel().equals(Level.FINEST)) {

                    return new JLabel(getTraceIcon());


                } else {

                    return new JLabel(getInfoIcon());

                }

            case 1:
                Date date = new Date(message.getCreatedOn());
                return super.getTableCellRendererComponent(
                        table, dateFormat.format(date), isSelected, hasFocus, row, column
                );
            case 2:
                return super.getTableCellRendererComponent(
                        table, message.getThread(), isSelected, hasFocus, row, column
                );
            case 3:
                return super.getTableCellRendererComponent(
                        table, message.getSource(), isSelected, hasFocus, row, column
                );
            default:
                return super.getTableCellRendererComponent(
                        table, message.getMessage().replaceAll("\n", "<NL>").replaceAll("\r", "<CR>"), isSelected, hasFocus, row, column
                );
        }
    }

    protected abstract ImageIcon getWarnErrorIcon();

    protected abstract ImageIcon getInfoIcon();

    protected abstract ImageIcon getDebugIcon();

    protected abstract ImageIcon getTraceIcon();
}
