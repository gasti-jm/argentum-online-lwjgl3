package org.aoclient.engine.game.models;

/**
 * Representa las habilidades que un personaje puede desarrollar, como combate, magia y otras destrezas.
 */

public enum E_Skills {
    MAGIA(1, """
        - Representa la habilidad de un personaje de las áreas mágicas.
        - Indica la variedad de hechizos que es capaz de dominar el personaje.
        """),
    ROBAR(2, """
        - Habilidades de hurto. Nunca por medio de la violencia.
        - Indica la probabilidad de éxito del personaje al intentar apoderarse de oro de otro. En caso de ser Ladrón, también podrá apoderarse de ítems.
        """),
    TACTICAS(3, """
        - Representa la habilidad general para moverse en combate entre golpes enemigos sin morir o tropezar en el intento.
        - Indica la posibilidad de evadir un golpe físico del personaje.
        """),
    ARMA(4, """
        - Representa la habilidad del personaje para manejar armas de combate cuerpo a cuerpo.
        - Indica la probabilidad de impactar al oponente con armas cuerpo a cuerpo.
        """),
    MEDITA(5, """
        - Representa la capacidad del personaje de concentrarse para abstraerse dentro de su mente y así revitalizar su fuerza espiritual.
        - Indica la velocidad a la que el personaje recupera maná (Clases mágicas).
        """),
    STAB(6, """
        - Representa la destreza para infligir daño grave con armas cortas.
        - Indica la posibilidad de apuñalar al enemigo en un ataque. El Asesino es la única clase que no necesitará 10 skills para comenzar a entrenar esta habilidad.
        """),
    OCULTARSE(7, """
        - La habilidad propia de un personaje para mimetizarse con el medio y evitar que se perciba su presencia.
        - Indica la facilidad con la que uno puede desaparecer de la vista de los demás y por cuánto tiempo.
        """),
    SUPERVIVENCIA(8, """
        - Es el conjunto de habilidades necesarias para sobrevivir fuera de una ciudad en base a lo que la naturaleza ofrece.
        - Permite conocer la salud de las criaturas guiándose exclusivamente por su aspecto, así como encender fogatas junto a las que descansar.
        """),
    TALAR(9, """
        - Es la habilidad en el uso del hacha para evitar desperdiciar leña y maximizar la efectividad de cada golpe dado.
        - Indica la probabilidad de obtener leña por golpe.
        """),
    COMERCIAR(10, """
        - Es la habilidad para regatear los precios exigidos en la compra y evitar ser regateado al vender.
        - Indica qué tan caro se compra en el comercio con NPCs.
        """),
    DEFENSA(11, """
        - Es la habilidad de interponer correctamente el escudo ante cada embate enemigo para evitar ser impactado sin perder el equilibrio y poder responder rápidamente con la otra mano.
        - Indica las probabilidades de bloquear un impacto con el escudo.
        """),
    PESCA(12, """
        - Es el conjunto de conocimientos básicos para poder armar un señuelo, poner la carnada en el anzuelo y saber dónde buscar peces.
        - Indica la probabilidad de tener éxito en cada intento de pescar.
        """),
    MINERIA(13, """
        - Es el conjunto de conocimientos sobre los distintos minerales, dónde se obtienen, cómo deben ser extraídos y trabajados.
        - Indica la probabilidad de tener éxito en cada intento de minar y la capacidad o no de convertir estos minerales en lingotes.
        """),
    CARPINTERIA(14, """
        - Es el conjunto de conocimientos para saber serruchar, lijar, encolar y clavar madera con un buen nivel de terminación.
        - Indica la habilidad en el manejo de estas herramientas, qué tan bueno se es en el oficio de carpintero.
        """),
    HERRERIA(15, """
        - Es el conjunto de conocimientos para saber procesar cada tipo de mineral para fundirlo, forjarlo y crear aleaciones.
        - Indica la habilidad en el manejo de estas técnicas, qué tan bueno se es en el oficio de herrero.
        """),
    LIDERAZGO(16, """
        - Es la habilidad propia del personaje para convencer a otros a seguirlo en batalla.
        - Permite crear clanes y partys.
        """),
    DOMAR(17, """
        - Es la habilidad en el trato con animales para que estos te sigan y ayuden en combate.
        - Indica la posibilidad de lograr domar a una criatura y qué clases de criaturas se puede domar.
        """),
    PROYECTILES(18, """
        - Es el manejo de las armas de largo alcance.
        - Indica la probabilidad de éxito para impactar a un enemigo con este tipo de armas.
        """),
    WRESTLING(19, """
        - Es la habilidad del personaje para entrar en combate sin arma alguna salvo sus propios brazos.
        - Indica la probabilidad de éxito para impactar a un enemigo estando desarmado. El Bandido y Ladrón tienen habilidades extras asociadas a esta habilidad.
        """),
    NAVEGACION(20, """
        - Es la habilidad para controlar barcos en el mar sin naufragar.
        - Indica qué clase de barcos se pueden utilizar.
        """);

    public static final int FundirMetal = 88;
    private final byte value;
    private final String description;

    E_Skills(int value, String description) {
        this.value = (byte) value;
        this.description = description;
    }

    public byte getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
