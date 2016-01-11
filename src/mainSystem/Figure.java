package mainSystem;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public class Figure {
	public static final double e = 0.6;
	
	private Point tl, tr, bl, br;
	private Point vh, vv;
	private double rad1, rad2;
	public ColorType color;
	
	Figure(LinkedList<Ball> ball, Point tl, double wid, double hei, double rad1, double rad2, ColorType color){
		if(rad2 == 0.0) rad2 = rad1-Math.PI/2;
		vh = Point.polar(wid, rad1);
		vv = Point.polar(hei, rad2);
		
		this.tl = tl.copy();
		tr = tl.plus(vh);
		bl = tl.minus(vv);
		br = tl.plus(vh).minus(vv);
		
		this.rad1 = rad1;
		this.rad2 = rad2;
		this.color = color;
		final int I = ball.size();
		for(int i = 0; i < I; i++){
			ball.get(i).touchArea.add(new TouchArea());
		}
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
				b.touchArea.get(j).rad = rad1 + Math.PI;
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
			if(b.touchArea.get(j).dist <= b.size){
				final double rad = Math.atan2(b.pos.y-b.touchArea.get(j).p.y+b.vel.y, -b.pos.x+b.touchArea.get(j).p.x-b.vel.x);
				final Point velh = Point.polar(b.vel.cross(Point.polar(1.0, rad)), rad).right();
				Point velv = b.vel.minus(velh);
				
				final double excess = b.size-b.touchArea.get(j).dist;
				b.pos.translate(Point.polar(-excess, -rad));
				velv.zoom(-e);
				b.vel = velh.plus(velv);
				
				b.contact[b.collisionC].p = b.touchArea.get(j).p.copy();
				b.contact[b.collisionC].rad = b.touchArea.get(j).p.minus(b.pos).arg();
				b.contact[b.collisionC].tangent = b.contact[b.collisionC].rad+Math.PI/2;
				b.collisionC++;
			}
			else if(b.touchArea.get(j).dist <= b.size*1.5) return;
		}
		else if(b.touchArea.get(j).num < 3){
			final double drop = Point.polar(-1.0, b.touchArea.get(j).rad).cross(b.pos.minus(b.touchArea.get(j).p));
			if(drop <= b.size){
				final Point velh = Point.polar(b.vel.dot(Point.polar(1.0, -b.touchArea.get(j).rad)), -b.touchArea.get(j).rad);				
				Point velv = b.vel.minus(velh);
				b.contact[b.collisionC].p = b.pos.plus(Point.polar(drop, b.touchArea.get(j).rad).left());
				b.contact[b.collisionC].rad = b.touchArea.get(j).p.minus(b.pos).arg();
				b.contact[b.collisionC].tangent = b.touchArea.get(j).rad+Math.PI; 
				b.collisionC++;
				
				final double excess = b.size-drop;
				b.vel.translate(Math.sqrt(2*0.3*e*excess), Math.sqrt(2*0.3*e*excess));
				b.pos.translate(Point.polar(excess, b.touchArea.get(j).rad).right());
				velv.zoom(-e);
				b.vel = velh.plus(velv);
			}
			else if(drop <= b.size*1.5) return;
		}
		else{
			System.out.println("?");
		}
		b.touchArea.get(j).num = 0;
	}
}
