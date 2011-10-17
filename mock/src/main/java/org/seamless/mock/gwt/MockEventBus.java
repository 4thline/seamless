package org.seamless.mock.gwt;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class MockEventBus extends EventBus {

    public List<Event<?>> events = new ArrayList();

    @Override
    public <H extends EventHandler> HandlerRegistration addHandler(GwtEvent.Type<H> type, H handler) {
        return null;
    }

    @Override
    public <H> com.google.web.bindery.event.shared.HandlerRegistration addHandler(Event.Type<H> type, H handler) {
        return null;
    }

    @Override
    public <H extends EventHandler> HandlerRegistration addHandlerToSource(GwtEvent.Type<H> type, Object source, H handler) {
        return null;
    }

    @Override
    public <H> com.google.web.bindery.event.shared.HandlerRegistration addHandlerToSource(Event.Type<H> type, Object source, H handler) {
        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        events.add(event);
    }

    @Override
    public void fireEvent(Event<?> event) {
        events.add(event);
    }

    @Override
    public void fireEventFromSource(GwtEvent<?> event, Object source) {
        events.add(event);
    }

    @Override
    public void fireEventFromSource(Event<?> event, Object source) {
        events.add(event);
    }
}
