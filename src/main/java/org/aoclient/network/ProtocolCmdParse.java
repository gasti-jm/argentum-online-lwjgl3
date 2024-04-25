package org.aoclient.network;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.game.User;
import org.aoclient.engine.gui.ImGUISystem;
import org.aoclient.engine.gui.forms.FNewPassword;
import org.aoclient.engine.renderer.RGBColor;
import org.aoclient.network.packets.eEditOptions;
import org.aoclient.network.packets.eNumber_Types;

import java.nio.charset.StandardCharsets;

import static org.aoclient.network.Protocol.*;

public class ProtocolCmdParse {

    public static void parseUserCommand(String rawCommand) {
        // Declaración de variables
        String[] tmpArgs;
        String comando;
        String[] argumentosAll = new String[0];
        String argumentosRaw = "";
        String[] argumentos2;
        String[] argumentos3;
        String[] argumentos4;
        int cantidadArgumentos;
        boolean notNullArguments = false;
        String[] tmpArr;
        int tmpInt;

        // Separar el comando y los argumentos
        tmpArgs = rawCommand.split(" ", 2);
        comando = tmpArgs[0].toUpperCase();

        if (tmpArgs.length > 1) {
            argumentosRaw = tmpArgs[1];
            notNullArguments = argumentosRaw.trim().length() > 0;
            argumentosAll = tmpArgs[1].split(" ");
            cantidadArgumentos = argumentosAll.length;

            if (notNullArguments) {
                argumentos2 = tmpArgs[1].split(" ", 2);
                argumentos3 = tmpArgs[1].split(" ", 3);
                argumentos4 = tmpArgs[1].split(" ", 4);
            }
        } else {
            cantidadArgumentos = 0;
        }

        // Procesar el comando
        if (comando.startsWith("/")) {
            // Comando normal
            switch (comando) {

                case "/ONLINE":
                    writeOnline();
                    break;

                case "/SALIR":
                    writeQuit();
                    break;

                case "/SALIRCLAN":
                    writeGuildLeave();
                    break;

                case "/BALANCE":
                    writeRequestAccountState();
                    break;

                case "/QUIETO":
                    writePetStand();
                    break;

                case "/ACOMPAÑAR":
                    writePetFollow();
                    break;

                case "/LIBERAR":
                    writeReleasePet();
                    break;

                case "/ENTRENAR":
                    writeTrainList();
                    break;

                case "/DESCANSAR":
                    writeRest();
                    break;

                case "/MEDITAR":
                    writeMeditate();
                    break;

                case "/CONSULTA":
                    writeConsultation();
                    break;

                case "/RESUCITAR":
                    writeResucitate();
                    break;

                case "/CURAR":
                    writeHeal();
                    break;

                case "/EST":
                    writeRequestStats();
                    break;

                case "/AYUDA":
                    writeHelp();
                    break;

                case "/COMERCIAR":
                    writeCommerceStart();
                    break;

                case "/BOVEDA":
                    writeBankStart();
                    break;

                case "/ENLISTAR":
                    writeEnlist();
                    break;

                case "/INFORMACION":
                    writeInformation();
                    break;

                case "/RECOMPENSA":
                    writeReward();
                    break;

                case "/MOTD":
                    writeRequestMOTD();
                    break;

                case "/UPTIME":
                    writeUpTime();
                    break;

                case "/SALIRPARTY":
                    writePartyLeave();
                    break;

                case "/CREARPARTY":
                    writePartyCreate();
                    break;

                case "/PARTY":
                    writePartyJoin();
                    break;

                case "/COMPARTIRNPC":
                    writeShareNpc();
                    break;

                case "/NOCOMPARTIRNPC":
                    writeStopSharingNpc();
                    break;

                case "/ENCUESTA":
                    if (cantidadArgumentos == 0) {
                        writeInquiry();
                    } else {
                        if (validNumber(argumentosRaw, eNumber_Types.ent_Byte)) {
                            writeInquiryVote(Integer.parseInt(argumentosRaw));
                        } else {
                            Console.get().addMsgToConsole(new String("Para votar una opción, escribe /encuesta NUMERODEOPCION, por ejemplo para votar la opción 1, escribe /encuesta 1.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    }
                    break;

                case "/CMSG":
                    if (cantidadArgumentos == 0) {
                        writeGuildMessage(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Escribe un mensaje.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/PMSG":
                    if (cantidadArgumentos == 0) {
                        writePartyMessage(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Escribe un mensaje.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/CENTINELA":
                    if (cantidadArgumentos != 1) {
                        Console.get().addMsgToConsole(new String("El comando /CENTINELA requiere un argumento. Por favor, ingrese el código de verificación..".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());

                    } else {
                        if (validNumber(argumentosRaw, eNumber_Types.ent_Integer)) {
                            writeCentinelReport(Integer.parseInt(argumentosRaw));

                        } else {
                            Console.get().addMsgToConsole(new String("El código de verificación debe ser numérico. Utilice /centinela X, donde X es el código de verificación.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    }
                    break;

                case "/ONLINECLAN":
                    writeGuildOnline();
                    break;

                case "/ONLINEPARTY":
                    writePartyOnline();
                    break;

                case "/BMSG":
                    if (notNullArguments) {
                        writeCouncilMessage(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Escriba un mensaje.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/ROL":
                    if (notNullArguments) {
                        writeRoleMasterRequest(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Escriba un mensaje.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/GM":
                    writeGMRequest();
                    break;

                case "/_BUG":
                    if (notNullArguments) {
                        writeBugReport(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Escriba una descripción del bug.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/DESC":
                    if (User.get().isDead()) {
                        Console.get().addMsgToConsole(new String("¡Estás muerto!".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    } else {
                        writeChangeDescription(argumentosRaw);
                    }
                    break;

                case "/VOTO":
                    if (notNullArguments) {
                        writeGuildVote(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /voto NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/PENAS":
                    if (notNullArguments) {
                        writePunishments(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /penas NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/CONTRASEÑA":
                    ImGUISystem.get().checkAddOrChange("frmNewPassword", new FNewPassword());
                    break;

                case "/APOSTAR":
                    if (User.get().isDead()) {
                        Console.get().addMsgToConsole(new String("¡Estás muerto!".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    } else {
                        if (notNullArguments) {
                            if (validNumber(argumentosRaw, eNumber_Types.ent_Integer)) {
                                writeGamble(Short.parseShort(argumentosRaw));
                            } else {
                                Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /apostar CANTIDAD.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        } else {
                            Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /apostar CANTIDAD.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    }
                    break;

                case "/RETIRARFACCION":
                    if (User.get().isDead()) {
                        Console.get().addMsgToConsole(new String("¡Estás muerto!".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    } else {
                        writeLeaveFaction();
                    }
                    break;

                case "/RETIRAR":
                    if (User.get().isDead()) {
                        Console.get().addMsgToConsole(new String("¡Estás muerto!".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    } else {

                        if (notNullArguments) {
                            if (validNumber(argumentosRaw,eNumber_Types.ent_Long)) {
                                writeBankExtractGold(Integer.parseInt(argumentosRaw));
                            } else {
                                Console.get().addMsgToConsole(new String("Cantidad incorrecta. Utilice /retirar CANTIDAD.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        }

                    }
                    break;

                case "/DEPOSITAR":
                    if (User.get().isDead()) {
                        Console.get().addMsgToConsole(new String("¡Estás muerto!".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    } else {

                        if (notNullArguments) {
                            if (validNumber(argumentosRaw,eNumber_Types.ent_Long)) {
                                writeBankDepositGold(Integer.parseInt(argumentosRaw));
                            } else {
                                Console.get().addMsgToConsole(new String("Cantidad incorrecta. Utilice /depositar CANTIDAD.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        }

                    }
                    break;

                case "/DENUNCIAR":
                    if (notNullArguments) {
                        writeDenounce(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Formule su denuncia.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/FUNDARCLAN":
                    if (User.get().getUserLvl() >= 25) {
                        writeGuildFundate();
                    } else {
                        Console.get().addMsgToConsole(new String("Para fundar un clan tenés que ser nivel 25 y tener 90 skills en liderazgo.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/FUNDARCLANGM":
                    writeGuildFundation(3); //NOTA: En el codigo original clanType pertenece a un Enum que solo se utiliza en este caso, evaluar si crear el Enum o dejarlo así. El 3 pertenece al clan de GM.
                    break;

                case "/ECHARPARTY":
                    if(notNullArguments) {
                        writePartyKick(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /echarparty NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/PARTYLIDER":
                    if(notNullArguments) {
                        writePartySetLeader(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /partylider NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/ACCEPTPARTY":
                    if(notNullArguments) {
                        writePartyAcceptMember(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /acceptparty NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                /* ##############################################
                   #        COMIENZAN LOS COMANDOS DE GM        #
                   ##############################################*/

                case "/GMSG":
                    if(notNullArguments) {
                        writeGMMessage(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Escriba un mensaje.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/SHOWNAME":
                    writeShowName();
                    break;

                case "/ONLINEREAL":
                    writeOnlineRoyalArmy();
                    break;

                case "/ONLINECAOS":
                    writeOnlineChaosLegion();
                    break;

                case "/IRCERCA":
                    if(notNullArguments) {
                        writeGoNearby(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /ircerca NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/REM":
                    if(notNullArguments) {
                        writeComment(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Escriba un comentario.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/HORA":
                    writeServerTime();
                    break;

                case "/DONDE":
                    if(notNullArguments) {
                        writeWhere(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /donde NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/NENE":
                    if(notNullArguments) {
                        if (validNumber(argumentosRaw, eNumber_Types.ent_Integer)) {
                            writeCreaturesInMap(Short.parseShort(argumentosRaw));
                        } else {
                            // No es numérico.
                            Console.get().addMsgToConsole(new String("Mapa incorrecto. Utilice /nene MAPA..".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Por defecto toma el mapa en el que está.
                        writeCreaturesInMap(User.get().getUserMap());
                    }
                    break;

                case "/TELEPLOC":
                    writeWarpMeToTarget();
                    break;

                case "/TELEP":
                    if (notNullArguments && cantidadArgumentos >= 4) {
                        if (validNumber(argumentosAll[1], eNumber_Types.ent_Integer) &&
                                validNumber(argumentosAll[2], eNumber_Types.ent_Byte) &&
                                validNumber(argumentosAll[3], eNumber_Types.ent_Byte)) {
                            writeWarpChar(argumentosAll[0], Short.parseShort(argumentosAll[1]), Integer.parseInt(argumentosAll[2]), Integer.parseInt(argumentosAll[3]));
                        } else {
                            Console.get().addMsgToConsole(new String("Valor incorrecto. Utilice /telep NICKNAME MAPA X Y.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else if (cantidadArgumentos == 3) {
                        if (validNumber(argumentosAll[0], eNumber_Types.ent_Integer) &&
                                validNumber(argumentosAll[1], eNumber_Types.ent_Byte) &&
                                validNumber(argumentosAll[2], eNumber_Types.ent_Byte)) {
                            // Por defecto, si no se indica el nombre, se teletransporta el mismo usuario
                            writeWarpChar("YO", Short.parseShort(argumentosAll[0]), Integer.parseInt(argumentosAll[1]), Integer.parseInt(argumentosAll[2]));
                        } else if (validNumber(argumentosAll[1], eNumber_Types.ent_Byte) &&
                                validNumber(argumentosAll[2], eNumber_Types.ent_Byte)) {
                            // Por defecto, si no se indica el mapa, se teletransporta al mismo donde esta el usuario
                            writeWarpChar(argumentosAll[0], User.get().getUserMap(), Integer.parseInt(argumentosAll[1]), Integer.parseInt(argumentosAll[2]));
                        } else {
                            // No uso ningun formato por defecto
                            Console.get().addMsgToConsole(new String("Valor incorrecto. Utilice /telep NICKNAME MAPA X Y.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else if (cantidadArgumentos == 2) {
                        if (validNumber(argumentosAll[0], eNumber_Types.ent_Byte) &&
                                validNumber(argumentosAll[1], eNumber_Types.ent_Byte)) {
                            // Por defecto, se considera que se quiere unicamente cambiar las coordenadas del usuario, en el mismo mapa
                            writeWarpChar("YO", User.get().getUserMap(), Integer.parseInt(argumentosAll[0]), Integer.parseInt(argumentosAll[1]));
                        } else {
                            // No uso ningun formato por defecto
                            Console.get().addMsgToConsole(new String("Valor incorrecto. Utilice /telep NICKNAME MAPA X Y.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parametro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /telep NICKNAME MAPA X Y.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/SILENCIAR":
                    if(notNullArguments) {
                        writeSilence(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /silenciar NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/SHOW":
                    if (notNullArguments) {
                        switch (argumentosAll[0].toUpperCase()) {
                            case "SOS":
                                writeSOSShowList();
                                break;
                            case "INT":
                                writeShowServerForm();
                                break;
                        }
                    }
                    break;

                case "/IRA":
                    if(notNullArguments) {
                        writeGoToChar(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /ira NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/INVISIBLE":
                    writeInvisible();
                    break;

                case "/PANELGM":
                    writeGMPanel();
                    break;

                case "/TRABAJANDO":
                    writeWorking();
                    break;

                case "/OCULTANDO":
                    writeHiding();
                    break;

                case "/CARCEL":
                    if (notNullArguments) {
                        tmpArr = argumentosRaw.split("@");
                        if (tmpArr.length == 3) {
                            if (validNumber(tmpArr[2], eNumber_Types.ent_Byte)) {
                                writeJail(tmpArr[0], tmpArr[1], Integer.parseInt(tmpArr[2]));
                            } else {
                                // No es numérico
                                Console.get().addMsgToConsole(new String("Tiempo incorrecto. Utilice /carcel NICKNAME@MOTIVO@TIEMPO.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        } else {
                            // Faltan los parámetros con el formato propio
                            Console.get().addMsgToConsole(new String("Formato incorrecto. Utilice /carcel NICKNAME@MOTIVO@TIEMPO.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /carcel NICKNAME@MOTIVO@TIEMPO.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/RMATA":
                    writeKillNPC();
                    break;

                case "/ADVERTENCIA":
                    if (notNullArguments){
                        tmpArr = argumentosRaw.split("@", 2);
                        if (tmpArr.length == 2) {
                            writeWarnUser(tmpArr[0], tmpArr[1]);
                        } else {
                            //Faltan los parametros con el formato propio
                            Console.get().addMsgToConsole(new String("Formato incorrecto. Utilice /advertencia NICKNAME@MOTIVO".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /advertencia NICKNAME@MOTIVO@TIEMPO.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/MOD":
                    if (notNullArguments && cantidadArgumentos >= 3) {
                        String argumento = argumentosAll[1].toUpperCase();
                        tmpInt = -1;
                        switch (argumento) {
                            case "BODY":
                                tmpInt = eEditOptions.eo_Body.getValue();
                                break;
                            case "HEAD":
                                tmpInt = eEditOptions.eo_Head.getValue();
                                break;
                            case "ORO":
                                tmpInt = eEditOptions.eo_Gold.getValue();
                                break;
                            case "LEVEL":
                                tmpInt = eEditOptions.eo_Level.getValue();
                                break;
                            case "SKILLS":
                                tmpInt = eEditOptions.eo_Skills.getValue();
                                break;
                            case "SKILLSLIBRES":
                                tmpInt = eEditOptions.eo_SkillPointsLeft.getValue();
                                break;
                            case "CLASE":
                                tmpInt = eEditOptions.eo_Class.getValue();
                                break;
                            case "EXP":
                                tmpInt = eEditOptions.eo_Experience.getValue();
                                break;
                            case "CRI":
                                tmpInt = eEditOptions.eo_CriminalsKilled.getValue();
                                break;
                            case "CIU":
                                tmpInt = eEditOptions.eo_CiticensKilled.getValue();
                                break;
                            case "NOB":
                                tmpInt = eEditOptions.eo_Nobleza.getValue();
                                break;
                            case "ASE":
                                tmpInt = eEditOptions.eo_Asesino.getValue();
                                break;
                            case "SEX":
                                tmpInt = eEditOptions.eo_Sex.getValue();
                                break;
                            case "RAZA":
                                tmpInt = eEditOptions.eo_Raza.getValue();
                                break;
                            case "AGREGAR":
                                tmpInt = eEditOptions.eo_addGold.getValue();
                                break;

                            default:
                                tmpInt = -1;
                        }

                        if (tmpInt > 0) {
                            if (cantidadArgumentos == 3) {
                                writeEditChar(argumentosAll[0], tmpInt, argumentosAll[2], "");
                            } else {
                                writeEditChar(argumentosAll[0], tmpInt, argumentosAll[2], argumentosAll[3]);
                            }
                        } else {
                            // Avisar que no existe el comando
                            Console.get().addMsgToConsole(new String("Comando incorrecto.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Faltan parámetros.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/INFO":
                    if(notNullArguments) {
                        writeRequestCharInfo(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /info NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/STAT":
                    if(notNullArguments) {
                        writeRequestCharStats(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /stat NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/BAL":
                    if(notNullArguments) {
                        writeRequestCharGold(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /bal NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/INV":
                    if(notNullArguments) {
                        writeRequestCharInventory(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /inv NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/BOV":
                    if(notNullArguments) {
                        writeRequestCharBank(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /bov NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/SKILLS":
                    if(notNullArguments) {
                        writeRequestCharSkills(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /skills NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/REVIVIR":
                    if(notNullArguments) {
                        writeReviveChar(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /revivir NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/ONLINEGM":
                    writeOnlineGM();
                    break;

                case "/ONLINEMAP":
                    if(notNullArguments) {
                        if (validNumber(argumentosAll[0], eNumber_Types.ent_Integer)){
                            writeOnlineMap(Short.parseShort(argumentosAll[0]));
                        } else {
                            Console.get().addMsgToConsole(new String("Mapa incorrecto.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        writeOnlineMap(User.get().getUserMap());
                    }
                    break;

                case "/PERDON":
                    if(notNullArguments) {
                        writeForgive(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /perdon NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/ECHAR":
                    if(notNullArguments) {
                        writeKick(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /echar NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/EJECUTAR":
                    if(notNullArguments) {
                        writeExecute(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /ejecutar NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/BAN":
                    if (notNullArguments) {
                        tmpArr = argumentosRaw.split("@", 2);
                        if (tmpArr.length == 2) {
                            writeBanChar(tmpArr[0], tmpArr[1]);
                        } else {
                            // Faltan los parámetros con el formato propio
                            Console.get().addMsgToConsole(new String("Formato incorrecto. Utilice /ban NICKNAME@MOTIVO.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /ban NICKNAME@MOTIVO.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/UNBAN":
                    if(notNullArguments) {
                        writeUnbanChar(argumentosRaw);
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /unban NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/SEGUIR":
                    writeNPCFollow();
                    break;

                case "/SUM":
                    if(notNullArguments) {
                        writeSummonChar();
                    } else {
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /sum NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/CC":
                    writeSpawnListRequest();
                    break;

                case "/RESETINV":
                    writeResetNPCInventory();
                    break;

                case "/LIMPIAR":
                    writeCleanWorld();
                    break;

                case "/RMSG":
                    if(notNullArguments) {
                        writeServerMessage();
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Escriba un mensaje.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/NICK2IP":
                    if(notNullArguments) {
                        writeNickToIP(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /nick2ip ip.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/IP2NICK":
                    if (notNullArguments) {
                        if (validIPv4Str(argumentosRaw)) {
                            int[] ip = str2ipv4l(argumentosRaw);
                            if (ip != null) {
                                writeIPToNick(ip);
                            } else {
                                // La conversión de la IP falló
                                Console.get().addMsgToConsole(new String("Error al convertir la IP.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        } else {
                            // No es una IP válida
                            Console.get().addMsgToConsole(new String("IP incorrecta. Utilice /ip2nick IP.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /ip2nick IP.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/ONCLAN":
                    if(notNullArguments) {
                        writeGuildOnlineMembers(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Utilice /onclan nombre del clan".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/CT":
                    if (notNullArguments && cantidadArgumentos >= 3) {
                        if (validNumber(argumentosAll[0], eNumber_Types.ent_Integer) &&
                                validNumber(argumentosAll[1], eNumber_Types.ent_Byte) &&
                                validNumber(argumentosAll[2], eNumber_Types.ent_Byte)) {

                            if (cantidadArgumentos == 3) {
                                writeTeleportCreate(Short.parseShort(argumentosAll[0]), Integer.parseInt(argumentosAll[1]), Integer.parseInt(argumentosAll[2]),0);
                            } else {
                                if (validNumber(argumentosAll[3], eNumber_Types.ent_Byte)) {
                                    writeTeleportCreate(Short.parseShort(argumentosAll[0]), Integer.parseInt(argumentosAll[1]), Integer.parseInt(argumentosAll[2]), Integer.parseInt(argumentosAll[3]));
                                } else {
                                    // No es numérico
                                    Console.get().addMsgToConsole(new String("Valor incorrecto. Utilice /ct MAPA X Y RADIO(Opcional).".getBytes(), StandardCharsets.UTF_8),
                                            false, true, new RGBColor());
                                }
                            }
                        } else {
                            // No es numérico
                            Console.get().addMsgToConsole(new String("Valor incorrecto. Utilice /ct MAPA X Y RADIO(Opcional).".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /ct MAPA X Y RADIO(Opcional).".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/DT":
                    writeTeleportDestroy();
                    break;

                case "/LLUVIA":
                    writeRainToggle();
                    break;

                case "/SETDESC":
                    writeSetCharDescription(argumentosRaw);
                    break;

                case "/FORCEMIDIMAP":
                    if (notNullArguments) {
                        // Elegir el mapa es opcional
                        if (cantidadArgumentos == 1) {
                            if (validNumber(argumentosAll[0], eNumber_Types.ent_Byte)) {
                                // Enviamos un mapa nulo para que tome el del usuario.
                                writeForceMIDIToMap(Integer.parseInt(argumentosAll[0]), (short) 0);
                            } else {
                                // No es numérico
                                Console.get().addMsgToConsole(new String("Midi incorrecto. Utilice /forcemidimap MIDI MAPA, siendo el mapa opcional.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        } else if (cantidadArgumentos == 2) {
                            if (validNumber(argumentosAll[0], eNumber_Types.ent_Byte) &&
                                    validNumber(argumentosAll[1], eNumber_Types.ent_Integer)) {
                                writeForceMIDIToMap(Integer.parseInt(argumentosAll[0]), Short.parseShort(argumentosAll[1]));
                            } else {
                                // No es numérico
                                Console.get().addMsgToConsole(new String("Valor incorrecto. Utilice /forcemidimap MIDI MAPA, siendo el mapa opcional.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        } else {
                            // Avisar que falta el parámetro
                            Console.get().addMsgToConsole(new String("Utilice /forcemidimap MIDI MAPA, siendo el mapa opcional.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Utilice /forcemidimap MIDI MAPA, siendo el mapa opcional.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/FORCEWAVMAP":
                    if (notNullArguments) {
                        // Elegir la posición es opcional
                        if (cantidadArgumentos == 1) {
                            if (validNumber(argumentosAll[0], eNumber_Types.ent_Byte)) {
                                // Enviamos una posición nula para que tome la del usuario.
                                writeForceWAVEToMap(Integer.parseInt(argumentosAll[0]), (short) 0, 0, 0);
                            } else {
                                // No es numérico
                                Console.get().addMsgToConsole(new String("Utilice /forcewavmap WAV MAP X Y, siendo los últimos 3 opcionales.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        } else if (cantidadArgumentos == 4) {
                            if (validNumber(argumentosAll[0], eNumber_Types.ent_Byte) &&
                                    validNumber(argumentosAll[1], eNumber_Types.ent_Integer) &&
                                    validNumber(argumentosAll[2], eNumber_Types.ent_Byte) &&
                                    validNumber(argumentosAll[3], eNumber_Types.ent_Byte)) {
                                writeForceWAVEToMap(Integer.parseInt(argumentosAll[0]), Short.parseShort(argumentosAll[1]), Integer.parseInt(argumentosAll[2]), Integer.parseInt(argumentosAll[3]));
                            } else {
                                // No es numérico
                                Console.get().addMsgToConsole(new String("Utilice /forcewavmap WAV MAP X Y, siendo los últimos 3 opcionales.".getBytes(), StandardCharsets.UTF_8),
                                        false, true, new RGBColor());
                            }
                        } else {
                            // Avisar que falta el parámetro
                            Console.get().addMsgToConsole(new String("Utilice /forcewavmap WAV MAP X Y, siendo los últimos 3 opcionales.".getBytes(), StandardCharsets.UTF_8),
                                    false, true, new RGBColor());
                        }
                    } else {
                        // Avisar que falta el parámetro
                        Console.get().addMsgToConsole(new String("Utilice /forcewavmap WAV MAP X Y, siendo los últimos 3 opcionales.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/REALMSG":
                    if(notNullArguments) {
                        writeRoyaleArmyMessage(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Escriba un mensaje".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/CAOSMSG":
                    if(notNullArguments) {
                        writeChaosLegionMessage(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Escriba un mensaje".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/CIUMSG":
                    if(notNullArguments) {
                        writeCitizenMessage(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Escriba un mensaje".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/CRIMSG":
                    if(notNullArguments) {
                        writeCriminalMessage(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Escriba un mensaje".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/TALKAS":
                    if(notNullArguments) {
                        writeTalkAsNPC(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Escriba un mensaje".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/MASDEST":
                    writeDestroyAllItemsInArea();
                    break;

                case "/ACEPTCONSE":
                    if(notNullArguments) {
                        writeAcceptRoyalCouncilMember(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /aceptconse NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/ACEPTCONSECAOS":
                    if(notNullArguments) {
                        writeAcceptChaosCouncilMember(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /aceptconsecaos NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/PISO":
                    writeItemsInTheFloor();
                    break;

                case "/ESTUPIDO":
                    if(notNullArguments) {
                        writeMakeDumb(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /estupido NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/NOESTUPIDO":
                    if(notNullArguments) {
                        writeMakeDumbNoMore(argumentosRaw);
                    } else {
                        // Avisar de que falta parametro
                        Console.get().addMsgToConsole(new String("Faltan parámetros. Utilice /noestupido NICKNAME.".getBytes(), StandardCharsets.UTF_8),
                                false, true, new RGBColor());
                    }
                    break;

                case "/DUMPSECURITY":
                    writeDumpIPTables();
                    break;

                case "/KICKCONSE":
                    break;

                case "/TRIGGER":
                    break;

                case "/BANIPLIST":
                    break;

                case "/BANIPRELOAD":
                    break;

                case "/MIEMBROSCLAN":
                    break;

                case "/BANCLAN":
                    break;

                case "/BANIP":
                    break;

                case "/UNBANIP":
                    break;

                case "/CI":
                    if (notNullArguments) {
                        if (validNumber(argumentosAll[0], eNumber_Types.ent_Long)) {
                            writeCreateItem(Integer.parseInt(argumentosAll[0]));
                        } else {
                            Console.get().addMsgToConsole("Objeto incorrecto. Utilice /ci OBJETO.", false, true, new RGBColor());
                        }
                    } else {
                        Console.get().addMsgToConsole("Faltan parámetros. Utilice /ci OBJETO.", false, true, new RGBColor());
                    }
                    break;

                case "/DEST":
                    break;

                case "/NOCAOS":
                    break;

                case "/NOREAL":
                    break;

                case "/FORCEMIDI":
                    break;

                case "/FORCEWAV":
                    break;

                case "/BORRARPENA":
                    break;

                case "/BLOQ":
                    break;

                case "/MATA":
                    break;

                case "/MASSKILL":
                    break;

                case "/LASTIP":
                    break;

                case "/`MOTDCAMBIA":
                    break;

                case "/SMSG":
                    break;

                case "/ACC":
                    break;

                case "/RACC":
                    break;

                case "/AI":
                    break;

                case "/AC":
                    break;

                case "/NAVE":
                    break;

                case "/HABILITAR":
                    break;

                case "/APAGAR":
                    break;

                case "/CONDEN":
                    break;

                case "/RAJAR":
                    break;

                case "/RAJARCLAN":
                    break;

                case "/LASTEMAIL":
                    break;

                case "/APASS":
                    break;

                case "/AEMAIL":
                    break;

                case "/ANAME":
                    break;

                case "/SLOT":
                    break;

                case "/CENTINELAACTIVADO":
                    break;

                case "/CREARPRETORIANOS":
                    break;

                case "/ELIMINARPRETORIANOS":
                    break;

                case "/DOBACKUP":
                    break;

                case "/SHOWCMSG":
                    break;

                case "/GUARDARMAPA":
                    break;

                case "/MODMAPINFO":
                    break;

                case "/GRABAR":
                    break;

                case "/BORRAR":
                    break;

                case "/NOCHE":
                    break;

                case "/ECHARTODOSPJS":
                    break;

                case "/RELOADNPCS":
                    break;

                case "/RELOADSINI":
                    break;

                case "/RELOADHECHIZOS":
                    break;

                case "/RELOADOBJ":
                    break;

                case "/REINICIAR":
                    break;

                case "/AUTOUPDATE":
                    break;

                case "/CHATCOLOR":
                    break;

                case "/IGNORADO":
                    break;

                case "/PING":
                    break;

                case "/SETINIVAR":
                    break;

                case "/HOGAR":
                    break;

                case "/SETDIALOG":
                    break;

                case "/IMPERSONAR":
                    break;

                case "/MIMETIZAR":
                    break;

            }

        } else if (comando.startsWith("-")) { //Gritar
            writeYell(rawCommand.substring(1));

        } else { //Hablar
            writeTalk(rawCommand);
        }
    }

    public static boolean validNumber(String numero, eNumber_Types tipo) {
        long minimo;
        long maximo;

        if (!numero.matches("-?\\d+(\\.\\d+)?")) {
            return false;
        }

        switch (tipo) {
            case ent_Byte:
                minimo = 0;
                maximo = 255;
                break;
            case ent_Integer:
                minimo = -32768;
                maximo = 32767;
                break;
            case ent_Long:
                minimo = -2147483648L;
                maximo = 2147483647L;
                break;
            case ent_Trigger:
                minimo = 0;
                maximo = 6;
                break;
            default:
                throw new IllegalArgumentException("Tipo de número no válido.");
        }

        long valor = Long.parseLong(numero);
        return valor >= minimo && valor <= maximo;
    }

    private static boolean validIPv4Str(String ip) {
        String[] tmpArr = ip.split("\\.");

        if (tmpArr.length != 4) {
            return false;
        }

        for (String octet : tmpArr) {
            if (!validNumber(octet, eNumber_Types.ent_Byte)) {
                return false;
            }
        }

        return true;
    }

    private static int[] str2ipv4l(String ip) {
        String[] tmpArr = ip.split("\\.");
        int[] bArr = new int[4];

        if (tmpArr.length != 4) {
            return null;
        }

        try {
            for (int i = 0; i < 4; i++) {
                bArr[i] = Byte.parseByte(tmpArr[i]);
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return bArr;
    }

}
