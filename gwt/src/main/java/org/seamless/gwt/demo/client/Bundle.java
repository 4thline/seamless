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

package org.seamless.gwt.demo.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.resources.client.ImageResource;
import org.seamless.gwt.theme.shared.client.ThemeBundle;

/**
 * @author Christian Bauer
 */
public interface Bundle extends ClientBundle {

    @GwtCreateResource.ClassType(
        org.seamless.gwt.theme.fourthline.client.ThemeBundleImpl.class
    )
    GwtCreateResource<ThemeBundle> themeBundle();

    Icon48 icon48();

    public interface Icon48 extends ClientBundle {

        @Source("icon48/report.png")
        ImageResource report();

        @Source("icon48/currency.png")
        ImageResource currency();

    }
}
