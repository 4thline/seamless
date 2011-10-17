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

package org.seamless.gwt.component.client;

import com.google.gwt.user.client.ui.SuggestOracle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class StringSuggestion implements SuggestOracle.Suggestion {

    public static SuggestOracle.Response createResponse(List<String> strings) {
        return new SuggestOracle.Response(valueOf(strings));
    }

    public static List<StringSuggestion> valueOf(List<String> strings) {
        List<StringSuggestion> suggestions = new ArrayList();
        for (final String s : strings) {
            suggestions.add(new StringSuggestion(s));
        }
        return suggestions;
    }

    private String s;

    public StringSuggestion(String s) {
        this.s = s;
    }

    @Override
    public String getDisplayString() {
        return s;
    }

    @Override
    public String getReplacementString() {
        return s;
    }
}
