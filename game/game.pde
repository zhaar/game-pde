void setup() {
  size(500, 500, P3D);
  noStroke();
}

float xAxis = 1;



void draw() {
  camera(width/2.0, height / 2.0, height/2.0 / tan(radians(30)), width/2.0, height/2.0, 0, 0, -1, 0);
  directionalLight(50, 100, 125, 0.5, -0.5, 0);
  ambientLight(102, 102, 102);
  background(200);
  translate(width/2, height/2);
  float rz = map(mouseY, 0, height, -60, 60.0);
  float rx = map(mouseX, 0, width, -60, 60.0);
  rotateZ(radians(rz));
  rotateX(radians(rx));
  rotateY(xAxis);
  box(400, 5, 400);
}

void keyPressed() { 
  if (key == CODED) {
    if (keyCode == LEFT) { 
      xAxis += 50;
    } else if (keyCode == RIGHT) {
      xAxis -= 50;
    }
  }
}

