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
