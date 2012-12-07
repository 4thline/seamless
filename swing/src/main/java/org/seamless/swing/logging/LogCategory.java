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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Christian Bauer
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
