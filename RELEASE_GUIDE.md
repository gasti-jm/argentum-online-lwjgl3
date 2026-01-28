# Guía de Generación de Releases

Este proyecto está configurado para generar automáticamente los ejecutables (EXE) y archivos JAR mediante **GitHub Actions**.

## Pasos para crear una nueva versión

1.  **Asegúrate de que todo esté listo**:
    Verifica que tu código esté en el estado deseado en la rama `master` o `main`.

2.  **Crea una etiqueta (Tag)**:
    Debes crear un tag que empiece con `v` (ejemplo: `v1.0.0`, `v1.1.0-beta`).
    
    Abre tu terminal en la carpeta del proyecto y ejecuta:
    ```bash
    git tag v1.0.0
    ```

3.  **Sube el Tag a GitHub**:
    ```bash
    git push origin v1.0.0
    ```

## ¿Qué sucede después?

Una vez subido el tag, GitHub iniciará automáticamente el proceso de construcción:
1.  Compilará el código usando Java 21.
2.  Generará el **Shadow JAR** (JAR con dependencias).
3.  Generará el **Ejecutable de Windows (.exe)** con Java integrado (no requiere instalación previa de Java).
4.  Creará una **Release** en la página de GitHub del repositorio adjuntando estos archivos para que los usuarios los descarguen.

Puedes ver el progreso en la pestaña **Actions** de tu repositorio en GitHub.
