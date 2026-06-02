package org.aoclient.engine.game.bindkeys.actions;

import org.aoclient.engine.game.bindkeys.KeyAction;
import org.aoclient.engine.utils.Log;
import org.tinylog.Logger;

import static org.aoclient.engine.audio.Sound.stopMusic;
import static org.aoclient.engine.utils.GameData.options;

public class ToggleMusic implements KeyAction {
    @Override
    public void action() {
        if (options.isMusic())
            stopMusic();


        options.setMusic(!options.isMusic());
    }
}
