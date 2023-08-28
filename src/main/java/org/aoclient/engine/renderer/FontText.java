package org.aoclient.engine.renderer;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.aoclient.engine.renderer.FontText.E_FontType.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * Esta es la clase donde se inicializa y dibuja la fuente de letra.
 */
public class FontText {
    private static final int TILE_SIZE = 15;

    enum E_FontType {
        FONT_NORMAL,
        FONT_ITALIC,
        FONT_BOLD,
        FONT_BOLD_ITALIC
    }

    static class FontData {
        int x, y, width;

        public FontData(int x, int y, int width) {
            this.x = x; // posicion x del recorte
            this.y = y; // posicion y del recorte
            this.width = width; // ancho de letra (15 seria el alto, esto mas adelante debera cambiar).
        }
    }

    static class Font {
        TextureOGL[] textureFonts = new TextureOGL[4];
        Map<Integer, FontData> fontData = new HashMap<>(); // <ASCII, Valores de posicionamiento>
    }
    private static final Font[] fonts = new Font[1]; // por el momento va a ser 1, si queremos agregas mas fuentes, aumentamos...

    /**
     * Comienza a cargar los recursos para la fuente de letra.
     */
    public static void loadCSV(){
        fonts[0] = new Font();

        loadTextures(0);
        loadFontData(0, "normal.csv");
        loadFontData(0, "normal-italic.csv");
        loadFontData(0, "bold.csv");
        loadFontData(0, "bold-italic.csv");
    }


    /**
     *
     * @param text Texto a mostrar
     * @param x Posicion X de la pantalla
     * @param y Posicion Y de la pantalla
     * @param color Mezcla de colores RGB
     * @param fontIndex En este momento solo esta 0, ya que tenemos una fuente de letras con un tamaño de 13.
     * @param bold Activa el efecto negrita en la letra
     * @param italic Activa el efecto italica en la letra
     * @param multi_line Activa un limite de caracteres para que tenga un salto de linea.
     */
    public static void drawText(String text, int x, int y, RGBColor color, int fontIndex, boolean bold, boolean italic, boolean multi_line) {
        if (text.length() == 0) return;

        int fontType = FONT_NORMAL.ordinal();

        if (color == null) {
            color = new RGBColor(1.0f, 1.0f, 1.0f);
        }

        if (!bold && italic) {
            fontType = FONT_ITALIC.ordinal();
        } else if (bold && !italic) {
            fontType = FONT_BOLD.ordinal();
        } else if (bold) {
            fontType = FONT_BOLD_ITALIC.ordinal();
        }

        int space = 0;
        if(!multi_line) {
            for (int a = 0; a < text.length(); a++) {
                int ascii = text.charAt(a);

                if (ascii != 32) {
                    geometryBoxRenderFont(fonts[fontIndex].textureFonts[fontType], (x + space), y,
                            fonts[fontIndex].fontData.get(ascii).width, 15,
                            fonts[fontIndex].fontData.get(ascii).x, fonts[fontIndex].fontData.get(ascii).y,
                            false, 1.0f, color);

                    space += fonts[fontIndex].fontData.get(ascii).width;
                } else {
                    space += 4;
                }
            }

        } else {
            int e = 0;
            int f = 0;

            for (int a = 0; a < text.length(); a++) {
                int ascii = text.charAt(a);
                if (ascii > 255) ascii = 0;

                if (ascii == 32 || ascii == 13) {
                    if (e >= 20) {
                        f++;
                        e = 0;
                        space = 0;
                    } else {
                        if (ascii == 32) space += 4;
                    }
                } else {
                    geometryBoxRenderFont(fonts[fontIndex].textureFonts[fontType], (x + space) + 1, y + f * 14,
                            fonts[fontIndex].fontData.get(ascii).width, 15,
                            fonts[fontIndex].fontData.get(ascii).x, fonts[fontIndex].fontData.get(ascii).y,
                            false, 1.0f, color);

                    space += fonts[fontIndex].fontData.get(ascii).width;
                }

                e++;
            }
        }

    }

    /**
     *
     * @param text Texto a mostrar
     * @param x Posicion X de la pantalla
     * @param y Posicion Y de la pantalla
     * @param color Mezcla de colores RGB
     * @param fontIndex En este momento solo esta 0, ya que tenemos una fuente de letras con un tamaño de 13.
     * @param bold Activa el efecto negrita en la letra
     * @param italic Activa el efecto italica en la letra
     */
    public static void drawConsoleText(String text, int x, int y, RGBColor color, int fontIndex, boolean bold, boolean italic) {
        if (text.length() == 0) return;

        int fontType = FONT_NORMAL.ordinal();

        if (color == null) {
            color = new RGBColor(1.0f, 1.0f, 1.0f);
        }

        if (!bold && italic) {
            fontType = FONT_ITALIC.ordinal();
        } else if (bold && !italic) {
            fontType = FONT_BOLD.ordinal();
        } else if (bold) {
            fontType = FONT_BOLD_ITALIC.ordinal();
        }

        int space = 0;
        int e = 0;
        int f = 0;

        for (int a = 0; a < text.length(); a++) {
            int ascii = text.charAt(a);

            if (ascii > 255) ascii = 0;

            if (ascii == 32 || ascii == 13) {
                if (e >= 100) {
                    f++;
                    e = 0;
                    space = 0;
                } else {
                    if (ascii == 32) space += 4;
                }
            } else {
                geometryBoxRenderFont(fonts[fontIndex].textureFonts[fontType], (x + space) + 1, y + f * 14,
                        fonts[fontIndex].fontData.get(ascii).width, 15,
                        fonts[fontIndex].fontData.get(ascii).x, fonts[fontIndex].fontData.get(ascii).y,
                        false, 1.0f, color);

                space += fonts[fontIndex].fontData.get(ascii).width;
            }

            e++;
        }

    }

