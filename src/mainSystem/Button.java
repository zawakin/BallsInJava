package mainSystem;

import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public abstract class Button extends Polygon{
	private final double size;
	private Point pos;
	protected LinkedList<Figure> figs;
	protected boolean working;
	
	public Button(LinkedList<Ball> ball, Point pos, double size, ColorType color){
		super(ball, getPs(pos, size), color);
		this.pos = pos.copy();
		this.size = size;
		figs = new LinkedList<Figure>();
		working = false;
	}
	
	private static Point[] getPs(Point pos, double size){
		Point[] p = {
				new Point(pos.x+size, pos.y),
				new Point(pos.x, pos.y+size),
				new Point(pos.x-size, pos.y),
				new Point(pos.x, pos.y-size)
				};
		return p;
	}
	
	public void associate(Figure fig){
		figs.add(fig);
	}
	
	public void rmAssociation(Figure fig){
		figs.remove(fig);
	}
	/*
	@Override
	public void draw(GraphicsContext gc){

	}
	*/
	@Override
	public void collideWithApex(Ball b, Point p){
		super.collideWithApex(b, p);
		work(b);
	}

	@Override
	public void collideWithSide(Ball b, Point p, double arg){
		super.collideWithSide(b, p, arg);
		work(b);
	}

	protected abstract void work(Ball b);
}
