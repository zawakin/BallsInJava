package mainSystem;

import java.util.LinkedList;

public class ColorButton1 extends Button{
	
	public ColorButton1(LinkedList<Ball> ball, Point pos, double size, ColorType color){
		super(ball, pos, size, color);
	}
	
	protected void work(Ball b){
		for(int i = 0; i < figs.size(); i++){
			figs.get(i).color = b.color;
		}
	}
}