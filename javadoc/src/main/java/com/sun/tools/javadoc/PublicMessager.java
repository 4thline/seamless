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
package com.sun.tools.javadoc;

import com.sun.tools.javac.util.Context;

import java.io.PrintWriter;

/**
 * Why did you make this stuff protected? Do you think the world will cease to exist if
 * somone has to use your horrible "API"?
 */
public class PublicMessager extends Messager {

    public PublicMessager(Context context, String s) {
        super(context, s);
    }

    public PublicMessager(Context context, String s, PrintWriter printWriter, PrintWriter printWriter1, PrintWriter printWriter2) {
        super(context, s, printWriter, printWriter1, printWriter2);
    }
}
