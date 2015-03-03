void setup(){
  size(500,500,P3D);
  noStroke();
}

float yAxis = 1;



void draw(){
  camera(width/2, height/2, 200, 250, 250, 0, 0, 1, 0);
  directionalLight(50, 100, 125, 0, -1, 0);
  ambientLight(102, 102, 102);
  background(200);
  translate(width/2, height/2);
  float rz = map(mouseY, 0, height, 0, PI);
  float rx = map(mouseX, 0, width, 0, PI);
  rotateZ(rz % 60);
  rotateX(rx % 60);
  rotateY(yAxis);
  box(5,100,50);
}

void keyPressed() { if (key == CODED) {
if (keyCode == LEFT) { yAxis -= 50;
}
else if (keyCode == RIGHT) {
yAxis += 50; }
} }
