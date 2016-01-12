package mainSystem;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public class Player extends Ball{
	public boolean landing, toJump;
	public boolean prepR, prepL;
	public boolean shootR, shootL;
	private Point toAim;
	private double dist, rad;
	
	public Player(LinkedList<Ball> ball, LinkedList<Figure> figure, Point pos, Point vel, double size, ColorType color){
		super(ball, figure, pos, vel, size, color);
		landing = toJump = false;
		prepR = prepL = false;
		shootR = shootL = false;
		toAim = new Point();
		dist = rad = 0.0;
	}
	
	@Override
	public void draw(GraphicsContext gc){
		super.draw(gc);
		if(prepL && size+19.0 < dist) strokeDottedLine(gc, ColorType.BLUE);
		if(prepR && size+19.0 < dist) strokeDottedLine(gc, ColorType.RED);
	}
	
	public void culcToAim(Point aim, boolean shifted){
		toAim.set(aim.x-pos.x, -aim.y+pos.y);
		dist = pos.distance(aim)-size;
		rad = toAim.arg();
		if(shifted){
			int n = (int)Math.round(rad*4/Math.PI);
			rad = n/4.0*Math.PI;
			switch(n%4){
				case 0:
					dist = Math.abs(toAim.x);
					break;
				case 1:
					dist = Math.abs(Math.sqrt(0.5)*(toAim.x+toAim.y));
					break;
				case 2:
					dist = Math.abs(-toAim.y);
					break;
				default:
					dist = Math.abs(Math.sqrt(0.5)*(toAim.x-toAim.y));
					break;
			}
		}
		if(dist >= 380.0) dist = 380.0;
	}
	
	public void shoot(LinkedList<Ball> ball, LinkedList<Figure> figure, ColorType color){
		final double ns = 13.0;
		ball.add(new Ball(ball, figure, pos.plus(Point.polar(size+ns, -rad)), Point.polar(dist/25, rad), ns, color));
		weight -= ns*ns;
		size = Math.sqrt(weight);
	}
	
	public void strokeDottedLine(GraphicsContext gc, ColorType color){
		final double space = 10.0;
		final double dotted = Math.floor((dist+11)/space);
		Point p1, p2;
		gc.setStroke(color.toGc());
		gc.setLineWidth(6);
		for(int i = 1; i < dotted/2-1; i++){
			gc.beginPath();
			p1 = pos.plus(Point.polar(dist-8.0-space*2*i+size, -rad));
			p2 = pos.plus(Point.polar(dist-8.0-space*(2*i+1)+size, -rad));
			gc.moveTo(p1.x, p1.y);
			gc.lineTo(p2.x, p2.y);
			gc.closePath();
			gc.stroke();
		}
		if(dotted%2==0){
			gc.beginPath();
			p1 = pos.plus(Point.polar(dist-8.0-space*(dotted-2)+size, -rad));
			p2 = pos.plus(Point.polar(size, -rad));
			gc.moveTo(p1.x, p1.y);
			gc.lineTo(p2.x, p2.y);
			gc.closePath();
			gc.stroke();
		}
	}
}
