package org.aoclient.network;

import org.aoclient.engine.game.Console;
import org.aoclient.engine.renderer.RGBColor;
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
                    break;

                case "/BMSG":
                    break;

                case "/ROL":
                    break;

                case "/GM":
                    break;

                case "/_BUG":
                    break;

                case "/DESC":
                    break;

                case "/VOTO":
                    break;

                case "/PENAS":
                    break;

                case "/CONTRASEÑA":
                    break;

                case "/APOSTAR":
                    break;

                case "/RETIRARFACCION":
                    break;

                case "/RETIRAR":
                    break;

                case "/DEPOSITAR":
                    break;

                case "/DENUNCIAR":
                    break;

                case "/FUNDARCLAN":
                    break;

                case "/FUNDARCLANGM":
                    break;

                case "/ECHARPARTY":
                    break;

                case "/ACCEPTPARTY":
                    break;

                // COMANDOS DE GM
                case "/GMSG":
                    break;

                case "/SHOWNAME":
                    break;

                case "/ONLINEREAL":
                    break;

                case "/ONLINECAOS":
                    break;

                case "/IRCERCA":
                    break;

                case "/REM":
                    break;

                case "/HORA":
                    break;

                case "/DONDE":
                    break;

                case "/NENE":
                    break;

                case "/TELEPLOC":
                    writeWarpMeToTarget();
                    break;

                case "/TELEP":
                    break;

                case "/SILENCIAR":
                    break;

                case "/SHOW":
                    break;

                case "/DENUNCIAS":
                    break;

                case "/IRA":
                    break;

                case "/INVISIBLE":
                    break;

                case "/PANELGM":
                    break;

                case "/TRABAJANDO":
                    break;

                case "/OCULTANDO":
                    break;

                case "/CARCEL":
                    break;

                case "/RMATA":
                    break;

                case "/ADVERTENCIA":
                    break;

                case "/MOD":
                    break;

                case "/INFO":
                    break;

                case "/STAT":
                    break;

                case "/BAL":
                    break;

                case "/INV":
                    break;

                case "/BOV":
                    break;

                case "/SKILLS":
                    break;

                case "/REVIVIR":
                    break;

                case "/ONLINEGM":
                    break;

                case "/ONLINEMAP":
                    break;

                case "/PERDON":
                    break;

                case "/ECHAR":
                    break;

                case "/EJECUTAR":
                    break;

                case "/BAN":
                    break;

                case "/UNBAN":
                    break;

                case "/SEGUIR":
                    break;

                case "/SUM":
                    break;

                case "/CC":
                    break;

                case "/RESETINV":
                    break;

                case "/LIMPIAR":
                    break;

                case "/RMSG":
                    break;

                case "/MAPMSG":
                    break;

                case "/NICK2IP":
                    break;

                case "/IP2NICK":
                    break;

                case "/ONCLAN":
                    break;

                case "/DT":
                    break;

                case "/LLUVIA":
                    break;

                case "/SETDESC":
                    break;

                case "/FORCEMIDIMAP":
                    break;

                case "/FORCEWAVMAP":
                    break;

                case "/REALMSG":
                    break;

                case "/CAOSMSG":
                    break;

                case "/CIUMSG":
                    break;

                case "/CRIMSG":
                    break;

                case "/TALKAS":
                    break;

                case "/MASDEST":
                    break;

                case "/ACEPTCONSE":
                    break;

                case "/ACEPTCONSECAOS":
                    break;

                case "/PISO":
                    break;

                case "/NOESTUPIDO":
                    break;

                case "/DUMPSECURITY":
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

}