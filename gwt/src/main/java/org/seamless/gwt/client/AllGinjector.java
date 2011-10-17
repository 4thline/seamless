package org.seamless.gwt.client;


import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.web.bindery.event.shared.EventBus;
import org.seamless.gwt.notify.client.Notifications;
import org.seamless.gwt.notify.client.NotifyModule;

@GinModules(
        {
                AllModule.class,
                NotifyModule.class
        }
)
public interface AllGinjector extends Ginjector {

    Notifications getNotifications();

    EventBus getEventBus();
}