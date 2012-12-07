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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public class LogCategorySelector extends JDialog {

    protected final JPanel mainPanel = new JPanel();

    public LogCategorySelector(List<LogCategory> logCategories) {
        setTitle("Select logging categories...");

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLogCategories(logCategories);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        setMaximumSize(new Dimension(750, 550));
        setResizable(false);
        pack();
    }

    protected void addLogCategories(List<LogCategory> logCategories) {
        for (LogCategory logCategory : logCategories) {
            addLogCategory(logCategory);
        }
    }

    protected void addLogCategory(final LogCategory logCategory) {

        final JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBorder(BorderFactory.createTitledBorder(logCategory.getName()));

        addLoggerGroups(logCategory, categoryPanel);

        mainPanel.add(categoryPanel);
    }

    protected void addLoggerGroups(final LogCategory logCategory, final JPanel categoryPanel) {

        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        for (final LogCategory.Group group : logCategory.getGroups()) {

            final JCheckBox checkBox = new JCheckBox(group.getName());
            checkBox.setSelected(group.isEnabled());
            checkBox.setFocusable(false);
            checkBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        disableLoggerGroup(group);
                    } else if (e.getStateChange() == ItemEvent.SELECTED) {
                        enableLoggerGroup(group);
                    }
                }
            });
            checkboxPanel.add(checkBox);
        }

        JToolBar buttonBar = new JToolBar();
        buttonBar.setFloatable(false);

        JButton enableAllButton = new JButton("All");
        enableAllButton.setFocusable(false);
        enableAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (LogCategory.Group group : logCategory.getGroups()) {
                    enableLoggerGroup(group);
                }
                categoryPanel.removeAll();
                addLoggerGroups(logCategory, categoryPanel);
                categoryPanel.revalidate();
            }
        });
        buttonBar.add(enableAllButton);

        JButton disableAllButton = new JButton("None");
        disableAllButton.setFocusable(false);
        disableAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (LogCategory.Group group : logCategory.getGroups()) {
                    disableLoggerGroup(group);
                }
                categoryPanel.removeAll();
                addLoggerGroups(logCategory, categoryPanel);
                categoryPanel.revalidate();
            }
        });
        buttonBar.add(disableAllButton);

        categoryPanel.add(checkboxPanel, BorderLayout.CENTER);
        categoryPanel.add(buttonBar, BorderLayout.NORTH);
    }

    protected void enableLoggerGroup(LogCategory.Group group) {
        group.setEnabled(true);

        group.getPreviousLevels().clear();
        for (LogCategory.LoggerLevel loggerLevel : group.getLoggerLevels()) {
            Logger logger = Logger.getLogger(loggerLevel.getLogger());

            group.getPreviousLevels().add(
                    new LogCategory.LoggerLevel(logger.getName(), getLevel(logger))
            );

            logger.setLevel(loggerLevel.getLevel());
        }
    }

    protected void disableLoggerGroup(LogCategory.Group group) {
        group.setEnabled(false);

        for (LogCategory.LoggerLevel loggerLevel : group.getPreviousLevels()) {
            Logger logger = Logger.getLogger(loggerLevel.getLogger());
            logger.setLevel(loggerLevel.getLevel());
        }

        if (group.getPreviousLevels().size() == 0) {
            // Failsafe, if someone messed with the correct order of enabling/disabling e.g. in a subclass
            for (LogCategory.LoggerLevel loggerLevel : group.getLoggerLevels()) {
                Logger logger = Logger.getLogger(loggerLevel.getLogger());
                logger.setLevel(Level.INFO);
            }
        }

        group.getPreviousLevels().clear();
    }

    public Level getLevel(Logger logger) {
        Level level = logger.getLevel();
        if (level == null && logger.getParent() != null) {
            level = logger.getParent().getLevel();
        }
        return level;
    }
}
