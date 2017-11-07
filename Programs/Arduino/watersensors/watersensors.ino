

// Sample Code
int sensor = 0; // Analog in
int val =0; // Current reading for analog pin
int avg; // Running average of the wave amplitude
int MIDPOINT = 520; // Base reading

void setup() {
  Serial.begin(115200);
  avg = MIDPOINT; // set average at midpoint
}

void loop() {
  val = analogRead(sensor);
  Serial.println(val);
  delay(100);

  // Compute wave amplittue
  if (val > MIDPOINT) {
    val = val - MIDPOINT;
  } else {
    val = MIDPOINT - val; 
  }

  // compute running average fr the amplitute
  avg = (avg * 0.5) + (val * 0.5); 

  if (avg > 50) {
    // vibration detected!
    Serial.println("TAP");
    Serial.println(avg);
    delay(100); // delay to ensure Serial port is not overloaded
  }
}

