int b_izquierda = 2;
int b_derecha = 7;
int b_arriba = 10;
int b_abajo = 9;

unsigned long lastSendTime = 0;
const unsigned long sendInterval = 1000; // Intervalo mínimo entre mensajes en ms

void setup() {
  pinMode(b_izquierda, INPUT);
  pinMode(b_derecha, INPUT);
  pinMode(b_arriba, INPUT);
  pinMode(b_abajo, INPUT);
  Serial.begin(9600);
}

void loop() {
  if (digitalRead(b_izquierda) == HIGH) {
    sendSerialMessage("I");
  }
  if (digitalRead(b_arriba) == HIGH) {
    sendSerialMessage("A");
  }
  if (digitalRead(b_abajo) == HIGH) {
    sendSerialMessage("b");
  }
  if (digitalRead(b_derecha) == HIGH) {
    sendSerialMessage("D");
  }
}

void sendSerialMessage(String message) {
  unsigned long currentTime = millis();
  if (currentTime - lastSendTime >= sendInterval) {
    Serial.println(message);
    lastSendTime = currentTime;
  }
}
