import javafx.geometry.Point2D;

public class Player extends Ball{
	public boolean landing;
	
	Player(Point pos, Point vel, double size, ColorType color){
		super(pos, vel, size, color);
		landing = false;
	}
	
	public void shoot(double dist, double rad, ColorType color){
		final double ns = 13.0;
		final double u = dist/25*Math.cos(rad);
		final double v = dist/25*Math.sin(rad);
		final double nx = pos.x+(size+ns)*Math.cos(rad);
		final double ny = pos.y-(size+ns)*Math.sin(rad);	
		Main.ball.add(new Ball(new Point(nx, ny), new Point(u, v), ns, color));
		weight -= ns*ns;
		size = Math.sqrt(weight);
	}
}
