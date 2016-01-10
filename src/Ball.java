import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public class Ball {
	public static final double maxVel = 30.0;
	
	public Point pos;
	public Point vel;
	public double size;
	public double weight;
	public ColorType color;
	public int collisionC;
	public Contact[] contact;
	public LinkedList<TouchArea> touchArea;
	public LinkedList<Boolean> ballCollisionF;
	
	Ball(Point pos, Point vel, double size, ColorType color){
		this.pos = pos.copy();
		this.vel = vel.copy();
		this.size = size;
		weight = size*size;
		this.color = color;
		collisionC = 0;
		contact = new Contact[12];
		for(int n = 0; n < contact.length; n++){
			contact[n] = new Contact();
		}
		touchArea = new LinkedList<TouchArea>();
		ballCollisionF = new LinkedList<Boolean>();
		final int I = Main.ball.size();
		for(int i = 0; i < I; i++){
			Main.ball.get(i).touchArea.add(new TouchArea());
			Main.ball.get(i).ballCollisionF.add(false);
		}
		for(int i = 0; i < I+1; i++){
			touchArea.add(new TouchArea());
			ballCollisionF.add(false);
		}
	}
	
	public void draw(GraphicsContext gc){
		
		gc.setFill(color.toGc());
		gc.fillOval(pos.x-size, pos.y-size, 2*size, 2*size);
	}
		
	public void move(){
		vel.y -= 0.2;
		
		
		
		
		vel.x = Math.max(Math.min(vel.x, maxVel), -maxVel);
		vel.y = Math.max(Math.min(vel.y, maxVel), -maxVel);
		pos.x += vel.x;
		pos.y -= vel.y;
		
		collisionC = 0;
	}
	
	public void adjustPos(Ball b){
		final Point v = b.pos.minus(pos);
		final double dist = v.abs();
		double excess = size+b.size-dist;
		
		final double dist1 = (dist*dist+size*size-b.size*b.size)/(2.0*dist);
		final double dist2 = (dist*dist-size*size+b.size*b.size)/(2.0*dist);
		
		contact[collisionC].rad = v.arg();
		contact[collisionC].tangent = contact[collisionC].rad+Math.PI/2;
		contact[collisionC].excess = size-dist1;
		contact[collisionC].x = pos.x + Math.cos(contact[collisionC].rad)*dist1;
		contact[collisionC].y = pos.y + Math.sin(contact[collisionC].rad)*dist1;
		
		b.contact[b.collisionC].rad = v.arg()+Math.PI;
		b.contact[b.collisionC].tangent = b.contact[b.collisionC].rad+Math.PI/2;
		b.contact[b.collisionC].excess = b.size-dist2;
		b.contact[b.collisionC].x = b.pos.x + Math.cos(b.contact[b.collisionC].rad)*dist2;
		b.contact[b.collisionC].y = b.pos.y + Math.sin(b.contact[b.collisionC].rad)*dist2;
		collisionC++;
		b.collisionC++;
		
		v.zoom(1/dist);		
		excess /= size+b.size;
		pos.translate(v.scalar(-excess*b.size));
		b.pos.translate(v.scalar(excess*size));
	}
	
	public void collide(Ball b){
		double t;
		final Point v = new Point(b.pos.x-pos.x,-b.pos.y+pos.y);
		
		t = -v.dot(vel)/v.norm();
		final Point ar = vel.plus(v.scalar(t));
		
		t = -v.left().dot(vel)/v.norm();
		final Point am = vel.plus(v.left().scalar(t));
		
		t = -v.dot(b.vel)/v.norm();
		final Point br = b.vel.plus(v.scalar(t));
		
		t = -v.left().dot(b.vel)/v.norm();
		final Point bm = b.vel.plus(v.left().scalar(t));
		
		final double e = 0.9;
		final Point ad = am.scalar(weight).plus(bm.scalar(b.weight)).plus(bm.scalar(e*b.weight)).minus(am.scalar(e*b.weight)).scalar(1.0/(weight+b.weight));
		final Point bd = ad.minus(bm.minus(am).scalar(e));
		
		vel = ad.plus(ar);
		b.vel = bd.plus(br);
	}
	
	public void absorb(Ball b){
		final Point cp = pos.scalar(weight).plus(b.pos.scalar(b.weight)).scalar(1.0/(weight+b.weight));
		final Point cv = vel.scalar(weight).plus(b.vel.scalar(b.weight)).scalar(1.0/(weight+b.weight));		
		b.pos = cp.copy();
		b.vel = cv.copy();
		b.weight = b.weight+weight;
		b.size = Math.sqrt(b.weight);
		Main.ball.remove(this);
	}
}
