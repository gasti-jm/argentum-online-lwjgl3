package org.aoclient.engine.scenes;

import org.aoclient.engine.game.BindKeys;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_KeyType;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FConnect;
import org.aoclient.engine.listeners.KeyListener;

import static org.aoclient.engine.Engine.closeClient;

/**
 * Esta es la escena donde se va a mostrar un conectar renderizado y se va a tener en cuenta el crear personaje.
 * <p>
 * Se recomienda leer el JavaDoc de la clase padre "Scene.java".
 */

public final class MainScene extends Scene {

    private final FConnect frmConnect = new FConnect();
    private BindKeys bindKeys;

    @Override
    public void init() {
        super.init();
        canChangeTo = SceneType.GAME_SCENE;
        bindKeys = BindKeys.get();

        ImGUISystem.get().addFrm(frmConnect);
    }

    @Override
    public void mouseEvents() {

    }

    @Override
    public void keyEvents() {
        if (KeyListener.isKeyPressed(bindKeys.getBindedKey(E_KeyType.mKeyExitGame))) closeClient();
    }

    /**
     * Cierre de escena
     */
    @Override
    public void close() {
        this.visible = false;
    }

    @Override
    public void render() {
        if (User.get().isUserConected()) {
            this.close();
            ImGUISystem.get().closeAllFrms();
        }
    }

}
