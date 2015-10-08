package kevinJDoodleJump;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DisappearingPlatform extends Obstacle {
	private BufferedImage img;
	private boolean wasJumpedOn;

	public DisappearingPlatform(int x, int y, JDoodleJump dj) {
		setBody(x, y, 128, 32);
		
		wasJumpedOn = false;

		try {
			img = ImageIO.read(getClass().getResource("greenPlatform.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean getJumpedOn() {
		return wasJumpedOn;
	}
	
	public void gotJumpedOn() {
		wasJumpedOn = true;
	}

	public void render(Graphics2D g) {
		if (wasJumpedOn == false) {
			g.drawImage(img, null, (int) getBody().x, (int) getBody().y);
		}
	}
}
