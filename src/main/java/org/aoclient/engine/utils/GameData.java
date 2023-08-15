package org.aoclient.engine.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aoclient.engine.Sound;
import org.aoclient.engine.game.models.Character;
import org.aoclient.engine.renderer.Surface;
import org.aoclient.engine.utils.filedata.*;

import static org.aoclient.engine.Sound.clearSounds;
import static org.aoclient.engine.game.models.Character.eraseChar;

public final class GameData {
    public static BodyData[] bodyData;
    public static HeadData[] headData;
    public static HeadData[] helmetsData;
    public static WeaponData[] weaponData;
    public static ShieldData[] shieldData;
    public static FxData[] fxData;
    public static GrhData[] grhData;
    public static MapData[][] mapData;
    public static FontData[] fontTypes = new FontData[2];

    public static Character charList[] = new Character[10000+1];
    public static Sound music;
    public static Map<String, Sound> sounds = new HashMap<>();

    /**
     * @desc: Inicializamos todos los datos almacenados en archivos.
     */
    public static void initialize() {
        for (int i = 0; i < charList.length; i++) {
            charList[i] = new Character();
        }

        loadGrhData();
        loadHeads();
        loadHelmets();
        loadBodys();
        loadArms();
        loadShields();
        loadFonts();
        LoadFXs();

        //addMusic("resources/MP3/intro.ogg").play();
    }

