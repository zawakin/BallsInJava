package mainSystem;
import java.util.LinkedList;

public class Field {

	public static void field00(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ball.clear();
		figure.clear();
		ball.add(Main.player);
		figure.add(new Parallelogram(ball, new Point(0, 497), 800, 316, 0, 0));
		Main.nowStage = 0;
	}
	
	public static void field01(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ball.clear();
		figure.clear();
		ball.add(Main.player);
		figure.add(new Parallelogram(ball, new Point(0, 497), 800, 316, 0, 0));
		figure.add(new Parallelogram(ball, new Point(-300, 0), 320, 497, 0, 0));
		figure.add(new Parallelogram(ball, new Point(780, 0), 300, 437, 0, 0));
		figure.add(new Parallelogram(ball, new Point(400, 0), 50, 450, 0, 0));
		figure.add(new Parallelogram(ball, new Point(780, 437), 20, 60, 0, 0));
		figure.add(new Parallelogram(ball, new Point(600, 160), 150, 40, 0, 0));
		figure.add(new Parallelogram(ball, new Point(600, 0), 25, 160, 0, 0, ColorType.BLUE));
		figure.add(new Parallelogram(ball, new Point(720, 200), 30, 50, 0, 0));
		figure.add(new Parallelogram(ball, new Point(720, 250), 60, 30, 0, 0));
		figure.add(new Parallelogram(ball, new Point(755, 240), 20, 10, 0, 0, ColorType.RED));
		Main.nowStage = 1;
	}
	
	public static void test00(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ball.clear();
		figure.clear();
		ball.add(Main.player);
		figure.add(new Parallelogram(ball, new Point(0, 497), 800, 316, 0, 0));
		figure.add(new Parallelogram(ball, new Point(500, 370), 80, 80, 0, 0));
		Point[] p = {
				new Point(90, 400),
				new Point(80, 300),
				new Point(120, 200),
				new Point(210, 280),
				new Point(370, 260),
				new Point(300, 410),
				};
		figure.add(new Polygon(ball, p));
		Main.nowStage = -1;
	}
	
	public static void test01(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ball.clear();
		figure.clear();
		ball.add(Main.player);
		figure.add(new Parallelogram(ball, new Point(0, 497), 800, 316, 0, 0));
		
		Parallelogram fig0 = new Parallelogram(ball, new Point(500, 370), 80, 80, 0, 0, ColorType.RED);
		ColorButton0 cb0 = new ColorButton0(ball, new Point(300, 200), 15);
		cb0.associate(fig0);
		figure.add(fig0);
		figure.add(cb0);
		
		Parallelogram fig1 = new Parallelogram(ball, new Point(200, 120), 40, 30, 0, 0, ColorType.GRAY);
		ColorButton1 cb1 = new ColorButton1(ball, new Point(100, 340), 15);
		cb1.associate(fig1);
		figure.add(fig1);
		figure.add(cb1);

		Parallelogram fig2 = new Parallelogram(ball, new Point(300, 420), 40, 30, 0, 0, ColorType.GREEN);
		ColorButton2 cb2 = new ColorButton2(ball, new Point(500, 240), 15);
		cb2.associate(fig2);
		figure.add(fig2);
		figure.add(cb2);

		Main.nowStage = -2;
	}

}
