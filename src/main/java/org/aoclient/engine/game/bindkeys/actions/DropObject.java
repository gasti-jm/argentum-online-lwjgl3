package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FCantidad;

public class DropObject implements KeyAction {

    @Override
    public void action() {
        ImGUISystem.INSTANCE.show(new FCantidad());
    }
}
