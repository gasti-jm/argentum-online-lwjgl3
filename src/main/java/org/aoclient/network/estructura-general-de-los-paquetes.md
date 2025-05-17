## Estructura general de los paquetes

La comunicacion entre el cliente y el servidor sigue un patron especifico:

1. **El cliente envia paquetes minimos con intenciones:**
    - Cada paquete inicia con 1 byte que identifica el tipo de paquete (ID)
    - Solo incluye la informacion esencial necesaria para expresar la intencion del usuario
    - No incluye informacion de estado que es responsabilidad del servidor

2. **El servidor enriquece la informacion y responde:**
    - Procesa la intencion del cliente aplicando reglas y validaciones
    - Añade informacion contextual (quien realiza la accion, resultados, etc.)
    - Puede enviar multiples paquetes en respuesta a una unica accion del cliente
    - Determina quien debe recibir la informacion (el usuario, usuarios cercanos, etc.)

## Ejemplos de transformacion de paquetes

### TALK → CHAT_OVER_HEAD

**Cliente envia (TALK):**

- 1 byte: ID del paquete (3)
- N bytes: String del mensaje (longitud variable)

**Servidor responde (CHAT_OVER_HEAD):**

- 1 byte: ID del paquete (23)
- N bytes: String del mensaje (igual que el enviado)
- 2 bytes: CharIndex del personaje que habla
- 1 byte: Componente rojo del color (R)
- 1 byte: Componente verde del color (G)
- 1 byte: Componente azul del color (B)

## Beneficios de este diseño

- **Seguridad**: El cliente no puede tomar decisiones criticas que afecten al juego
- **Consistencia**: El servidor mantiene el estado unico y autoritativo del juego
- **Eficiencia**: Los paquetes del cliente son minimos, reduciendo el trafico de red
- **Autoridad centralizada**: Todas las reglas se aplican en un solo lugar
- **Anti-cheating**: Los jugadores no pueden modificar datos criticos como daño o posicion

## Notas importantes para desarrolladores

- Los IDs de paquetes en `ClientPacket.java` y `ServerPacket.java` **deben coincidir exactamente** con los valores en el
  enum `ClientPacketID` y `ServerPacketID` del codigo VB6 del servidor.
- Modificar estos IDs rompera la compatibilidad con el servidor.
- Al recibir un paquete, el servidor valida que el cliente tenga permiso para realizar la accion solicitada.

## Conclusion

No todos los paquetes del cliente son minimos, pero incluso los que contienen informacion "completa" siguen un principio
fundamental: el cliente propone, el servidor dispone. El cliente nunca tiene autoridad final sobre el estado del juego.
Esta arquitectura es caracteristica de los juegos online multijugador y proporciona un equilibrio entre rendimiento 
(minimizando datos transmitidos) y seguridad (manteniendo la autoridad en el servidor).
