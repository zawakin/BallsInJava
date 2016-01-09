import javafx.geometry.Point2D;

public class Player extends Ball{
	public boolean landing;
	
	Player(Point pos, Point vel, double size, ColorType color){
		super(pos, vel, size, color);
		landing = false;
	}
	
	public void shoot(){
		
	}
}
