import java.util.LinkedList;

public class Field {

	public static void field00(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ball.clear();
		figure.clear();
		ball.add(Main.player);
		figure.add(new Figure(0.0, 497.0, 800.0, 316.0, 0.0, 0.0, ColorType.GRAY));
		Main.nowStage = 0;
	}
}
