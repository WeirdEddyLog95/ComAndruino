#include <SoftwareSerial.h> // Se usa el serial de Software
SoftwareSerial bluetooth(2, 4); // Los puertos RX y TX del Modulo Bluetooth
String dato;
boolean activo;
char c_leer;

void setup() {
  bluetooth.begin(9600); // Se inicia el Serial para el Bluetooth, por Default
  pinMode(13, OUTPUT); // para el estado del LED
  //bluetooth.print("HOLAs");  //Sirve enviar un mensaje de vuelta a Android para verificar si hay conexion
}

void loop() {
  if (bluetooth.available()) {
        c_leer = bluetooth.read();
        bluetooth.flush();
        switch(c_leer){
          case '1': 
          digitalWrite(13, HIGH);
          bluetooth.print("LED Encendido");
          activo = true;//indica si el LED esta activado
          break;
          case '2': 
          digitalWrite(13, LOW);
          bluetooth.print("LED Apagado");
          activo = false;//indica si el LED esta desactivado
          break;
          case 'a': 
          if(activo == true){
            bluetooth.print("Auto hacia el Frente");  
          } else {
            bluetooth.print("Coche apagado, no se puede girar a Frente");
          }
          break;
          case 'b': 
          if(activo == true){
            bluetooth.print("Auto hacia la Izquierda");
          } else {
            bluetooth.print("Coche apagado, no se puede girar a la Izquierda");
          }
          break;
          case 'c': 
          if(activo == true){
            bluetooth.print("Auto hacia la Derecha");
          } else {
            bluetooth.print("Coche apagado, no se puede girar a la Derecha");
          }
          break;
          case 'd': 
          if(activo == true){
            bluetooth.print("Auto hacia Atras");
          } else {
            bluetooth.print("Coche apagado, no se puede girar Atras");
          }
          break;
          default:
          break;
        }
    }
}
 
