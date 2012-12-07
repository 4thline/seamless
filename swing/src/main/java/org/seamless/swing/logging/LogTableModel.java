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

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class LogTableModel extends AbstractTableModel {

    protected int maxAgeSeconds;
    protected boolean paused = false;

    public LogTableModel(int maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

    public int getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public void setMaxAgeSeconds(int maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    protected List<LogMessage> messages = new ArrayList();

    synchronized public void pushMessage(LogMessage message) {
        if (paused) return;
        
        if (maxAgeSeconds != Integer.MAX_VALUE) {
            // Pop old messages
            Iterator<LogMessage> it = messages.iterator();
            long currentTime = new Date().getTime();
            while (it.hasNext()) {
                LogMessage logMessage = it.next();
                long delta = (maxAgeSeconds*1000);
                if ((logMessage.getCreatedOn() + delta) < currentTime) {
                    it.remove();
                }
            }
        }

        messages.add(message);
        fireTableDataChanged();
    }

    public Object getValueAt(int row, int column) {
        return messages.get(row);
    }

    public void clearMessages() {
        messages.clear();
        fireTableDataChanged();
    }

    public int getRowCount() {
        return messages.size();
    }

    public int getColumnCount() {
        return 5;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return LogMessage.class;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "";
            case 1:
                return "Time";
            case 2:
                return "Thread";
            case 3:
                return "Source";
            default:
                return "Message";
        }
    }
}
