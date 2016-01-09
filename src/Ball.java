import java.util.LinkedList;

public class Ball {
	public static final double maxVel = 30.0;
	
	public Point pos;
	public Point vel;
	public double size;
	public double weight;
	private ColorType color;
	public int collisionC;
	public Contact[] contact;
	public LinkedList<TouchArea> touchArea;
	
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
		final int I = Main.ball.size();
		for(int i = 0; i < I; i++){
			Main.ball.get(i).touchArea.add(new TouchArea());
		}
		for(int i = 0; i < I+1; i++){
			touchArea.add(new TouchArea());
		}
	}
	
	public void draw(Main.MainWindow mw){
		
		mw.gc.setFill(color.toGc());
		mw.gc.fillOval(pos.x, pos.y, size, size);
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
	
}
