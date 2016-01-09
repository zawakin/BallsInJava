import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.print.PrinterJob;
import javafx.embed.swing.SwingFXUtils;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application{

	public static int count = 0;
	
	public static final int dt = 1000/60;
	private static TimerTask timerTask;
	private static Timer timer;
	
	private static boolean run;
	private static boolean paused;
	
	private static boolean genB, genR;
	
	public static MainWindow mw;
	private static Point mouse;
	private static LinkedList<KeyCode> key1, key2;
	
	public static Player player;
	public static LinkedList<Ball> ball;
	private static LinkedList<Figure> figure;
	
	public static int nowStage;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage){
		stage.setTitle("Rubber Ball Puzzle");
		stage.setWidth(900);
		stage.setHeight(800);
		stage.setMinWidth(800);
		stage.setMinHeight(700);
		
		VBox root = new VBox();
		Scene scene = new Scene(root);
		
		mw = new MainWindow(800, 512);
		root.getChildren().add(mw);
		
		mouse = new Point(0.0, 0.0);
		key1 = new LinkedList<KeyCode>();
		key2 = new LinkedList<KeyCode>();
		mw.setOnMouseMoved(event -> mouse.set(event.getSceneX(), event.getSceneY()));
		mw.setOnMousePressed(event -> {
			System.out.println("$");
		});
		mw.setOnMouseReleased(event -> {
			
		});
		scene.setOnKeyPressed(event -> {
			KeyCode code = event.getCode();
//			System.out.println("#"+key1.indexOf(code)+code.getName());
			if(key1.indexOf(code) == -1) key1.add(code);
			run = !(code == KeyCode.ESCAPE);
		});
		scene.setOnKeyReleased(event -> key1.remove(event.getCode()));

		ball = new LinkedList<Ball>();
		figure = new LinkedList<Figure>();
		player = new Player(new Point(mw.getWidth()/2, mw.getHeight()/2-15.0), new Point(), 15.0, ColorType.GREEN);
		
		nowStage = 0;
		
		run = true;
		paused = false;
		
		genB = genR = false;
		
		Field.field00(ball, figure);
		
		timerTask = new TimerTask(){
			@Override
			public void run(){
				loop();
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 0, dt);
		
		stage.setScene(scene);
		stage.show();

	}
	
	public class MainWindow extends Canvas{
		public GraphicsContext gc;
		
		MainWindow(double width, double height){
			super(width, height);
			gc = getGraphicsContext2D();
		}
		
		public void draw(){
			gc.clearRect(0.0, 0.0, getWidth(), getHeight());
			gc.setLineWidth(4.0);
			gc.setStroke(Color.rgb(180, 180, 180, 1.0));
			gc.strokeRect(0.0, 0.0, getWidth(), getHeight());
			
			for(int j = 0; j < figure.size(); j++){
				figure.get(j).draw(mw);
			}
			
			for(int i = 0; i < ball.size(); i++){
				ball.get(i).draw(mw);
			}
			
		}
	}
	
	public void loop(){
		if((key1.indexOf(KeyCode.SPACE) != -1) && (key2.indexOf(KeyCode.SPACE) == -1)) paused = !paused;
		if(!paused){
//			System.out.println("$"+count);
//			count++;
			keyControl();
			for(int i = 0; i < ball.size(); i++){
				ball.get(i).move();
			}
			System.out.println(player.vel.x);
			for(int i = 0; i < ball.size(); i++){
				for(int j = 0; j < figure.size(); j++){
					figure.get(j).collision01(ball.get(i), j);
				}
			}
			/*
			Point vec = new Point(mouse.x-player.pos.x, -mouse.y+player.pos.y);
			double dist = player.pos.distance(mouse)-player.size;
			double rad = Math.atan2(vec.y, vec.x);
			
			if(dist >= 380.0) dist = 380.0;
			*/
			mw.draw();
		}
	}
	
	
	
	public void keyControl(){
		double u = player.vel.x, v = player.vel.y;			
		for(int n = 0; n < key1.size(); n++){
			switch(key1.get(n)){
			case C:
				genB = true;
				break;
			case V:
				genR = true;
				break;
			case A:
				u = Math.max(u-0.3, -4.0);
				break;
			case D:
				u = Math.min(u+0.3, 4.0);
				break;
			case S:
				u = 0.0;
				break;
			case W:
				v = 7.0;
				break;
			default:
			}
		}
		if((key1.indexOf(KeyCode.A) == -1) && (key1.indexOf(KeyCode.D) == -1)) u*= 0.85;
		player.vel.set(u, v);		
	}
}
