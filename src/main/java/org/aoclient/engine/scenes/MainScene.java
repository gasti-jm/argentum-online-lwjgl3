package org.aoclient.engine.scenes;

import org.aoclient.engine.game.BindKeys;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.Key;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FConnect;
import org.aoclient.engine.listeners.KeyListener;

import static org.aoclient.engine.Engine.closeClient;

/**
 * <p>
 * {@code MainScene} es la escena donde se muestra el formulario de conexion ({@code FConnect}) que permite al usuario ingresar
 * sus credenciales para acceder al juego o elegir crear un nuevo personaje. Esta escena sirve como punto de entrada principal a
 * la experiencia de juego.
 * <p>
 * Desde esta escena, el usuario puede:
 * <ul>
 * <li>Iniciar sesion con un personaje existente
 * <li>Navegar hacia la creacion de un nuevo personaje
 * <li>Salir
 * </ul>
 * <p>
 * La escena se mantiene activa hasta que el usuario establece una conexion exitosa con el servidor, momento en el cual se realiza
 * una transicion automatica hacia {@code GameScene}. La escena monitorea constantemente el estado de conexion del usuario
 * mediante la clase {@code User} para determinar cuando realizar esta transicion.
 *
 * @see Scene
 * @see GameScene
 * @see FConnect
 * @see User
 */

public final class MainScene extends Scene {

    private final FConnect frmConnect = new FConnect();

    @Override
    public void init() {
        super.init();
        canChangeTo = SceneType.GAME_SCENE;
        ImGUISystem.INSTANCE.addFrm(frmConnect);
    }

    @Override
    public void mouseEvents() {

    }

    @Override
    public void keyEvents() {
        if (KeyListener.isKeyPressed(BindKeys.INSTANCE.getBindedKey(Key.EXIT_GAME))) closeClient();
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
        if (User.INSTANCE.isUserConected()) {
            this.close();
            ImGUISystem.INSTANCE.closeAllFrms();
        }
    }

}
