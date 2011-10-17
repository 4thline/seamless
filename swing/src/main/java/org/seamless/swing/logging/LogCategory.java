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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 *
 */
public class LogCategory {

    private String name;
    private List<Group> groups = new ArrayList();

    public LogCategory(String name) {
        this.name = name;
    }

    public LogCategory(String name, Group[] groups) {
        this.name = name;
        this.groups = Arrays.asList(groups);
    }

    public String getName() {
        return name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void addGroup(String name, LoggerLevel[] loggerLevels) {
        groups.add(new Group(name, loggerLevels));
    }

    static public class Group {

        private String name;
        private List<LoggerLevel> loggerLevels = new ArrayList();
        private List<LoggerLevel> previousLevels  = new ArrayList();
        private boolean enabled;

        public Group(String name) {
            this.name = name;
        }

        public Group(String name, LoggerLevel[] loggerLevels) {
            this.name = name;
            this.loggerLevels = Arrays.asList(loggerLevels);
        }

        public String getName() {
            return name;
        }

        public List<LoggerLevel> getLoggerLevels() {
            return loggerLevels;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<LoggerLevel> getPreviousLevels() {
            return previousLevels;
        }

        public void setPreviousLevels(List<LoggerLevel> previousLevels) {
            this.previousLevels = previousLevels;
        }
    }

    static public class LoggerLevel {
        private String logger;
        private Level level;

        public LoggerLevel(String logger, Level level) {
            this.logger = logger;
            this.level = level;
        }

        public String getLogger() {
            return logger;
        }

        public Level getLevel() {
            return level;
        }
    }
}
