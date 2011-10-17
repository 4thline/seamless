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

package org.seamless.swing.logging;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
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
