package stagemaker;

import javafx.scene.paint.*;
import mainSystem.Point;
import javafx.scene.canvas.*;

public class Ball extends Figure{
    protected double x,y,r,vx,vy;
    protected Point pos,vel;
    
    public Ball(double x,double y, double r,double vx,double vy,int kind){
        super(x,y,r,r,0,0,kind);
        this.pos = new Point(x,y);
        this.vel = new Point(vx,vy);
        this.r = r;
        this.kind = kind;
    }
    public Ball(double x,double y,double r,int kind){
        this(x,y,r,0,0,kind);
    }

    @Override
    public void Draw(GraphicsContext gc){
        switch(kind){
            case 0:
                gc.setFill(Color.GREEN);
                break;
            case 1:
                gc.setFill(Color.BLUE);
                break;
            case 2:
                gc.setFill(Color.RED);
                break;
            default:
                gc.setFill(Color.GRAY);
                break;
        }
        gc.fillOval(pos.x-r,pos.y-r,2*r,2*r);
        gc.setFill(Color.BLACK);
    }

    @Override
    public String getGaneCode(){
        return "{x:"+x+",y:"+y+"},"+r+",{x:"+vx+",y:"+vy+"},"+kind;
    }
}