package kevinJDoodleJump;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import multigame.*;
import static kevinJDoodleJump.GameState.*;

/**
 * simple template for a MultiGameUprite2015 for uprite cabinet
 */
public class JDoodleJump implements Game {

	private KeyHandler kh;
	private boolean gameOver;
	private MultiGame mg;
	private GameState gameState;
	private Sound sound = new Sound();
	private boolean musicOn = false;

	private int frameCount = 0;
	private Dimension screen;// used for screen resolution
	private Random randGen;
	private Random randGen2;

	private String message = "";
	private Point messagePos = new Point();
	private int round = 1 + 2; // 3 rounds
	private int toppestPlatform = 1200, toppestPlatform2 = 1200;

	private ArrayList<Obstacle> obstacles1;
	private ArrayList<Obstacle> obstacles2;
	private Rectangle viewport1;
	private Rectangle viewport2;

	private Doodle davidGomez;
	private Doodle paulBotti;

	private Font scoreFont = new Font("arial", Font.PLAIN, 32);
	private Font timeFont = new Font("arial", Font.PLAIN, 64);

	/**
	 * A Game class must have only one constructor and it must have exactly one
	 * MultiGame parameter.<br>
	 * Initialize all instance variables.
	 */
	public JDoodleJump(MultiGame mg) {

		kh = mg.getKeyHandler();
		this.mg = mg;
		gameState = new GameState(mg);
		gameOver = false;
		randGen = new Random(29); // 29 is the seed
		randGen2 = new Random(69); // 69 is the seed

		screen = new Dimension(1280, 1024); // 1280x1024
		viewport1 = new Rectangle(screen.width / 2, 0, screen.width / 2,
				screen.height);
		viewport2 = new Rectangle(0, 0, screen.width / 2, screen.height);
		String soundList[] = { "jump.wav", "sanic.wav" };
		sound = new Sound();
		sound.loadSoundFiles(soundList);
		musicOn = false;
		// sound.play(1, true);

		initRound();
	}

	/**
	 * Called at the beginning of each round to init appropriate variables.
	 */
	public void initRound() {

		round--;
		if (round == 0) {
			gameOver = true;// puts multigame back to menu state
			return;
		}
		message = "Ready!";
		messagePos.x = 0;
		toppestPlatform = 1200;
		toppestPlatform2 = 1200;
		davidGomez = new Doodle(1, this);
		paulBotti = new Doodle(2, this);
		obstacles1 = new ArrayList<Obstacle>();
		obstacles2 = new ArrayList<Obstacle>();
		for (int i = 0; i < 10; i++) {
			int rng = randGen2.nextInt(100); // 100 numbers, 100%
			if (rng < 10) { // 10%
				int x = randGen.nextInt(512) + 640;
				obstacles1.add(new DisappearingPlatform(x,
						toppestPlatform - 150, this));
			} else if (rng > 95) { // 5%
				obstacles1.add(new Spikes(randGen.nextInt(512) + 640,
						toppestPlatform - 150, this));
			} else { // 85%
				int x = randGen.nextInt(512) + 640;
				obstacles1.add(new Platform(x, toppestPlatform - 150, this));
			}
			toppestPlatform -= 150;
		}
		for (int i = 0; i < 10; i++) {
			int rng = randGen2.nextInt(99); // 100 numbers, 100%
			if (rng < 10) { // 10%
				obstacles2.add(new DisappearingPlatform(randGen.nextInt(512),
						toppestPlatform2 - 150, this));
			} else if (rng > 95) { // 5%
				obstacles2.add(new Spikes(randGen.nextInt(512),
						toppestPlatform2 - 150, this));
			} else { // 85%
				obstacles2.add(new Platform(randGen.nextInt(512),
						toppestPlatform2 - 150, this));
			}
			toppestPlatform2 -= 150;
		}
	}

