package mainSystem;

public class Point {
	public double x, y;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	public Point(){
		this(0.0, 0.0);
	}
	
	public Point copy(){
		return new Point(x, y);
	}
	
	public static Point polar(double r, double arg){
		return new Point(r*Math.cos(arg), r*Math.sin(arg));
	}
	
	public void set(double x, double y){
		this.x = x;
		this.y = y;
	}

	public void translate(Point p){
		x += p.x;
		y += p.y;
	}
	
	public void translate(double dx, double dy){
		x += dx;
		y += dy;
	}
	
	public void zoom(double a){
		x *= a;
		y *= a;
	}
	
	public double square(){
		return x*x+y*y;
	}

	public double abs(){
		return Math.sqrt(x*x+y*y);
	}
	
	public double distance(Point p){
		return Math.sqrt((x-p.x)*(x-p.x)+(y-p.y)*(y-p.y));
	}
	
	public double arg(){
		return Math.atan2(y, x);
	}

	public Point left(){
		return new Point(-y, x);
	}

	public Point right(){
		return new Point(y, -x);
	}
	
	public Point inv(){
		return new Point(-x, -y);
	}
	
	public Point scalar(double k){
		return new Point(k*x, k*y);
	}
	
	public Point plus(Point p){
		return new Point(x+p.x, y+p.y);
	}
	
	public Point minus(Point p){
		return new Point(x-p.x, y-p.y);
	}
	
	public double dot(Point p){
		return x*p.x+y*p.y;
	}
	
	public double cross(Point p){
		return x*p.y-y*p.x;
	}
}