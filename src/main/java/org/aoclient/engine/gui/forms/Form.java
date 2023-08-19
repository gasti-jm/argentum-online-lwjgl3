package org.aoclient.engine.gui.forms;

import org.aoclient.engine.Window;
import org.aoclient.engine.listeners.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public abstract class Form {
    protected int frmPosX, frmPosY;
    protected int frmWidth, frmHeight;
    protected boolean visible;

    public Form() {
        this.frmPosX = (Window.get().getWidth() / 2);
        this.frmPosY = (Window.get().getHeight() / 2);
        this.frmWidth = 0;
        this.frmHeight = 0;
        this.visible = true;
    }

    public abstract void render();
    public abstract void checkButtons();

    public void close() {
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getFrmPosX() {
        return frmPosX;
    }

    public void setFrmPosX(int frmPosX) {
        this.frmPosX = frmPosX;
    }

    public int getFrmPosY() {
        return frmPosY;
    }

    public void setFrmPosY(int frmPosY) {
        this.frmPosY = frmPosY;
    }

    public boolean isInside(){
        return MouseListener.getX() >= this.frmPosX && MouseListener.getX() <= this.frmPosX + this.frmWidth &&
                MouseListener.getY() >= this.frmPosY && MouseListener.getY() <= this.frmPosY + this.frmHeight;
    }
}
