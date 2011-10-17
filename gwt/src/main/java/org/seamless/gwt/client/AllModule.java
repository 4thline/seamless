package org.seamless.gwt.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;


public class AllModule extends AbstractGinModule {

    @Override
    protected void configure() {

        bind(EventBus.class)
                .to(SimpleEventBus.class)
                .in(Singleton.class);

    }
}
