package org.seamless.gwt.theme.shared.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;

public interface ThemeBundle extends ClientBundle {

    @GwtCreateResource.ClassType(CellTable.Resources.class)
    GwtCreateResource<CellTable.Resources> cellTableResources();

    Icon48 icon48();

    Icon24 icon24();

    Icon16 icon16();

    IconMisc iconMisc();

    public interface IconMisc extends ClientBundle {

        @Source("misc/busy.gif")
        ImageResource busy();

    }

    public interface Icon48 extends ClientBundle {
        @Source("icon48/home.png")
        ImageResource home();

        @Source("icon48/settings.png")
        ImageResource settings();
    }

    public interface Icon24 extends ClientBundle {

        @Source("icon24/leftRightSwitch.png")
        ImageResource leftRightSwitch();

    }

    public interface Icon16 extends ClientBundle {

        @Source("icon16/edit.png")
        ImageResource edit();

        @Source("icon16/editAdd.png")
        ImageResource editAdd();

        @Source("icon16/editRemove.png")
        ImageResource editRemove();

        @Source("icon16/close.png")
        ImageResource close();

        @Source("icon16/info.png")
        ImageResource info();

        @Source("icon16/warn.png")
        ImageResource warn();

    }

}
