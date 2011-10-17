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

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public abstract class SuggestionSelectPresenter<S extends Suggestion> implements SuggestionSelectView.Presenter<S> {

    final SuggestionSelectView view;

    List<S> suggestions = new ArrayList();

    public SuggestionSelectPresenter(SuggestionSelectView view) {
        this.view = view;
    }

    public List<S> getSuggestions() {
        return suggestions;
    }

    @Override
    public void startWith(String name) {
        getView().setPresenter(this);
        getView().reset();
        getView().setName(name);
    }

    @Override
    public SuggestionSelectView getView() {
        return view;
    }

    @Override
    public void suggestionSelected(int index) {
        S selected;
        if ((selected = suggestions.get(index)) != null) {
            onSelection(selected);
        }
    }

    abstract public void onSelection(S selected);

    public abstract class SuggestionCallback<T> implements AsyncCallback<List<T>> {

        @Override
        public void onFailure(Throwable caught) {
            getSuggestions().clear();
            getView().setSuggestions(null, -1);
            handleFailure(caught);
        }

        @Override
        public void onSuccess(List<T> results) {
            getSuggestions().clear();
            for (int i = 0; i < results.size(); i++) {
                T result  = results.get(i);
                S suggestion = createSuggestion(i, result);
                if (suggestion != null)
                    getSuggestions().add(suggestion);
            }
            getView().setSuggestions(getSuggestions(), getSelectedIndex(getSuggestions()));
        }

        protected int getSelectedIndex(List<S> suggestions) {
            return -1;
        }

        abstract protected void handleFailure(Throwable caught);

        abstract protected S createSuggestion(int index, T result);
    }
}
