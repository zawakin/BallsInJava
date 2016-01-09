import java.util.LinkedList;

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
		this.pos = new Point(pos.x, pos.y);
		this.vel = new Point(vel.x, vel.y);
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
	
	public void draw(Main.MainWindow mw){
		
		mw.gc.setFill(color.toGc());
		mw.gc.fillOval(pos.x-size, pos.y-size, 2*size, 2*size);
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
		double vx = b.pos.x-pos.x;
		double vy = b.pos.y-pos.y;
		double dist = Math.sqrt(vx*vx+vy*vy);
		double excess = size+b.size-dist;
		
		final double dist1 = (dist*dist+size*size-b.size*b.size)/(2.0*dist);
		final double dist2 = (dist*dist-size*size+b.size*b.size)/(2.0*dist);

		contact[collisionC].rad = Math.atan2(vy, vx);
		contact[collisionC].tangent = contact[collisionC].rad+Math.PI/2;
		contact[collisionC].excess = size-dist1;
		contact[collisionC].x = pos.x + Math.cos(contact[collisionC].rad)*dist1;
		contact[collisionC].y = pos.y + Math.sin(contact[collisionC].rad)*dist1;
		
		b.contact[b.collisionC].rad = Math.atan2(vy, vx)+Math.PI;
		b.contact[b.collisionC].tangent = b.contact[b.collisionC].rad+Math.PI/2;
		b.contact[b.collisionC].excess = b.size-dist2;
		b.contact[b.collisionC].x = b.pos.x + Math.cos(b.contact[b.collisionC].rad)*dist2;
		b.contact[b.collisionC].y = b.pos.y + Math.sin(b.contact[b.collisionC].rad)*dist2;
		collisionC++;
		b.collisionC++;
		
		vx /= dist;
		vy /= dist;
		
		excess /= size+b.size;
		pos.x -= vx*excess*b.size;
		pos.y -= vy*excess*b.size;
		b.pos.x += vx*excess*size;
		b.pos.y += vy*excess*size;
	}
	
	public void collide(Ball b){
		double t;
		final double vx = (b.pos.x-pos.x);
		final double vy = -(b.pos.y-pos.y);
		
		t = -(vx*vel.x+vy*vel.y)/(vx*vx+vy*vy);
		final double arx = vel.x+vx*t;
		final double ary = vel.y+vy*t;
		
		t = -(-vy*vel.x+vx*vel.y)/(vx*vx+vy*vy);
		final double amx = vel.x-vy*t;
		final double amy = vel.y+vx*t;
		
		t = -(vx*b.vel.x+vy*b.vel.y)/(vx*vx+vy*vy);
		final double brx = b.vel.x+vx*t;
		final double bry = b.vel.y+vy*t;
		
		t = -(-vy*b.vel.x+vx*b.vel.y)/(vx*vx+vy*vy);
		final double bmx = b.vel.x-vy*t;
		final double bmy = b.vel.y+vx*t;
		
		final double e = 0.9;
		final double adx = (weight*amx+b.weight*bmx+bmx*e*b.weight-amx*e*b.weight)/(weight+b.weight);
		final double bdx = -e*(bmx-amx)+adx;
		final double ady = (weight*amx+b.weight*bmy+bmy*e*b.weight-amy*e*b.weight)/(weight+b.weight);
		final double bdy = -e*(bmy-amy)+ady;
		
		vel.x = adx+arx;
		vel.y = ady+ary;
		b.vel.x = bdx+brx;
		b.vel.y = bdy+bry;
	}
	
	public void absorb(Ball b){
		System.out.println(1);
		Point cp = new Point();
		cp.x = (weight*pos.x+b.weight*b.pos.x)/(weight+b.weight);
		cp.y = (weight*pos.y+b.weight*b.pos.y)/(weight+b.weight);
		
		Point cv = new Point();
		cv.x = (weight*vel.x+b.weight*b.vel.x)/(weight+b.weight);
		cv.y = (weight*vel.y+b.weight*b.vel.y)/(weight+b.weight);
		
		b.vel.x = cv.x;
		b.vel.y = cv.y;
		b.weight = b.weight+weight;
		b.size = Math.sqrt(b.weight);
		Main.ball.remove(this);
	}
}
