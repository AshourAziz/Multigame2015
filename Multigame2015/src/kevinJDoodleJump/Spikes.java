package kevinJDoodleJump;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Spikes extends Obstacle {

	private BufferedImage img[];
	private Boolean wasHit;
	private int frameCount;

	public Spikes(int x, int y, JDoodleJump dj) {
		setBody(x, y, 128, 62);
		wasHit = false;
		frameCount = 0;
		img = new BufferedImage[5];

		try {
			for (int i = 0; i < 5; i++) {
				img[i] = ImageIO.read(getClass().getResource("SPIKE BOYS" + i + ".png"));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean getWasHit() {
		return wasHit;
	}

	public void gotHit() {
		wasHit = true;
	}

	public void render(Graphics2D g) {
		frameCount++;
		g.drawImage(img[frameCount % 5], null, (int) getBody().x, (int) getBody().y);
	}

}
