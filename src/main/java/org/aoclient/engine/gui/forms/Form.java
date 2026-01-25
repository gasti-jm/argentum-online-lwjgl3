package org.aoclient.engine.gui.forms;

import imgui.ImGui;
import imgui.ImVec2;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.engine.renderer.Texture;
import org.aoclient.engine.utils.inits.GrhInfo;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.aoclient.engine.Window.SCREEN_HEIGHT;
import static org.aoclient.engine.Window.SCREEN_WIDTH;
import static org.aoclient.engine.scenes.Camera.TILE_PIXEL_SIZE;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.engine.utils.Time.deltaTime;
import static org.aoclient.scripts.Compressor.readResourceAsBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.*;

/**
 * <p>
 * Define la estructura comun y comportamiento basico que deben implementar todos los formularios. Proporciona un marco unificado
 * para la creacion de interfaces de usuario interactivas, obligando a todas las subclases a implementar su propia logica de
 * renderizado mediante el metodo abstracto {@code render()}.
 * <p>
 * Incluye funcionalidad para gestionar recursos graficos comunes como imagenes de fondo, y metodos utilitarios que pueden ser
 * utilizados por todos los formularios, como la carga de texturas desde archivos comprimidos y la apertura de URLs en el
 * navegador del sistema.
 * <p>
 * Establece el ciclo de vida basico de los formularios, incluyendo su inicializacion, renderizado y cierre, integrandose con el
 * sistema de gestion de interfaz {@code ImGUISystem} para garantizar un comportamiento coherente de todas las ventanas.
 * <p>
 * TODO Cambiar el concepto/palabra "Form" ya que no estamos trabajando mas con formularios, sino con ventanas
 */

public abstract class Form {

    protected int backgroundImage;
    protected static final User USER = User.INSTANCE;
    protected static final ImGUISystem IM_GUI_SYSTEM = ImGUISystem.INSTANCE;

    public abstract void render();

    public void close() {
        glDeleteTextures(backgroundImage);
        IM_GUI_SYSTEM.deleteFrmArray(this);
    }

    /**
     * Permite que podamos mover nuestro frm si tenemos el mouse en la parte superior del frm
     * simulando una barra de titulo como funciona en windows. <br> <br>
     * 
     * Para evitar errores antes de crear la ventana necesitamos establecer las siguentes condiciones: <br>
     * 
     * - Antes de crear la ventana en la funcion definida de {@code render()} debemos establecerle foco 
     * con {@code ImGui.setNextWindowFocus();}. Esto es opcional, pero si la ventana se crea por encima
     * del frmMain debemos hacerlo si o si. <br>
     * - En las flags de ImGui al momento de crear una ventana debemos establecer {@code ImGuiWindowFlags.NoMove}. <br>
     * - Por ultimo, luego de crear la ventana debemos llamar a esta misma funcion. <br> <br>
     * 
     * <b>Pueden observar como ejemplo en el FComerce, FBank, etc.</b>
     */
    protected void checkMoveFrm() {

        float barHeight = 10.0f; // Altura de la "barra de título"
        ImVec2 windowPos = ImGui.getWindowPos();
        ImVec2 mousePos = ImGui.getMousePos();

        boolean isHovered = ImGui.isWindowHovered();
        boolean isClicked = ImGui.isMouseDown(0);

        // Simluando una barra para que nos permita mover la ventana ( - 32 por si hay algun boton de cerrar en el frm).
        ImGui.invisibleButton("title_bar", ImGui.getWindowSizeX() - 32, barHeight);
        boolean isTitleBarActive = ImGui.isItemActive();

        // Guardar posición del mouse al empezar a arrastrar
        if (isTitleBarActive) {
            ImVec2 delta = ImGui.getIO().getMouseDelta();

            final float newPosX = windowPos.x + delta.x;
            final float newPosY = windowPos.y + delta.y;

            if (
                    (newPosX > 0 && newPosX < SCREEN_WIDTH) && (newPosY > 0 && newPosY < SCREEN_HEIGHT)
            ) {
                ImGui.setWindowPos(newPosX, newPosY);
            }


        }
    }

    protected int loadTexture(final String file) throws IOException {
        ByteBuffer resourceBuffer = readResourceAsBuffer("assets/gui.ao", file);
        if (resourceBuffer == null) {
            return -1;
        }

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load_from_memory(resourceBuffer, w, h, comp, 4);
        if (image == null) {
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
        }

        final int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        stbi_image_free(image);

        return textureID;
    }

    protected void openURL(String url) {
        // Verifica si Desktop es compatible con la plataforma actual
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Crea una instancia de URI desde la URL
                URI uri = new URI(url);
                // Abrimos la URL en el navegador predeterminado
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("La apertura de URL no es compatible en esta plataforma.");
    }

    protected void imageRegion(
            Texture tex,
            int x, int y, int w, int h
    ) {
        float u0 = (float) x / tex.getTex_width();
        float v0 = (float) y / tex.getTex_height();
        float u1 = (float) (x + w) / tex.getTex_width();
        float v1 = (float) (y + h) / tex.getTex_height();

        ImGui.image(tex.getId(), w, h, u0, v0, u1, v1);
    }

    protected void imageRegion(Texture tex, GrhInfo grh, boolean animate) {
        if (grh.getGrhIndex() == 0 || grhData[grh.getGrhIndex()].getNumFrames() == 0) return;
        if (animate && grh.isStarted()) {
            grh.setFrameCounter(grh.getFrameCounter() + (deltaTime * grhData[grh.getGrhIndex()].getNumFrames() / grh.getSpeed()));
            if (grh.getFrameCounter() > grhData[grh.getGrhIndex()].getNumFrames()) {
                grh.setFrameCounter((grh.getFrameCounter() % grhData[grh.getGrhIndex()].getNumFrames()) + 1);
                if (grh.getLoops() != -1) {
                    if (grh.getLoops() > 0) grh.setLoops(grh.getLoops() - 1);
                    else grh.setStarted(false);
                }
            }
        }

        final int currentGrhIndex = grhData[grh.getGrhIndex()].getFrame((int) (grh.getFrameCounter()));

        if (currentGrhIndex == 0 || grhData[currentGrhIndex].getFileNum() == 0) return;

        float u0 = (float) grhData[currentGrhIndex].getsX() / tex.getTex_width();
        float v0 = (float) grhData[currentGrhIndex].getsY() / tex.getTex_height();
        float u1 = (float) (grhData[currentGrhIndex].getsX() + grhData[currentGrhIndex].getPixelWidth()) / tex.getTex_width();
        float v1 = (float) (grhData[currentGrhIndex].getsY() + grhData[currentGrhIndex].getPixelHeight()) / tex.getTex_height();

        ImGui.image(tex.getId(), grhData[currentGrhIndex].getPixelWidth(), grhData[currentGrhIndex].getPixelHeight(), u0, v0, u1, v1);
    }

}
