package org.aoclient.engine.utils;

import org.aoclient.engine.game.Options;
import org.aoclient.engine.game.models.Character;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.utils.inits.*;

import java.io.IOException;

import static org.aoclient.engine.game.Messages.loadMessages;
import static org.aoclient.engine.game.models.Character.eraseAllChars;
import static org.aoclient.engine.renderer.FontRenderer.loadFonts;
import static org.aoclient.scripts.Compressor.readResource;

/**
 * <p>
 * Clase central de almacenamiento y gestion de datos del juego. {@code GameData} contiene las referencias a todos los recursos,
 * estados y configuraciones necesarios para el funcionamiento del cliente.
 * <p>
 * Esta clase es de tipo utilidad (utility class) y no debe ser instanciada. Provee metodos estaticos para cargar, inicializar y
 * acceder a los diferentes elementos del juego como graficos, sonidos, personajes, mapas y configuraciones.
 * <p>
 * Entre sus responsabilidades principales se encuentran:
 * <ul>
 * <li>Gestionar los arrays estaticos de datos del juego (cuerpos, cabezas, graficos, etc.)
 * <li>Cargar los recursos desde archivos comprimidos
 * <li>Inicializar los datos necesarios al inicio del juego
 * <li>Proporcionar metodos de acceso a los datos del juego
 * <li>Mantener el estado global de elementos como personajes, mapas y efectos
 * </ul>
 * <p>
 * La clase implementa un sistema de carga secuencial de recursos durante la inicializacion, garantizando que todos los datos
 * necesarios esten disponibles antes de que el juego comience a ejecutarse.
 */

public final class GameData {

    public static BodyData[] bodyData;
    public static HeadData[] headData;
    public static HeadData[] helmetsData;
    public static WeaponData[] weaponData;
    public static ShieldData[] shieldData;
    public static FxData[] fxData;
    public static GrhData[] grhData;
    public static MapData[][] mapData;
    public static boolean[] bLluvia;
    public static Character[] charList = new Character[10000 + 1]; // se agrega aca porque hay mapas que tienen NPCs.
    public static Options options = Options.INSTANCE;
    private static BinaryDataReader reader;

    /**
     * Inicializamos todos los datos almacenados en archivos.
     */
    public static void init() {
        for (int i = 0; i < charList.length; i++) charList[i] = new Character();

        reader = new BinaryDataReader();
        options.load();

        loadGrhData();
        loadHeads();
        loadHelmets();
        loadBodys();
        loadWeapons();
        loadShields();
        loadFxs();
        loadFK();
        loadFonts();
        loadMessages(options.getLanguage());
    }

