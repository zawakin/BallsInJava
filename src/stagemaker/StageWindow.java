package stagemaker;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import mainSystem.Ball;
import mainSystem.ColorType;
import mainSystem.Figure;
import mainSystem.Main;
import mainSystem.Point;
import javafx.collections.*;

import java.util.LinkedList;

public class StageWindow extends Stage{
    int width,height;
    GraphicsContext gc;
    Point p0,pf,nowmouse;
    private DataStage datastage;
    private MyStage ms1;
    private Stage parent;
    public StageWindow(Stage owner,LinkedList<mainSystem.Ball> ball, LinkedList<mainSystem.Figure> figure){
        width = 800;
        height = 512;
        setWidth(width+100);
        setHeight(height+100);
        setTitle("StageMaker");
        setX(0);
        parent = owner;
        Group root = new Group();
        Canvas cvs = new Canvas(width,height);
        ms1 = new MyStage(width,height);
        datastage = new DataStage(this,ms1);
        ObservableList<stagemaker.Figure> obslist = datastage.dataobslist;

        //init datatable


        gc = cvs.getGraphicsContext2D();

        cvs.setFocusTraversable(true);
        cvs.setOnKeyPressed(e -> {
            System.out.println(e.getCode().toString());
            int kind = 0;
			if(e.getCode() == KeyCode.F5){
				ms1.requestDraw = true;
				System.out.println("F5");
				//ms1.SetField(ball, figure);
				return;
			}
            switch(e.getCode()){
                case C:
                    kind = 1;
                    break;
                case V:
                    kind = 2;
                    break;
                case X:
                    kind = 0;
                    break;
                default:
                    kind = -1;
                    break;
            }
            if(kind!=-1){
                if(kind==0){
                    ms1.setInitPos(nowmouse);
                }else{
                    ms1.addBall(new stagemaker.Ball(nowmouse.x,nowmouse.y,10,kind));
                }
                gc.clearRect(0,0,width,height);
                ms1.drawAllFigure(gc);
//                ms1.printAllFigure();
            }
        });
        cvs.setOnMouseMoved(e -> {
            nowmouse = new Point(e.getX(),e.getY());
        });
        cvs.setOnMousePressed(e -> {
            p0 = new Point(e.getX(),e.getY());
        });
        cvs.setOnMouseDragged(e -> {
            pf = new Point(e.getX(),e.getY());
            gc.clearRect(0,0,width,height);
            ms1.drawAllFigure(gc);
            gc.strokeRect(p0.x,p0.y,pf.x-p0.x,pf.y-p0.y);
        });
        cvs.setOnMouseReleased(e -> {
            double w = e.getX() - p0.x;
            double h = e.getY() - p0.y;
            gc.clearRect(0,0,width,height);
            if(w>0 && h>0){
                ms1.addFigure(new stagemaker.Figure(p0.x,p0.y,w,h,3));
                obslist.removeAll(obslist);
                for(int i=0;i<ms1.figureList.size();i++){
	                obslist.add(ms1.figureList.get(i));
                }
            }
            ms1.drawAllFigure(gc);
//            ms1.printAllFigure();
        });
//		cvs.setOnKeyPressed(event -> {
//			final KeyCode code = event.getCode();
//			if(code == KeyCode.F5){
//				ms1.requestDraw = true;
//				System.out.println("F5");
//				//ms1.SetField(ball, figure);
//			}
//		});

        root.setCursor(Cursor.CROSSHAIR);
        root.getChildren().add(cvs);
        setScene(new Scene(root,0,0));
        show();

        ms1.drawAllFigure(gc);
    }
    public MyStage getMyStage(){
    	return ms1;
    }
    
    public void drawAllFigure(){
        gc.clearRect(0,0,width,height);
        ms1.drawAllFigure(gc);
    }
}
