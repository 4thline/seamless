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
