package mainSystem;

import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public class Polygon extends Figure{
	private final int N;
	private Point[] p;
	private Point[] vh, vv;
	private double[] arg;
	
	public Polygon(LinkedList<Ball> ball, Point[] p, ColorType color){
		super(ball, color);
		N = p.length;
		this.p = new Point[N];
		vh = new Point[N];
		vv = new Point[N];
		arg = new double[N];
		for(int n = 0; n < N; n++){
			this.p[n] = p[n].copy();
			vh[n] = p[(n+1)%N].minus(p[n]);
			arg[n] = vh[n].arg();
			vh[n].zoom(1.0/vh[n].abs());
			vv[n] = vh[n].right();
		}
	}
	
	public void draw(GraphicsContext gc){
		double[] xPoints = new double[N];
		double[] yPoints = new double[N];
		for(int n = 0; n < N; n++){
			xPoints[n] = p[n].x;
			yPoints[n] = p[n].y;
		}
		gc.setFill(color.toGc());
		gc.fillPolygon(xPoints, yPoints, N);		
	}
	
	public void collision01(Ball b, int j){
		int n;
		for(n = 0; n < N; n++){
			Point d0 = b.pos.minus(p[n]);
			if(vv[(n-1+N)%N].cross(d0) > 0 && vv[n].cross(d0) < 0){
				b.touchArea.get(j).p = p[n].copy();
				b.touchArea.get(j).num = 1;
				System.out.println("$");
				break;
			}else if(vh[(n-1+N)%N].cross(vh[n]) < 0 && vv[(n-1+N)%N].cross(d0) < 0 && vv[n].cross(d0) > 0){
				b.touchArea.get(j).p = p[n].copy();
				b.touchArea.get(j).num = 2;
				System.out.println("%");
				break;
			}
		}
		if(n == N){
			for(n = 0; n < N; n++){
				Point d0 = b.pos.minus(p[n]);
				Point d1 = b.pos.minus(p[(n+1)%N]);
				if(vv[n].cross(d0) > 0 && vv[n].cross(d1) < 0 && vh[n].cross(d0) < 0){
					b.touchArea.get(j).p = p[n].copy();
					b.touchArea.get(j).rad = arg[n];
					b.touchArea.get(j).num = 3;
					System.out.println("#");
				break;
				}
			}
		}
		if(n == N) b.touchArea.get(j).num = 4;
		switch(b.touchArea.get(j).num){
		case 1:
			b.touchArea.get(j).dist = b.pos.distance(b.touchArea.get(j).p);
			if(b.touchArea.get(j).dist <= b.size) collideWithApex(b, p[n]);
			else if(b.touchArea.get(j).dist <= b.size*1.5) return;
			break;
		case 2:
			final double drop0 = Point.polar(-1.0, arg[(n-1+N)%N]).cross(b.pos.minus(p[(n-1+N)%N]));
			if(drop0 <= b.size) collideWithSide(b, p[(n-1+N)%N], arg[(n-1+N)%N]);
			// no break
		case 3:
			final double drop1 = Point.polar(-1.0, arg[n]).cross(b.pos.minus(p[n]));
			if(drop1 <= b.size) collideWithSide(b, p[n], arg[n]);
			else if(drop1 <= b.size*1.5) return;
			break;
		default:
			System.out.println("?");			
		}
		b.touchArea.get(j).num = 0;
	}
}
