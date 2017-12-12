# ComAndruino - Comunicacion Andruino

## Introducción 
En este proyecto se trata de establecer una aplicación que pueda enlazar y establecer una comunicación con un módulo bluetooth que es usado en un Arduino y realiza la tarea de recibir y enviar datos de vuelta a la aplicación Android y organizarlo por una Arquitectura MVP para hacer una estructura ordenada al proyecto Android. 
De este proyecto lo aplicamos en una simulación de un controlador para un auto a control remoto en aplicación Android, en esta simulación permite crear un enlace (o emparejamiento) entre el dispositivo bluetooth (en este caso, el módulo bluetooth) y la aplicación. Una vez que se ha creado un emparejamiento entre el teléfono y el dispositivo bluetooth, se puede usar diferentes comandos para enviar datos hacia el microcontrolador Arduino por modulo Bluetooth, sea encender o apagar el auto, mover el carrito de la aplicación y recibir mensajes de vuelta a la aplicación, creando una interacción donde se envía y recibe datos la aplicación y el Arduino, todo posible con el enlace establecido con el dispositivo bluetooth. 

## Justificación
### ¿Para qué hago esto?
Realizo este proyecto para lograr entender cómo se puede crear una comunicación entre un módulo Bluetooth que se ejecuta junto con un programa hecho con Arduino y la aplicación Android. Este trabajo también sirve para hacer un proyecto básico para aquellos que tienen dudas en cómo crear un proyecto que requiere una app de Android y que se pueda comunicar con un dispositivo Bluetooth y ocupan una referencia que puedan entender y crear también por ellos mismos, como si fuera el primer paso de muchos para hacer proyectos con comunicación Bluetooth. 

## Captura de Pantalla del Proyecto Final
### Estas son imagenes de unos ejemplos que se puede aplicar cuando se usa este proyecto realizado
La interfaz que sale al de este proyecto.

![alt text](https://github.com/WeirdEddyLog95/ComAndruino/blob/master/IMG_Proyecto/AppOrdenado.png)

Ejemplo donde se enciende el coche, que es visto en forma de un LED, junto con un mensaje enviado desde el Arduino que dice "Coche Activado".

![Alt text](https://github.com/WeirdEddyLog95/ComAndruino/blob/master/IMG_Proyecto/IMG_0485.JPG?raw=true "Ejemplo 1")

El efecto que se ve desde el lado Arduino cuando encendemos el auto (LED).
![Alt text](https://github.com/WeirdEddyLog95/ComAndruino/blob/master/IMG_Proyecto/IMG_0486.JPG?raw=true "Ejemplo 2")

Ejemplo donde se mueve los botones de direcciones de la aplicacion, al mover el boton de Arriba (Frente), se envia y un mensaje al Arduino, y el Arduino envia un mensaje de vuelta que dice "Auto hacia el Frente".

![Alt text](https://github.com/WeirdEddyLog95/ComAndruino/blob/master/IMG_Proyecto/IMG_0486.JPG?raw=true "Ejemplo 2")

## Referencias.
### Referencias utilizadas para el desarrollo del Proyecto, si quiere saber a mas detalle la conexión a bluetooth puede consultarse a estas paginas
http://www.theappguruz.com/blog/android-bluetooth-connection-demo

https://github.com/bauerjj/Android-Simple-Bluetooth-Example
