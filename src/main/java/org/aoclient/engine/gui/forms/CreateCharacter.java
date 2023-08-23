package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;
import org.aoclient.engine.gui.elements.Button;
import org.aoclient.engine.gui.elements.TextBox;

import java.util.ArrayList;

import static org.aoclient.connection.Protocol.writeThrowDices;
import static org.aoclient.engine.Sound.SND_DICE;
import static org.aoclient.engine.Sound.playSound;

public class CreateCharacter extends Form {
    private static CreateCharacter instance;

    private CreateCharacter() {

    }

    public static CreateCharacter get() {
        if (instance == null) {
            instance = new CreateCharacter();
        }

        return instance;
    }

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


    @Override
    public void render() {
        if (!visible) return;
        background.render();
        this.renderTextBoxes();
        this.renderButtons();
    }

    @Override
    public void close(){
        super.close();
        buttonList.clear();
        txtList.clear();
    }

}
