package kevinJDoodleJump;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import multigame.KeyHandler;

public abstract class Obstacle {
	private Rectangle2D.Double body;
	
	public Rectangle2D.Double getBody() {
		return body;
	}
	
	public void setBody(int x, int y, int w, int h) {
		body = new Rectangle2D.Double(x, y, w, h);
	}
	
	public void setPosition(int x, int y) {
		body.x = x;
		body.y = y;
	}
	
	public abstract void render(Graphics2D g);
	
	public void move(Doodle dood) {
		body.y -= dood.getSurplusYVel();
	}
}
