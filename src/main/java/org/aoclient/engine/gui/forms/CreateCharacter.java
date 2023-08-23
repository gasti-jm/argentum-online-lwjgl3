package org.aoclient.engine.gui.forms;

import org.aoclient.connection.SocketConnection;

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

        buttonList.add(addButton(13, 185, 60, 59, ()-> {
            SocketConnection.getInstance().connect();
            writeThrowDices();
            playSound(SND_DICE);
        }));

        buttonList.add(addButton(89, 546, 86, 30, ()-> {
            this.visible = false;
        }, "", "BotonVolverRollover.jpg", "BotonVolverClick.jpg"));

    }

    private void loadTxtBoxes() {
        this.txtList = new ArrayList<>();
        this.tabIndexSelected = 0;
        txtTabIndexsAdded = 0;



        //txtList.get(tabIndexSelected).setSelected(true);
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
