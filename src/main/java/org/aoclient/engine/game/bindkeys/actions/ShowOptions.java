package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FOptions;

public class ShowOptions implements KeyAction {
    @Override
    public void action() {
        ImGUISystem.INSTANCE.show(new FOptions());
    }
}
