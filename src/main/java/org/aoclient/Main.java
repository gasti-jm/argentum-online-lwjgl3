package org.aoclient;

import org.aoclient.engine.Engine;

import java.io.File;

/**
 * @desc: Aqui es donde inicia nuestro programa. Crea un objeto de la clase Engine
 *        para que se inicie nuestra ventana GLFW, OpenGL y OpenAL.
 */
public class Main {
    public static void main(String[] args) {
        new Engine().start();
    }
}