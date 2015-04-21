package Game;

import processing.core.*;
import processing.event.MouseEvent;

import java.util.ArrayList;

public class Game extends PApplet {
  float xAxis = 1;
  boolean mouseDragged = false;
  boolean shiftMode = false;
  float rz;
  float rx;
  float wheelDirection = 0;
  int wheelVelocity = 1;
  float normalForce = 1;
  float mu = 0.02f;
  float frictionMagnitude = normalForce * mu;
  final static int SPHERE_RADIUS = 20;
  final static int BOX_DIMENSIONS = 400;
  final static float gravityConstant = 0.1f;
  Ball ball;
  public ArrayList<Cylinder> cylinders = new ArrayList<Cylinder>();
  Surface bottomSurf;
  Minimap topView;

  public void setup() {
    size(700, 700, P3D);
    noStroke();
    ball = new Ball(this, SPHERE_RADIUS);
    bottomSurf = new Surface(this, width, 150);
    topView = new Minimap(this, 140, 140);
  }

  public void draw() {
    background(200);
    camera(width / 2.0f, height / 2.0f, height / 2.0f / tan(radians(30)), width / 2.0f, height / 2.0f, 0, 0, -1, 0);
    bottomSurf.draw(0, 0, 165);
    topView.draw(width - 145, 4, 100);
    for (Cylinder cylinder : cylinders) {
      topView.draw(cylinder.X, cylinder.Z);
    }
    directionalLight(50, 100, 125, 0.5f, -0.5f, 0);
    ambientLight(102, 102, 102);

    if (!shiftMode) {
      translate(width / 2, height / 2);
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
        rx += (wheelVelocity / 2.0) * (mouseY - pmouseY);
        rz += (wheelVelocity / 2.0) * (mouseX - pmouseX);
      }
      limitAngle();
      rotateZ(radians(rz));
      rotateX(radians(rx));
      //rotateY(xAxis);
      ball.update(frictionMagnitude, rx, rz);
    } else {
      directionalLight(25, 50, 65, 0, 0, -1);
      translate(width / 2, height / 2);
      rotateX(PI / 2);
    }

    box(BOX_DIMENSIONS, 5, BOX_DIMENSIONS);

    for (Cylinder cylinder : cylinders) {
      cylinder.draw();
    }

    ball.draw();
  }

  public void keyPressed() {
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

  public void keyReleased() {
    if (key == CODED) {
      if (keyCode == SHIFT) {
        shiftMode = false;
      }
    }
  }

  public void mousePressed() {
    if (!shiftMode) {
      mouseDragged = true;
    } else {
      int X = mouseX - width / 2;
      int Y = mouseY - height / 2;
      if (X <= BOX_DIMENSIONS / 2 && X >= -BOX_DIMENSIONS / 2 && Y <= BOX_DIMENSIONS / 2 && Y >= -BOX_DIMENSIONS / 2)
        cylinders.add(new StupidTree(this, -X, Y)); //change type of object added here
    }
  }

  public void mouseReleased() {
    if (!shiftMode) {
      mouseDragged = false;
    }
  }

  public void mouseWheel(MouseEvent event) {
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

}