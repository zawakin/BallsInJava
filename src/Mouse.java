
public class Mouse extends Point{
	public boolean prepR, prepL;
	public boolean shootR, shootL;
	
	Mouse(){
		prepR = prepL = false;
		shootR = shootL = false;
	}
	
	public void draw(Main.MainWindow mw){
		mw.gc.beginPath();
		mw.gc.moveTo(x, y-15);
		mw.gc.lineTo(x+15, y);
		mw.gc.lineTo(x, y+15);
		mw.gc.lineTo(x-15, y);
		mw.gc.closePath();
		mw.gc.arc(x, y, 10, 10, 0, 360);
		mw.gc.closePath();
		mw.gc.arc(x, y, 4, 4, 0, 360);
		mw.gc.closePath();
		mw.gc.setFill(ColorType.GREEN.toGc());
		mw.gc.fill();
	}
}
