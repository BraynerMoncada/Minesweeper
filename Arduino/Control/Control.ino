int b_izquierda = 2;
int b_derecha = 7;
int b_arriba = 10;
int b_abajo = 9;

void setup() {
  pinMode(b_izquierda, INPUT);
  pinMode(b_derecha,INPUT);
  pinMode(b_arriba,INPUT);
  pinMode(b_abajo,INPUT);
  Serial.begin(9600);
}

void loop() {
  if (digitalRead(b_izquierda) == HIGH) {
    Serial.println("Izquierda");
  }
  if (digitalRead(b_arriba) == HIGH) {
    Serial.println("Arriba");
  }
  if (digitalRead(b_abajo) == HIGH) {
    Serial.println("Abajo");
    }
    if (digitalRead(b_derecha) == HIGH) {
    Serial.println("Derecha");
    }
}
