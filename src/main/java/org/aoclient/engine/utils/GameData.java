package org.aoclient.engine.utils;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.aoclient.engine.Sound;
import org.aoclient.engine.game.Options;
import org.aoclient.engine.game.models.Character;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.utils.structs.*;

import static org.aoclient.engine.Sound.*;
import static org.aoclient.engine.game.models.Character.eraseAllChars;
import static org.aoclient.engine.renderer.FontText.loadCSV;
import static org.aoclient.engine.utils.ByteMigration.*;

/**
 * Clase en donde se carga todos los archivos necesarios del juego.
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

    public static Character[] charList = new Character[10000+1]; // se agrega aca porque hay mapas que tienen NPCs.
    public static Sound music;
    public static Map<String, Sound> sounds = new HashMap<>();
    public static Options options;

    /**
     * Inicializamos todos los datos almacenados en archivos.
     */
    public static void initialize() {
        for (int i = 0; i < charList.length; i++) {
            charList[i] = new Character();
        }

        // Creamos el objeto de las opciones.
        options = new Options();
        options.LoadOptions();

        loadGrhData();
        loadHeads();
        loadHelmets();
        loadBodys();
        loadArms();
        loadShields();
        loadFxs();
        loadFK();
        loadCSV();

        playMusic("intro.ogg");
    }


    /**
     * Cargamos y almacenamos los datos del archivo "graphics.ind".
     */
    private static void loadGrhData() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/graphics.ind", "rw")) {
            f.seek(0);


            final int fileversion = bigToLittle_Int( f.readInt() );
            final int grhCount = bigToLittle_Int(f.readInt());

            grhData = new GrhData[grhCount + 1];
            int grh = 0;
            grhData[0] = new GrhData();

            while (grh < grhCount) {
                grh = bigToLittle_Int(f.readInt());
                grhData[grh] = new GrhData();
                grhData[grh].setNumFrames( bigToLittle_Short( f.readShort() ) );

                if (grhData[grh].getNumFrames() <= 0) {
                    throw new IOException("getFrame(frame) ERROR IN THE GRHINDEX: " + grh);
                }

                grhData[grh].setFrames( new int[grhData[grh].getNumFrames() + 1] );

                if (grhData[grh].getNumFrames() > 1) {

                    for(int i = 1; i <= grhData[grh].getNumFrames(); i++) {
                        grhData[grh].setFrame(i, bigToLittle_Int( f.readInt() ));

                        if (grhData[grh].getFrame(i) <= 0) {
                            throw new IOException("getFrame(frame) ERROR IN THE GRHINDEX: " + grh);
                        }
                    }

                    grhData[grh].setSpeed( bigToLittle_Float( f.readFloat() ));
                    if (grhData[grh].getSpeed() <= 0){
                        throw new IOException("getSpeed ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setPixelHeight( grhData[grhData[grh].getFrame(1) ].getPixelHeight());

                    if (grhData[grh].getPixelHeight() <= 0) {
                        throw new IOException("getPixelHeight ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setPixelWidth(grhData[grhData[grh].getFrame(1)].getPixelWidth());
                    if (grhData[grh].getPixelWidth() <= 0) {
                        throw new IOException("getPixelWidth ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setTileWidth(grhData[grhData[grh].getFrame(1)].getTileWidth());
                    if (grhData[grh].getTileWidth() <= 0) {
                        throw new IOException("getTileWidth ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setTileHeight(grhData[grhData[grh].getFrame(1)].getTileHeight());
                    if (grhData[grh].getTileHeight() <= 0) {
                        throw new IOException("getTileHeight ERROR IN THE GRHINDEX: " + grh);
                    }

                } else {

                    grhData[grh].setFileNum( bigToLittle_Int( f.readInt() ));
                    if (grhData[grh].getFileNum() <= 0) {
                        throw new IOException("getFileNum ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setsX( bigToLittle_Short( f.readShort() ));
                    if (grhData[grh].getsX() < 0) {
                        throw new IOException("getsX ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setsY( bigToLittle_Short( f.readShort() ));
                    if (grhData[grh].getsY() < 0) {
                        throw new IOException("getsY ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setPixelWidth( bigToLittle_Short( f.readShort() ));
                    if (grhData[grh].getPixelWidth() <= 0) {
                        throw new IOException("getPixelWidth ERROR IN THE GRHINDEX: " + grh);
                    }

                    grhData[grh].setPixelHeight( bigToLittle_Short( f.readShort() ));
                    if (grhData[grh].getPixelHeight() <= 0) {
                        throw new IOException("getPixelHeight ERROR IN THE GRHINDEX: " + grh);
                    }


                    grhData[grh].setTileWidth( (float) grhData[grh].getPixelWidth() / 32);
                    grhData[grh].setTileHeight( (float) grhData[grh].getPixelHeight() / 32);
                    grhData[grh].setFrame(1, grh);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos y almacenamos los datos del archivo "heads.ind".
     */
    private static void loadHeads() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/heads.ind", "rw")) {
            f.seek(0);

            final IndexHeads[] myHeads;
            final byte[] cabecera = new byte[263];

            f.read(cabecera);
            final short numHeads = bigToLittle_Short(f.readShort());

            headData = new HeadData[numHeads + 1];
            myHeads = new IndexHeads[numHeads + 1];

            headData[0] = new HeadData();
            for (int i = 1; i <= numHeads; i++) {
                myHeads[i] = new IndexHeads();
                myHeads[i].setHead(1, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(2, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(3, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(4, bigToLittle_Short(f.readShort()));

                headData[i] = new HeadData();
                if (myHeads[i].getHead(1) != 0) {
                    headData[i].setHead(1, initGrh(headData[i].getHead(1), myHeads[i].getHead(1), false));
                    headData[i].setHead(2, initGrh(headData[i].getHead(2), myHeads[i].getHead(2), false));
                    headData[i].setHead(3, initGrh(headData[i].getHead(3), myHeads[i].getHead(3), false));
                    headData[i].setHead(4, initGrh(headData[i].getHead(4), myHeads[i].getHead(4), false));
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos y almacenamos los datos del archivo "helmets.ind".
     */
    private static void loadHelmets() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/helmets.ind", "rw")) {
            f.seek(0);

            final IndexHeads[] myHeads;
            final byte[] cabecera = new byte[263];

            f.read(cabecera);
            final short numHeads = bigToLittle_Short(f.readShort());

            helmetsData = new HeadData[numHeads + 1];
            myHeads = new IndexHeads[numHeads + 1];

            helmetsData[0] = new HeadData();
            for (int i = 1; i <= numHeads; i++) {
                myHeads[i] = new IndexHeads();
                myHeads[i].setHead(1, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(2, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(3, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(4, bigToLittle_Short(f.readShort()));

                helmetsData[i] = new HeadData();
                if (myHeads[i].getHead(1) != 0) {
                    helmetsData[i].setHead(1, initGrh(helmetsData[i].getHead(1), myHeads[i].getHead(1), false));
                    helmetsData[i].setHead(2, initGrh(helmetsData[i].getHead(2), myHeads[i].getHead(2), false));
                    helmetsData[i].setHead(3, initGrh(helmetsData[i].getHead(3), myHeads[i].getHead(3), false));
                    helmetsData[i].setHead(4, initGrh(helmetsData[i].getHead(4), myHeads[i].getHead(4), false));
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos y almacenamos los datos del archivo "bodys.ind".
     */
    private static void loadBodys() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/bodys.ind", "rw")) {
            f.seek(0);

            final IndexBodys[] myBodys;
            final byte[] cabecera = new byte[263];

            f.read(cabecera);
            final short numBodys = bigToLittle_Short(f.readShort());

            bodyData = new BodyData[numBodys + 1];
            myBodys = new IndexBodys[numBodys + 1];

            bodyData[0] = new BodyData();
            for (int i = 1; i <= numBodys; i++) {
                myBodys[i] = new IndexBodys();
                myBodys[i].setBody(1, bigToLittle_Short(f.readShort()));
                myBodys[i].setBody(2, bigToLittle_Short(f.readShort()));
                myBodys[i].setBody(3, bigToLittle_Short(f.readShort()));
                myBodys[i].setBody(4, bigToLittle_Short(f.readShort()));

                myBodys[i].setHeadOffsetX( bigToLittle_Short(f.readShort()) );
                myBodys[i].setHeadOffsetY( bigToLittle_Short(f.readShort()) );

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

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos y almacenamos los datos del archivo "arms.ind".
     */
    private static void loadArms() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/arms.ind", "rw")) {
            final int numArms = bigToLittle_Short(f.readShort());
            weaponData = new WeaponData[numArms + 1];

            weaponData[0] = new WeaponData();
            for(int loopc = 1; loopc <= numArms; loopc++) {
                weaponData[loopc] = new WeaponData();
                weaponData[loopc].setWeaponWalk(1, initGrh(weaponData[loopc].getWeaponWalk(1), bigToLittle_Short(f.readShort()), false));
                weaponData[loopc].setWeaponWalk(2, initGrh(weaponData[loopc].getWeaponWalk(2), bigToLittle_Short(f.readShort()), false));
                weaponData[loopc].setWeaponWalk(3, initGrh(weaponData[loopc].getWeaponWalk(3), bigToLittle_Short(f.readShort()), false));
                weaponData[loopc].setWeaponWalk(4, initGrh(weaponData[loopc].getWeaponWalk(4), bigToLittle_Short(f.readShort()), false));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos y almacenamos los datos del archivo "shields.ind".
     */
    private static void loadShields() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/shields.ind", "rw")) {
            final int numShields = bigToLittle_Short(f.readShort());
            shieldData = new ShieldData[numShields + 1];


            shieldData[0] = new ShieldData();
            for(int loopc = 1; loopc <= numShields; loopc++) {
                shieldData[loopc] = new ShieldData();
                shieldData[loopc].setShieldWalk(1, initGrh(shieldData[loopc].getShieldWalk(1), bigToLittle_Short(f.readShort()), false));
                shieldData[loopc].setShieldWalk(2, initGrh(shieldData[loopc].getShieldWalk(2), bigToLittle_Short(f.readShort()), false));
                shieldData[loopc].setShieldWalk(3, initGrh(shieldData[loopc].getShieldWalk(3), bigToLittle_Short(f.readShort()), false));
                shieldData[loopc].setShieldWalk(4, initGrh(shieldData[loopc].getShieldWalk(4), bigToLittle_Short(f.readShort()), false));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos el mapa.
     */
    public static void loadMap(int map) {
        try (RandomAccessFile f = new RandomAccessFile("resources/maps/mapa" + map + ".map", "rw")) {
            f.seek(0);

            mapData = new MapData[101][101];

            final short mapversion = bigToLittle_Short(f.readShort());
            final byte[] cabecera = new byte[263];
            f.read(cabecera);

            byte byflags = 0;

            // Falta implementar el mapInfo xd....
            bigToLittle_Short(f.readShort());
            bigToLittle_Short(f.readShort());
            bigToLittle_Short(f.readShort());
            bigToLittle_Short(f.readShort());

            byte bloq;

            mapData[0][0] = new MapData();

            for (int y = 1; y <= 100; y++) {
                for (int x = 1; x <= 100; x++) {
                    mapData[x][y] = new MapData();

                    byflags = bigToLittle_Byte(f.readByte());
                    bloq = (byte)(byflags & 1);
                    mapData[x][y].setBlocked(bloq == 1);

                    mapData[x][y].getLayer(1).setGrhIndex( bigToLittle_Short(f.readShort()) );
                    mapData[x][y].setLayer(1, initGrh(mapData[x][y].getLayer(1), mapData[x][y].getLayer(1).getGrhIndex(), true));

                    if ((byte)(byflags & 2) != 0) {
                        mapData[x][y].getLayer(2).setGrhIndex( bigToLittle_Short(f.readShort()) );
                        mapData[x][y].setLayer(2, initGrh(mapData[x][y].getLayer(2), mapData[x][y].getLayer(2).getGrhIndex(), true));

                    } else {
                        mapData[x][y].getLayer(2).setGrhIndex(0);
                    }

                    if ((byte)(byflags & 4) != 0) {
                        mapData[x][y].getLayer(3).setGrhIndex( bigToLittle_Short(f.readShort()) );
                        mapData[x][y].setLayer(3, initGrh(mapData[x][y].getLayer(3), mapData[x][y].getLayer(3).getGrhIndex(), true));
                    } else {
                        mapData[x][y].getLayer(3).setGrhIndex(0);
                    }

                    if ((byte)(byflags & 8) != 0) {
                        mapData[x][y].getLayer(4).setGrhIndex( bigToLittle_Short(f.readShort()) );
                        mapData[x][y].setLayer(4, initGrh(mapData[x][y].getLayer(4), mapData[x][y].getLayer(4).getGrhIndex(), true));
                    } else {
                        mapData[x][y].getLayer(4).setGrhIndex(0);
                    }

                    if ((byte)(byflags & 16) != 0) {
                        mapData[x][y].setTrigger( bigToLittle_Short(f.readShort()) );
                    } else {
                        mapData[x][y].setTrigger(0);
                    }

//                    if (mapData[x][y].getCharIndex() > 0) {
//                        eraseChar(mapData[x][y].getCharIndex());
//                    }
                    mapData[x][y].getObjGrh().setGrhIndex(0);
                }
            }

            // Liberar memoria
            clearSounds();
            Surface.get().deleteAllTextures();
            eraseAllChars();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos los indices de animaciones FXs del archivo "fxs.ind"
     */
    private static void loadFxs() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/fxs.ind", "rw")) {
            f.seek(0);

            final byte[] cabecera = new byte[263];

            f.read(cabecera);
            final short numFXs = bigToLittle_Short(f.readShort());
            fxData = new FxData[numFXs + 1];

            for (int i = 1; i <= numFXs; i++) {
                fxData[i] = new FxData();
                fxData[i].setAnimacion( bigToLittle_Short(f.readShort()) );
                fxData[i].setOffsetX( bigToLittle_Short(f.readShort()) );
                fxData[i].setOffsetY( bigToLittle_Short(f.readShort()) );
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Cargamos los indices de mapas donde se puede visualizar la lluvia en el archivo "fk.ind"
     */
    private static void loadFK() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/fk.ind", "rw")) {
            f.seek(0);

            final byte[] cabecera = new byte[263];

            f.read(cabecera);
            final short Nu = bigToLittle_Short(f.readShort());
            bLluvia = new boolean[Nu + 1];

            bLluvia[0] = false;
            for (int i = 1; i <= Nu; i++) {
                bLluvia[i] = bigToLittle_Byte(f.readByte()) == 1;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * Inicializa los graficos, ya sean animaciones o no.
     */
    public static GrhInfo initGrh(GrhInfo grh, short grhIndex, boolean started) {
        if (grh == null) throw new NullPointerException("Se esta intentando incializar un GrhInfo nulo...");

        grh.setGrhIndex(grhIndex);
        grh.setStarted(false);
        grh.setLoops(0);

        if (started) {
            grh.setStarted(grhData[grh.getGrhIndex()].getNumFrames() > 1);
        }

        if (grh.isStarted()) {
            grh.setLoops(-1);
        }

        grh.setFrameCounter(1);
        //grh.setSpeed( grhData[grhIndex].getSpeed() );
        grh.setSpeed(0.4f);

        return grh;
    }

}
