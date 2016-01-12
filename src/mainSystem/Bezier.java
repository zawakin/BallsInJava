package mainSystem;

public class Bezier {
	public Point mid;
	public double mid_tan;
	public double mid_excess;
	public double arc1, arc2, arc_mid;
	
	public Bezier(){
		mid = new Point();
		mid_tan = 0.0;
		mid_excess = 0.0;
		arc1 = arc2 = arc_mid = 0.0;
	}
}
