package kevinJDoodleJump;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import multigame.KeyHandler;

/**
 * This is the Doodle class for the JDoodleJump game
 * 
 * No Doodles were harmed in the making of this class
 * 
 * 1/22/2015 -
 */

public class Doodle {

	private double xVelocity, xMaxVel, xDamp, xAccel; // X-SPECIFIC VARIABLES
	private double yVelocity, surplusYVel, gravity; // Y-SPECIFIC VARIABLES
	private int playerNum; // either 1 or 2
	private Rectangle2D.Double body; // hit box for Doodle
	private int dir; // -1 = LEFT, 0 = NEUTRAL, 1 = RIGHT
	private int score;

	private JDoodleJump dj; // reference to client for public methods
	private Rectangle vp; // convenient reference to the viewport
	private BufferedImage imgLeft;
	private BufferedImage imgRight;
	private BufferedImage lastImage;
	private KeyHandler kh;

	public Doodle(int pNum, JDoodleJump dj) {
		playerNum = pNum;
		this.dj = dj;
		body = new Rectangle2D.Double();
		vp = dj.getViewport(pNum);
		body.width = 64;
		body.height = 100;
		xVelocity = 0.0;
		xMaxVel = 5.0;
		xDamp = 0.05;
		xAccel = 0.2;
		gravity = 0.06;
		yVelocity = 0.0;
		surplusYVel = 0.0;
		dir = 1;
		body.x = vp.x + 320;
		body.y = vp.height / 2;
		this.kh = dj.getKeyHandler();
		try {
			imgLeft = ImageIO.read(getClass().getResource("doodLeft.png"));
			imgRight = ImageIO.read(getClass().getResource("doodRight.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public double getSurplusYVel() {
		return surplusYVel;
	}

	public Rectangle2D.Double getBody() {
		return body;
	}

	public void move() {

		// X MOVEMENT CALCULATIONS

		body.x += xVelocity;

		if (dir == 1) {
			xVelocity += xAccel;
		} else if (dir == -1) {
			xVelocity -= xAccel;
		}

		if (dir == 0) {
			if (xVelocity > 0) {
				xVelocity -= xDamp;
			} else if (xVelocity < 0) {
				xVelocity += xDamp;
			}
			if (Math.abs(xVelocity) < xDamp)
				xVelocity = 0;
			/*
			 * the if statement above handles the case where the xVelocity value
			 * is stuck between 0.05 and -0.05. for example: the xVelocity value
			 * goes to 0.04, then goes to -0.05 the xVelocity will keep
			 * oscillating between these values and this handles the case
			 * appropriately. absolute value is used to only have one if
			 * statement instead of two. the compared value is the dampening
			 * because these cases only appear between the negative and positive
			 * value of the dampening.
			 * 
			 * tl;dr doodle's seizure medicine
			 */
		}

		if (xVelocity > xMaxVel) {
			xVelocity = xMaxVel;
		} else if (xVelocity < -xMaxVel) {
			xVelocity = -xMaxVel;
		}

		if (body.x > vp.x + 640 - 64) {
			body.x = vp.x;
		} else if (body.x < vp.x) {
			body.x = vp.x + 640 - 64;
		}

		// Y VALUE CALCULATIONS

		if (body.y + yVelocity < vp.height / 2) {
			body.y = vp.height / 2;
			surplusYVel = yVelocity;
			score++; // score increment for score
		} else {
			body.y += yVelocity;
		}

		yVelocity += gravity;

		if (yVelocity >= 0 && body.y >= vp.height / 2) {
			surplusYVel = 0;
		}

		// ^ CALCULATIONS
		// ------------------
		// v KEYPRESS CHECKS

		if (kh.isRightPressed(playerNum)) {
			dir = 1;
		} else if (kh.isLeftPressed(playerNum)) {
			dir = -1;
		} else if (!kh.isLeftPressed(playerNum) && !kh.isRightPressed(playerNum)) {
			dir = 0;
		}
	}
	
	public int getScore() {
		return score;
	}

	public void setDirection(int direction) {
		dir = direction;
	}

	public void render(Graphics2D g) {
		if (dir == -1) {
			g.drawImage(imgLeft, null, (int) body.x, (int) body.y);
			lastImage = imgLeft;
		} else if (dir == 1) {
			g.drawImage(imgRight, null, (int) body.x, (int) body.y);
			lastImage = imgRight;
		} else {
			g.drawImage(lastImage, null, (int) body.x, (int) body.y);
		}
	}

	public void jumpPlatform(Obstacle o, Sound s) {
		if (surplusYVel == 0 && body.y < o.getBody().y - 80
				&& body.intersects(o.getBody())) {
			if (o instanceof Platform) {
				yVelocity = -6.0; // JUMP HEIGHT: 300 PIXELS
			} else if (o instanceof DisappearingPlatform) {
				if (!((DisappearingPlatform) o).getJumpedOn()) {
					yVelocity = -6.0;
					((DisappearingPlatform) o).gotJumpedOn();
				}
			}
			s.play(0, false);
		} else if (body.intersects(o.getBody()) && o instanceof Spikes) {
			if (!((Spikes) o).getWasHit() && (surplusYVel != 0 || yVelocity < 0)) {
				yVelocity = -10;
				score -= 600;
				((Spikes) o).gotHit();
			}
		}
	}
	
	public void hit() {
		yVelocity = -10;
		score -= 300;
	}
}