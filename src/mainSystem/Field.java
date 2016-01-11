package mainSystem;
import java.util.LinkedList;

public class Field {

	public static void field00(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ball.clear();
		figure.clear();
		ball.add(Main.player);
		figure.add(new Figure(ball, new Point(0, 497), 800, 316, 0, 0, ColorType.GRAY));
		Main.nowStage = 0;
	}
	
	public static void field01(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ball.clear();
		figure.clear();
		ball.add(Main.player);
		figure.add(new Figure(ball, new Point(0, 497), 800, 316, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(-300, 0), 320, 497, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(780, 0), 300, 437, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(400, 0), 50, 450, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(780, 437), 20, 60, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(600, 160), 150, 40, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(600, 0), 25, 160, 0, 0, ColorType.BLUE));
		figure.add(new Figure(ball, new Point(720, 200), 30, 50, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(720, 250), 60, 30, 0, 0, ColorType.GRAY));
		figure.add(new Figure(ball, new Point(755, 240), 20, 10, 0, 0, ColorType.RED));
		Main.nowStage = 1;
	}
}