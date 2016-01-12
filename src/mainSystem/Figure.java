package mainSystem;

import java.util.LinkedList;
import javafx.scene.canvas.GraphicsContext;

public abstract class Figure {
	public static final double e = 0.6;
	public ColorType color;
	
	public Figure(LinkedList<Ball> ball, ColorType color){
		this.color = color;
		for(int i = 0; i < ball.size(); i++){
			ball.get(i).touchArea.add(new TouchArea());
		}
	}
	
	public abstract void draw(GraphicsContext gc);
	public abstract void collision01(Ball b, int j);
	
	public void collision02(Ball b, int j){
		if(b.touchArea.get(j).num < 2){
			final double rad = b.touchArea.get(j).p.minus(b.pos).arg();
			int i;
			for(i = 0; i < b.dot.length; i++){
				if((rad+2*Math.PI)%(2*Math.PI) < (b.dot[i].rad+2*Math.PI-b.dot[0].rad)%(2*Math.PI)) break;
			}
			if(i >= b.dot.length) i = 0;
			final double t = (rad-b.dot[(i+b.dot.length-1)%b.dot.length].rad)/(b.dot[i].rad-b.dot[(i+b.dot.length-1)%b.dot.length].rad);
			final double tangent1 = b.dot[i].p.minus(b.dot[(i+b.dot.length-2)%b.dot.length].p).arg(); 
			final double tangent2 = b.dot[(i+1)%b.dot.length].p.minus(b.dot[(i+b.dot.length-1)%b.dot.length].p).arg(); 
			final double excess = b.dot[(i+b.dot.length-1)%b.dot.length].p.minus(b.dot[i].p).cross(b.touchArea.get(j).p.minus(b.dot[i].p));
			if(excess < 0){				
				b.contact[b.collisionC+b.collisionCC].p = b.touchArea.get(j).p.copy();
				b.contact[b.collisionC+b.collisionCC].rad = rad;
				b.contact[b.collisionC+b.collisionCC].tangent = (1-t)*tangent1+t*tangent2;
				b.contact[b.collisionC+b.collisionCC].excess = -excess;
				b.collisionCC++;
			}
			return;
		}
		else if(b.touchArea.get(j).num < 3){
			final double rad = b.touchArea.get(j).rad;
			int dot_number = (int)(Math.round((rad+Math.PI/2)*b.dot.length/Math.PI/2)+b.dot.length)%b.dot.length;
			if(b.dot[(dot_number+1)%b.dot.length].p.minus(b.pos).dot(Point.polar(1.0, rad+Math.PI/2))
				> b.dot[(dot_number+b.dot.length-1)%b.dot.length].p.minus(b.pos).dot(Point.polar(1.0, rad+Math.PI/2))){
				while(b.dot[(dot_number+1)%b.dot.length].p.minus(b.pos).dot(Point.polar(1.0, rad+Math.PI/2))
					> (b.dot[dot_number].p.minus(b.pos)).dot(Point.polar(1.0, rad+Math.PI/2))){
					dot_number++;
					if(dot_number >= b.dot.length) dot_number = 0;
				}
			}else{
				while(b.dot[(dot_number+b.dot.length-1)%b.dot.length].p.minus(b.pos).dot(Point.polar(1.0, rad+Math.PI/2))
						> b.dot[dot_number].p.minus(b.pos).dot(Point.polar(1.0, rad+Math.PI/2))){
					dot_number--;
					if(dot_number <= -1) dot_number = b.dot.length-1;
				}
			}
			if(Point.polar(1.0, rad).cross(b.dot[dot_number].p.minus(b.touchArea.get(j).p)) > 0){
				final double div = b.dot[dot_number].p.minus(b.pos).cross(Point.polar(1.0, rad));
				b.contact[b.collisionC+b.collisionCC].p = Point.polar(b.dot[dot_number].p.cross(b.pos), rad).minus(b.dot[dot_number].p.minus(b.pos).scalar(b.touchArea.get(j).p.plus(Point.polar(1.0, rad)).cross(b.touchArea.get(j).p))).scalar(1.0/div);
				b.contact[b.collisionC+b.collisionCC].rad = rad+Math.PI/2; 
				b.contact[b.collisionC+b.collisionCC].tangent = rad+Math.PI;
				b.contact[b.collisionC+b.collisionCC].excess = Point.polar(1.0, rad).cross(b.dot[dot_number].p.minus(b.contact[b.collisionC+b.collisionCC].p));
				b.collisionCC++;
			}
			return;
		}
		else{
			System.out.println("@");
		}
	}
	
	protected void collideWithApex(Ball b, Point p){
		final double dist = b.pos.distance(p);
		final double rad = Math.atan2(b.pos.y-p.y+b.vel.y, -b.pos.x+p.x-b.vel.x);
		final Point velh = Point.polar(b.vel.cross(Point.polar(1.0, rad)), rad).right();
		Point velv = b.vel.minus(velh);
		
		final double excess = b.size-dist;
		b.pos.translate(Point.polar(-excess, -rad));
		velv.zoom(-e);
		b.vel = velh.plus(velv);
		
		b.contact[b.collisionC].p = p.copy();
		b.contact[b.collisionC].rad = p.minus(b.pos).arg();
		b.contact[b.collisionC].tangent = b.contact[b.collisionC].rad+Math.PI/2;
		b.collisionC++;
	}
	
	protected void collideWithSide(Ball b, Point p, double arg){
		final double drop = Point.polar(-1.0, arg).cross(b.pos.minus(p));
		final Point velh = Point.polar(b.vel.dot(Point.polar(1.0, -arg)), -arg);
		Point velv = b.vel.minus(velh);
		b.contact[b.collisionC].p = b.pos.plus(Point.polar(drop, arg).left());
		b.contact[b.collisionC].rad = p.minus(b.pos).arg();
		b.contact[b.collisionC].tangent = arg+Math.PI; 
		b.collisionC++;
		
		final double excess = b.size-drop;
		b.vel.translate(Math.sqrt(2*0.3*e*excess), Math.sqrt(2*0.3*e*excess));
		b.pos.translate(Point.polar(excess, arg).right());
		velv.zoom(-e);
		b.vel = velh.plus(velv);	
	}
}
