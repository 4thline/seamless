package org.seamless.gwt.demo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.seamless.gwt.notify.client.Message;
import org.seamless.gwt.notify.client.NotifyEvent;

public class DemoEntryPoint implements EntryPoint {

    private final DemoGinjector injector = GWT.create(DemoGinjector.class);

    public void onModuleLoad() {

        injector.getEventBus().addHandler(
            NotifyEvent.TYPE,
            injector.getNotifications()
        );

        injector.getEventBus().fireEvent(
            new NotifyEvent("Demo page", "This is a demo.")
        );
    }
}
