/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
