package org.aoclient.engine.renderer;

import org.aoclient.engine.utils.BinaryDataReader;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import static org.aoclient.engine.renderer.Drawn.drawGrhIndex;
import static org.aoclient.engine.utils.GameData.grhData;
import static org.aoclient.scripts.Compressor.readResource;

/**
 * Clase utilitaria que maneja la carga y renderizado de fuentes de texto.
 * <p>
 * Esta clase estatica proporciona funcionalidad para cargar distintos tipos de fuentes desde archivos, renderizar texto en
 * pantalla con diferentes estilos, y calcular las dimensiones del texto para su correcta ubicacion.
 * <p>
 * Contiene constantes predefinidas para los diferentes tipos de fuentes utilizados (como fuente normal y fuente para mostrar
 * golpes), y metodos para dibujar texto con diferentes colores, alineaciones y formatos. Tambien ofrece funciones para medir el
 * ancho y alto del texto renderizado.
 * <p>
 * La clase utiliza un sistema de mapeo de caracteres a sus representaciones graficas, permitiendo renderizar texto de manera
 * eficiente mediante el sistema de graficos. Ademas, soporta caracteres Unicode.
 * <p>
 * Es fundamental para mostrar dialogos, mensajes del sistema, nombres de personajes y cualquier otro texto que aparece en el
 * juego.
 */

public class FontRenderer {
    public static final int NORMAL_FONT = 0;
    public static final int HIT_FONT = 1;
    private static final BinaryDataReader reader = new BinaryDataReader();
    /** Ancho predeterminado para caracteres que no estan en el mapa. */
    private static final int DEFAULT_CHAR_WIDTH = 8;
    /** Caracter de reemplazo para caracteres no soportados. */
    private static final char REPLACEMENT_CHAR = '?';
    private static Font[] fonts;

    public static void loadFonts() {
        byte[] data = readResource("resources/inits.ao", "fonts");
        if (data == null) {
            System.err.println("Could not load fonts data!");
            return;
        }

        reader.init(data, ByteOrder.BIG_ENDIAN);

        int cantFontTypes = reader.readInt();
        fonts = new Font[cantFontTypes];

        for (int i = 0; i < cantFontTypes; i++) {
            fonts[i] = new Font();
            fonts[i].size = reader.readInt();
            // Inicializa el mapa de caracteres
            fonts[i].characters = new HashMap<>();
            // Cargar los caracteres ASCII (0-255) en el mapa
            for (int k = 0; k < 256; k++) {
                int grh = reader.readInt();
                if (grh > 0) fonts[i].characters.put((char) k, grh);

                // precargamos textura.
                TextureManager.requestTextureFile(grhData[grh].getFileNum());
            }
        }
    }

