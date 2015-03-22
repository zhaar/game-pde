void setup () {
  size(700, 700, P2D);
}

  float value = 1;
  int yCoordinate;
  int yDraggedCoord;
  float angleX = 0;
  float angleY = 0;
void draw() {
  background(255, 255, 255);
  My3DPoint eye = new My3DPoint(0, 0, -5000);
  My3DPoint origin = new My3DPoint(0, 0, 0);
  My3DBox input3DBox = new My3DBox(origin, 100, 150, 300);
  //rotated around x
  float[][] transform1 = rotateXMatrix(PI/8);
  if(mousePressed){
    yCoordinate = mouseY;
  }
  input3DBox = transformBox(transformBox(transformBox(transformBox(transformBox(input3DBox, transform1), scaleMatrix(value, value, value)), rotateXMatrix(angleX)), rotateYMatrix(angleY)), translationMatrix(width/2, height/2, 0));
  projectBox(eye, input3DBox).render();
}

void mouseDragged() 
{
  yDraggedCoord = mouseY;
  if(yDraggedCoord < yCoordinate){
    if(value>0.5){
      value = value - 0.2;
    }
  }else{
    if(value<3){
      value = value + 0.2;
    }
  }
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP) {
      angleX = angleX + PI/12;
    } else if (keyCode == DOWN) {
      angleX = angleX - PI/12;
    } else if (keyCode == RIGHT) {
      angleY = angleY + PI/12;
    } else if (keyCode == LEFT) {
      angleY = angleY - PI/12;
    }
  }
}

class My2DPoint {
  float x;
  float y;
  My2DPoint(float x, float y) {
    this.x = x;
    this.y = y;
  }
}

class My3DPoint {
  float x;
  float y;
  float z;
  My3DPoint(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}

My2DPoint projectPoint(My3DPoint eye, My3DPoint p) {
  float[][] T = {
    {
      1, 0, 0, -eye.x
    }
    , {
      0, 1, 0, -eye.y
    }
    , {
      0, 0, 1, -eye.z
    }
    , {
      0, 0, 0, 1
    }
  };
  float[][] P = {
    {
      1, 0, 0, 0
    }
    , {
      0, 1, 0, 0
    }
    , {
      0, 0, 1, 0
    }
    , {
      0, 0, 1/eye.z, 0
    }
  };
  float[][] point = {
    {
      p.x
    }
    , {
      p.y
    }
    , {
      p.z
    }
    , {
      1
    }
  };
  float[][] projection = new float[1][4];

  projection = matrixMultiply(matrixMultiply(P, T), point);
  return new My2DPoint(projection[0][0]/(1-p.z/eye.z), projection[1][0]/(1-p.z/eye.z));
}

float[][] matrixMultiply(float[][] left, float[][] right) {
  if (left[0].length != right.length) {
    print("woops, matrix multiply failed (wrong dimensions)");
    exit();
  }
  float[][] result = new float[left.length][right[0].length];
  for (int i = 0; i < result.length; i++) {
    for (int j = 0; j < result[i].length; j++) {
      float value = 0;
      for (int k = 0; k < right.length; k++) {
        value += left[i][k] * right[k][j];
      }
      result[i][j] = value;
    }
  }
  return result;
}

class My2DBox {
  My2DPoint[] s;
  My2DBox(My2DPoint[] s) {
    this.s = s;
  }
  void render() {
    link(s[0], s[3]);
    link(s[0], s[1]);
    link(s[0], s[4]);
    link(s[3], s[2]);
    link(s[3], s[7]);
    link(s[1], s[5]);
    link(s[1], s[2]);
    link(s[2], s[6]);
    link(s[6], s[7]);
    link(s[6], s[5]);
    link(s[4], s[5]);
    link(s[4], s[7]);
  }
}

void link(My2DPoint a, My2DPoint b) {
  line(a.x, a.y, b.x, b.y);
}

class My3DBox {
  My3DPoint[] p;
  My3DBox(My3DPoint origin, float dimX, float dimY, float dimZ) {
    float x = origin.x;
    float y = origin.y;
    float z = origin.z;
    this.p = new My3DPoint[] {
      new My3DPoint(x, y+dimY, z+dimZ), 
      new My3DPoint(x, y, z+dimZ), 
      new My3DPoint(x+dimX, y, z+dimZ), 
      new My3DPoint(x+dimX, y+dimY, z+dimZ), 
      new My3DPoint(x, y+dimY, z), 
      origin, 
      new My3DPoint(x+dimX, y, z), 
      new My3DPoint(x+dimX, y+dimY, z)
    };
  }
  My3DBox(My3DPoint[] p) {
    this.p = p;
  }
}

My2DBox projectBox (My3DPoint eye, My3DBox box) {
  My2DPoint[] s = new My2DPoint[8];
  for (int i=0; i<8; i++) {
    s[i] = projectPoint(eye, box.p[i]);
    print("\n"+s[i].x +", "+ s[i].y);
  } 
  return new My2DBox(s);
}

float[] homogeneous3DPoint (My3DPoint p) {
  float[] result = {
    p.x, p.y, p.z, 1
  };
  return result;
}

float[][] rotateXMatrix(float angle) {
  return(new float[][] {
    {
      1, 0, 0, 0
    }
    , 
    {
      0, cos(angle), sin(angle), 0
    }
    , 
    {
      0, -sin(angle), cos(angle), 0
    }
    , 
    {
      0, 0, 0, 1
    }
  }
  );
}
float[][] rotateYMatrix(float angle) {
  return(new float[][] {
    {
      cos(angle), 0, -sin(angle), 0
    }
    , 
    {
      0, 1, 0, 0
    }
    , 
    {
      sin(angle), 0, cos(angle), 0
    }
    , 
    {
      0, 0, 0, 1
    }
  }
  );
}
float[][] rotateZMatrix(float angle) {
  return(new float[][] {
    {
      cos(angle), sin(angle), 0, 0
    }
    , 
    {
      -sin(angle), cos(angle), 0, 0
    }
    , 
    {
      0, 0, 1, 0
    }
    , 
    {
      0, 0, 0, 1
    }
  }
  );
}
float[][] scaleMatrix(float x, float y, float z) {
  return(new float[][] {
    {
      x, 0, 0, 0
    }
    , 
    {
      0, y, 0, 0
    }
    , 
    {
      0, 0, z, 0
    }
    , 
    {
      0, 0, 0, 1
    }
  }
  );
}
float[][] translationMatrix(float x, float y, float z) {
  return(new float[][] {
    {
      1, 0, 0, x
    }
    , 
    {
      0, 1, 0, y
    }
    , 
    {
      0, 0, 1, z
    }
    , 
    {
      0, 0, 0, 1
    }
  }
  );
}

float[] matrixProduct(float[][] a, float[] b) {
  float[] product = new float[4];
  for (int i=0; i<4; i++) {
    for (int j=0; j<4; j++) {
      product[i] += a[i][j]*b[j];
    }
  }
  return product;
} 

My3DBox transformBox(My3DBox box, float[][] transformMatrix) {
  My3DPoint[] transformed = new My3DPoint[8];
  for (int i=0; i<8; i++) {
    transformed[i] = euclidian3DPoint(matrixProduct(transformMatrix, homogeneous3DPoint(box.p[i])));
  }
  return new My3DBox(transformed);
}

My3DPoint euclidian3DPoint (float[] a) {
  My3DPoint result = new My3DPoint(a[0]/a[3], a[1]/a[3], a[2]/a[3]);
  return result;
}


