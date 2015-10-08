package kevinJDoodleJump;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Platform extends Obstacle {
	private BufferedImage img;

	public Platform(int x, int y, JDoodleJump dj) {
		setBody(x, y, 128, 32);
		
		try {
			img = ImageIO.read(getClass().getResource("greenPlatform.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void render(Graphics2D g) {
		g.drawImage(img, null, (int) getBody().x, (int) getBody().y);
	}
}
