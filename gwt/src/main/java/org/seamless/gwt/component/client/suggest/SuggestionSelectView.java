 /*
 * Copyright (C) 2012 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.seamless.gwt.component.client.suggest;

import com.google.gwt.user.client.ui.IsWidget;
import org.seamless.gwt.validation.shared.ValidationError;

import java.util.List;

/**
 * The bundled GWT suggestion stuff is not flexible enough. This is better.
 *
 * @author Christian Bauer
 */
public interface SuggestionSelectView<S extends Suggestion> extends IsWidget {

    public interface Presenter<S> {

        void startWith(String name);

        SuggestionSelectView getView();

        void nameEntered(String name);

        void reset();

        void deselect();

        void suggestionSelected(int index);

    }

    void setPresenter(Presenter presenter);

    void reset();

    void setName(String name);

    void setSuggestions(List<S> suggestions, int selectedIndex);

    void showValidationError(ValidationError error);

    void clearValidationError();

}
