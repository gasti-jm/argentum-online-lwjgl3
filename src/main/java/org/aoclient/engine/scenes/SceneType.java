package org.aoclient.engine.scenes;

/**
 * <p>
 * Proporciona una manera tipificada y segura de identificar las distintas escenas que conforman el flujo del juego. Este enum es
 * utilizado por el sistema de escenas para gestionar las transiciones entre diferentes estados del juego.
 * <p>
 * Cada escena concreta puede tener una referencia a un SceneType en su propiedad {@link Scene#canChangeTo canChangeTo}, indicando
 * a que escena puede transicionar. El motor utiliza esta informacion para realizar los cambios de escena cuando sea necesario.
 * <p>
 * Los tipos definidos son:
 * <ul>
 * <li>{@code INTRO_SCENE}: Representa la escena de introduccion con logos y presentacion inicial del juego.
 * <li>{@code MAIN_SCENE}: Representa el menu principal donde se muestran las opciones de conexion, creacion de personaje, etc.
 * <li>{@code GAME_SCENE}: Representa la escena principal de juego donde el usuario controla su personaje e interactua con el mundo.
 * </ul>
 *
 * @see org.aoclient.engine.scenes.Scene
 */

public enum SceneType {

    INTRO_SCENE,
    MAIN_SCENE,
    GAME_SCENE

}
