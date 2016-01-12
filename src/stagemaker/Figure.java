package stagemaker;

import javafx.beans.property.StringProperty;
import javafx.geometry.*;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.LinkedList;
import mainSystem.Point;
import mainSystem.ColorType;

public class Figure{
    protected Point pos;
    protected double width,height,angle,angle2;
    protected int kind;
    protected String name;
    public Figure(double x,double y, double width, double height, double angle, double angle2,int kind){
    	this.name = "WALL";
        this.pos = new Point(x,y);
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.angle2 = angle2;
        this.kind = kind;
    }
    public Figure(double x,double y, double width,double height,int kind){
        this(x,y,width,height,0.0,0.0,kind);
    }
    public String getType(){return "WALL";}
    
    public double getX(){return pos.x;}
    public void setX(Number x){pos.x = x.doubleValue();}
    public void setX(String strx){pos.x = Double.parseDouble(strx);}
    
    public double getY(){return pos.y;}
    public void setY(Number y){pos.y = y.doubleValue();}
    
    public double getWidth(){return width;}
    public void setWidth(Number w){width = w.doubleValue();}
    
    public double getHeight(){return height;}
    public void setHeight(Number h){height = h.doubleValue();}
    
    public double getAngle(){return angle;}
    public void setAngle1(Number a1){angle = a1.doubleValue();}
    
    public double getAngle2(){return angle2;}
    public void setAngle2(Number a2){angle2 = a2.doubleValue();}
    
    public void Draw(GraphicsContext gc){
    	gc.setFill(getColorType().toGc());
    	gc.fillRect(pos.x,pos.y,width,height);
    }
    @Override
    public String toString(){
        return name + " " + pos.x + " " + pos.y + " " + width + " " + height + " " + angle + " " + angle2 + " " + kind;
    }
    public String getGaneCode(){
        return pos.x+","+pos.y+","+width+","+height+","+angle+","+angle2+","+kind;
    }
    public ColorType getColorType(){
    	switch(kind){
    	case 0:
    		return ColorType.GREEN;
    	case 1:
    		return ColorType.BLUE;
    	case 2:
    		return ColorType.RED;
    	case 3:
    		return ColorType.GRAY;
    	}
    	return ColorType.GRAY;
    }
    public void setColorType(Number colornum){
    	kind = colornum.intValue();
    }
    public int getKind(){
    	return kind;
    }
    public void setKind(Number kindnum){
    	kind = kindnum.intValue();
    }
    public mainSystem.Figure getMainFigure(LinkedList<mainSystem.Ball> ball){
    	return new mainSystem.Figure(ball, 
    			new Point(pos.x,pos.y), 
    			width, height, angle, angle2, getColorType());
    }
}
