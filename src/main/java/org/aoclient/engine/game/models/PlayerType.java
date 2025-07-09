package org.aoclient.engine.game.models;

/**
 * Enumeracion que define los tipos de privilegios de jugador, correspondiente exactamente a los {@code PlayerType} del servidor
 * VB6 definidos en {@code Declare.bas}.
 * <p>
 * Valores extraidos del servidor VB6:
 * <pre>{@code
 * Public Enum PlayerType
 *     User = &H1           ' 1 decimal
 *     Consejero = &H2      ' 2 decimal
 *     SemiDios = &H4       ' 4 decimal
 *     Dios = &H8           ' 8 decimal
 *     Admin = &H10         ' 16 decimal
 *     RoleMaster = &H20    ' 32 decimal
 *     ChaosCouncil = &H40  ' 64 decimal
 *     RoyalCouncil = &H80  ' 128 decimal
 * End Enum
 * }</pre>
 */

public enum PlayerType {
    USER(1),           // PlayerType.User
    CONSEJERO(2),      // PlayerType.Consejero  
    SEMIDIOS(4),       // PlayerType.SemiDios
    DIOS(8),           // PlayerType.Dios o GM
    ADMIN(16),         // PlayerType.Admin
    ROLEMASTER(32),    // PlayerType.RoleMaster
    CHAOSCOUNCIL(64),  // PlayerType.ChaosCouncil
    ROYALCOUNCIL(128); // PlayerType.RoyalCouncil

    private final int value;

    PlayerType(int value) {
        this.value = value;
    }

    /**
     * Verifica si el valor de privilegio corresponde a algun tipo de GM.
     */
    public static boolean isGM(int privilege) {
        return hasPrivilege(privilege, ADMIN) || hasPrivilege(privilege, DIOS) || hasPrivilege(privilege, SEMIDIOS) || hasPrivilege(privilege, CONSEJERO);
    }

    /**
     * Obtiene el tipo de privilegio mas alto que tiene el usuario.
     */
    public static PlayerType getHighestPrivilege(int privilege) {
        if (hasPrivilege(privilege, ADMIN)) return ADMIN;
        if (hasPrivilege(privilege, DIOS)) return DIOS;
        if (hasPrivilege(privilege, SEMIDIOS)) return SEMIDIOS;
        if (hasPrivilege(privilege, CONSEJERO)) return CONSEJERO;
        if (hasPrivilege(privilege, ROLEMASTER)) return ROLEMASTER;
        if (hasPrivilege(privilege, CHAOSCOUNCIL)) return CHAOSCOUNCIL;
        if (hasPrivilege(privilege, ROYALCOUNCIL)) return ROYALCOUNCIL;
        return USER;
    }

    /**
     * Verifica si el valor de privilegios contiene el tipo especificado.
     */
    public static boolean hasPrivilege(int privilege, PlayerType type) {
        return (privilege & type.getValue()) != 0;
    }

    public int getValue() {
        return value;
    }

}