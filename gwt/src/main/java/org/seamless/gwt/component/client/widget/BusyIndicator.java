package org.seamless.gwt.component.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import org.seamless.gwt.theme.shared.client.ThemeBundle;
import org.seamless.gwt.theme.shared.client.ThemeStyle;

import javax.inject.Inject;

public class BusyIndicator extends SimplePanel {

    public BusyIndicator() {
        this((ThemeBundle) GWT.create(ThemeBundle.class));
    }

    @Inject
    public BusyIndicator(ThemeBundle themeBundle) {
        setStyleName(ThemeStyle.BusyIndicator());
        add(new Image(themeBundle.iconMisc().busy()));
    }

}
