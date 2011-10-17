package org.seamless.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.seamless.gwt.notify.client.Message;
import org.seamless.gwt.notify.client.NotifyEvent;

public class AllEntryPoint implements EntryPoint {

    //private final AllGinjector injector = GWT.create(AllGinjector.class);

    public void onModuleLoad() {

        /*
        injector.getEventBus().addHandler(
                NotifyEvent.TYPE,
                injector.getNotifications()
        );

        injector.getEventBus().fireEvent(new NotifyEvent(
                new Message("Test page", "This is a test page, ignore.")
        ));
        */

    }
}
