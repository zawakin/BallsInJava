

public class Figure {
	public static final double e = 0.6;
	
	private double tl_x, tl_y, tr_x, tr_y, bl_x, bl_y, br_x, br_y;
	private double wid, hei;
	private double rad1, rad2;
	private double vechx, vechy, vecvx, vecvy;
	private ColorType color;
	
	Figure(double tl_x, double tl_y, double wid, double hei, double rad1, double rad2, ColorType color){
		if(rad2 == 0.0) rad2 = rad1-Math.PI/2;
		vechx = wid*Math.cos(rad1);
		vechy = wid*Math.sin(rad1);
		vecvx = hei*Math.cos(rad2);
		vecvy = hei*Math.sin(rad2);
		
		this.tl_x = tl_x;
		this.tl_y = tl_y;
		tr_x = tl_x+vechx;
		tr_y = tl_y+vechy;
		bl_x = tl_x-vecvx;
		bl_y = tl_y-vecvy;
		br_x = tl_x+vechx-vecvx;
		br_y = tl_y+vechy-vecvy;
		
		this.wid = wid;
		this.hei = hei;
		this.rad1 = rad1;
		this.rad2 = rad2;
		this.color = color;
	}
	
	public void draw(Main.MainWindow mw){
		final double[] xPoints = {tl_x, tr_x, br_x, bl_x};
		final double[] yPoints = {tl_y, tr_y, br_y, bl_y};
		mw.gc.setFill(color.toGc());
		mw.gc.fillPolygon(xPoints, yPoints, 4);
	}
	
