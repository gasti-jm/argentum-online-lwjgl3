package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.Rain;
import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.utils.Log;

import static org.aoclient.engine.utils.GameData.options;

public class ToggleSound implements KeyAction {
    @Override
    public void action() {
        if(options.isSound())
            Rain.INSTANCE.stopSounds();

        options.setSound(!options.isSound());
    }
}
