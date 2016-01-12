package stagemaker;

import java.util.*;
import javafx.geometry.Point2D;
import java.io.*;
import javafx.scene.canvas.*;
import mainSystem.ColorType;
import mainSystem.Main;
import mainSystem.Point;

//green:0 red:1 blue:2 black:3
public class MyStage{
  public ArrayList<Figure> figureList;
  private ArrayList<Ball> ballList;
  private Ball initBall;
  private boolean initposflag;
  public boolean requestDraw = false;
  private int width, height;
  public MyStage(int width,int height){
	  this.width = width;
	  this.height = height;
      figureList = new ArrayList<Figure>();
      ballList = new ArrayList<Ball>();
      initposflag = false;
      init(true);
  }
  public ArrayList<Figure> getAllFigure(){
      return figureList;
  }
  public void addFigure(Figure figure){
      figureList.add(figure);
  }
  public void addBall(Ball ball){
      ballList.add(ball);
  }
  public void setInitPos(Point pos){
      initBall = new Ball(pos.x,pos.y,15,0);
      initposflag = true;
  }
  public void printAllFigure(){
      System.out.println();
      for(int i=0;i<figureList.size();i++){
          System.out.println(figureList.get(i));
      }
      System.out.println(getGaneCode());
      try{
          FileWriter fw = new FileWriter(new File("RBP/MyStage.js"));
          
          String ss = "var MyStage01 = function(ball, object){\n"
      	+"for(var i=0; i<object.length; i++){\n"
      	+"	object[i].alive = false;\n"
      	+"}\n"
      	+"for(var i=1; i<ball.length; i++){\n"
      	+"	ball[i].alive = false;\n"
      	+"	ball[i].collisionC = 0;\n"
      	+"	ball[i].collisionCC = 0;\n"
	        +"}\n";
          fw.write(ss);
          fw.write(getGaneCode());
          ss = 	"nowStage = 100;\n"
                  +"};\n";
          fw.write(ss);
          fw.close();
      }catch(IOException e){
        System.out.println(e);
      }
  }
  public void drawAllFigure(GraphicsContext gc){
      //085green red blue 0.6 gray 0.75
//      gc.setGlobalAlpha(0.53);
      for(int i=0;i<figureList.size();i++){
          figureList.get(i).Draw(gc);
      }
//      gc.setGlobalAlpha(0.6);
      for(int i=0;i<ballList.size();i++){
          ballList.get(i).Draw(gc);
      }
      if(initposflag){
//          gc.setGlobalAlpha(0.85);
          initBall.Draw(gc);
      }
  }
  public String getGaneCode(){
      String s = "";

      for(int i=0;i<figureList.size();i++){
          s += "object["+i+"].set("+figureList.get(i).getGaneCode()+");\n";
      }
      if(initposflag){
          s += "ball[0].set("+initBall.getGaneCode()+");\n";
      }
      for(int i=0;i<ballList.size();i++){
          int a = i+1;
          s += "ball["+a+"].set("+ballList.get(i).getGaneCode()+");\n";
      }

      return s;
  }    

	public void init(boolean initwall){
		figureList.clear();
		ballList.clear();
		if(initwall){
	      figureList.add(new Figure(   0, height-3, width, 316, 0, 0, 3));
	      figureList.add(new Figure(-297,-300, 300, height+300, 0, 0, 3));
	      figureList.add(new Figure( width-3,-300, 300, height+300, 0, 0, 3));
		}
	}
	
	public void save(File savefile){
		 try{
	          FileWriter fw = new FileWriter(savefile);
	          for(int i=0;i<figureList.size();i++){
	        	  fw.write(figureList.get(i).toString() + "\n");
	          }
	          fw.close();
	      }catch(IOException e){
	        System.out.println(e);
	      }
	}
	public void load(File loadfile){
		try{
			init(false);
			  BufferedReader br = new BufferedReader(new FileReader(loadfile));
			  String str;
			  while((str = br.readLine()) != null){
//				  System.out.println(str);
				  String[] data = str.split(" ",0);
				  System.out.println(data[0]);
				  if(data[0].equals("WALL")){
					  final double x = Double.parseDouble(data[1]);
					  final double y = Double.parseDouble(data[2]);
					  final double w = Double.parseDouble(data[3]);
					  final double h = Double.parseDouble(data[4]);
					  final double a1 = Double.parseDouble(data[5]);
					  final double a2 = Double.parseDouble(data[6]);
					  final int kind = Integer.parseInt(data[7]);
					  figureList.add(new Figure(x,y,w,h,a1,a2,kind));					  
				  }
				  
			  }

			  br.close();
			}catch(FileNotFoundException e){
			  System.out.println(e);
			}catch(IOException e){
			  System.out.println(e);
			}
	}
	
  public void SetField(LinkedList<mainSystem.Ball> ball, LinkedList<mainSystem.Figure> figure){
		System.out.println("setfield");
		ball.clear();
		figure.clear();
		double x;
		double y;
		if (initposflag) {
			x = initBall.pos.x;
			y = initBall.pos.y;
			Main.player.pos.set(x, y);
		}
		ball.add(Main.player);
	      for(int i=0;i<figureList.size();i++){
	          figure.add(figureList.get(i).getMainFigure(ball));
	      }
//		figure.add(new mainSystem.Figure(ball, new mainSystem.Point(0, 497), 800, 316, 0, 0, ColorType.GRAY));
		Main.nowStage = 0;
		requestDraw = false;
  }
}