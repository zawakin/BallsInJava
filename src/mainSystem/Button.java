package mainSystem;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;

public abstract class Button extends Polygon{
	protected final double size;
	protected Point center;
	protected LinkedList<Figure> figs;
	protected boolean working;
	protected long timer, endTime;
	
	public Button(LinkedList<Ball> ball, Point center, double size, ColorType color){
		super(ball, getPs(center, size), color);
		this.center = center.copy();
		this.size = size;
		figs = new LinkedList<Figure>();
		working = false;
		timer = 0;
		endTime = -1;
	}
	public Button(LinkedList<Ball> ball, Point center, double size){
		this(ball, center, size, ColorType.GRAY);
	}
	
	private static Point[] getPs(Point center, double size){
		Point[] p = {
				new Point(center.x+size, center.y),
				new Point(center.x, center.y+size),
				new Point(center.x-size, center.y),
				new Point(center.x, center.y-size)
				};
		return p;
	}
	
	public void associate(Figure fig){
		figs.add(fig);
	}
	
	public void rmAssociation(Figure fig){
		figs.remove(fig);
	}
	
	@Override
	public void time(){
		if(endTime < 0 || !working) return;
		timer++;
		if(timer >= endTime){
			timer = 0;
			working = false;
		}
	}
	
	@Override
	public void draw(GraphicsContext gc){
		if(working){
			gc.beginPath();
			gc.arc(center.x, center.y, size/2, size/2, 0, 360);
			gc.closePath();
			gc.setEffect(new Bloom(0.1));
			gc.setFill(Color.ORANGE);
			gc.fill();
		}
		gc.beginPath();
		gc.moveTo(p[0].x, p[0].y);
		gc.lineTo(p[1].x, p[1].y);
		gc.lineTo(p[2].x, p[2].y);
		gc.lineTo(p[3].x, p[3].y);
		gc.closePath();
		gc.arc(center.x, center.y, size/2, size/2, 0, 360);
		gc.closePath();
		gc.setEffect(null);
		gc.setFill(color.toGc());
		gc.fill();
	}
	
	@Override
	public void collideWithApex(Ball b, Point p){
		super.collideWithApex(b, p);
		working = true;
		work(b);
	}

	@Override
	public void collideWithSide(Ball b, Point p, double arg){
		super.collideWithSide(b, p, arg);
		working = true;
		work(b);
	}
	
	protected abstract void work(Ball b);
}
