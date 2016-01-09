
public class Point {
	public double x, y;
	Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	Point(){
		this(0.0, 0.0);
	}
	
	public void set(double x, double y){
		this.x = x;
		this.y = y;		
	}
	
	public double distance(Point p){
		return Math.sqrt((x-p.x)*(x-p.x)+(y-p.y)*(y-p.y));
	}
}
