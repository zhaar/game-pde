float xAxis = 1;
boolean mouseDragged = false;
boolean shiftMode = false;
float rz;
float rx;
float wheelDirection = 0;
int wheelVelocity = 1;
float normalForce = 1;
float mu = 0.02;
float frictionMagnitude = normalForce * mu;
final static int SPHERE_RADIUS = 20;
final static int BOX_DIMENSIONS = 400;
final static float gravityConstant = 0.1;
Ball ball;
ArrayList<Cylinder> cylinders = new ArrayList<Cylinder>();
Surface surface;

void setup() {
  size(700, 700, P3D);
  noStroke();
  ball = new Ball(SPHERE_RADIUS);
  surface = new Surface();
}

void draw() {
  camera(width/2.0, height / 2.0, height/2.0 / tan(radians(30)), width/2.0, height/2.0, 0, 0, -1, 0);
  directionalLight(50, 100, 125, 0.5, -0.5, 0);
  ambientLight(102, 102, 102);
  background(200);
  surface.draw();
  
  if (!shiftMode) {
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
    //rotateY(xAxis);
    ball.update();
  } else {
    directionalLight(25, 50, 65, 0, 0, -1);
    translate(width/2, height/2);
    rotateX(PI/2);
  }

  box(BOX_DIMENSIONS, 5, BOX_DIMENSIONS);

  for (Cylinder cylinder : cylinders) {
    cylinder.draw();
  }
  
  ball.draw();
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == LEFT) {
      xAxis += 50;
    } else if (keyCode == RIGHT) {
      xAxis -= 50;
    } else if (keyCode == SHIFT) {
      shiftMode = true;
    }
  }
}

void keyReleased() {
  if (key == CODED) {
    if (keyCode == SHIFT) {
      shiftMode = false;
    }
  }
}

void mousePressed() {
  if (!shiftMode) {
    mouseDragged = true;
  } else {
    int X = mouseX - width/2;
    int Y = mouseY - height/2;
    if (X <= BOX_DIMENSIONS/2 && X >= -BOX_DIMENSIONS/2 && Y <= BOX_DIMENSIONS/2 && Y >= -BOX_DIMENSIONS/2)
      cylinders.add(new StupidTree(-X, Y)); //change type of object added here
  }
}

void mouseReleased() {
  if (!shiftMode) {
    mouseDragged = false;
  }
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

