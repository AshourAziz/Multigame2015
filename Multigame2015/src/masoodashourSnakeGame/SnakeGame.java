package masoodashourSnakeGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import multigame.*;
import static masoodashourSnakeGame.GameState.*;

public class SnakeGame implements Game {

	String message1 = "";
	String message3 = "";
	String message4 = "";
	String message5 = "";
	String message6 = "";
	Rectangle border = new Rectangle();
	Rectangle box1 = new Rectangle();
	Rectangle box2 = new Rectangle();
	Rectangle box3 = new Rectangle();
	Rectangle box4 = new Rectangle();
	private KeyHandler kh;
	private boolean gameOver;
	private MultiGame mg;
	Color dead = new Color(133, 133, 133);
	private int borderPosX = 320;
	private int borderPosY = 275;
	private int borderPosWidth = 600;
	private int borderPosHeight = 448;
	private GameState gameState;
	private Nip nip;
	private Bullet bullet1;
	private Bullet bullet2;
	private Dimension screen;// used for screen resolution
	private Random randGen;
	private snek snek1;
	private String message = "";
	private String message2 = "";
	private String welcome = "";
	private String currentGame = "";
	private Point messagePos = new Point();
	private Point messagePos2 = new Point();
	private int round = 3 + 2; // 3 rounds
	private int xpos;
	private int ypos;
	private snek snek2;
	private snek menuSnek;
	public int gameMode;
	private boolean canDrawMenuSnake = false;
	public boolean waitingOnResponse = true;
	private boolean snek1IsDead = false;
	private boolean snek2IsDead = false;
	private boolean canDrawNip = false;
	private boolean canDrawSnek1 = false;
	private boolean canDrawSnek2 = false;
	private int fontsize = 0;
	private int messageYMovement = 400;
	private int messageYMovement3 = 600;
	private int messageYMovement4 = 600;
	private int messageYMovement5 = 600;
	private int messageYMovement6 = 600;
	private int tokens = 2;
	private int whowon = 0;
	private int menuMessageXPos = 0;
	private int xPosMessage1 = 500;
	private int countdown = 3;
	private boolean forward = true;
	private int xPosMessage2 = 500;
	private boolean forward2 = true;
	private int snek2head = 0;
	private int snek1head = 0;
	private boolean bullet1Spawned = false;
	private boolean bullet2Spawned = false;
	private int bullety = 0;
	private int bulletx = 0;
	Color rainbow;
	Sound menu;
	Sound ready;
	Sound playing;

	/**
	 * Our array lists, please see top of construct for more usage
	 */
	ArrayList<Integer> fontArray;
	ArrayList<Integer> modesForSnek1;
	ArrayList<Integer> modesForSnek2;
	ArrayList<Integer> modesForNips;
	ArrayList<Integer> modesForPvp;

	public SnakeGame(MultiGame mg) {

		/**
		 * Create the menu snek
		 */
		menuSnek = new snek(620, 480, 3, 0);

		/**
		 * creates the 4 boxes for menu
		 */
		box1 = new Rectangle(borderPosX - 27, 445, 50, 100);
		box2 = new Rectangle(borderPosWidth - 27, 250, 100, 50);
		box3 = new Rectangle(borderPosX + borderPosWidth - 20, 445, 50, 100);

		box4 = new Rectangle(borderPosWidth - 27, borderPosHeight + 255, 100,
				50);

		/**
		 * let's create a list for the gamemodes that snek1 and snek2 should be
		 * used for this will allow us to easily add more gamemodes making this
		 * a BAD BAD BAD BAD ASS MODULE SYSTEM
		 * 
		 */
		modesForSnek1 = new ArrayList<Integer>();
		modesForSnek1.addAll(Arrays.asList(1, 2, 3, 4, 5));
		modesForSnek2 = new ArrayList<Integer>();
		modesForSnek2.addAll(Arrays.asList(2, 3, 4, 5));
		modesForNips = new ArrayList<Integer>();
		modesForNips.addAll(Arrays.asList(1, 2, 3));
		modesForPvp = new ArrayList<Integer>();
		modesForPvp.addAll(Arrays.asList(3, 4, 5));

		fontArray = new ArrayList<Integer>();
		fontArray.addAll(Arrays.asList(12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
				22, 23, 24));

		snek1IsDead = false;
		snek2IsDead = false;
		canDrawSnek1 = false;
		canDrawSnek2 = false;
		canDrawNip = false;

		kh = mg.getKeyHandler();
		this.mg = mg;
		gameState = new GameState(mg);
		gameOver = false;
		randGen = new Random(29); // 29 is the seed
		// randGen.nextInt()
		screen = new Dimension(1280, 1024);// 1280x1024 for cabinet two player
											// game

		initRound();
	}

