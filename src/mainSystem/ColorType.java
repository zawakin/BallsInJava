package mainSystem;
import javafx.scene.paint.Color;

public enum ColorType{
	GREEN(0, 255, 0, 0.85),
	BLUE(0, 0, 255, 0.60),
	RED(255, 0, 0, 0.60),
	GRAY(85, 85, 85, 0.75);
	
	private int r, g, b;
	private double a;
	
	ColorType(int r, int g, int b, double a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color toGc(){
		return Color.rgb(r, g, b, a);
	}
}