	/**
	 * Required method for implementing Game interface .. gameUpdate must
	 * eventually set gameOver to false
	 */
	public void gameUpdate() {
		gameState.tick();

		if (gameState.inState(READY)) {

			if (gameState.getCurTick() == 1) {
				initRound();
			}

		} else if (gameState.inState(PLAYING)) {
			frameCount++;
			message = "Playing!";

			toppestPlatform = (int) obstacles1.get(obstacles1.size() - 1)
					.getBody().y;
			toppestPlatform2 = (int) obstacles2.get(obstacles2.size() - 1)
					.getBody().y;

			if (600 + messagePos.x >= 800) {
				gameState.toState(DEAD);
			}

			for (Obstacle o : obstacles1) {
				if (o.getBody().y > viewport1.height / 2) {
					davidGomez.jumpPlatform(o, sound);
				}
			}
			for (Obstacle o : obstacles2) {
				if (o.getBody().y > viewport2.height / 2) {
					paulBotti.jumpPlatform(o, sound);
				}
			}
			if (obstacles1.get(0).getBody().y > 1024) {
				obstacles1.remove(0);
				int rng = randGen2.nextInt(99); // 100 numbers, 100%
				if (rng < 10) { // 10%
					obstacles1.add(new DisappearingPlatform(randGen
							.nextInt(512) + 640, toppestPlatform - 150, this));
				} else if (rng > 95) { // 5%
					obstacles1.add(new Spikes(randGen.nextInt(512) + 640,
							toppestPlatform - 150, this));
				} else { // 85%
					obstacles1.add(new Platform(randGen.nextInt(512) + 640,
							toppestPlatform - 150, this));
				}
			}
			if (obstacles2.get(0).getBody().y > 1024) {
				obstacles2.remove(0);
				int rng = randGen2.nextInt(99); // 100 numbers, 100%
				if (rng < 10) { // 10%
					obstacles2.add(new DisappearingPlatform(randGen
							.nextInt(512), toppestPlatform2 - 150, this));
				} else if (rng > 95) { // 5%
					obstacles2.add(new Spikes(randGen.nextInt(512),
							toppestPlatform2 - 150, this));
				} else { // 85%
					obstacles2.add(new Platform(randGen.nextInt(512),
							toppestPlatform2 - 150, this));
				}
			}
			davidGomez.move();
			paulBotti.move();
			for (int i = 0; i < 10; i++) {
				obstacles1.get(i).move(davidGomez);
			}
			for (int i = 0; i < 10; i++) {
				obstacles2.get(i).move(paulBotti);
			}

			if (davidGomez.getBody().y >= 1200) {
				davidGomez.hit();
			}
			if (paulBotti.getBody().y >= 1200) {
				paulBotti.hit();
			}

			if (kh.wasBtn1JustPressed(1)) {
				if (!musicOn) {
					sound.play(1, true);
					musicOn = true;
				} else {
					sound.stop(1);
					musicOn = false;
				}
			}

		} else if (gameState.inState(DEAD)) {

			// no rounds left to play ..back to menu after dead state
			if (round <= 1) {
				message = "Time ran out! Game Over";
				sound.stopAll();
			}
			// end of round ... more rounds to play
			else {
				message = "Dead!";
			}
		}
	}

	/**
	 * Required method for implementing Game interface.. renders all Game
	 * elements to the Graphics2D variable
	 */
	public void gameRender(Graphics2D g) {
		// clear the screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, screen.width, screen.height);

		g.setColor(Color.LIGHT_GRAY);
		g.fill(viewport1);
		g.fill(viewport2);
		g.setColor(Color.BLACK);
		g.draw(viewport1);
		g.draw(viewport2);

		for (int i = 0; i < obstacles1.size(); i++) {
			if (obstacles1.get(i).getBody().y >= -64) {
				obstacles1.get(i).render(g);
			}
		}
		for (int i = 0; i < obstacles2.size(); i++) {
			if (obstacles2.get(i).getBody().y >= -64) {
				obstacles2.get(i).render(g);
			}
		}
		davidGomez.render(g);
		paulBotti.render(g);
		g.setColor(Color.BLACK);

		if (gameState.inState(READY)) {

			if (gameState.getCurTick() < 15) {
				return;// return to the menu before round 0 flashes on screen
			}
			g.drawString(
					message + "  "
							+ (gameState.getCurTicksLeft() / mg.getFps() + 1),
					800, 300);
		} else if (gameState.inState(PLAYING)) {
			g.setFont(timeFont);
			g.drawString("" + (90 - (frameCount / 150)), 604, 50);
			g.setFont(scoreFont);
			g.drawString("" + paulBotti.getScore(), 320, 100);
			g.drawString("" + davidGomez.getScore(), 960, 100);
		} else if (gameState.inState(DEAD)) {
			g.setFont(scoreFont);
			g.drawString("" + paulBotti.getScore(), 320, 100);
			g.drawString("" + davidGomez.getScore(), 960, 100);
			g.setFont(timeFont);
			if (davidGomez.getScore() > paulBotti.getScore()) {
				g.drawString("RIGHT PLAYER WINS!", 320, 50);
			} else {
				g.drawString("LEFT PLAYER WINS!", 320, 50);
			}
		}
	}

	public Rectangle getViewport(int i) {
		if (i == 1)
			return viewport1;
		else if (i == 2)
			return viewport2;
		else
			return null;
	}

	/**
	 * Required method for implementing Game interface
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * can't remember now why we need this method ...
	 */
	public Graphics2D getGraphics2D() {
		return mg.getGraphics2D();
	}

	public KeyHandler getKeyHandler() {
		return kh;
	}
}
// End of line