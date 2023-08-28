package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.TextBox;

import java.util.ArrayList;

import static org.aoclient.connection.Protocol.writeThrowDices;
import static org.aoclient.engine.Sound.SND_DICE;
import static org.aoclient.engine.Sound.playSound;

/**
 * Este seria la clase que define nuestro formulario "frmCrearPersonaje".
 * Contiene elementos de GUI.
 *
 * EN PROGRESO....
 */
public class CreateCharacter extends Form {
    private static CreateCharacter instance;

    /**
     * @desc: Constructor privado por singleton.
     */
    private CreateCharacter() {

    }

    /**
     *
     * @return Mismo objeto (Patron de diseño Singleton)
     */
    public static CreateCharacter get() {
        if (instance == null) {
            instance = new CreateCharacter();
        }

        return instance;
    }

    /**
     * Como tenemos un singleton, necesitamos de una funcion init para incializar nuestros atributos de la clase.
     */
    public void init(){
        this.background.init("VentanaCrearPersonaje.jpg");

        this.buttonList = new ArrayList<>();
        this.loadTxtBoxes();

        buttonList.add(new Button(13, 185, 60, 59, ()-> {
            SocketConnection.getInstance().connect();
            writeThrowDices();
            playSound(SND_DICE);
        }));

        buttonList.add(new Button(89, 546, 86, 30, ()-> {
            this.visible = false;
        }, "", "BotonVolverRollover.jpg", "BotonVolverClick.jpg"));

    }

    /**
     * @desc Inicializa el tabIndex del formulario y nuestra lista de TextBoxes, agregando tambien los definidos.
     *       No es necesario crear esta funcion.. Es mas que nada para dividir un poco la funcion "init", y podemos hacer
     *       una funcion similar si tenemos muchos botones o algun otro componente.
     */
    private void loadTxtBoxes() {
        this.txtList = new ArrayList<>();
        this.tabIndexSelected = 0;
        txtTabIndexsAdded = 0;

        // txtUsername
        txtList.add(new TextBox(txtTabIndexsAdded++,232, 88, 337, 15,
                true, false));

        // txtPassword
        txtList.add(new TextBox(txtTabIndexsAdded++, 232, 120, 161, 15,
                true, false, true));
        // txtCofirmPassword
        txtList.add(new TextBox(txtTabIndexsAdded++, 408, 120, 161, 15,
                true, false, true));

        //txtMail
        txtList.add(new TextBox(txtTabIndexsAdded++, 232, 152, 337, 15,
                true, false));





        txtList.get(tabIndexSelected).setSelected(true);
    }


    /**
     * @desc Renderiza el contenido de nuestro formulario.
     */
    @Override
    public void render() {
        if (!visible) return;
        background.render();
        this.renderTextBoxes();
        this.renderButtons();
    }

    /**
     * @desc Cierra y elimina nuestro formulario.
     */
    @Override
    public void close(){
        super.close();
        buttonList.clear();
        txtList.clear();
    }

}
