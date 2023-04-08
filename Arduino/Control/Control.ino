int b_izquierda = 2;
int b_derecha = 7;
int b_arriba = 10;
int b_abajo = 9;
int b_select = 3;

unsigned long lastSendTime = 0;
const unsigned long sendInterval = 100; // Intervalo mínimo entre mensajes en ms

void setup() {
  pinMode(b_izquierda, INPUT);
  pinMode(b_derecha, INPUT);
  pinMode(b_arriba, INPUT);
  pinMode(b_abajo, INPUT);
  pinMode(b_select,INPUT);
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
}

void sendSerialMessage(String message) {
  unsigned long currentTime = millis();
  if (currentTime - lastSendTime >= sendInterval) {
    Serial.println(message);
    lastSendTime = currentTime;
  }
}