	/**
	 * Called at the beginning of each round to init appropriate variables.
	 */

	public void initRound() {

		waitingOnResponse = true;

		/**
		 * Border cords, multiple of 8 1200 // 150 896 // 112
		 */

		round--;
		if (round == 0) {
			gameOver = true;// puts multigame back to menu state
			return;
		}

		/**
		 * Select your GameMode message here
		 */
		messagePos2.x = 0;
		messagePos.x = 0;

		welcome = "Hello, For player 1 push joystick left | For co-op 2 player push right | For co-op comp push up | For Tron push down |";
	}

	/**
	 * Required method for implementing Game interface .. gameUpdate must
	 * eventually set gameOver to false
	 * 
	 * update all your instance variables
	 */
	public void gameUpdate() {
		border = new Rectangle(borderPosX, borderPosY, borderPosWidth,
				borderPosHeight);
		gameState.tick();

		if (gameState.inState(MENU)) {
			if (gameState.getCurTick() == 1) {
				menuSnek = new snek(500, 500, 3, 0);
				/**
				 * So the flashing colors start in the center every time
				 */
				messageYMovement = 400;
				messageYMovement3 = 600;
				messageYMovement4 = 600;
				messageYMovement5 = 600;
				messageYMovement6 = 600;
				forward = true;
				countdown = 3;
			}

			borderPosX = 320;
			borderPosY = 275;
			borderPosWidth = 600;
			borderPosHeight = 448;
			message1 = "- Welcome -";
			message3 = "For single player Go left ";
			message4 = "for snake WARFARE!!!!";
			message5 = "for multiplayer competitive go right ";
			message6 = "and for TRON go down";
			if (menuSnek.head.x < box1.getMinX()) {
				gameMode = 1;
				gameState.toState(READY);
			}
			if (menuSnek.head.y < box2.getMinY()) {
				gameMode = 2; //for casual
				//gameMode = 5; BULLET WAREFARE
				gameState.toState(READY);
			}
			if (menuSnek.head.x > box3.getMaxX()) {
				gameMode = 3;
				gameState.toState(READY);
			}
			if (menuSnek.head.y > box4.getMaxY()) {
				gameMode = 4;
				gameState.toState(READY);
			}
			messagePos2.x = 0;

			canDrawMenuSnake = false;

			if (menuSnek.head.intersects(box1)
					|| menuSnek.head.intersects(box3)
					|| menuSnek.head.intersects(box2)
					|| menuSnek.head.intersects(box4)) {

			} else if (menuSnek.head.y < border.getMinY()
					|| menuSnek.head.y > border.getMaxY()
					|| menuSnek.head.x > border.getMaxX()
					|| menuSnek.head.x < border.getMinX()) {

				menuSnek = new snek(500, 500, 3, 0);

			}

			/**
			 * Game mode selecting system
			 */

			/**
			 * MenuSnake Commands
			 * 
			 */

			if (gameState.getCurTick() % 5 == 0) {
				if (kh.isDownPressed(1)) {
					menuSnek.setDirection(2);
					menuSnek.move();
				}
				if (kh.isLeftPressed(1)) {
					menuSnek.setDirection(4);
					menuSnek.move();
				}
				if (kh.isRightPressed(1)) {
					menuSnek.setDirection(6);
					menuSnek.move();
				}
				if (kh.isUpPressed(1)) {
					menuSnek.setDirection(8);
					menuSnek.move();
				}
			}
		}

		if (gameState.inState(READY)) {

			borderPosX = 32;
			borderPosY = 50;
			borderPosWidth = 1200;
			borderPosHeight = 896;
			border = new Rectangle(borderPosX, borderPosY, borderPosWidth,
					borderPosHeight);

			/**
			 * Select the gamemode and the if statements that manage the
			 * different gamemodes GameMode 1 == Single Player GameMode 2 ==
			 * Co-Op (2 players, Cannot kill each other) GameMode 3 ==
			 * Competitive Co-Op (2 players, Hit eachother and you'll die)
			 * GameMode 4 == TRON
			 */

			if (gameMode == 1) {
				snek1 = new snek(500, 500, 1, 1);
				canDrawSnek1 = true;
				waitingOnResponse = false;

			} else if (gameMode == 2) {
				snek1 = new snek(100, 500, 1, 2);
				snek2 = new snek(1200, 500, 2, 2);
				canDrawSnek1 = true;
				canDrawSnek2 = true;
				waitingOnResponse = false;

			} else if (gameMode == 3) {
				snek1 = new snek(100, 500, 1, 3);
				snek2 = new snek(1200, 500, 2, 3);
				canDrawSnek1 = true;
				canDrawSnek2 = true;
				waitingOnResponse = false;

			} else if (gameMode == 4) {
				snek1 = new snek(100, 500, 1, 4);
				snek2 = new snek(1200, 500, 2, 4);
				canDrawSnek1 = true;
				canDrawSnek2 = true;
				waitingOnResponse = false;

			} else if (gameMode == 5) {
				snek1 = new snek(100, 500, 1, 4);
				snek2 = new snek(1200, 500, 2, 4);
				canDrawSnek1 = true;
				canDrawSnek2 = true;
				waitingOnResponse = false;
			}

			/**
			 * Messages for current game
			 * 
			 */
			if (gameMode == 1) {
				currentGame = "You are now playing SINGLE PLAYER";

			} else if (gameMode == 2) {

				currentGame = "You are now playing CASUAL";

			} else if (gameMode == 3) {

				currentGame = "You are now playing COMPETITIVE ";

			} else if (gameMode == 4) {
				currentGame = "You are now playing TRON";

			} else if (kh.wasRightJustPressed(1)) {
				messagePos.x += 5;
			}

			// move snakes here

			if (gameState.getCurTick() > 440) {

				kh.wasUpJustPressed(1);
				kh.wasDownJustPressed(1);
				kh.wasLeftJustPressed(1);
				kh.wasRightJustPressed(1);

				kh.wasUpJustPressed(2);
				kh.wasDownJustPressed(2);
				kh.wasLeftJustPressed(2);
				kh.wasRightJustPressed(2);

			}

			if (gameState.getCurTick() == 1) {
				initRound();
			}

		} else if (gameState.inState(PLAYING)) {

			/**
			 * clears any key presses before the game started I.E the countdown
			 * period and during the menu state.
			 * 
			 */
			if (gameState.getCurTick() == 1) {
				kh.wasUpJustPressed(1);
				kh.wasDownJustPressed(1);
				kh.wasLeftJustPressed(1);
				kh.wasRightJustPressed(1);

				kh.wasUpJustPressed(2);
				kh.wasDownJustPressed(2);
				kh.wasLeftJustPressed(2);
				kh.wasRightJustPressed(2);

			}

			/**
			 * Shrinking border system
			 */
			if (gameMode == 1) {

				border = new Rectangle(borderPosX, borderPosY, borderPosWidth,
						borderPosHeight);
				if (gameState.getCurTick() < 5) {

					borderPosX = 32;
					borderPosY = 50;
					borderPosWidth = 1200;
					borderPosHeight = 896;

				}

				if (gameState.getCurTick() % 150 == 0) {
					borderPosX += 2;
					borderPosY += 2;
					borderPosWidth -= 4;
					borderPosHeight -= 4;
				}
			}

			/**
			 * Tron speed boost system.
			 */
			if (gameMode == 4) {
				if (kh.isBtn1Pressed(1)) {
					if (gameState.getCurTick() % 4 == 0) {
						snek1.move();
					}

				}
				if (kh.isBtn1Pressed(2)) {
					if (gameState.getCurTick() % 4 == 0) {
						snek2.move();

					}

				}

			}

			if (gameState.getCurTick() % 5 == 0) {

				/**
				 * If this gamemode works with snek1, Then we can let snek1
				 * move. otherwise just return true Like this isn't needed but
				 * what the hey
				 */

				if (snek1IsDead == false) {
					snek1.move();

				}
				if (modesForSnek2.contains(gameMode) && snek2IsDead == false) {
					snek2.move();
				}
				if (modesForPvp.contains(gameMode)) {

					for (int i = 0; i < snek2.body.size(); i++) {
						if (i < snek2.body.size()) {
							if (snek1.head.intersects(snek2.body.get(i))) {
								snek1IsDead = true;

							}
						} else {
							i = snek2.body.size() - 1;
						}

					}

					for (int a = 0; a < snek1.body.size(); a++) {
						if (a < snek1.body.size()) {
							if (snek2.head.intersects(snek1.body.get(a))) {
								snek2IsDead = true;

							}

						} else {
							a = snek1.body.size() - 1;
						}
					}

				}
			}

			/**
			 * Player 1 controls
			 * 
			 */

			if (kh.wasDownJustPressed(1)) {
				snek1.setDirection(2);
			} else if (kh.wasLeftJustPressed(1)) {
				snek1.setDirection(4);

			} else if (kh.wasRightJustPressed(1)) {
				snek1.setDirection(6);

			} else if (kh.wasUpJustPressed(1)) {
				snek1.setDirection(8);

			}

			/**
			 * Player 2 controls
			 */
			if (modesForSnek2.contains(gameMode)) {
				if (kh.wasDownJustPressed(2)) {
					snek2.setDirection(2);
				}
				if (kh.wasLeftJustPressed(2)) {
					snek2.setDirection(4);
				}
				if (kh.wasRightJustPressed(2)) {
					snek2.setDirection(6);
				}
				if (kh.wasUpJustPressed(2)) {
					snek2.setDirection(8);
				}
			}

			/**
			 * THis handles death
			 */
			if (modesForPvp.contains(gameMode)) {
				/**
				 * If it contains PvP we'll assume there is snek1 & snek2. And
				 * we'll go ahead and check if snek1 or snek2 die and
				 * automatically set it to dead state
				 */
				if (snek1IsDead == true || snek2IsDead == true) {
					System.out.println("Debugging here, Error: Death-004");
					gameState.toState(DEAD);
				}

			} else {
				if (modesForSnek1.contains(gameMode)) {
					if (modesForSnek2.contains(gameMode)) {
						if (snek1IsDead == true && snek2IsDead == true) {
							System.out
									.println("Debugging here, Error: Death-005");

							gameState.toState(DEAD);

						}
					} else {
						if (snek1IsDead == true) {
							// System.out
							// .println("Debugging here, Error: Death-006");

							System.out
									.println("Debugging here, Error: Death-003");
							gameState.toState(DEAD);

						}
					}
				}
			}

			/**
			 * Checks to instantiate to death, I did not use our arraylist
			 * because I thought it was just to complicated.
			 */

		} else if (gameState.inState(DEAD)) {
			if (tokens == 0) {
				if (gameState.getCurTick() > 450) {
					gameOver = true;
				}

			} else {
				if (gameState.getCurTick() > 450) {
					tokens--;
					gameMode = 0;
					canDrawSnek1 = false;
					canDrawSnek2 = false;
					snek1IsDead = false;
					snek2IsDead = false;
					gameState.toState(MENU);

				}

			}

		}

	}

