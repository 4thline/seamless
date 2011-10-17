/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
