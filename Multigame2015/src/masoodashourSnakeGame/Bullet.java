package masoodashourSnakeGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import multigame.*;

public class Bullet {
	Rectangle body;
	int d;
	private int direction;

	public Bullet(int x, int y, int d) {
		direction = d;
		body = new Rectangle(x, y, 12, 12);

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		
		g.fill(body);
		 g.draw(body);
		

	}

	public void setPosition(int x, int y) {
		body.x = x;
		body.y = y;
	}

	public int getDirection() {
		return direction;
	}
	

	public void move() {
		if (direction == 4) {
			body.x -= 5;
		}
		if (direction == 2) {
			body.y += 5;
		}
		if (direction == 6) {
			body.x += 5;
		}
		if (direction == 8) {
			body.y -= 5;
		}
	}
}
