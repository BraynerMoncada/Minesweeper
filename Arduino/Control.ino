int b_izquierda = 2;
int led= 8;
int b_derecha = 4;
int b_arriba = 10;
int b_abajo = 9;

void setup() {
  pinMode(b_izquierda, INPUT);
  pinMode(led, OUTPUT);
  pinMode(b_derecha,INPUT);
  Serial.begin(9600);
}

void loop() {
  if (digitalRead(b_izquierda) == HIGH) {
    digitalWrite(led, HIGH);
    Serial.println("Izquierda");
  } else {
    digitalWrite(led, LOW);
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
