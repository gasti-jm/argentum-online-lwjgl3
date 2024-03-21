package org.aoclient.engine.scenes;

import org.aoclient.engine.Window;
import org.aoclient.engine.game.BindKeys;
import org.aoclient.engine.game.User;
import org.aoclient.engine.game.models.E_KeyType;
import org.aoclient.engine.gui.forms.FConnect;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.listeners.KeyListener;
import org.aoclient.engine.renderer.RGBColor;

import static org.aoclient.engine.Engine.closeClient;
import static org.aoclient.engine.game.models.Character.drawCharacter;
import static org.aoclient.engine.renderer.Drawn.drawTexture;
import static org.aoclient.engine.scenes.Camera.TILE_BUFFER_SIZE;
import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.mapData;

/**
 * Esta es la escena donde se va a mostrar un conectar renderizado y se va a tener en cuenta el crear personaje.
 *
 * Se recomienda leer el JavaDoc de la clase padre "Scene.java".
 */
public final class MainScene extends Scene {
    private RGBColor ambientColor;
    private final FConnect frmConnect = new FConnect();
    private BindKeys bindKeys;

    @Override
    public void init() {
        super.init();
        canChangeTo     = SceneType.GAME_SCENE;
        bindKeys        = BindKeys.get();
        ambientColor    = new RGBColor(1.0f, 1.0f, 1.0f);

        camera.setHalfWindowTileWidth(((Window.get().getWidth() / TILE_PIXEL_SIZE) / 2));
        camera.setHalfWindowTileHeight(((Window.get().getHeight() / TILE_PIXEL_SIZE) / 2));

        ImGUISystem.get().addFrm(frmConnect);
    }

    @Override
    public void mouseEvents() {

    }

    @Override
    public void keyEvents() {
        if (KeyListener.isKeyPressed(bindKeys.getBindedKey(E_KeyType.mKeyExitGame))) {
            closeClient();
        }
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
        if(User.get().isUserConected()) {
            this.close();
            ImGUISystem.get().closeAllFrms();
        }

        if (!visible) return;
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