    /**
     * Cargamos y almacenamos los datos del archivo "graphics.ind".
     */
    private static void loadGrhData() {

        byte[] data = readResource("resources/inits.ao", "graphics");
        if (data == null) {
            System.err.println("Could not load graphics data!");
            return;
        }

        try {
            reader.init(data);

            final int fileVersion = reader.readInt();
            final int grhCount = reader.readInt();

            grhData = new GrhData[grhCount + 1];

            int grh = 0;
            grhData[0] = new GrhData();

            while (grh < grhCount) {
                grh = reader.readInt();

                grhData[grh] = new GrhData();
                grhData[grh].setNumFrames(reader.readShort());

                if (grhData[grh].getNumFrames() <= 0) throw new IOException("getFrame(frame) ERROR IN THE GRHINDEX: " + grh);

                grhData[grh].setFrames(new int[grhData[grh].getNumFrames() + 1]);

                if (grhData[grh].getNumFrames() > 1) {
                    for (int i = 1; i <= grhData[grh].getNumFrames(); i++) {
                        grhData[grh].setFrame(i, reader.readInt());
                        if (grhData[grh].getFrame(i) <= 0) throw new IOException("getFrame(frame) ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setSpeed(reader.readFloat());
                    if (grhData[grh].getSpeed() <= 0) throw new IOException("getSpeed ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setPixelHeight(grhData[grhData[grh].getFrame(1)].getPixelHeight());

                    if (grhData[grh].getPixelHeight() <= 0) throw new IOException("getPixelHeight ERROR IN THE GRHINDEX: " + grh);


                    grhData[grh].setPixelWidth(grhData[grhData[grh].getFrame(1)].getPixelWidth());
                    if (grhData[grh].getPixelWidth() <= 0) throw new IOException("getPixelWidth ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setTileWidth(grhData[grhData[grh].getFrame(1)].getTileWidth());
                    if (grhData[grh].getTileWidth() <= 0) throw new IOException("getTileWidth ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setTileHeight(grhData[grhData[grh].getFrame(1)].getTileHeight());
                    if (grhData[grh].getTileHeight() <= 0) throw new IOException("getTileHeight ERROR IN THE GRHINDEX: " + grh);

                } else {
                    grhData[grh].setFileNum(reader.readInt());
                    if (grhData[grh].getFileNum() <= 0) throw new IOException("getFileNum ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setsX(reader.readShort());
                    if (grhData[grh].getsX() < 0) throw new IOException("getsX ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setsY(reader.readShort());
                    if (grhData[grh].getsY() < 0) throw new IOException("getsY ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setPixelWidth(reader.readShort());
                    if (grhData[grh].getPixelWidth() <= 0) throw new IOException("getPixelWidth ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setPixelHeight(reader.readShort());
                    if (grhData[grh].getPixelHeight() <= 0) throw new IOException("getPixelHeight ERROR IN THE GRHINDEX: " + grh);

                    grhData[grh].setTileWidth((float) grhData[grh].getPixelWidth() / 32);
                    grhData[grh].setTileHeight((float) grhData[grh].getPixelHeight() / 32);
                    grhData[grh].setFrame(1, grh);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Cargamos y almacenamos los datos del archivo "heads.ind".
     */
    private static void loadHeads() {
        byte[] data = readResource("resources/inits.ao", "heads");
        if (data == null) {
            System.err.println("Could not load heads data!");
            return;
        }

        reader.init(data);
        reader.skipBytes(263);

        final IndexHeads[] myHeads;
        final short numHeads = reader.readShort();
        headData = new HeadData[numHeads + 1];
        myHeads = new IndexHeads[numHeads + 1];

        headData[0] = new HeadData();
        for (int i = 1; i <= numHeads; i++) {
            myHeads[i] = new IndexHeads();
            myHeads[i].setHead(1, reader.readShort());
            myHeads[i].setHead(2, reader.readShort());
            myHeads[i].setHead(3, reader.readShort());
            myHeads[i].setHead(4, reader.readShort());

            headData[i] = new HeadData();
            if (myHeads[i].getHead(1) != 0) {
                headData[i].setHead(1, initGrh(headData[i].getHead(1), myHeads[i].getHead(1), false));
                headData[i].setHead(2, initGrh(headData[i].getHead(2), myHeads[i].getHead(2), false));
                headData[i].setHead(3, initGrh(headData[i].getHead(3), myHeads[i].getHead(3), false));
                headData[i].setHead(4, initGrh(headData[i].getHead(4), myHeads[i].getHead(4), false));
            }
        }

    }

    /**
     * Cargamos y almacenamos los datos del archivo "helmets.ind".
     */
    private static void loadHelmets() {
        byte[] data = readResource("resources/inits.ao", "helmets");
        if (data == null) {
            System.err.println("Could not load helmets data!");
            return;
        }

        reader.init(data);
        reader.skipBytes(263);

        final IndexHeads[] myHeads;
        final short numHeads = reader.readShort();
        helmetsData = new HeadData[numHeads + 1];
        myHeads = new IndexHeads[numHeads + 1];

        helmetsData[0] = new HeadData();
        for (int i = 1; i <= numHeads; i++) {
            myHeads[i] = new IndexHeads();
            myHeads[i].setHead(1, reader.readShort());
            myHeads[i].setHead(2, reader.readShort());
            myHeads[i].setHead(3, reader.readShort());
            myHeads[i].setHead(4, reader.readShort());

            helmetsData[i] = new HeadData();
            if (myHeads[i].getHead(1) != 0) {
                helmetsData[i].setHead(1, initGrh(helmetsData[i].getHead(1), myHeads[i].getHead(1), false));
                helmetsData[i].setHead(2, initGrh(helmetsData[i].getHead(2), myHeads[i].getHead(2), false));
                helmetsData[i].setHead(3, initGrh(helmetsData[i].getHead(3), myHeads[i].getHead(3), false));
                helmetsData[i].setHead(4, initGrh(helmetsData[i].getHead(4), myHeads[i].getHead(4), false));
            }
        }

    }

    /**
     * Cargamos y almacenamos los datos del archivo "bodys.ind".
     */
    private static void loadBodys() {
        byte[] data = readResource("resources/inits.ao", "bodys");
        if (data == null) {
            System.err.println("Could not load bodys data!");
            return;
        }

        reader.init(data);
        reader.skipBytes(263);

        final IndexBodys[] myBodys;
        final short numBodys = reader.readShort();
        bodyData = new BodyData[numBodys + 1];
        myBodys = new IndexBodys[numBodys + 1];

        bodyData[0] = new BodyData();
        for (int i = 1; i <= numBodys; i++) {
            myBodys[i] = new IndexBodys();
            myBodys[i].setBody(1, reader.readShort());
            myBodys[i].setBody(2, reader.readShort());
            myBodys[i].setBody(3, reader.readShort());
            myBodys[i].setBody(4, reader.readShort());

            myBodys[i].setHeadOffsetX(reader.readShort());
            myBodys[i].setHeadOffsetY(reader.readShort());

            bodyData[i] = new BodyData();
            if (myBodys[i].getBody(1) != 0) {
                bodyData[i].setWalk(1, initGrh(bodyData[i].getWalk(1), myBodys[i].getBody(1), false));
                bodyData[i].setWalk(2, initGrh(bodyData[i].getWalk(2), myBodys[i].getBody(2), false));
                bodyData[i].setWalk(3, initGrh(bodyData[i].getWalk(3), myBodys[i].getBody(3), false));
                bodyData[i].setWalk(4, initGrh(bodyData[i].getWalk(4), myBodys[i].getBody(4), false));

                bodyData[i].getHeadOffset().setX(myBodys[i].getHeadOffsetX());
                bodyData[i].getHeadOffset().setY(myBodys[i].getHeadOffsetY());
            }
        }

    }

    /**
     * Cargamos y almacenamos los datos del archivo "arms.ind".
     */
    private static void loadWeapons() {
        byte[] data = readResource("resources/inits.ao", "weapons");
        if (data == null) {
            System.err.println("Could not load weapons data!");
            return;
        }

        reader.init(data);

        final int numArms = reader.readShort();
        weaponData = new WeaponData[numArms + 1];

        weaponData[0] = new WeaponData();
        for (int loopc = 1; loopc <= numArms; loopc++) {
            weaponData[loopc] = new WeaponData();
            weaponData[loopc].setWeaponWalk(1, initGrh(weaponData[loopc].getWeaponWalk(1), reader.readShort(), false));
            weaponData[loopc].setWeaponWalk(2, initGrh(weaponData[loopc].getWeaponWalk(2), reader.readShort(), false));
            weaponData[loopc].setWeaponWalk(3, initGrh(weaponData[loopc].getWeaponWalk(3), reader.readShort(), false));
            weaponData[loopc].setWeaponWalk(4, initGrh(weaponData[loopc].getWeaponWalk(4), reader.readShort(), false));
        }

    }

    /**
     * Cargamos y almacenamos los datos del archivo "shields.ind".
     */
    private static void loadShields() {
        byte[] data = readResource("resources/inits.ao", "shields");
        if (data == null) {
            System.err.println("Could not load shields data!");
            return;
        }

        reader.init(data);

        final int numShields = reader.readShort();
        shieldData = new ShieldData[numShields + 1];

        shieldData[0] = new ShieldData();
        for (int loopc = 1; loopc <= numShields; loopc++) {
            shieldData[loopc] = new ShieldData();
            shieldData[loopc].setShieldWalk(1, initGrh(shieldData[loopc].getShieldWalk(1), reader.readShort(), false));
            shieldData[loopc].setShieldWalk(2, initGrh(shieldData[loopc].getShieldWalk(2), reader.readShort(), false));
            shieldData[loopc].setShieldWalk(3, initGrh(shieldData[loopc].getShieldWalk(3), reader.readShort(), false));
            shieldData[loopc].setShieldWalk(4, initGrh(shieldData[loopc].getShieldWalk(4), reader.readShort(), false));
        }

    }

    /**
     * Cargamos el mapa.
     */
    public static void loadMap(int numMap) {
        byte[] data = readResource("resources/maps.ao", "mapa" + numMap);
        if (data == null) {
            System.err.println("Could not load mapa" + numMap + " data!");
            return;
        }

        reader.init(data);

        mapData = new MapData[101][101];

        final short mapversion = reader.readShort();
        reader.skipBytes(263); // cabecera.

        byte byflags;

        // Falta implementar el mapInfo xd....
        reader.readShort();
        reader.readShort();
        reader.readShort();
        reader.readShort();

        byte bloq;

        mapData[0][0] = new MapData();

        for (int y = 1; y <= 100; y++) {
            for (int x = 1; x <= 100; x++) {
                mapData[x][y] = new MapData();

                byflags = reader.readByte();
                bloq = (byte) (byflags & 1);
                mapData[x][y].setBlocked(bloq == 1);

                mapData[x][y].getLayer(1).setGrhIndex(reader.readShort());
                mapData[x][y].setLayer(1, initGrh(mapData[x][y].getLayer(1), mapData[x][y].getLayer(1).getGrhIndex(), true));

                if ((byte) (byflags & 2) != 0) {
                    mapData[x][y].getLayer(2).setGrhIndex(reader.readShort());
                    mapData[x][y].setLayer(2, initGrh(mapData[x][y].getLayer(2), mapData[x][y].getLayer(2).getGrhIndex(), true));

                } else mapData[x][y].getLayer(2).setGrhIndex(0);

                if ((byte) (byflags & 4) != 0) {
                    mapData[x][y].getLayer(3).setGrhIndex(reader.readShort());
                    mapData[x][y].setLayer(3, initGrh(mapData[x][y].getLayer(3), mapData[x][y].getLayer(3).getGrhIndex(), true));
                } else mapData[x][y].getLayer(3).setGrhIndex(0);

                if ((byte) (byflags & 8) != 0) {
                    mapData[x][y].getLayer(4).setGrhIndex(reader.readShort());
                    mapData[x][y].setLayer(4, initGrh(mapData[x][y].getLayer(4), mapData[x][y].getLayer(4).getGrhIndex(), true));
                } else mapData[x][y].getLayer(4).setGrhIndex(0);

                if ((byte) (byflags & 16) != 0) mapData[x][y].setTrigger(reader.readShort());
                else mapData[x][y].setTrigger(0);

//                    if (mapData[x][y].getCharIndex() > 0) {
//                        eraseChar(mapData[x][y].getCharIndex());
//                    }
                mapData[x][y].getObjGrh().setGrhIndex(0);
            }
        }

        // Liberar memoria
        Surface.INSTANCE.deleteAllTextures();
        eraseAllChars();

    }

    /**
     * Cargamos los indices de animaciones FXs del archivo "fxs.ind"
     */
    private static void loadFxs() {
        byte[] data = readResource("resources/inits.ao", "fxs");
        if (data == null) {
            System.err.println("Could not load fxs data!");
            return;
        }

        reader.init(data);
        reader.skipBytes(263);

        final short numFXs = reader.readShort();
        fxData = new FxData[numFXs + 1];

        for (int i = 1; i <= numFXs; i++) {
            fxData[i] = new FxData();
            fxData[i].setAnimacion(reader.readShort());
            fxData[i].setOffsetX(reader.readShort());
            fxData[i].setOffsetY(reader.readShort());
        }
    }

    /**
     * Cargamos los indices de mapas donde se puede visualizar la lluvia en el archivo "fk.ind"
     */
    private static void loadFK() {
        byte[] data = readResource("resources/inits.ao", "fk");
        if (data == null) {
            System.err.println("Could not load fk data!");
            return;
        }

        reader.init(data);
        reader.skipBytes(263);

        final short Nu = reader.readShort();
        bLluvia = new boolean[Nu + 1];

        bLluvia[0] = false;
        for (int i = 1; i <= Nu; i++)
            bLluvia[i] = reader.readByte() == 1;

    }

    /**
     * Inicializa los graficos, ya sean animaciones o no.
     */
    public static GrhInfo initGrh(GrhInfo grh, short grhIndex, boolean started) {
        if (grh == null) throw new NullPointerException("Se esta intentando incializar un GrhInfo nulo...");

        grh.setGrhIndex(grhIndex);
        grh.setStarted(false);
        grh.setLoops(0);

        if (started) grh.setStarted(grhData[grh.getGrhIndex()].getNumFrames() > 1);

        if (grh.isStarted()) grh.setLoops(-1);

        grh.setFrameCounter(1);
        //grh.setSpeed( grhData[grhIndex].getSpeed() );
        grh.setSpeed(0.4f);

        return grh;
    }

}
