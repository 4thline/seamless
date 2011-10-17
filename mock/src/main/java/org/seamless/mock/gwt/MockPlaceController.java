package org.seamless.mock.gwt;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;

import java.lang.Override;import java.lang.String;import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class MockPlaceController extends PlaceController {

    static public class MockDelegate implements Delegate {
        public List<HandlerRegistration> registrations = new ArrayList();

        @Override
        public HandlerRegistration addWindowClosingHandler(Window.ClosingHandler handler) {
            HandlerRegistration reg = new HandlerRegistration() {
                @Override
                public void removeHandler() {
                    registrations.remove(this);
                }
            };
            registrations.add(reg);
            return reg;
        }

        @Override
        public boolean confirm(String message) {
            return true;
        }
    }

    public MockPlaceController(EventBus eventBus) {
        super(eventBus, new MockDelegate());
    }

    public MockPlaceController(EventBus eventBus, MockDelegate delegate) {
        super(eventBus, delegate);
    }
}
