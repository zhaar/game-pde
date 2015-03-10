void setup() {
  size(500, 500, P3D);
  noStroke();
}

float xAxis = 1;
boolean mouseDragged = false;
float rz;
float rx;
float wheelDirection = 0;
int wheelVelocity = 1;

void draw() {
  camera(width/2.0, height / 2.0, height/2.0 / tan(radians(30)), width/2.0, height/2.0, 0, 0, -1, 0);
  directionalLight(50, 100, 125, 0.5, -0.5, 0);
  ambientLight(102, 102, 102);
  background(200);
  translate(width/2, height/2);
  if (wheelDirection > 0) {
    if (wheelVelocity < 5) {
      wheelVelocity++;
    }
  } else if (wheelDirection < 0) {
    if (wheelVelocity > 1) {
      wheelVelocity--;
    }
  }
  wheelDirection = 0;
  if (mouseDragged) {
    rx += (wheelVelocity/2.0)*(mouseY-pmouseY);
    rz += (wheelVelocity/2.0)*(mouseX-pmouseX);
  }
  limitAngle();
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

void mousePressed() {
  mouseDragged = true;
}

void mouseReleased() {
  mouseDragged = false;
}

void mouseWheel(MouseEvent event) {
  wheelDirection = event.getCount();
}

void limitAngle() {
  if (rz < -60) {
    rz = -60;
  } else if (rz > 60) {
    rz = 60;
  }
  if (rx < -60) {
    rx = -60;
  } else if (rx > 60) {
    rx = 60;
  }
}