    /**
     * Renderiza texto en la pantalla utilizando el sistema de fuentes del juego.
     * <p>
     * Este metodo dibuja cada caracter del texto proporcionado en la posicion especificada, utilizando la fuente y color
     * indicados. Soporta caracteres Unicode y puede renderizar texto en multiples lineas si se habilita la opcion
     * correspondiente. El texto se dibuja desde la posicion (x,y) hacia la derecha y abajo.
     * <p>
     * Caracteristicas principales:
     * <ul>
     *   <li>Renderizado de caracteres basado en representaciones graficas (grh)
     *   <li>Soporte para multiples fuentes mediante el parametro fontIndex
     *   <li>Manejo automatico de saltos de linea cuando excede 20 caracteres (si multiLine=true)
     *   <li>Reconocimiento de caracteres de control (\n, \r) para forzar nuevas lineas
     *   <li>Espaciado automatico entre caracteres segun su ancho grafico
     * </ul>
     *
     * @param text      texto a dibujar
     * @param x         posicion x inicial (coordenada horizontal)
     * @param y         posicion y inicial (coordenada vertical)
     * @param color     color del texto
     * @param fontIndex indice del tipo de fuente a utilizar
     * @param multiLine si es {@code true}, el texto se dividira automaticamente en multiples lineas cuando exceda 20 caracteres o
     *                  encuentre caracteres de nueva linea (\n). Si es {@code false}, todo el texto se renderizara en una sola
     *                  linea.
     */
    public static void drawText(String text, int x, int y, RGBColor color, int fontIndex, boolean multiLine) {
        int posX = x + 1;  // Posicion x actual (con pequeño desplazamiento)
        int posY = y;      // Posicion y actual
        int charWidth;     // Ancho del caracter actual
        int lineWidth = 0; // Ancho de la linea actual
        int lineCount = 0; // Contador de lineas dibujadas
        int charCount = 0; // Contador de caracteres en la linea actual

        if (text.isEmpty()) return; // Si el texto esta vacio, termina
        if (fontIndex >= fonts.length) fontIndex = NORMAL_FONT; // Usa la fuente normal si el indice es invalido

        // Itera cada caracter del texto y los procesa individualmente
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Manejo de saltos de linea y espacios
            if (c == ' ' || c == '\r' || c == '\n') {
                // Si es multilinea y hay muchos caracteres o es salto de linea explicito
                if (multiLine && (charCount >= 20 || c == '\n')) {
                    // Crea una nueva linea
                    lineCount++;
                    charCount = 0;
                    lineWidth = 0;
                    posX = x + 1; // Crea una nueva linea
                    posY = y + lineCount * 14; // Baja 14 pixeles
                } else if (c == ' ') {
                    // Para espacios, solo avanza la posicion
                    posX += 4;
                    lineWidth += 4;
                }

                charCount++;
                continue; // Pasa al siguiente caracter
            }

            // Obtiene el indice grafico para el caracter
            Integer grhIndex = getCharGrhIndex(c, fontIndex);

            if (grhIndex != null && grhIndex > 12) {
                // Dibuja el caracter usando su representacion grafica
                drawGrhIndex(grhIndex, posX, posY, color);

                // Actualiza la posicion para el siguiente caracter
                charWidth = grhData[grhIndex].getPixelWidth();
                posX += charWidth;
                lineWidth += charWidth;
            }

            charCount++; // Incrementa contador de caracteres

        }
    }

    /**
     * Calcula el ancho total en pixeles que ocupa un texto al ser renderizado.
     * <p>
     * Este metodo procesa cada caracter del texto, considerando su representacion grafica y manejando los saltos de linea si se
     * permite el texto multilinea. Para los caracteres que no tienen una representacion grafica especifica, se utiliza un ancho
     * predeterminado. Adicionalmente, se consideran los espacios y saltos de linea explicitos o automaticos.
     *
     * @param text      Texto cuyo ancho sera calculado. Si es nulo o vacio, el ancho devuelto sera 0.
     * @param multiLine Indica si el texto puede dividirse en multiples lineas. Si es {@code true}, se aplican saltos de linea
     *                  automaticos y se calcula el ancho maximo por linea. Si es {@code false}, todo el texto se tratara como una
     *                  sola linea.
     * @return El ancho total en pixeles que ocupa el texto. Si es vacio o nulo, se retorna 0.
     */
    public static int getTextWidth(String text, boolean multiLine) {
        int retVal = 0; // Almacena el ancho maximo de todas las lineas
        int lineWidth = 0; // Ancho acumulado de la linea actual
        int charCount = 0; // Contador de caracteres en la linea actual

        // Si el texto es nulo o vacio, el ancho es 0
        if (text == null || text.isEmpty()) return 0;

        // Itera cada caracter del texto y calcula incrementalmente el ancho
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // Manejo de saltos de linea y espacios
            if (c == ' ' || c == '\r' || c == '\n') {
                // Si es multilinea y hay muchos caracteres o es salto de linea explicito
                if (multiLine && (charCount >= 20 || c == '\n')) {
                    // Nueva linea y reinicia el ancho de linea actual
                    charCount = 0;
                    lineWidth = 0;
                } else if (c == ' ') lineWidth += 4; // Los espacios ocupan 4 pixeles
                charCount++;
                continue;
            }

            Integer grhIndex = getCharGrhIndex(c, NORMAL_FONT);

            // Si existe una representacion grafica valida, usa su ancho especifico
            if (grhIndex != null && grhIndex > 12)
                lineWidth += grhData[grhIndex].getPixelWidth(); // Usa el ancho real del caracter segun su representacion grafica
            else lineWidth += DEFAULT_CHAR_WIDTH;  // Para caracteres sin representacion grafica, usa un ancho predeterminado

            if (lineWidth > retVal) retVal = lineWidth; // Actualiza el ancho maximo (retVal) si la linea actual es mas ancha

            charCount++;
        }

        return retVal;
    }

    /**
     * Calcula la altura total en pixeles que ocupa un texto al ser renderizado, basandose en el numero de lineas de texto.
     * <p>
     * Si el texto es de una sola linea, se utiliza una altura fija estandar. Si es multilinea, se calculan las lineas necesarias
     * considerando los saltos de linea y un maximo de 20 caracteres por linea.
     *
     * @param text      El texto para el cual se calcula la altura. No debe ser nulo.
     * @param multiLine Indica si el texto puede dividirse en multiples lineas. Si es {@code true}, se procesan los saltos de
     *                  linea explicitos y automáticos. Si es {@code false}, el texto se considera de una sola linea.
     * @return La altura total en pixeles del texto calculada en base al numero de lineas. Devuelve 14 si el texto es de una sola
     * linea.
     */
    public static int getTextHeight(String text, boolean multiLine) {
        // Si el texto no es multilinea, siempre devuelve 14 pixeles, que es la altura estandar de una linea de texto
        if (!multiLine) return 14;
        int lineCount = 1; // Comienza con una linea
        int charCount = 0; // Contador de caracteres en la linea actual

        // Itera cada caracter del texto
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // Incrementa el contador de lineas cuando encuentra un salto de linea explicito o alcanza 20 caracteres y encuentra un espacio o retorno de carro
            if (c == '\n' || (charCount >= 20 && (c == ' ' || c == '\r'))) {
                lineCount++;
                charCount = 0;
            }
            charCount++;
        }

        // Calcula la altura total
        return lineCount * 14;
    }

    /**
     * Muestra los caracteres disponibles de la fuente normal.
     */
    public static void debugPrintAvailableChars() {
        int i = 0;
        System.out.println("Caracteres disponibles en la fuente normal:");
        for (Character c : fonts[HIT_FONT].characters.keySet())
            System.out.print(c + (++i % 10 == 0 ? "\n" : " "));
        System.out.println();
    }

    /**
     * Obtiene el indice grafico para un caracter especifico.
     *
     * @param c         caracter a renderizar
     * @param fontIndex indice de la fuente a utilizar
     * @return indice grafico para el caracter, o null si no existe
     */
    private static Integer getCharGrhIndex(char c, int fontIndex) {
        if (fontIndex >= fonts.length) fontIndex = NORMAL_FONT;
        // Busca el caracter en el mapa
        Integer grhIndex = fonts[fontIndex].characters.get(c);
        // Si no se encuentra, intenta con el caracter de reemplazo
        if (grhIndex == null) grhIndex = fonts[fontIndex].characters.get(REPLACEMENT_CHAR);
        // Si aun no se encuentra, usa el primero disponible
        if (grhIndex == null && !fonts[fontIndex].characters.isEmpty())
            grhIndex = fonts[fontIndex].characters.values().iterator().next();
        return grhIndex;
    }

    private static class Font {
        int size;
        /**
         * Reemplazo del array {@code ascii_code} por un mapa para poder mapear cualquier caracter Unicode a su representacion
         * grafica.
         */
        Map<Character, Integer> characters;
    }

}