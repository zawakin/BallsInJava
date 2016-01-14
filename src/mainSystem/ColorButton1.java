package mainSystem;
import java.util.LinkedList;

public class ColorButton1 extends Button{
	
	public ColorButton1(LinkedList<Ball> ball, Point pos, double size, ColorType color){
		super(ball, pos, size, color);
		endTime = 60;
	}
	public ColorButton1(LinkedList<Ball> ball, Point pos, double size){
		this(ball, pos, size, ColorType.GRAY);
	}	
	
	protected void work(Ball b){
		color = b.color;
		for(int i = 0; i < figs.size(); i++){
			figs.get(i).color = color;
		}
	}
}