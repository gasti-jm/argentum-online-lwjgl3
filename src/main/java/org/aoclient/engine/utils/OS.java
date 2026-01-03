package org.aoclient.engine.utils;

public class OS {
    public static String operationSystem;
    public static String osVersion;
    public static String javaVersion;
    public static String arch;

    public static void init() {
        operationSystem = System.getProperty("os.name");
        osVersion = System.getProperty("os.version");
        arch = System.getProperty("os.arch");
        javaVersion = System.getProperty("java.version");
    }

}
