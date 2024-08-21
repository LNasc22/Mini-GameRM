import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class JogoRMFInalTDE extends PApplet {

Bola bola;

Mover mover = new Mover();
int N = 2;             // N perseguidores
Mover[] perseguidores;
int cont = 0;

TSR tsr = new TSR();
Matriz2D matrizObjeto, matrizTransf, matrizObjetoTransformado;
float [][] objeto = {{ 40, 60, 1},
                      {50, 60, 1},
                      {80, 40, 1},                      
                      {110, 60, 1},
                      {110, 60, 1},
                      {110, 80, 1},
                      {110, 100, 1}, 
                      {110, 100, 1},                      
                      {80, 120, 1},
                      {50, 100, 1},
                      {50, 100, 1},
                      {50, 80, 1},
                      {50, 60, 1}};



int health = 10;
int score = 0;

public void setup (){ 
   background(0);
  matrizObjeto = new Matriz2D(objeto);
  matrizTransf = new Matriz2D(3,3);
  
  perseguidores = new Mover[N]; // inicialização do Array
  for (int i = 0; i<N; i++)     // inicialização de cada instância do Array
  perseguidores[i] = new Mover();
  
  bola = new Bola(width/2,height/2, 40);
  
  
}

public void draw(){
  
  //para colisão do escudo;
  fill(0);
  ellipse(width/2,height/2,80,80);
  
  //Confere se o mouse foi pressionado, se sim cria a barreira
  if(mousePressed == true)
  {
      tsr.Translate(224,168);
      matrizObjetoTransformado = matrizObjeto.times(matrizTransf);
      stroke(random(44,255),random(20,150),random(150,255));
      matrizObjetoTransformado.desenhaObjeto();
    }
    
  //Confere se o mouse não está sendo pressionado, se sim, redefine o bg
  else
    {
      background(0);
    }
    
  //Desenha a ellipse 
  bola.display();

  
  
  //Criação dos perseguidores
  for (int i = 0; i<N; i++)
    { 
      perseguidores[i].update();   
      perseguidores[i].display(); 
    }
  
  //colision perseguidores e reposicionamento
  if(mousePressed == false){
  for (int i = 0; i<N; i++) 
  {
    if(perseguidores[i].location.xcomp >= bola.esquerda() &&  perseguidores[i].location.ycomp >= height/2)
    {
      health -=1;
      perseguidores[i].location = new Vetor(random(width),random(height));
       
    }
   }
 }
  
  
  if(mousePressed == true){
  for (int i = 0; i<N; i++) 
  {
    if(perseguidores[i].location.xcomp >= bola.esquerda() &&  perseguidores[i].location.ycomp >= height/2)
    {
      score +=1;
      perseguidores[i].location = new Vetor(random(width),random(height));
       
    }
  }
}

    System.out.println("Cont: " + cont);
    
   fill(255);
   textAlign(CENTER);
   textSize(32);
   text("HP: " + health,110,40);
   text("Score: " + score, 110,85);
   
//   cont = 10;
//  while (cont > 0){
//  health--;
//cont--;
//} 
   if(health <= 0 )
   {
     textAlign(CENTER);
     textSize(64);
     text("GAME OVER",305,250);
   }
   
   

}
class Bola{
float x,y,d;

//Construtor
Bola(float tempX, float tempY, float tempD){
  x = tempX;
  y = tempY;
  d = tempD;
  }

public void display(){
  fill(random(44,255),random(20,150),random(150,255));
  ellipse(x,y,d,d);
  }

public float esquerda(){
  return x-d/2;
  }
public float direita(){
  return x+d/2;
  }
public float cima(){
  return y-d/2;
  }
public float baixo(){
  return y+d/2;
  }
}
class Matriz2D {
    private int M;             // numero de linhas
    private int N;             // numero de colunas
    private float[][] data;    // M-by-N array

    // cria a matriz M-by-N de zeros
    public Matriz2D(int M, int N) {
        this.M = M;
        this.N = N;
        data = new float[M][N];
    }
    
    // imprime a matriz na tela 
    public void show(int xpos, int ypos) {
    fill(255,0,0);
    int tx=xpos;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
            text(data[i][j], xpos, ypos);
            xpos=xpos+50;
      }
      ypos=ypos+20;
      xpos=tx;
        }
    }
    
    public void desenhaObjeto(){ // somente para matrizes objetos 2D
    int M = this.M;
    for(int i=0; i<M-1; i++) {
    ellipse(data[i][0],height-data[i][1], 5,5);
    line(data[i][0],height-data[i][1], data[i+1][0],height-data[i+1][1]); 
      }
    }

    // cria a matriz baseado num array2d de dados 
    public Matriz2D(float[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new float[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    this.data[i][j] = data[i][j];
    }

    // transforma em matriz identidade
    public void identity() {
      if(this.M==this.N) {
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                    if (i==j) this.data[i][j] = 1;
                    else this.data[i][j] = 0;
      } else println("Erro: Matriz Identidade somente com M = N");
    }

    // cria e retorna a transposta da matriz que invocou o método
    public Matriz2D transpose() {
        Matriz2D A = new Matriz2D(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public Matriz2D plus(Matriz2D B) {
        Matriz2D A = this;
        if (B.M == A.M && B.N == A.N){
          Matriz2D C = new Matriz2D(M, N);
          for (int i = 0; i < M; i++)
              for (int j = 0; j < N; j++)
                  C.data[i][j] = A.data[i][j] + B.data[i][j];
          return C;}
        else {
          println("Erro: Matrizes de tamanhos diferentes");
          return A;
        }
    }

    // return C = A - B
    public Matriz2D minus(Matriz2D B) {
        Matriz2D A = this;
        if (B.M == A.M && B.N == A.N) {
          Matriz2D C = new Matriz2D(M, N);
          for (int i = 0; i < M; i++)
              for (int j = 0; j < N; j++)
                  C.data[i][j] = A.data[i][j] - B.data[i][j];
          return C;}
         else {
         println("Erro: Matrizes de tamanhos diferentes");
         return A;
        }
    }

    // return C = A * B
    public Matriz2D times(Matriz2D B) {
        Matriz2D A = this;
        if (A.N == B.M) {
        Matriz2D C = new Matriz2D(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;}
       else {
       println("Erro: Matrizes incompatíveis para multiplicação");
       return A;
      }        
    }

    // calcula determinante (método de triangularização)
    // triangulariza a matriz que invoca o método
    public float determinant(){
      Matriz2D A = this;
      if (M==N) {
        float temp, factor; 
        int count = 0;
        // faz a transformação em um triangulo...
        for(int i = 0; i < M - 1; i++)
        {
            if(A.data[i][i] == 0)
            {
                for(int k = i; k < M; k++)
                {
                    if(A.data[k][i] != 0)
                    {
                        for(int j = 0; j < M; j++)
                        {
                            temp = A.data[i][j];
                            A.data[i][j] = A.data[k][j];
                            A.data[k][j] = temp;
                        }
                        k = M;
                    }
                }
                count++;
            } 
            if(A.data[i][i] != 0)
            {
                for(int k = i + 1; k < M; k++)
                {
                    factor = -1.0f * A.data[k][i] /  A.data[i][i];
                    for(int j = i; j < M; j++)
                    {
                        A.data[k][j] = A.data[k][j] + (factor * A.data[i][j]);
                    }
                }
            }
        } 
        temp = 1.0f;
        // Calcula o determinante, multiplica a diagonal
        for(int i = 0; i < M; i++) temp = temp * A.data[i][i];
        return temp; 
        }
        else {
        println("Erro: A Matriz não é quadrada");
        return 0;
        }
    }
}
class Mover {
 
  Vetor location;
  Vetor velocity;
  Vetor acceleration;
 
  Mover() {
    location = new Vetor(random(width),random(height));
    velocity = new Vetor(random(-2,2),random(-2,2));
  }
 
 public void update() {
    Vetor mouse = new Vetor(width/2,height/2);
    Vetor dir = Subtrai2Vetores(mouse,location);
    dir.Normalizar();
    dir.MultEscalar(1);
    acceleration = dir;
    velocity.SomaVetor(acceleration);
    velocity.Limite(3);
    location.SomaVetor(velocity);
 }
  
  public void display() {
    stroke(0);
    fill(random(19,247),random(79,247),random(0,48));
    ellipse(location.xcomp,location.ycomp,16,16);
  }
}
class TSR {
  
  public TSR() {
  }
  
  public void Translate(float tx, float ty) {
    matrizTransf.data[0][0] = 1;
    matrizTransf.data[0][1] = 0;  
    matrizTransf.data[0][2] = 0;
    matrizTransf.data[1][0] = 0;
    matrizTransf.data[1][1] = 1;  
    matrizTransf.data[1][2] = 0;  
    matrizTransf.data[2][0] = tx;
    matrizTransf.data[2][1] = ty;  
    matrizTransf.data[2][2] = 1;
  }

}
///////////////////////////////////////////////////////// classe Vetor

class Vetor {
  float xcomp;
  float ycomp;
  
  Vetor(float txcomp, float tycomp){
    xcomp = txcomp;
    ycomp = tycomp;
  }
  
  public void Display(float xpos, float ypos, int c){
    stroke(c);
    line(xpos,height-ypos, xpos+xcomp,height-(ypos+ycomp));
    ellipse(xpos+xcomp,height-(ypos+ycomp),5,5);
    fill(c);
    text("v(m"+Magnitude()+")", (2*xpos+xcomp)/2, height-(2*ypos+ycomp)/2);
  }
  
  public float Magnitude(){
     return sqrt(xcomp*xcomp+ycomp*ycomp); 
  }
  
  public void MultEscalar(float k){
    xcomp = k * xcomp;
    ycomp = k * ycomp;
  }
  
  public void SomaVetor(Vetor tVetor){
     xcomp = xcomp + tVetor.xcomp;
     ycomp = ycomp + tVetor.ycomp;
  }
  
  public void SubtraiVetor(Vetor tVetor){
     xcomp = xcomp - tVetor.xcomp;
     ycomp = ycomp - tVetor.ycomp;     
  }
  
  public float ProdutoEscalar(Vetor tVetor){
    return xcomp * tVetor.xcomp + ycomp * tVetor.ycomp;
  }
  
  public boolean Ortogonal(Vetor tVetor){
    if (ProdutoEscalar(tVetor)==0) return true; else return false;
  }
  
  public void Normalizar(){
     float Mag = Magnitude();
     xcomp = xcomp / Mag;
     ycomp = ycomp / Mag;
  }
  
  public void Limite(float limite){
      if (Magnitude()>limite) 
      Normalizar();
      MultEscalar(limite);
  }
}

////////////////////////////////////////////////////////////// funções isoladas

public Vetor Subtrai2Vetores(Vetor tVetor1, Vetor tVetor2){
     Vetor VetorResultado;
     VetorResultado = new Vetor(0,0);
     VetorResultado.xcomp = tVetor1.xcomp - tVetor2.xcomp;
     VetorResultado.ycomp = tVetor1.ycomp - tVetor2.ycomp;
     return VetorResultado;
}
  public void settings() {  size(610,500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "JogoRMFInalTDE" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
