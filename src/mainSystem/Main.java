package mainSystem;
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
import javafx.scene.Cursor;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
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

	public static final double width = 800.0;
	public static final double height = 512.0;
	
	public static final int dt = 1000/60;
	
	private static TimerTask timerTask;
	private static Timer timer;
	
	private static boolean run;
	private static boolean paused;
	
	private static boolean genB, genR;
	
	private static MainWindow mw;
	private static Mouse mouse;
	private static LinkedList<KeyCode> key1, key2;
	
	public static Player player;
	private static LinkedList<Ball> ball;
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
		stage.setOnCloseRequest(event -> timer.cancel());
		
		VBox root = new VBox();
		Scene scene = new Scene(root);
		
		mw = new MainWindow(width, height);
		mw.setCursor(Cursor.NONE);
		root.getChildren().add(mw);
		
		mouse = new Mouse();
		key1 = new LinkedList<KeyCode>();
		key2 = new LinkedList<KeyCode>();
	

		ball = new LinkedList<Ball>();
		figure = new LinkedList<Figure>();
		player = new Player(ball, figure, new Point(mw.getWidth()/2, mw.getHeight()/2-15.0), new Point(), 15.0, ColorType.GREEN);
		
		nowStage = 0;
		
		run = true;
		paused = false;
		
		genB = genR = false;
		
		Field.field01(ball, figure);
		
		mw.setOnMouseMoved(event -> mouse.set(event.getSceneX(), event.getSceneY()));
		mw.setOnMouseDragged(event -> mouse.set(event.getSceneX(), event.getSceneY()));
		mw.setOnMousePressed(event -> {
			System.out.println("$");
			final MouseButton button = event.getButton();
			if(button == MouseButton.PRIMARY){
				if(!player.prepR){
					player.prepL = true;
				}else{
					player.prepR = false;
				}
			}
			if(button == MouseButton.SECONDARY){
				if(!player.prepL){
					player.prepR = true;
				}else{
					player.prepL = false;	
				}
			}
		});
		mw.setOnMouseReleased(event -> {
			System.out.println("#");
			final MouseButton button = event.getButton();
			if(player.prepL && button == MouseButton.PRIMARY) player.shootL = true;
			if(player.prepR && button == MouseButton.SECONDARY) player.shootR = true;
		});
		scene.setOnKeyPressed(event -> {
			final KeyCode code = event.getCode();
			if(key1.indexOf(code) == -1) key1.add(code);
			run = !(code == KeyCode.ESCAPE);
			if(code == KeyCode.F5){
				timer.cancel();
				start(stage);
			}
		});
		scene.setOnKeyReleased(event -> key1.remove(event.getCode()));
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
		private GraphicsContext gc;
		
		MainWindow(double width, double height){
			super(width, height);
			gc = getGraphicsContext2D();
		}
		
		public void draw(){
			gc.clearRect(0.0, 0.0, getWidth(), getHeight());
			gc.setLineWidth(4.0);
			gc.setStroke(Color.rgb(180, 180, 180, 1.0));
			gc.strokeRect(0.0, 0.0, getWidth(), getHeight());
			for(int i = 0; i < figure.size(); i++){
				figure.get(i).draw(gc);
			}
			for(int i = 0; i < ball.size(); i++){
				ball.get(i).draw(gc);
			}
			mouse.draw(gc);
		}
	}
	
	public void loop(){
		if(keyPressed(key1, KeyCode.SPACE) && !keyPressed(key2, KeyCode.SPACE)) paused = !paused;
		if(!paused){
			keyControl();
			if(genR){
				ball.add(new Ball(ball, figure, mouse, new Point(), 10.0, ColorType.RED));
				genR = false;
			}
			if(genB){
				ball.add(new Ball(ball, figure, mouse, new Point(), 10.0, ColorType.BLUE));
				genB = false;
			}
						
			if(player.shootL){
				if(player.weight > 300.0) player.shoot(ball, figure, ColorType.BLUE);
				player.prepL = player.shootL = false;
			}
			if(player.shootR){
				if(player.weight > 300.0) player.shoot(ball, figure, ColorType.RED);
				player.prepR = player.shootR = false;
			}
			
			for(int i = 0; i < ball.size(); i++){
				for(int j = i+1; j < ball.size(); j++){
					if((ball.get(i).pos.distance(ball.get(j).pos) < ball.get(j).size+ball.get(i).size)
							&& (((ball.get(i).color == ColorType.BLUE && ball.get(j).color == ColorType.RED)
							|| (ball.get(i).color == ColorType.RED && ball.get(j).color == ColorType.BLUE))
							|| i == 0 && player.size < ball.get(j).size+1.0)){
						ball.get(j).adjustPos(ball.get(i));
						ball.get(j).collide(ball.get(i));
						ball.get(i).ballCollisionF.set(j, true);
						ball.get(j).ballCollisionF.set(i, true);
					}
					else if(ball.get(i).pos.distance(ball.get(j).pos) < ball.get(j).size+ball.get(i).size-2.0
							&& !((ball.get(i).color == ColorType.BLUE && ball.get(j).color == ColorType.RED)
							|| (ball.get(i).color == ColorType.RED && ball.get(j).color == ColorType.BLUE))){
						if(i == 0 && player.size < ball.get(j).size+1.0) break;
						ball.get(j).absorb(ball, ball.get(i));
					}
				}
			}
			
			for(int i = 0; i < ball.size(); i++){
				ball.get(i).move(ball);
			}
			for(int i = 0; i < ball.size(); i++){
				for(int j = 0; j < figure.size(); j++){
					if(ball.get(i).color == figure.get(j).color) continue;
					figure.get(j).collision01(ball.get(i), j);
				}
			}
			player.culcToAim(mouse, keyPressed(key1, KeyCode.SHIFT));
		}
		if(keyPressed(key1, KeyCode.W) && !keyPressed(key2, KeyCode.W)) player.toJump = true;
		for(int i = 0; i < player.collisionC; i++){
			final double i_rad = (player.contact[i].tangent+2*Math.PI)%(2*Math.PI);
			/*
			player.landing = i_rad >= 3/4*Math.PI && i_rad <= 5/4*Math.PI
					&& keyPressed(key1, KeyCode.W) && !keyPressed(key2, KeyCode.W);
			*/		
			if(i_rad >= 3/4*Math.PI && i_rad <= 5/4*Math.PI && player.toJump) player.landing = true;
		}
		
		mw.draw();
		key2 = (LinkedList<KeyCode>)key1.clone();
		
	}
	
	public boolean keyPressed(LinkedList<KeyCode> key, KeyCode code){
		return key.indexOf(code) != -1;
	}
	
	public void keyControl(){
		double u = player.vel.x, v = player.vel.y;
		for(int n = 0; n < key1.size(); n++){
			switch(key1.get(n)){
			case C:
				genB = true;
				genR = false;
				break;
			case V:
				genR = true;
				genB = false;
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
				if(player.landing){
					v = 7.0;
					player.landing = player.toJump = false;
				}
				break;
			default:
			}
		}
		if(!keyPressed(key1, KeyCode.A) && !keyPressed(key1, KeyCode.D)) u*= 0.85;
		player.vel.set(u, v);
	}
}
