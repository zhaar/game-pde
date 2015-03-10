float xAxis = 1;
boolean mouseDragged = false;
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

void setup() {
  size(800, 800, P3D);
  noStroke();
  ball = new Ball(SPHERE_RADIUS);
}
Sphere s = new Sphere(20, 5);

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
  //rotateY(xAxis);
  box(BOX_DIMENSIONS, 5, BOX_DIMENSIONS);

  ball.update();
  ball.draw();
}

class Sphere{
  PVector position;
  PVector velocity;
  PVector friction;
  final float radius;
  final float y;
  Sphere(float radius, float y){
    position = new PVector(width/2, height/2);
    velocity = new PVector(0,0);
    friction = new PVector(0,0);
    this.radius = radius;
    this.y = y;
  }

  void update(){
    updateSpeed();
    checkEdges();
    updateFriction();
    position.add(velocity);
    position.add(friction);
  }

  void draw(){
    translate(position.x,y + radius, position.y);
    sphere(radius);
    translate(-position.x,-y - radius,-position.y);
  }

  private void updateSpeed(){
//    gravityForce.x = sin(rz) * gravityConstant;
//    gravityForce.z = sin(rx) * gravityConstant;
    println("updating speed to");
    velocity.set(sin(rz) * gravityConstant, sin(rx) * gravityConstant);
  }

  private void updateFriction(){
    float normalForce = 1;
    float mu = 0.01;
    float frictionMagnitude = normalForce * mu;
    friction = velocity.get();
    friction.mult(-1);
    friction.normalize();
    friction.mult(frictionMagnitude);
  }

  private void checkEdges(){
      if (position.x > width){
        velocity.x = -velocity.x;
      }
      else if (position.x < 0) {
        velocity.x = -velocity.x;
      }
      if (position.y > height) {
        velocity.y = -velocity.y;
      }
      else if (position.y < 0) {
        velocity.y = -velocity.y;
      }
  }
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
