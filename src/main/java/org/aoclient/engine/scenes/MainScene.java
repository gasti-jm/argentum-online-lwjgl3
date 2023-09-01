package org.aoclient.engine.scenes;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.Window;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.forms.FrmCreateCharacter;
import org.aoclient.engine.gui.forms.FrmLogin;
import org.aoclient.engine.gui.forms.FrmMessage;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.utils.GameData;

import static org.aoclient.connection.Protocol.writeLoginExistingChar;
import static org.aoclient.engine.Engine.forms;
import static org.aoclient.engine.game.models.Character.drawCharacter;
import static org.aoclient.engine.renderer.Drawn.drawTexture;
import static org.aoclient.engine.scenes.Camera.TILE_BUFFER_SIZE;
import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.mapData;
import static org.aoclient.engine.utils.GameData.music;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;

/**
 * Esta es la escena donde se va a mostrar un conectar renderizado y se va a tener en cuenta el crear personaje.
 *
 * Se recomienda leer el JavaDoc de la clase padre "Scene.java".
 */
public final class MainScene extends Scene {
    private FrmLogin frmLogin;
    private FrmCreateCharacter frmCreateCharacter;

    // Conectar
    private RGBColor ambientColor;

    @Override
    public void init() {
        super.init();
        canChangeTo = SceneType.GAME_SCENE;

        frmLogin = FrmLogin.get();
        frmCreateCharacter = FrmCreateCharacter.get();

        frmLogin.init();
        ambientColor = new RGBColor(1.0f, 1.0f, 1.0f);

        GameData.loadMap(58); // banderbill
        camera.setHalfWindowTileWidth(((Window.get().getWidth() / TILE_PIXEL_SIZE) / 2));
        camera.setHalfWindowTileHeight(((Window.get().getHeight() / TILE_PIXEL_SIZE) / 2));
    }

    @Override
    public void mouseEvents() {
        if(!forms.isEmpty()) return;

        if (frmCreateCharacter.isVisible()) {
            frmCreateCharacter.checkButtons();
            frmCreateCharacter.checkMouseTextBoxes();
        } else {
            frmLogin.checkButtons();
            frmLogin.checkMouseTextBoxes();
        }
    }

    @Override
    public void keyEvents() {
        if (!forms.isEmpty()) return;

        if (frmCreateCharacter.isVisible()) {
            frmCreateCharacter.checkKeyTextBoxes();
        } else {
            if (KeyListener.isKeyReadyForAction(GLFW_KEY_ENTER)) {
                if (frmLogin.getUsername().isEmpty() || !frmLogin.getPassword().isEmpty()) {
                    SocketConnection.getInstance().connect();
                    writeLoginExistingChar(frmLogin.getUsername(), frmLogin.getPassword());
                } else {
                    forms.add(new FrmMessage("Por favor, ingrese un nombre de usuario y/o contraseña valida."));
                }
            }

            frmLogin.checkKeyTextBoxes();
        }
    }

    /**
     * Cierre de escena
     */
    @Override
    public void close() {
        music.stop();
        this.visible = false;
        frmLogin.close();
    }

    @Override
    public void render() {
        if(User.get().isUserConected()) {
            this.close();
        }

        if (!visible) return;

        if (frmCreateCharacter.isVisible()) {
            frmCreateCharacter.render();
        } else {
            renderMap();
            frmLogin.render();
        }
    }

    private void renderMap() {
        camera.update(34, 16);

        for (int y = camera.getScreenminY(); y <= camera.getScreenmaxY(); y++) {
            int x;
            for (x = camera.getScreenminX(); x <= camera.getScreenmaxX(); x++) {

                if (mapData[x][y].getLayer(1).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(1),
                            (camera.getScreenX() - 1) * TILE_PIXEL_SIZE,
                            (camera.getScreenY() - 1) * TILE_PIXEL_SIZE,
                            true, true, false, 1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.setScreenX(camera.getScreenX() - x + camera.getScreenminX());
            camera.incrementScreenY();
        }

        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getLayer(2).getGrhIndex() != 0) {
                    drawTexture(mapData[x][y].getLayer(2),
                            camera.getScreenX() * TILE_PIXEL_SIZE,
                            camera.getScreenY() * TILE_PIXEL_SIZE, true, true, false,1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {
                    if (mapData[x][y].getObjGrh().getGrhIndex() != 0) {
                        drawTexture(mapData[x][y].getObjGrh(),
                                camera.getScreenX() * TILE_PIXEL_SIZE,
                                camera.getScreenY() * TILE_PIXEL_SIZE,
                                true, true, false,1.0f, ambientColor);
                    }

                    if (mapData[x][y].getCharIndex() > 0) {
                        drawCharacter(mapData[x][y].getCharIndex(),
                                camera.getScreenX() * TILE_PIXEL_SIZE,
                                camera.getScreenY() * TILE_PIXEL_SIZE, ambientColor);
                    }

                    if (mapData[x][y].getLayer(3).getGrhIndex() != 0) {
                        drawTexture(mapData[x][y].getLayer(3),
                                camera.getScreenX() * TILE_PIXEL_SIZE,
                                camera.getScreenY() * TILE_PIXEL_SIZE,
                                true, true, false, 1.0f, ambientColor);
                    }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

        camera.setScreenY(camera.getMinYOffset() - TILE_BUFFER_SIZE);
        for (int y = camera.getMinY(); y <= camera.getMaxY(); y++) {
            camera.setScreenX(camera.getMinXOffset() - TILE_BUFFER_SIZE);
            for(int x = camera.getMinX(); x <= camera.getMaxX(); x++) {

                if (mapData[x][y].getLayer(4).getGrhIndex() > 0) {
                    drawTexture(mapData[x][y].getLayer(4),
                            camera.getScreenX() * TILE_PIXEL_SIZE,
                            camera.getScreenY() * TILE_PIXEL_SIZE,
                            true, true, false,1.0f, ambientColor);
                }

                camera.incrementScreenX();
            }
            camera.incrementScreenY();
        }

    }

}
