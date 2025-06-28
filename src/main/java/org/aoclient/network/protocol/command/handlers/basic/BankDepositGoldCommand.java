package org.aoclient.network.protocol.command.handlers.basic;

import org.aoclient.network.protocol.command.BaseCommandHandler;
import org.aoclient.network.protocol.command.Command;
import org.aoclient.network.protocol.command.CommandContext;
import org.aoclient.network.protocol.command.CommandException;

import static org.aoclient.network.protocol.Protocol.writeBankDepositGold;

/**
 * Comando para depositar oro en el banco.
 * <p>
 * <b>Uso del sistema de auto-registro:</b><br>
 * Esta clase utiliza la anotacion {@code @Command("/depositar")} para ser automaticamente registrada en el sistema de comandos
 * mediante reflexion. El proceso funciona asi:
 * <ol>
 *  <li>El metodo {@code CommandProcessor.autoRegisterAnnotatedCommands()} escanea el paquete
 *      {@code org.aoclient.network.protocol.command.handlers}
 *  <li>Encuentra esta clase porque tiene la anotacion {@code @Command}
 *  <li>Crea una instancia usando {@code clazz.getDeclaredConstructor().newInstance()}
 *  <li>La registra en el mapa de comandos con la clave "/depositar"
 * </ol>
 * <b>Nota sobre {@code @SuppressWarnings("unused")}:</b>
 * <br>
 * Esta anotacion es necesaria porque el IDE y el compilador no pueden detectar que esta clase se usa indirectamente mediante
 * reflexion. Sin esta anotacion, apareceria una advertencia de "clase no utilizada" cuando en realidad SI se utiliza
 * automaticamente por el sistema de comandos.
 */

@Command("/depositar")
@SuppressWarnings("unused")
public class BankDepositGoldCommand extends BaseCommandHandler {

    @Override
    public void handle(CommandContext context) throws CommandException {
        if (user.isDead()) {
            showError("You are dead!");
            return;
        }
        requireArguments(context, 1, "/depositar <quantity>");
        requireInteger(context, 0, "quantity");

        int quantity = Integer.parseInt(context.getArgument(0));

        if (quantity > user.getUserGLD()) {
            showError("You don't have enough gold!");
            return;
        }

        writeBankDepositGold(quantity);
    }

}
