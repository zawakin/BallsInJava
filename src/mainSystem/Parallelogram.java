package mainSystem;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public class Parallelogram extends Figure{
	private Point tl, tr, bl, br;
	private Point vh, vv;
	private double rad1, rad2;
	
	public Parallelogram(LinkedList<Ball> ball, Point tl, double wid, double hei, double rad1, double rad2, ColorType color){
		super(ball, color);
		if(rad2 == 0.0) rad2 = rad1-Math.PI/2;
		vh = Point.polar(wid, rad1);
		vv = Point.polar(hei, rad2);
		
		this.tl = tl.copy();
		tr = tl.plus(vh);
		bl = tl.minus(vv);
		br = tl.plus(vh).minus(vv);
		
		this.rad1 = rad1;
		this.rad2 = rad2;
	}
	
	public void draw(GraphicsContext gc){
		final double[] xPoints = {tl.x, tr.x, br.x, bl.x};
		final double[] yPoints = {tl.y, tr.y, br.y, bl.y};
		gc.setFill(color.toGc());
		gc.fillPolygon(xPoints, yPoints, 4);
	}
	
	public void collision01(Ball b, int j){
		final double htl = vh.cross(b.pos.minus(tl));
		final double vbl = vv.cross(b.pos.minus(bl));
		final double vtr = vv.inv().cross(b.pos.minus(tr));
		final double hbr = vh.inv().cross(b.pos.minus(br));
		
		if(htl < 0){
			if(vbl < 0){
				b.touchArea.get(j).p = tl.copy();
				b.touchArea.get(j).num = 1;
			}
			else if(vtr < 0){
				b.touchArea.get(j).p = tr.copy();
				b.touchArea.get(j).num = 1;
			}
			else{
				b.touchArea.get(j).p = tl.copy();
				b.touchArea.get(j).rad = rad1;
				b.touchArea.get(j).num = 2;
			}
		}
		else if(hbr < 0){
			if(vbl < 0){
				b.touchArea.get(j).p = bl.copy();
				b.touchArea.get(j).num = 1;
			}
			else if(vtr < 0){
				b.touchArea.get(j).p = br.copy();
				b.touchArea.get(j).num = 1;
			}
			else{
				b.touchArea.get(j).p = br.copy();
				b.touchArea.get(j).rad = rad1+Math.PI;
				b.touchArea.get(j).num = 2;
			}
		}
		else{
			if(vbl < 0){
				b.touchArea.get(j).p = bl.copy();
				b.touchArea.get(j).rad = rad2;
				b.touchArea.get(j).num = 2;
			}
			else if(vtr < 0){
				b.touchArea.get(j).p = tr.copy();
				b.touchArea.get(j).rad = rad2+Math.PI;
				b.touchArea.get(j).num = 2;
			}
			else{
				b.touchArea.get(j).num = 3;
			}
		}
		if(b.touchArea.get(j).num < 2){
			b.touchArea.get(j).dist = b.pos.distance(b.touchArea.get(j).p);
			if(b.touchArea.get(j).dist <= b.size) collideWithApex(b, b.touchArea.get(j).p);
			else if(b.touchArea.get(j).dist <= b.size*1.5) return;
		}
		else if(b.touchArea.get(j).num < 3){
			final double drop = Point.polar(-1.0, b.touchArea.get(j).rad).cross(b.pos.minus(b.touchArea.get(j).p));
			if(drop <= b.size) collideWithSide(b, b.touchArea.get(j).p, b.touchArea.get(j).rad);
			else if(drop <= b.size*1.5) return;
		}
		else{
			System.out.println("?");
		}
		b.touchArea.get(j).num = 0;
	}
}
