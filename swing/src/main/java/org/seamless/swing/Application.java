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
package org.seamless.swing;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Application {

    public static void showError(Component parent, Throwable ex) {
        final JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
        textArea.setEditable(false);
        StringWriter writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        textArea.setText(writer.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        JOptionPane.showMessageDialog(parent, scrollPane, "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(Component parent, String... warningLines) {
        final JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
        textArea.setEditable(false);
        for (String s : warningLines) textArea.append(s + "\n");
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        JOptionPane.showMessageDialog(parent, scrollPane, "Warning", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String... infoLines) {
        final JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));
        textArea.setEditable(false);
        for (String s : infoLines) textArea.append(s + "\n");
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        JOptionPane.showMessageDialog(parent, scrollPane, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

/*
    public static void showWarning(Component parent, InvalidValue[] invalidValues) {
        String[] warnings = new String[invalidValues.length];
        int i = 0;
        for (InvalidValue invalidValue : invalidValues) warnings[i++] = invalidValue.getMessage();
        showWarning(parent, warnings);
    }
    */

    public static void increaseFontSize(JComponent l) {
        l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), l.getFont().getSize() + 2));
    }

    public static void decreaseFontSize(JComponent l) {
        l.setFont(new Font(l.getFont().getFontName(), l.getFont().getStyle(), l.getFont().getSize() - 2));
    }

    public static Window center(Window w) {
        // After packing a Frame or Dialog, centre it on the screen.
        Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
        int newX = (them.width - us.width) / 2;
        int newY = (them.height - us.height) / 2;
        if (newX < 0) newX = 0;
        if (newY < 0) newY = 0;
        w.setLocation(newX, newY);
        return w;
    }

    public static Window center(Window w, Window reference) {
        double refCenterX = reference.getX() + (reference.getSize().getWidth()/2);
        double refCenterY = reference.getY() + (reference.getSize().getHeight()/2);
        int newX = (int) (refCenterX - (w.getSize().getWidth()/2));
        int newY = (int) (refCenterY - (w.getSize().getHeight()/2));
        w.setLocation(newX, newY);
        return w;
    }

    public static Window maximize(Window w) {
        Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit().getScreenSize();
        w.setBounds(0, 0, them.width, them.height);
        return w;
    }

    public static ImageIcon createImageIcon(Class base, String path, String description) {
        java.net.URL imgURL = base.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            throw new RuntimeException("Couldn't find image icon on path: " + path);
        }
    }

    public static ImageIcon createImageIcon(Class base, String path) {
        return createImageIcon(base, path, null);
    }

    public static void copyToClipboard(String s) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection data = new StringSelection(s);
        clipboard.setContents(data, data);
    }

}
