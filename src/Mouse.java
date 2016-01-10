import javafx.scene.canvas.GraphicsContext;

public class Mouse extends Point{
	
	public void draw(GraphicsContext gc){
		gc.beginPath();
		gc.moveTo(x, y-15);
		gc.lineTo(x+15, y);
		gc.lineTo(x, y+15);
		gc.lineTo(x-15, y);
		gc.closePath();
		gc.arc(x, y, 10, 10, 0, 360);
		gc.closePath();
		gc.arc(x, y, 4, 4, 0, 360);
		gc.closePath();
		gc.setFill(ColorType.GREEN.toGc());
		gc.fill();
	}
}
