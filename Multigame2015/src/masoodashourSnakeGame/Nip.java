package masoodashourSnakeGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import multigame.*;

public class Nip {
	private BufferedImage img;
	Rectangle niprec;

	public Nip(int x, int y) {
		niprec = new Rectangle(x, y, 12, 12);

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fill(niprec);
		// g.draw(niprec);

	}

	public void setPosition(int x, int y) {
		niprec.x = x;
		niprec.y = y;
	}
}
