package org.aoclient.engine.gui;

import imgui.ImGui;
import org.aoclient.engine.gui.forms.Form;

import java.util.ArrayList;
import java.util.List;

import static org.aoclient.engine.utils.LaunchOptions.CLIENT_DEBUG;

public class FormManager {
    // arreglo de ventanas gui
    private static final List<Form> frms = new ArrayList<>();

    public static void show(Form frm) {
        boolean exits = false;
        // esta abierto?
        for (int i = 0; i < frms.size(); i++) {
            if (frms.get(i).getClass().getSimpleName().equals(frm.getClass().getSimpleName())) {
                frms.set(i, frm); // bueno remplazamos su contenido (esto si es que tenemos un frmMessage).
                exits = true;
                break;
            }
        }
        if (!exits) addFrm(frm);
    }

    public static boolean isFormVisible(String fromClass) {
        boolean visible = false;
        for (Form frm : frms) {
            if (frm.getClass().getSimpleName().equals(fromClass)) {
                visible = true;
                break;
            }
        }
        return visible;
    }

    /**
     * Checkea si el frm solicitiado es el ultimo de nuestro array de frms.
     */
    public static boolean isMainLast() {
        if (frms.size() <= 0) return false;
        return frms.get(frms.size() - 1).getClass().getSimpleName().equals("FMain");
    }

    public static void deleteFrmArray(Form frm) {
        frms.remove(frm);
    }

    public static void addFrm(Form e) {
        frms.add(e);
    }

    public static void closeAllFrms() {
        frms.clear();
    }

    /**
     * Devuelve una copia de la lista de formularios activos.
     */
    public static List<Form> getActiveForms() {
        return new ArrayList<>(frms);
    }

    public static void renderFrms() {
        for (int i = 0; i < frms.size(); i++)
            frms.get(i).render();

        if (CLIENT_DEBUG)  ImGui.showDemoWindow();
    }

}
