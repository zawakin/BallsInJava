package mainSystem;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public class Ball {
	public static final double maxVel = 30.0;
	
	public Point pos;
	public Point vel;
	public Point prevPos;
	public double size;
	public double weight;
	public ColorType color;
	public int collisionC, collisionCC;
	public Contact[] contact;
	public LinkedList<TouchArea> touchArea;
	public LinkedList<Boolean> ballCollisionF;
	public Dot[] dot;
	public double[] rad_gap;
	public int[] gap_num;
	public Bezier[] bezier;
	public boolean distorted;
	public boolean broken;
	
	public Ball(LinkedList<Ball> ball, LinkedList<Figure> figure, Point pos, Point vel, double size, ColorType color){
		this.pos = pos.copy();
		this.vel = vel.copy();
		prevPos = new Point();
		this.size = size;
		weight = size*size;
		this.color = color;
		collisionC = collisionCC = 0;
		distorted = broken = false;
		contact = new Contact[12];
		rad_gap = new double[12];
		gap_num = new int[12];
		bezier = new Bezier[12];
		for(int n = 0; n < contact.length; n++){
			contact[n] = new Contact();
			rad_gap[n] = 0.0;
			gap_num[n] = 0;
			bezier[n] = new Bezier();
		}
		dot = new Dot[64];
		for(int n = 0; n < dot.length; n++){
			dot[n] = new Dot();
		}
		touchArea = new LinkedList<TouchArea>();
		ballCollisionF = new LinkedList<Boolean>();
		final int I = ball.size();
		final int J = figure.size();
		for(int i = 0; i < I; i++){
			ball.get(i).ballCollisionF.add(false);
		}
		for(int i = 0; i < I+1; i++){
			ballCollisionF.add(false);
		}
		for(int j = 0; j < J; j++){
			touchArea.add(new TouchArea());
		}
	}
	
	public void resetLists(LinkedList<Ball> ball, LinkedList<Figure> figure){
		ballCollisionF.clear();
		touchArea.clear();
		for(int i = 0; i < ball.size(); i++){
			ballCollisionF.add(false);
		}
		for(int j = 0; j < figure.size(); j++){
			touchArea.add(new TouchArea());
		}		
	}
	
	public void draw(GraphicsContext gc){
		if(collisionC+collisionCC >= 2){
			if(collisionCC > 0) culcBezier();
			gc.beginPath();
			gc.moveTo(contact[0].p.x, contact[0].p.y);
			for(int j = 0; j < collisionC+collisionCC; j++){
				gc.bezierCurveTo(contact[j].p.x+bezier[j].arc1*Math.cos(contact[j].tangent), contact[j].p.y+bezier[j].arc1*Math.sin(contact[j].tangent),
								  bezier[j].mid.x-bezier[j].arc_mid*Math.cos(bezier[j].mid_tan), bezier[j].mid.y-bezier[j].arc_mid*Math.sin(bezier[j].mid_tan),
								  bezier[j].mid.x, bezier[j].mid.y);
				
				gc.bezierCurveTo(bezier[j].mid.x+bezier[j].arc_mid*Math.cos(bezier[j].mid_tan), bezier[j].mid.y+bezier[j].arc_mid*Math.sin(bezier[j].mid_tan),
								  contact[(j+1)%(collisionC+collisionCC)].p.x-bezier[j].arc2* Math.cos(contact[(j+1)%(collisionC+collisionCC)].tangent), contact[(j+1)%(collisionC+collisionCC)].p.y-bezier[j].arc2*Math.sin(contact[(j+1)%(collisionC+collisionCC)].tangent),
								  contact[(j+1)%(collisionC+collisionCC)].p.x, contact[(j+1)%(collisionC+collisionCC)].p.y);
			}
			gc.closePath();
			gc.setFill(color.toGc());
			gc.fill();
			distorted = true;
		}
		if(distorted) return;
		gc.setFill(color.toGc());
		gc.fillOval(pos.x-size, pos.y-size, 2*size, 2*size);
	}
		
	public void move(LinkedList<Ball> ball){
		if(!distorted) vel.y -= 0.2;
		else vel.zoom(0.85);
		vel.x = Math.max(Math.min(vel.x, maxVel), -maxVel);
		vel.y = Math.max(Math.min(vel.y, maxVel), -maxVel);
		pos.x += vel.x;
		pos.y -= vel.y;
		collisionC = collisionCC = 0;
		distorted = false;
		for(int j = 0; j < contact.length; j++){
			contact[j].p.set(0.0, 0.0);
		}
		for(int j = 0; j < dot.length; j++){
			dot[j].p = pos.plus(Point.polar(size, j*2*Math.PI/dot.length));
		}
		prevPos = pos.copy();
		for(int j = 0; j < touchArea.size(); j++){
			touchArea.get(j).num = 0;
		}
		if(pos.y > 2.0*Main.height){
			System.out.println("&");
			ball.remove(this);
		}
	}
	
	public void scatter(LinkedList<Ball> ball, LinkedList<Figure> figure){
		final int amount = (int)(Math.random()*3+7);
		for(int j = 0; j < amount; j++){
			final double size = Math.random()*3+Math.sqrt(weight/amount)-2;
			final Point v = new Point(Math.random()*12-6, Math.random()*12);
			final ColorType col = Math.random() >= 0.5? ColorType.BLUE: ColorType.RED;
			Ball b = new Ball(ball, figure, pos, v, size, col);
			b.broken = true;
			ball.add(b);
		}
		ball.remove(this);
	}
	
	public void adjustPos(Ball b){
		final Point v = b.pos.minus(pos);
		final double dist = v.abs();
		double excess = size+b.size-dist;
		final double dist1 = (dist*dist+size*size-b.size*b.size)/(2.0*dist);
		final double dist2 = (dist*dist-size*size+b.size*b.size)/(2.0*dist);
		
		contact[collisionC].rad = v.arg();
		contact[collisionC].tangent = contact[collisionC].rad+Math.PI/2;
		contact[collisionC].excess = size-dist1;
		contact[collisionC].p = pos.plus(Point.polar(dist1, contact[collisionC].rad));
		
		b.contact[b.collisionC].rad = v.arg()+Math.PI;
		b.contact[b.collisionC].tangent = b.contact[b.collisionC].rad+Math.PI/2;
		b.contact[b.collisionC].excess = b.size-dist2;
		b.contact[b.collisionC].p = b.pos.plus(Point.polar(dist2, b.contact[b.collisionC].rad));
		collisionC++;
		b.collisionC++;
		
		v.zoom(1.0/dist);		
		excess /= size+b.size;
		pos.translate(v.scalar(-excess*b.size));
		b.pos.translate(v.scalar(excess*size));
	}
	
	private void collide(Ball b){
		double t;
		final Point v = new Point(b.pos.x-pos.x,-b.pos.y+pos.y);
		
		t = -v.dot(vel)/v.square();
		final Point ar = vel.plus(v.scalar(t));
		
		t = -v.left().dot(vel)/v.square();
		final Point am = vel.plus(v.left().scalar(t));
		
		t = -v.dot(b.vel)/v.square();
		final Point br = b.vel.plus(v.scalar(t));
		
		t = -v.left().dot(b.vel)/v.square();
		final Point bm = b.vel.plus(v.left().scalar(t));
		
		final double e = 0.9;
		final Point ad = am.scalar(weight).plus(bm.scalar(b.weight)).plus(bm.scalar(e*b.weight)).minus(am.scalar(e*b.weight)).scalar(1.0/(weight+b.weight));
		final Point bd = ad.minus(bm.minus(am).scalar(e));
		
		vel = ad.plus(ar);
		b.vel = bd.plus(br);
	}
	
	private void absorb(LinkedList<Ball> ball, Ball b){
		final Point cp = pos.scalar(weight).plus(b.pos.scalar(b.weight)).scalar(1.0/(weight+b.weight));
		final Point cv = vel.scalar(weight).plus(b.vel.scalar(b.weight)).scalar(1.0/(weight+b.weight));		
		b.pos = cp.copy();
		b.vel = cv.copy();
		b.weight = b.weight+weight;
		b.size = Math.sqrt(b.weight);
		ball.remove(this);
	}
	
	public static void collideAndAbsorb(LinkedList<Ball> ball){
		for(int i = 0; i < ball.size(); i++){
			for(int j = i+1; j < ball.size(); j++){
				if(ball.get(i).broken || ball.get(j).broken) continue;
				if((ball.get(i).pos.distance(ball.get(j).pos) < ball.get(j).size+ball.get(i).size)
						&& (((ball.get(i).color == ColorType.BLUE && ball.get(j).color == ColorType.RED)
						|| (ball.get(i).color == ColorType.RED && ball.get(j).color == ColorType.BLUE))
						|| i == 0 && Main.player.size < ball.get(j).size+1.0)){
					ball.get(j).adjustPos(ball.get(i));
					ball.get(j).collide(ball.get(i));
					ball.get(i).ballCollisionF.set(j, true);
					ball.get(j).ballCollisionF.set(i, true);
				}else if(ball.get(i).pos.distance(ball.get(j).pos) < ball.get(j).size+ball.get(i).size-2.0
						&& !((ball.get(i).color == ColorType.BLUE && ball.get(j).color == ColorType.RED)
						|| (ball.get(i).color == ColorType.RED && ball.get(j).color == ColorType.BLUE))){
					if(i == 0 && Main.player.size < ball.get(j).size+1.0) break;
					ball.get(j).absorb(ball, ball.get(i));
				}
			}
		}
	}
	
	public static void checkCollision(LinkedList<Ball> ball, LinkedList<Figure> figure){
		for(int i = 0; i < ball.size(); i++){
			if(!ball.get(i).distorted) continue;
			for(int j = 0; j < figure.size(); j++){
				if(ball.get(i).touchArea.get(j).num != 0) figure.get(j).collision02(ball.get(i), j);
			}
			for(int j = 0; j < ball.size(); j++){
				if(ball.get(j).broken || ball.get(i).pos.distance(ball.get(j).pos) >= (ball.get(i).size+ball.get(j).size)*2) continue;
				if(ball.get(i).color == ball.get(j).color && i!=j){
					if(!ball.get(j).distorted) ball.get(i).absorption01(ball, ball.get(j));
					else if(i < j)             ball.get(i).absorption02(ball, ball.get(j));
				}
				else if(!ball.get(i).ballCollisionF.get(j)){
					if(!ball.get(j).distorted) ball.get(i).collision01(ball.get(j));
					else if(i < j)             ball.get(i).collision02(ball.get(j));
				}
			}
		}
	}
	
	private void culcBezier(){
		Point power = new Point();
		for(int j = collisionC; j < collisionC+collisionCC; j++){
			if(contact[j].excess < 0) continue;
			power.translate(Point.polar(-contact[j].excess*contact[j].excess, contact[j].rad));
			contact[j].excess = size-pos.distance(contact[j].p);
		}
		pos.translate(power.scalar(1.0/size*2));
		
		for(int j = 0; j < collisionC+collisionCC; j++){
			for(int k = collisionC+collisionCC-1; k > j; k--){
				if(contact[k-1].rad > contact[k].rad){
					final Contact temp = contact[k];
					contact[k] = contact[k-1];
					contact[k-1] = temp;
				}
			}
		}

		for(int j = 0; j < collisionC+collisionCC; j++){
			rad_gap[j] = (contact[(j+1)%(collisionC+collisionCC)].rad-contact[j].rad+2*Math.PI)%(2*Math.PI);
			gap_num[j] = (int)(Math.round(contact[j].rad*dot.length/Math.PI/2)+dot.length)%dot.length;
		}
		
		for(int j = 0; j < collisionC+collisionCC; j++){
			bezier[j].arc1 = 4.0/3*size*size/(size-contact[j].excess)*Math.tan(rad_gap[j]/4);
			bezier[j].arc2 = 4.0/3*size*size/(size-contact[(j+1)%(collisionC+collisionCC)].excess)*Math.tan(rad_gap[j]/4);

			final double x1 = contact[j].p.x;
			final double x2 = contact[j].p.x +bezier[j].arc1*Math.cos(contact[j].tangent);
			final double x3 = contact[(j+1)%(collisionC+collisionCC)].p.x-bezier[j].arc2*Math.cos(contact[(j+1)%(collisionC+collisionCC)].tangent);
			final double x4 = contact[(j+1)%(collisionC+collisionCC)].p.x;
			final double y1 = contact[j].p.y;
			final double y2 = contact[j].p.y +bezier[j].arc1*Math.sin(contact[j].tangent);
			final double y3 = contact[(j+1)%(collisionC+collisionCC)].p.y-bezier[j].arc2*Math.sin(contact[(j+1)%(collisionC+collisionCC)].tangent);
			final double y4 = contact[(j+1)%(collisionC+collisionCC)].p.y;
			
			bezier[j].mid.x = 1.0/8*x1+3.0/8*x2+3.0/8*x3+1.0/8*x4;
			bezier[j].mid.y = 1.0/8*y1+3.0/8*y2+3.0/8*y3+1.0/8*y4;
			bezier[j].mid_tan = Math.atan2(y1+y2-y3-y4, x1+x2-x3-x4) + Math.PI;
			bezier[j].mid_excess = size-pos.distance(bezier[j].mid);
			bezier[j].arc1    = 4.0/3*size*size/(size-contact[j].excess)*Math.tan(rad_gap[j]/8);
			bezier[j].arc_mid = 4.0/3*size*size/(size-bezier[j].mid_excess)*Math.tan(rad_gap[j]/8);
			bezier[j].arc2    = 4.0/3*size*size/(size-contact[(j+1)%(collisionC+collisionCC)].excess)*Math.tan(rad_gap[j]/8);
		}
	}
	
	public void collision01(Ball b){
		final double rad = b.pos.minus(pos).arg();
		int i = (int)(Math.round(rad*dot.length/Math.PI/2)+dot.length)%dot.length;
		if(dot[(i+1)%b.dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
			i++;
			if(i >= dot.length) i = 0;
			while(dot[(i+1)%b.dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i++;
				if(i >= this.dot.length) i = 0;
			}
		}
		else{
			while(dot[(i+dot.length-1)%dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i--;
				if(i <= 0) i = dot.length-1;
			}
		}
		final double dist = this.dot[i].p.distance(b.pos);
		if(dist < b.size){
			contact[collisionC+collisionCC].excess = (b.size-dist)*size/(b.size+size);
			contact[collisionC+collisionCC].p = b.pos.plus(Point.polar(b.size-b.contact[b.collisionC+b.collisionCC].excess, rad+Math.PI));
			contact[collisionC+collisionCC].rad = dot[i].rad;
			contact[collisionC+collisionCC].tangent = dot[i].rad+ Math.PI/2;
			
			b.contact[b.collisionC+b.collisionCC].excess = (b.size-dist)*b.size/(b.size+size);
			b.contact[b.collisionC+b.collisionCC].p = contact[collisionC+collisionCC].p.copy();
			b.contact[b.collisionC+b.collisionCC].rad = b.contact[b.collisionC+b.collisionCC].p.minus(b.pos).arg();
			b.contact[b.collisionC+b.collisionCC].tangent = b.contact[b.collisionC+b.collisionCC].rad+Math.PI/2;
			collisionCC++;
			b.collisionCC++;
		}
	}

	public void collision02(Ball b){
		final double rad1 = b.pos.minus(pos).arg();
		int i = (int)(Math.round(rad1*dot.length/Math.PI/2)+dot.length)%dot.length;
		if(dot[(i+1)%dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
			i++;
			if(i >= dot.length) i = 0;
			while(dot[(i+1)%b.dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i++;
				if(i >= dot.length) i = 0;
			}
		}
		else{
			while(dot[(i+dot.length-1)%dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i--;
				if(i <= 0) i = dot.length-1;
			}
		}
		final double rad2 = pos.minus(b.pos).arg();
		int j = (int)(Math.round(rad2*b.dot.length/Math.PI/2)+b.dot.length)%b.dot.length;
		if(b.dot[(j+1)%b.dot.length].p.distance(pos) < b.dot[j].p.distance(pos)){
			j++;
			if(j >= b.dot.length) j = 0;
			while(b.dot[(j+1)%b.dot.length].p.distance(pos) < b.dot[j].p.distance(pos)){
				j++;
				if(j >= b.dot.length) j = 0;
			}
		}
		else{
			while(b.dot[(j+b.dot.length-1)%b.dot.length].p.distance(pos) <  b.dot[j].p.distance(pos)){
				j--;
				if(j <= 0) j= b.dot.length-1;
			}
		}
		final double dist1 = dot[i].p.distance(pos);
		final double dist2 = b.dot[j].p.distance(b.pos);
		final double excess = (dist1+dist2)-pos.distance(b.pos);
		if(excess > 0){
			contact[collisionC+collisionCC].excess = excess*size/(b.size+size);
			contact[collisionC+collisionCC].p = pos.plus(Point.polar(dist1-contact[collisionC+collisionCC].excess, dot[i].rad));
			contact[collisionC+collisionCC].rad = dot[i].rad;
			contact[collisionC+collisionCC].tangent = dot[i].rad+Math.PI/2;
			
			b.contact[b.collisionC+b.collisionCC].excess = excess*b.size/(b.size+size);
			b.contact[b.collisionC+b.collisionCC].p = b.pos.plus(Point.polar(dist2-b.contact[b.collisionC+b.collisionCC].excess, b.dot[j].rad));
			b.contact[b.collisionC+b.collisionCC].rad = b.dot[j].rad;
			b.contact[b.collisionC+b.collisionCC].tangent = b.dot[j].rad+Math.PI/2;
			collisionCC++;
			b.collisionCC++;
		}
	}
	
	public void absorption01(LinkedList<Ball> ball, Ball b){
		final double rad = b.pos.minus(pos).arg();
		int i = (int)(Math.round(rad*dot.length/Math.PI/2)+dot.length)%dot.length;
		if(dot[(i+1)%b.dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
			i++;
			if(i >= dot.length) i = 0;
			while(dot[(i+1)%b.dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i++;
				if(i >= dot.length) i = 0;
			}
		}
		else{
			while(dot[(i+dot.length-1)%dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i--;
				if(i <= 0) i = dot.length-1;
			}
		}
		final double len = dot[i].p.distance(b.pos);
		if(len < b.size*0.95){
			final Point cp = pos.scalar(weight).plus(b.pos.scalar(b.weight)).scalar(1.0/(weight+b.weight));
			final Point cv = vel.scalar(weight).plus(b.vel.scalar(b.weight)).scalar(1.0/(weight+b.weight));
			b.collisionC  = b.collisionCC = 0;
			pos = cp.copy();
			vel = cv.copy();
			weight = b.weight+weight;
			size = Math.sqrt(weight);
			ball.remove(b);
		}
	}
	
	public void absorption02(LinkedList<Ball> ball, Ball b){
		final double rad1 = b.pos.minus(pos).arg();
		int i = (int)(Math.round(rad1*dot.length/Math.PI/2)+dot.length)%dot.length;
		if(dot[(i+1)%dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
			i++;
			if(i >= dot.length) i = 0;
			while(dot[(i+1)%b.dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i++;
				if(i >= dot.length) i = 0;
			}
		}
		else{
			while(dot[(i+dot.length-1)%dot.length].p.distance(b.pos) < dot[i].p.distance(b.pos)){
				i--;
				if(i <= 0) i = dot.length-1;
			}
		}
		final double rad2 = pos.minus(b.pos).arg();
		int j = (int)(Math.round(rad2*b.dot.length/Math.PI/2)+b.dot.length)%b.dot.length;
		if(b.dot[(j+1)%b.dot.length].p.distance(pos) < b.dot[j].p.distance(pos)){
			j++;
			if(j >= b.dot.length) j = 0;
			while(b.dot[(j+1)%b.dot.length].p.distance(pos) < b.dot[j].p.distance(pos)){
				j++;
				if(j >= b.dot.length) j = 0;
			}
		}
		else{
			while(b.dot[(j+b.dot.length-1)%b.dot.length].p.distance(pos) <  b.dot[j].p.distance(pos)){
				j--;
				if(j <= 0) j= b.dot.length-1;
			}
		}
		final double dist1 = dot[i].p.distance(pos);
		final double dist2 = b.dot[j].p.distance(b.pos);
		if(pos.distance(b.pos)*1.05 < dist1+dist2){
			final Point cp = pos.scalar(weight).plus(b.pos.scalar(b.weight)).scalar(1.0/(weight+b.weight));
			final Point cv = vel.scalar(weight).plus(b.vel.scalar(b.weight)).scalar(1.0/(weight+b.weight));		
			b.collisionC = b.collisionCC = 0;
			pos = cp.copy();
			vel = cv.copy();
			weight = b.weight+weight;
			size = Math.sqrt(weight);
			ball.remove(b);
		}
	}
	
	public void distort(LinkedList<Ball> ball, LinkedList<Figure> figure){
		int excessC = 0;
		for(int j = 0; j < collisionC; j++){
			contact[j].rad = contact[j].p.minus(prevPos).arg();
			contact[j].excess = size-prevPos.distance(contact[j].p);
//			if(contact[j].tangent == "NaN") contact[j].tangent = contact[j].rad+Math.PI/2;
			if(contact[j].excess >= size*0.25) excessC++;
		}
		for(int j = 0; j < collisionC-1; j++){
			for(int k = j+1; k < collisionC; k++){
				if(contact[j].p.distance(contact[k].p) < 0.1){
					for(int l = k; l < collisionC; l++){
						contact[l] = contact[l+1];
					}
					collisionC--;
				}
			}
		}
		if(collisionC >= 2){
			pos = prevPos.copy();
			if(collisionC >= 3){
				double max1, max2;
				int num1 = 0, num2 = 0;
				max1 = 0;
				max2 = 0;
				for(int j = 0; j < collisionC; j++){
					if(contact[j].excess > max1){
						max2 = max1;
						num2 = num1;
						max1 = contact[j].excess;
						num1 = j;
					} 
					else if(contact[j].excess > contact[contact.length-2].excess){
						max2 = contact[j].excess;
						num2 = j;
					}
				}
				if(max2 > 0){
					for(int j = 0; j < collisionC; j++){
						if((num1 != j && (Math.cos(contact[num1].tangent)*(contact[j].p.y-contact[num1].p.y)-Math.sin(contact[num1].tangent)*(contact[j].p.x-contact[num1].p.x) < 0.0001))||
						   (num2 != j && (Math.cos(contact[num2].tangent)*(contact[j].p.y-contact[num2].p.y)-Math.sin(contact[num2].tangent)*(contact[j].p.x-contact[num2].p.x) < 0.0001))){
						   final Contact temp = contact[j];
						   for(int k = j; k < collisionC; k++){
							   if(k == collisionC-1) break;
							   contact[k] = contact[k+1];
						   }
						   contact[collisionC-1] = temp;
						   collisionC--;
						   excessC--;
						   break;
						}
					}
				}
			}
			
			if(excessC >= collisionC) scatter(ball, figure);
			else{
				for(int j = 0; j < collisionC; j++){
					for(int k = collisionC-1; k > j; k--){
						if(contact[k-1].rad > contact[k].rad){
							final Contact temp = contact[k];
							contact[k] = contact[k-1];
							contact[k-1] = temp;
						}
					}
				}
				for(int j = 0; j < collisionC; j++){
					rad_gap[j] = (contact[(j+1)%collisionC].rad-contact[j].rad+2*Math.PI)%(2*Math.PI);
					gap_num[j] = (int)(Math.round(contact[j].rad*dot.length/Math.PI/2)+dot.length)%dot.length;
				}
				
				Point power = new Point();
				for(int j = 0; j < collisionC; j++){
					if(contact[j].excess <= 0) continue;
					power.translate(Point.polar(-contact[j].excess*contact[j].excess, contact[j].tangent-Math.PI/2));
				}
				pos.translate(power.scalar(1.0/size));
				
				for(int j = 0; j < collisionC; j++){
					bezier[j].arc1 = 4.0/3*size*size/(size-contact[j].excess)*Math.tan(rad_gap[j]/4);
					bezier[j].arc2 = 4.0/3*size*size/(size-contact[(j+1)%collisionC].excess)*Math.tan(rad_gap[j]/4);
					
					bezier[j].mid.x = 1.0/8*contact[j].p.x+3.0/8*(contact[j].p.x+bezier[j].arc1*Math.cos(contact[j].tangent))+3.0/8*(contact[(j+1)%collisionC].p.x-bezier[j].arc2*Math.cos(contact[(j+1)%collisionC].tangent))+1.0/8*contact[(j+1)%collisionC].p.x;
					bezier[j].mid.y = 1.0/8*contact[j].p.y+3.0/8*(contact[j].p.y+bezier[j].arc1*Math.sin(contact[j].tangent))+3.0/8*(contact[(j+1)%collisionC].p.y-bezier[j].arc2*Math.sin(contact[(j+1)%collisionC].tangent))+1.0/8*contact[(j+1)%collisionC].p.y;
					bezier[j].mid_tan = contact[j].rad+rad_gap[j]/2+Math.PI/2;
					bezier[j].mid_excess = size-pos.distance(bezier[j].mid);
					bezier[j].arc1    = 4.0/3*size*size/(size-contact[j].excess)*Math.tan(rad_gap[j]/8);
					bezier[j].arc_mid = 4.0/3*size*size/(size-bezier[j].mid_excess)*Math.tan(rad_gap[j]/8);
					bezier[j].arc2    = 4.0/3*size*size/(size-contact[(j+1)%collisionC].excess)*Math.tan(rad_gap[j]/8);
					
					final int gap = (gap_num[(j+1)%collisionC]-gap_num[j]+dot.length)%dot.length;
					for(int k = 0; k < gap/2; k++){
						final double t = 2.0*k/gap;
						dot[(gap_num[j]+k)%dot.length].p.x = (1-t)*(1-t)*(1-t)*contact[j].p.x+3*(1-t)*(1-t)*t*(contact[j].p.x+bezier[j].arc1*Math.cos(contact[j].tangent))+
												  3*(1-t)*t*t*(bezier[j].mid.x-bezier[j].arc_mid*Math.cos(bezier[j].mid_tan))+t*t*t*bezier[j].mid.x;
						dot[(gap_num[j]+k)%dot.length].p.y = (1-t)*(1-t)*(1-t)*contact[j].p.y+3*(1-t)*(1-t)*t*(contact[j].p.y+bezier[j].arc1*Math.sin(contact[j].tangent))+
												  3*(1-t)*t*t*(bezier[j].mid.y-bezier[j].arc_mid*Math.sin(bezier[j].mid_tan))+t*t*t*bezier[j].mid.y;
					}
					for(int k = gap-1; k >= gap/2; k--){
						final double t = (k-gap/2.0)*2/gap;
						dot[(gap_num[j]+k)%dot.length].p.x = (1-t)*(1-t)*(1-t)*bezier[j].mid.x+3*(1-t)*(1-t)*t*(bezier[j].mid.x+bezier[j].arc_mid* Math.cos(bezier[j].mid_tan))+
												  3*(1-t)*t*t*(contact[(j+1)%collisionC].p.x-bezier[j].arc2*Math.cos(contact[(j+1)%collisionC].tangent))+t*t*t*contact[(j+1)%collisionC].p.x;
						dot[(gap_num[j]+k)%dot.length].p.y = (1-t)*(1-t)*(1-t)*bezier[j].mid.y+3*(1-t)*(1-t)*t*(bezier[j].mid.y+bezier[j].arc_mid*Math.sin(bezier[j].mid_tan))+
												  3*(1-t)*t*t*(contact[(j+1)%collisionC].p.y-bezier[j].arc2*Math.sin(contact[(j+1)%collisionC].tangent))+t*t*t*contact[(j+1)%collisionC].p.y;
					}		
				}
				distorted = true;
				for(int j = 0; j < dot.length; j++){
					dot[j].rad = dot[j].p.minus(pos).arg();
				}
			}
		}
	}
}
