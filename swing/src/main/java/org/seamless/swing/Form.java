package org.seamless.swing;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * http://javatechniques.com/blog/gridbaglayout-example-a-simple-form-layout/
 * <br/>
 * Simple utility class for creating forms that have a column
 * of labels and a column of fields. All of the labels have the
 * same width, determined by the width of the widest label
 * component.
 * <p>
 * Philip Isenhour - 060628 - http://javatechniques.com/
 * </p>
 */
public class Form {

    /**
     * Grid bag constraints for fields and labels
     */
    public GridBagConstraints lastConstraints = null;
    public GridBagConstraints middleConstraints = null;
    public GridBagConstraints labelConstraints = null;
    public GridBagConstraints separatorConstraints = null;

    public Form(int padding) {
        // Set up the constraints for the "last" field in each
        // row first, then copy and modify those constraints.

        // weightx is 1.0 for fields, 0.0 for labels
        // gridwidth is REMAINDER for fields, 1 for labels
        lastConstraints = new GridBagConstraints();

        // Stretch components horizontally (but not vertically)
        lastConstraints.fill = GridBagConstraints.HORIZONTAL;

        // Components that are too short or narrow for their space
        // Should be pinned to the northwest (upper left) corner
        lastConstraints.anchor = GridBagConstraints.NORTHWEST;

        // Give the "last" component as much space as possible
        lastConstraints.weightx = 1.0;

        // Give the "last" component the remainder of the row
        lastConstraints.gridwidth = GridBagConstraints.REMAINDER;

        // Add a little padding
        lastConstraints.insets = new Insets(padding, padding, padding, padding);

        // Now for the "middle" field components
        middleConstraints =
                (GridBagConstraints) lastConstraints.clone();

        // These still get as much space as possible, but do
        // not close out a row
        middleConstraints.gridwidth = GridBagConstraints.RELATIVE;

        // And finally the "label" constrains, typically to be
        // used for the first component on each row
        labelConstraints =
                (GridBagConstraints) lastConstraints.clone();

        // Give these as little space as necessary
        labelConstraints.weightx = 0.0;
        labelConstraints.gridwidth = 1;

        separatorConstraints = new GridBagConstraints();
        separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraints.gridwidth = GridBagConstraints.REMAINDER;
    }

    /**
     * Adds a field component. Any component may be used. The
     * component will be stretched to take the remainder of
     * the current row.
     */
    public void addLastField(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, lastConstraints);
        parent.add(c);
    }

    /**
     * Adds an arbitrary label component, starting a new row
     * if appropriate. The width of the component will be set
     * to the minimum width of the widest component on the
     * form.
     */
    public void addLabel(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, labelConstraints);
        parent.add(c);
    }

    /**
     * Adds a JLabel with the given string to the label column
     */
    public JLabel addLabel(String s, Container parent) {
        JLabel c = new JLabel(s);
        addLabel(c, parent);
        return c;
    }

    /**
     * Adds a "middle" field component. Any component may be
     * used. The component will be stretched to take all of
     * the space between the label and the "last" field. All
     * "middle" fields in the layout will be the same width.
     */
    public void addMiddleField(Component c, Container parent) {
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(c, middleConstraints);
        parent.add(c);
    }

    public void addLabelAndLastField(String s, Container c, Container parent) {
        addLabel(s, parent);
        addLastField(c, parent);
    }

    public void addLabelAndLastField(String s, String value, Container parent) {
        addLabel(s, parent);
        addLastField(new JLabel(value), parent);
    }

    public void addSeparator(Container parent) {
        JSeparator separator = new JSeparator();
        GridBagLayout gbl = (GridBagLayout) parent.getLayout();
        gbl.setConstraints(separator, separatorConstraints);
        parent.add(separator);
    }
}