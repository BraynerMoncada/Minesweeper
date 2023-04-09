int b_izquierda = 2;
int b_derecha = 7;
int b_arriba = 10;
int b_abajo = 9;
int b_select = 3;
int LED_PIN = 13;

unsigned long lastSendTime = 0;
const unsigned long sendInterval = 100; // Intervalo mínimo entre mensajes en ms

void setup() {
  pinMode(b_izquierda, INPUT);
  pinMode(b_derecha, INPUT);
  pinMode(b_arriba, INPUT);
  pinMode(b_abajo, INPUT);
  pinMode(b_select,INPUT);
  pinMode(LED_PIN,OUTPUT);
  Serial.begin(9600);
}

void loop() {
  if (digitalRead(b_izquierda) == HIGH) {
    sendSerialMessage("I");
  }
  else if (digitalRead(b_arriba) == HIGH) {
    sendSerialMessage("A");
  }
  else if (digitalRead(b_abajo) == HIGH) {
    sendSerialMessage("b");
  }
  else if (digitalRead(b_derecha) == HIGH) {
    sendSerialMessage("D");
  }
  else if(digitalRead(b_select)== HIGH){
    sendSerialMessage("c");
    
  }
  if (Serial.available() > 0) {
    String mensaje = Serial.readStringUntil('\n');
    if (mensaje == "EncenderLED") {
      digitalWrite(LED_PIN, HIGH);
      delay(1000); // Esperar un segundo
      digitalWrite(LED_PIN, LOW);
    } 
  }
}

void sendSerialMessage(String message) {
  unsigned long currentTime = millis();
  if (currentTime - lastSendTime >= sendInterval) {
    Serial.println(message);
    lastSendTime = currentTime;
  }
}
