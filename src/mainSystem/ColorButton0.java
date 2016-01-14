package mainSystem;
import java.util.LinkedList;

public class ColorButton0 extends Button{
	
	public ColorButton0(LinkedList<Ball> ball, Point pos, double size, ColorType color){
		super(ball, pos, size, color);
		endTime = 60;
	}
	public ColorButton0(LinkedList<Ball> ball, Point pos, double size){
		this(ball, pos, size, ColorType.GRAY);
	}
	
	protected void work(Ball b){
		for(int i = 0; i < figs.size(); i++){
			if(figs.get(i).color == ColorType.BLUE) figs.get(i).color = ColorType.RED;
			else if(figs.get(i).color == ColorType.RED) figs.get(i).color = ColorType.BLUE;
		}
	}
}
