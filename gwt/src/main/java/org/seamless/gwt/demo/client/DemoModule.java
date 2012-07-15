package org.seamless.gwt.demo.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import org.seamless.gwt.notify.client.NotificationDisplay;
import org.seamless.gwt.notify.client.PopupNotificationDisplay;

public class DemoModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(EventBus.class)
            .to(SimpleEventBus.class)
            .in(Singleton.class);
    }

    @Provides
    @Singleton
    NotificationDisplay getNotificationDisplay(Bundle bundle) {
        return new PopupNotificationDisplay(bundle.themeBundle().create());
    }

}