    /**
     * @desc: Permite migrar un Integer de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static int bigToLittle_Int(int bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt(0);
    }

    /**
     * @desc: Permite migrar un Float de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static float bigToLittle_Float(float bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putFloat(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat(0);
    }

    /**
     * @desc: Permite migrar un Float de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static double bigToLittle_Double(double bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(8);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putDouble(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getDouble(0);
    }

    /**
     * @desc: Permite migrar un Short de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static short bigToLittle_Short(short bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(2);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort(0);
    }

    /**
     * @desc: Permite migrar un Byte de mas cantidad de bytes a uno mas chico por que en
     *        VB6 las variables son de distinta cantidad de bytes
     */
    public static byte bigToLittle_Byte(byte bigendian) {
        ByteBuffer buf = ByteBuffer.allocate(1);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.put(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.get(0);
    }

    /**
     * @desc: Cargamos y almacenamos los datos del archivo "graphics.ind".
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
     * @desc: Cargamos y almacenamos los datos del archivo "heads.ind".
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

            for (int i = 1; i <= numHeads; i++) {
                myHeads[i] = new IndexHeads();
                myHeads[i].setHead(1, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(2, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(3, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(4, bigToLittle_Short(f.readShort()));

                if (myHeads[i].getHead(1) != 0) {
                    headData[i] = new HeadData();

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
     * @desc: Cargamos y almacenamos los datos del archivo "helmets.ind".
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

            for (int i = 1; i <= numHeads; i++) {
                myHeads[i] = new IndexHeads();
                myHeads[i].setHead(1, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(2, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(3, bigToLittle_Short(f.readShort()));
                myHeads[i].setHead(4, bigToLittle_Short(f.readShort()));

                //if (myHeads[i].getHead(1) != 0) {
                    helmetsData[i] = new HeadData();
                    helmetsData[i].setHead(1, initGrh(helmetsData[i].getHead(1), myHeads[i].getHead(1), false));
                    helmetsData[i].setHead(2, initGrh(helmetsData[i].getHead(2), myHeads[i].getHead(2), false));
                    helmetsData[i].setHead(3, initGrh(helmetsData[i].getHead(3), myHeads[i].getHead(3), false));
                    helmetsData[i].setHead(4, initGrh(helmetsData[i].getHead(4), myHeads[i].getHead(4), false));

                //}
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @desc: Cargamos y almacenamos los datos del archivo "bodys.ind".
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

            for (int i = 1; i <= numBodys; i++) {
                myBodys[i] = new IndexBodys();
                myBodys[i].setBody(1, bigToLittle_Short(f.readShort()));
                myBodys[i].setBody(2, bigToLittle_Short(f.readShort()));
                myBodys[i].setBody(3, bigToLittle_Short(f.readShort()));
                myBodys[i].setBody(4, bigToLittle_Short(f.readShort()));

                myBodys[i].setHeadOffsetX( bigToLittle_Short(f.readShort()) );
                myBodys[i].setHeadOffsetY( bigToLittle_Short(f.readShort()) );

                if (myBodys[i].getBody(1) != 0) {
                    bodyData[i] = new BodyData();
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
     * @desc: Cargamos y almacenamos los datos del archivo "arms.ind".
     */
    private static void loadArms() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/arms.ind", "rw")) {
            final int numArms = bigToLittle_Short(f.readShort());
            weaponData = new WeaponData[numArms + 1];

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
     * @desc: Cargamos y almacenamos los datos del archivo "shields.ind".
     */
    private static void loadShields() {
        try (RandomAccessFile f = new RandomAccessFile("resources/inits/shields.ind", "rw")) {
            final int numShields = bigToLittle_Short(f.readShort());
            shieldData = new ShieldData[numShields + 1];

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
     * @desc: Cargamos el mapa.
     */
    public static void loadMap(int map) {
        try (RandomAccessFile f = new RandomAccessFile("resources/maps/mapa" + map + ".map", "rw")) {
            f.seek(0);

            mapData = new MapData[101][101];

            final short mapversion = bigToLittle_Short(f.readShort());
            final byte[] cabecera = new byte[263];
            f.read(cabecera);

            byte byflags = 0;

            // que era esto?
            bigToLittle_Short(f.readShort());
            bigToLittle_Short(f.readShort());
            bigToLittle_Short(f.readShort());
            bigToLittle_Short(f.readShort());

            byte bloq;

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

                    if (mapData[x][y].getCharIndex() > 0) {
                        eraseChar(mapData[x][y].getCharIndex());
                    }

                    mapData[x][y].getObjGrh().setGrhIndex(0);
                }
            }

            // Liberar memoria
            clearSounds();
            Surface.getInstance().deleteAllTextures();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @desc: Cargamos los indices de animaciones FXs del archivo "fxs.ind"
     */
    private static void LoadFXs() {
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
     * @desc: Cargamos el indice de graficos de la fuente de letras (ESTA HARDCODEADO, HAY QUE ARMAR UN ARCHIVO!)
     */
    private static void loadFonts() {
        fontTypes[0] = new FontData();
        fontTypes[0].setFont_size(9);
        fontTypes[0].setAscii_code(48, 23703);
        fontTypes[0].setAscii_code(49, 23704);
        fontTypes[0].setAscii_code(50, 23705);
        fontTypes[0].setAscii_code(51, 23706);
        fontTypes[0].setAscii_code(52, 23707);
        fontTypes[0].setAscii_code(53, 23708);
        fontTypes[0].setAscii_code(54, 23709);
        fontTypes[0].setAscii_code(55, 23710);
        fontTypes[0].setAscii_code(56, 23711);
        fontTypes[0].setAscii_code(57, 23712);


        for (int a = 0; a <= 25; a++) {
            fontTypes[0].setAscii_code(a + 97, 23651 + a);
        }

        for (int a = 0; a <= 25; a++) {
            fontTypes[0].setAscii_code(a + 65, 23677 + a);
        }

        fontTypes[0].setAscii_code(33,23713);
        fontTypes[0].setAscii_code(161, 23714);
        fontTypes[0].setAscii_code(34,23715);
        fontTypes[0].setAscii_code(36,23716);
        fontTypes[0].setAscii_code(191, 23717);
        fontTypes[0].setAscii_code(35,23718);
        fontTypes[0].setAscii_code(36,23719);
        fontTypes[0].setAscii_code(37,23720);
        fontTypes[0].setAscii_code(38,23721);
        fontTypes[0].setAscii_code(47,23722);
        fontTypes[0].setAscii_code(92,23723);
        fontTypes[0].setAscii_code(40,23724);
        fontTypes[0].setAscii_code(41,23725);
        fontTypes[0].setAscii_code(61,23726);
        fontTypes[0].setAscii_code(39,23727);
        fontTypes[0].setAscii_code(123, 23728);
        fontTypes[0].setAscii_code(125, 23729);
        fontTypes[0].setAscii_code(95,23730);
        fontTypes[0].setAscii_code(45,23731);
        fontTypes[0].setAscii_code(63,23716);
        fontTypes[0].setAscii_code(64,23732);
        fontTypes[0].setAscii_code(94,23733);
        fontTypes[0].setAscii_code(91,23734);
        fontTypes[0].setAscii_code(93,23735);
        fontTypes[0].setAscii_code(60,23736);
        fontTypes[0].setAscii_code(62,23737);
        fontTypes[0].setAscii_code(42,23738);
        fontTypes[0].setAscii_code(43,23739);
        fontTypes[0].setAscii_code(46,23740);
        fontTypes[0].setAscii_code(44,23741);
        fontTypes[0].setAscii_code(58,23742);
        fontTypes[0].setAscii_code(59,23743);
        fontTypes[0].setAscii_code(124, 23744);
        /////////////////
        fontTypes[0].setAscii_code(252, 23845);
        fontTypes[0].setAscii_code(220, 23846);
        fontTypes[0].setAscii_code(225, 23847);
        fontTypes[0].setAscii_code(233, 23848);
        fontTypes[0].setAscii_code(237, 23849);
        fontTypes[0].setAscii_code(243, 23850);
        fontTypes[0].setAscii_code(250, 23851);
        fontTypes[0].setAscii_code(253, 23852);
        fontTypes[0].setAscii_code(193, 23853);
        fontTypes[0].setAscii_code(201, 23854);
        fontTypes[0].setAscii_code(205, 23855);
        fontTypes[0].setAscii_code(211, 23856);
        fontTypes[0].setAscii_code(218, 23857);
        fontTypes[0].setAscii_code(221, 23858);
        fontTypes[0].setAscii_code(224, 23859);
        fontTypes[0].setAscii_code(232, 23860);
        fontTypes[0].setAscii_code(236, 23861);
        fontTypes[0].setAscii_code(242, 23862);
        fontTypes[0].setAscii_code(249, 23863);
        fontTypes[0].setAscii_code(192, 23864);
        fontTypes[0].setAscii_code(200, 23865);
        fontTypes[0].setAscii_code(204, 23866);
        fontTypes[0].setAscii_code(210, 23867);
        fontTypes[0].setAscii_code(217, 23868);
        fontTypes[0].setAscii_code(241, 23869);
        fontTypes[0].setAscii_code(209, 23870);

        fontTypes[0].setAscii_code(196, 23970);
        fontTypes[0].setAscii_code(194, 23971);
        fontTypes[0].setAscii_code(203, 23972);
        fontTypes[0].setAscii_code(207, 23973);
        fontTypes[0].setAscii_code(214, 23974);
        fontTypes[0].setAscii_code(212, 23975);

        fontTypes[0].setAscii_code(172, 23975);

        fontTypes[1] = new FontData();
        fontTypes[1].setFont_size(9);
        fontTypes[1].setAscii_code(97,24076);
        fontTypes[1].setAscii_code(108, 24077);
        fontTypes[1].setAscii_code(115, 24078);
        fontTypes[1].setAscii_code(70,24079);
        fontTypes[1].setAscii_code(48,24080);
        fontTypes[1].setAscii_code(49,24081);
        fontTypes[1].setAscii_code(50,24082);
        fontTypes[1].setAscii_code(51,24083);
        fontTypes[1].setAscii_code(52,24084);
        fontTypes[1].setAscii_code(53,24085);
        fontTypes[1].setAscii_code(54,24086);
        fontTypes[1].setAscii_code(55,24087);
        fontTypes[1].setAscii_code(56,24088);
        fontTypes[1].setAscii_code(57,24089);
        fontTypes[1].setAscii_code(33,24090);
        fontTypes[1].setAscii_code(161, 24091);
        fontTypes[1].setAscii_code(42,24092);
    }

    /**
     *
     * @desc: Inicializa los graficos, ya sean animaciones o no.
     */
    public static GrhInfo initGrh(GrhInfo grh, short grhIndex, boolean started) {
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