    /**
     *
     * @param texture Textura almacenada
     * @param x Posicion X de la pantalla
     * @param y Posicion Y de la pantalla
     * @param src_width Ancho del recorte
     * @param src_height Alto del recorte
     * @param sX Posicion X del recorte
     * @param sY Posicion Y del recorte
     * @param blend Efecto blend
     * @param alpha Cantidad de transparencia (de 0 a 1)
     * @param color Mezcla de colores RGB
     *
     * @desc Sirve para las funciones de dibujado.
     */
    public static void geometryBoxRenderFont(TextureOGL texture, int x, int y, int src_width, int src_height, float sX, float sY, boolean blend, float alpha, RGBColor color) {
        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        final float src_right = sX + src_width;
        final float src_bottom = sY + src_height;

        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBegin(GL_QUADS);

        {
            //  0----0
            //  |    |
            //  1----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f (sX / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x, y + src_height);

            //  1----0
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f(sX / texture.tex_width, sY / texture.tex_height);
            glVertex2d(x, y);

            //  0----1
            //  |    |
            //  0----0
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.tex_width, sY / texture.tex_height);
            glVertex2d(x + src_width, y);

            //  0----0
            //  |    |
            //  0----1
            glColor4f(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            glTexCoord2f((src_right) / texture.tex_width, (src_bottom) / texture.tex_height);
            glVertex2d(x + src_width, y + src_height);
        }

        glEnd();

        if (blend)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * @param fontIndex indice de fuente
     * @desc carga las texturas (normal, italic, bold, etc...) del X indice de fuentes.
     */
    public static void loadTextures(int fontIndex) {
        fonts[fontIndex].textureFonts[0] = Surface.get().createFontTexture("normal");
        fonts[fontIndex].textureFonts[1] = Surface.get().createFontTexture("normal-italic");
        fonts[fontIndex].textureFonts[2] = Surface.get().createFontTexture("bold");
        fonts[fontIndex].textureFonts[3] = Surface.get().createFontTexture("bold-italic");
    }

    /**
     *
     * @param fontIndex indice de fuente
     * @param fileName nombre del archivo
     *
     * @desc Carga un archivo .csv donde contiene los espacios (width) en cada letra y asigna una pos (x,y) de recorte
     *       para que al momento de dibujar sepa que letra se debe mostrar.
     *
     * @pd Esto debera cambiar ya que si tenemos texturas de distintos tama;os de letras debemos detectar su altura
     *     y tambien al hacer esto necesitaremos mas fontIndex, mas texturas y archivos .csv para una letra
     *     normal, italic, bold, etc.
     */
    public static void loadFontData(int fontIndex, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/fonts/" + fileName))) {
            String line;
            Pattern pattern = Pattern.compile("Char (\\d+) Base Width,(\\d+)");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    int charNumber = Integer.parseInt(matcher.group(1));
                    FontData data = new FontData(0, 0, Integer.parseInt(matcher.group(2)));

                    fonts[fontIndex].fontData.put(charNumber, data);
                }
            }

            int x = 0, y = 0, posColumn = 0;
            for(int i = 0; i <= 255; i++) {
                fonts[fontIndex].fontData.get(i).x = x;
                fonts[fontIndex].fontData.get(i).y = y;

                if (posColumn < 16){
                    posColumn++;
                    x += TILE_SIZE;
                } else {
                    posColumn = 0;
                    x = 0;
                    y += TILE_SIZE;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @desc: Esta funcion sirve para pasarle una cadena (ej: nickname) y sacar el size total
     *          que hay por cada grafico en caracter (ya que los textos son atravez de graficos).
     */
    public static int getSizeText(String text) {
        if (text.length() == 0) return 0;
        int totalSize = 0;

        // recorremos los caracteres
        for (int a = 0; a < text.length(); a++) {
            int b = text.charAt(a); // obtenemos el ASCII
            if (b > 255) break;

            if (b != 32) {
                totalSize += fonts[0].fontData.get(b).width;
            } else {
                totalSize += 4;
            }
        }

        return totalSize;
    }
}
