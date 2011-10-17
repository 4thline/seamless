package org.seamless.gwt.theme.fourthline.client;

import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.user.cellview.client.CellTable;
import org.seamless.gwt.theme.shared.client.ThemeBundle;

public interface ThemeBundleImpl extends ThemeBundle {

    @Override
    @GwtCreateResource.ClassType(CellTableResources.class)
    GwtCreateResource<CellTable.Resources> cellTableResources();

    /* This is how you override shared images in a theme:

    Icon16 icon16();

    public interface Icon16 extends ThemeBundle.Icon16 {

        @Source("override.png")
        ImageResource edit();

    }

    */
}