	public void collision01(Ball b, int j){
		if(Math.cos(rad1)*(b.pos.y-tl_y)-Math.sin(rad1)*(b.pos.x-tl_x) < 0){
			if(Math.cos(rad2)*(b.pos.y-bl_y)-Math.sin(rad2)*(b.pos.x-bl_x) < 0){
				b.touchArea.get(j).x = tl_x;
				b.touchArea.get(j).y = tl_y;
				b.touchArea.get(j).num = 1;
			}
			else if(Math.cos(rad2+Math.PI)*(b.pos.y-tr_y)-Math.sin(rad2+Math.PI)*(b.pos.x-tr_x) < 0){
				b.touchArea.get(j).x = tr_x;
				b.touchArea.get(j).y = tr_y;
				b.touchArea.get(j).num = 1;
			}
			else{
				b.touchArea.get(j).x = tl_x;
				b.touchArea.get(j).y = tl_y;
				b.touchArea.get(j).rad = rad1;
				b.touchArea.get(j).num = 2;
			}
		}
		else if(Math.cos(rad1+Math.PI)*(b.pos.y-br_y)-Math.sin(rad1+Math.PI)*(b.pos.x-br_x) < 0){
			if(Math.cos(rad2)*(b.pos.y-bl_y)-Math.sin(rad2)*(b.pos.x-bl_x) < 0){
				b.touchArea.get(j).x = bl_x;
				b.touchArea.get(j).y = bl_y;
				b.touchArea.get(j).num = 1;
			}
			else if(Math.cos(rad2+Math.PI)*(b.pos.y-tr_y)-Math.sin(rad2+Math.PI)*(b.pos.x-tr_x) < 0){
				b.touchArea.get(j).x = br_x;
				b.touchArea.get(j).y = br_y;
				b.touchArea.get(j).num = 1;
			}
			else{
				b.touchArea.get(j).x = br_x;
				b.touchArea.get(j).y = br_y;
				b.touchArea.get(j).rad = rad1 + Math.PI;
				b.touchArea.get(j).num = 2;
			}
		}
		else{
			if(Math.cos(rad2)*(b.pos.y-bl_y)-Math.sin(rad2)*(b.pos.x-bl_x) < 0){
				b.touchArea.get(j).x = bl_x;
				b.touchArea.get(j).y = bl_y;
				b.touchArea.get(j).rad = rad2;
				b.touchArea.get(j).num = 2;
			}
			else if(Math.cos(rad2+Math.PI)*(b.pos.y-tr_y)-Math.sin(rad2+Math.PI)*(b.pos.x-tr_x) < 0){
				b.touchArea.get(j).x = tr_x;
				b.touchArea.get(j).y = tr_y;
				b.touchArea.get(j).rad = rad2+Math.PI;
				b.touchArea.get(j).num = 2;
			}
			else{
				b.touchArea.get(j).num = 3;
			}
		}
		if(b.touchArea.get(j).num < 2){
			b.touchArea.get(j).dist = b.pos.distance(b.touchArea.get(j));
			if(b.touchArea.get(j).dist <= b.size){
				final double rad = Math.atan2(b.pos.y-b.touchArea.get(j).y+b.vel.y, -b.pos.x+b.touchArea.get(j).x-b.vel.x);
				final double velhx = (b.vel.x*Math.sin(rad)-b.vel.y*Math.cos(rad))*Math.sin(rad);
				final double velhy = -(b.vel.x*Math.sin(rad)-b.vel.y*Math.cos(rad))*Math.cos(rad);
				double velvx = b.vel.x-velhx;
				double velvy = b.vel.y-velhy;
				
				final double excess = b.size-b.touchArea.get(j).dist;   
				b.pos.x += excess*-Math.cos(rad);
				b.pos.y += excess*Math.sin(rad);
				velvx *= -e;
				velvy *= -e;
				b.vel.x = velhx+velvx;
				b.vel.y = velhy+velvy;
				
				b.contact[b.collisionC].x = b.touchArea.get(j).x;
				b.contact[b.collisionC].y = b.touchArea.get(j).y;
				b.contact[b.collisionC].rad = Math.atan2(b.touchArea.get(j).y-b.pos.y, b.touchArea.get(j).x-b.pos.x);
				b.contact[b.collisionC].tangent = b.contact[b.collisionC].rad+Math.PI/2;
				b.collisionC++;
			}
			else if(b.touchArea.get(j).dist <= b.size*1.5) return;
		}
		else if(b.touchArea.get(j).num < 3){
			final double drop = -Math.cos(b.touchArea.get(j).rad)*(b.pos.y-b.touchArea.get(j).y)+(b.pos.x-b.touchArea.get(j).x)*Math.sin(b.touchArea.get(j).rad);
			if(drop <= b.size){
				final double velhx = (b.vel.x*Math.cos(b.touchArea.get(j).rad)+b.vel.y*Math.sin(-b.touchArea.get(j).rad))*Math.cos(b.touchArea.get(j).rad);
				final double velhy = (b.vel.x*Math.cos(b.touchArea.get(j).rad)+b.vel.y*Math.sin(-b.touchArea.get(j).rad))*Math.sin(-b.touchArea.get(j).rad);
				double velvx = b.vel.x-velhx;
				double velvy = b.vel.y-velhy;
				b.contact[b.collisionC].x = b.pos.x-drop*Math.sin(b.touchArea.get(j).rad);
				b.contact[b.collisionC].y = b.pos.y+drop*Math.cos(b.touchArea.get(j).rad);
				b.contact[b.collisionC].tangent = b.touchArea.get(j).rad+Math.PI; 
				b.collisionC++;
				
				final double excess = b.size-drop;
				b.vel.x += Math.sqrt(2*0.3*e*excess);
				b.vel.y += Math.sqrt(2*0.3*e*excess);
				b.pos.x += excess*Math.sin(b.touchArea.get(j).rad);
				b.pos.y += excess*-Math.cos(b.touchArea.get(j).rad);
				velvx *= -e;
				velvy *= -e;
				b.vel.x = velhx+velvx;
				b.vel.y = velhy+velvy;
			}
			else if(drop <= b.size*1.5) return;
		}
		else{
			System.out.println("正円が内部に入っている");
		}
		b.touchArea.get(j).num = 0;
	}
}