	/**
	 * Required method for implementing Game interface.. renders all Game
	 * elements to the Graphics2D variable
	 */

	public void gameRender(Graphics2D g) {

		if (gameState.getCurTick() % 20 == 0) {
			Random random = new Random();
			final float hue = random.nextFloat();
			final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
			final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
			rainbow = Color.getHSBColor(hue, saturation, luminance);
		}

		if (modesForNips.contains(gameMode) && canDrawNip == false) {
			nip = new Nip(xpos * 8 + 4, ypos * 8);
			canDrawNip = true;

		}
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screen.width, screen.height);

		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(5));
		g.draw(border);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));

		/**
		 * These boxes are the ones that create the entrences into the gamemode
		 * selection
		 */
		if (gameState.inState(MENU)) {

			g.setColor(Color.BLACK);
			g.fill(box1);
			// g.draw(box1);
			// g.draw(box2);
			g.fill(box2);
			// g.draw(box3);
			g.fill(box3);
			// g.draw(box4);
			g.fill(box4);

			// create menu snake
			menuSnek.draw(g, 3, Color.YELLOW);
			/**
			 * Flashing color and moving color system.
			 */

			if (gameState.getCurTick() % 2 == 0) {

				if (fontsize < 26) {
					fontsize++;
				} else {

					fontsize = fontsize - 12;
				}

				if (messageYMovement3 > 120) {
					messageYMovement3 -= 10;
				}
				if (messageYMovement4 > 140) {
					messageYMovement4 -= 10;
				}
				if (messageYMovement5 > 160) {
					messageYMovement5 -= 10;
				}
				if (messageYMovement6 > 180) {
					messageYMovement6 -= 10;
				}
			}

			for (int i = 0; i < fontArray.size(); i++) {
				fontsize = fontArray.get(i);
				if (i == fontArray.size()) {
					i = 0;

				}

			}

			g.setColor(rainbow);
			g.setFont(new Font("Dialog", Font.PLAIN, 30));
			if (messageYMovement6 < 181) {

				if (xPosMessage1 > 600) {
					forward = false;
					// System.out.println("1");
				}
				if (xPosMessage1 < 500) {
					forward = true;
					// System.out.println("2");
				}
				if (forward) {
					xPosMessage1++;
					// System.out.println("3");

				}
				if (!forward) {
					xPosMessage1--;
					// System.out.println("4");
				}
			}
			g.drawString(message1, xPosMessage1, 100);
			g.setFont(new Font("Dialog", Font.PLAIN, fontsize));
			g.drawString(message3, xPosMessage1, messageYMovement3);
			g.drawString(message4, xPosMessage1, messageYMovement4);
			g.drawString(message5, xPosMessage1, messageYMovement5);
			g.drawString(message6, xPosMessage1, messageYMovement6);

		} else if (gameState.inState(READY)) {

			if (gameState.getCurTick() < 15) {
				return;// return to the menu before round 0 flashes on screen
			}

			/**
			 * This will run the countdown system, the reason it's 2.5 and not 0
			 * is because it's taking too long so I've set it to under 2.5 and
			 * it works fine this way
			 */
			if (gameState.getCurTick() % 140 == 1) {

				countdown -= 1;
			}

			g.setColor(rainbow);
			g.setFont(new Font("Dialog", Font.PLAIN, 36));
			g.drawString("Starting in:", 550, 400);
			if (countdown == 3) {
				g.setColor(Color.GREEN);
			} else if (countdown == 2) {
				g.setColor(Color.yellow);
			} else if (countdown == 1) {
				g.setColor(Color.red);
			}
			g.drawString(String.valueOf(countdown), 610, 440);
			g.setColor(Color.ORANGE);
			g.setFont(new Font("Dialog", Font.PLAIN, 12));
			g.drawString(message, 300, 400);

			/**
			 * They all need player 1 But I was getting a null pointer So I just
			 * had to ;-;
			 */
			if (modesForSnek1.contains(gameMode)) {
				snek1.draw(g, 1, Color.GREEN);

			} else {
				canDrawSnek1 = canDrawSnek1;
			}

			/**
			 * If our gamemode is 2,3,4 which will need snek2 then let's draw
			 * him.
			 */
			if (modesForSnek2.contains(gameMode)) {

				if (canDrawSnek2 = true) {
					snek2.draw(g, 2, Color.RED);
				}
			}
			/**
			 * FLASHING COLORS
			 * 
			 */
			// menuMessageXPos
			if (gameState.getCurTick() % 2 == 0) {

				if (fontsize < 24) {
					fontsize++;

				} else {
					for (int i = 0; i < 24 && i > 5; i++) {
						i = fontsize;
					}
				}

				if (messageYMovement > 25) {
					messageYMovement -= 5;
				}

				if (xPosMessage2 > 700) {
					forward2 = false;
					// System.out.println("1");
				}
				if (xPosMessage2 < 400) {
					forward2 = true;
					// System.out.println("2");
				}
				if (forward2) {
					xPosMessage2 += 5;
					// System.out.println("3");

				}
				if (!forward2) {
					xPosMessage2 -= 5;
					// System.out.println("4");
				}

			}
			g.setColor(rainbow);
			g.setFont(new Font("Dialog", Font.PLAIN, fontsize));

			g.drawString(currentGame, xPosMessage2 - fontsize * 2,
					messageYMovement);

		} else if (gameState.inState(PLAYING)) {

			/**
			 * Snake Warefare bullet system
			 */

			/**
			 * Doesn't need an array list one bullet per person on the screen at
			 * once (bullet1) and (bullet2)
			 * 
			 * Setup the bullet to allow it to be created at a set x & y. - DONE
			 * 
			 * 
			 */

			if (gameMode == 5) {

				if (kh.wasBtn1JustPressed(1)) {

					bullet1 = new Bullet(snek1.head.x, snek1.head.y,
							snek1.getDirection());

					bullet1Spawned = true;
				}
				if (kh.wasBtn1JustPressed(2)) {
					bullet2 = new Bullet(snek2.head.x, snek2.head.y,
							snek2.getDirection());

					bullet2Spawned = true;
				}

				if (bullet1Spawned == true) {
					bullet1.draw(g);
					bullet1.move();
					for (int a = 0; a < snek2.body.size(); a++) {
						if (a < snek2.body.size()) {
							if (bullet1.body.intersects(snek1.body.get(a))) {
								// snek2IsDead = true;

							}

						}
					}
				}
				if (bullet2Spawned == true) {
					bullet2.draw(g);
					bullet2.move();
					for (int a = 0; a < snek1.body.size(); a++) {
						if (a < snek1.body.size()) {
							if (bullet1.body.intersects(snek1.body.get(a))) {
								snek1.body.remove(a);

							}

						}
					}
				}

			}

			/**
			 * FLASHING COLORS
			 * 
			 */
			// menuMessageXPos
			if (gameState.getCurTick() % 2 == 0) {

				if (fontsize < 24) {
					fontsize++;

				} else {
					for (int i = 0; i < 24 && i > 5; i++) {
						i = fontsize;
					}
				}

				if (messageYMovement > 25) {
					messageYMovement -= 5;
				}

				if (xPosMessage2 > 700) {
					forward2 = false;
					// System.out.println("1");
				}
				if (xPosMessage2 < 400) {
					forward2 = true;
					// System.out.println("2");
				}
				if (forward2) {
					xPosMessage2 += 5;
					// System.out.println("3");

				}
				if (!forward2) {
					xPosMessage2 -= 5;
					// System.out.println("4");
				}
			}

			g.setColor(rainbow);
			g.setFont(new Font("Dialog", Font.PLAIN, fontsize));

			g.drawString(currentGame, xPosMessage2 - fontsize * 2,
					messageYMovement);

			// System.out.println(" " + messageYMovement);

			/**
			 * While we're playing let's just always generate our rando, #'s for
			 * nipple cords
			 */

			xpos = randGen.nextInt(150);
			ypos = randGen.nextInt(112);

			/**
			 * PVP (Intersection Debugging notes)
			 */

			if (snek1.isDead() == true) {
				snek1IsDead = true;
				snek1.draw(g, 1, dead);
			} else if (modesForSnek2.contains(gameMode)) {
				if (snek2.isDead() == true) {
					snek2IsDead = true;
					snek2.draw(g, 2, dead);
				}
			}

			/**
			 * If our GameMode is equal to 1,2,3 which are the game modes that
			 * use nips then let's spawn them in
			 */
			// NIP IS BEING USED
			if (modesForNips.contains(gameMode)) {
				if (canDrawNip == true) {
					nip.draw(g);
				} else {
					System.out.println("Nips are NOT playing.");
				}
			}
			// nip.draw(g);

			/**
			 * They all need player 1 But I was getting a null pointer So I just
			 * had to ;-;
			 */
			if (modesForSnek1.contains(gameMode)) {
				snek1.draw(g, 1, Color.GREEN);

			} else {
				canDrawSnek1 = canDrawSnek1;
			}

			/**
			 * If our gamemode is 2,3,4 which will need snek2 then let's draw
			 * him.
			 */
			if (modesForSnek2.contains(gameMode)) {

				if (canDrawSnek2 = true) {
					snek2.draw(g, 2, Color.RED);
				} else {
					canDrawSnek2 = canDrawSnek2;
				}
			}

			/**
			 * Make sure our NIBBLE is being generated within the border, if he
			 * is not then we will have him placed in a newly generated location
			 * but first we will go ahead and check if it's suppose to be
			 * playing
			 */
			if (modesForNips.contains(gameMode)) {
				if (canDrawNip = true) {
					if (nip.niprec.y < border.getMinY()
							|| nip.niprec.y > border.getMaxY()
							|| nip.niprec.x > border.getMaxX()
							|| nip.niprec.x < border.getMinX()) {
						nip.setPosition(xpos * 8 + 4, ypos * 8);
					}
				}
			}
			/**
			 * Check if snek1 is active if so go ahead and make sure if it
			 * touches the border that it will then set itself to die
			 */

			if (snek1.head.x > border.getMaxX()
					|| snek1.head.x < border.getMinX()
					|| snek1.head.y < border.getMinY()
					|| snek1.head.y > border.getMaxY()) {

				System.out.println("Debugging here, Error: intersection-002");

				snek1IsDead = true;

			}

			/**
			 * This time we will check if snek2 is active and if it we will go
			 * ahead and check if it's hitting the border then if it is, we'll
			 * go ahead and set it to dead BUT NOT THE GAMESTATE DEAD and we
			 * don't need to check if there is a snek1 because if snek2 is
			 * playing then snek1 should be playing as well player 2 can't play
			 * if player 1 isn't playing.
			 */
			if (modesForSnek2.contains(gameMode)) {
				if (snek2.head.x > border.getMaxX()
						|| snek2.head.x < border.getMinX()
						|| snek2.head.y < border.getMinY()
						|| snek2.head.y > border.getMaxY()) {
					snek2IsDead = true;
				}
			}

			/**
			 * IF WE ARE IN CO-OP COMPETITIVE MAKE INTERACTION DEADLY
			 */
			// snek 1 hitting snek2
			if (gameMode == 3) {
				for (int i = 0; i < snek2.body.size(); i++) {
					if (snek1.head.intersects(snek2.body.get(i))) {
						snek1IsDead = true;

					} else {
						if (i == snek2.body.size()) {
							i = 0;
						}
					}
				}
				// snek 2 hitting snek1
				for (int j = 0; j < snek1.body.size(); j++) {
					if (snek2.head.intersects(snek1.body.get(j))) {
						snek2IsDead = true;
					} else {
						if (j == snek1.body.size()) {
							j = 0;
						}
					}
				}

			}
			// 1280, 1024
			if (modesForSnek1.contains(gameMode)) {

				if (modesForNips.contains(gameMode)
						&& snek1.head.intersects(nip.niprec)) {

					if (gameMode == 2) {
						snek1.grow();
						snek2.grow();
					} else {
						snek1.grow();
					}

					nip.setPosition(xpos * 8 + 4, ypos * 8);

				}

			}

			if (modesForNips.contains(gameMode)) {

				if (modesForSnek2.contains(gameMode)
						&& snek2.head.intersects(nip.niprec)) {
					if (gameMode == 2) {
						snek1.grow();
						snek2.grow();
					} else {
						snek2.grow();
					}

					nip.setPosition(xpos * 8 + 4, ypos * 8);

				}
			} else {
				for (int i = 0; i < gameState.getCurTick() + 200; i++) {
					if (i % 10 == 0) {
						snek1.grow();
						snek2.grow();
					}
				}
			}

			g.drawString(message, 600 + messagePos.x, 400);

		} else if (gameState.inState(DEAD)) {

			if (snek1IsDead) {
				snek1.draw(g, 1, dead);
			} else {
				snek1.draw(g, 1, rainbow);
			}

			/**
			 * If our gamemode is 2,3,4 which will need snek2 then let's draw
			 * him.
			 */
			if (modesForSnek2.contains(gameMode)) {

				if (canDrawSnek2 = true) {
					if (snek2IsDead) {
						snek2.draw(g, 1, dead);
					} else {
						snek2.draw(g, 1, rainbow);
					}
				} else {
					canDrawSnek2 = canDrawSnek2;
				}
			}
			/**
			 * 5 seconds of leaving snake trail
			 */

			g.setColor(rainbow);
			g.setFont(new Font("Dialog", Font.PLAIN, 24));
			g.drawString("Better luck next time", 525, 425);
			g.drawString(message, 600 + messagePos.x, 400);

		}
	}

	/**
	 * Required method for implementing Game interface
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Sorry, I can't remember now why we need this method ...
	 */
	public Graphics2D getGraphics2D() {
		return mg.getGraphics2D();
	}

}
