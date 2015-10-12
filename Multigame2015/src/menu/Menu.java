package menu;

import javax.imageio.ImageIO;

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import multigame.*;

/**
 * class Menu handles the choosing and launching of a game. It displays each
 * game screenshot (600x600 image) in a scrolling list
 */
public class Menu {

	// private long code = 123456L;

	private MultiGame mg;
	private KeyHandler kh;
	private ArrayList<String> gameList;
	private ArrayList<Integer> currentStat;
	private ArrayList<BufferedImage> imageList;
	private int gameIndex;
	private Sound sound;
	private Font infoFont;
	private String[] soundFiles = // soundFiles
	{ "coin.wav", // sound1
	// "mii.wav" // sound 2
	};

	private int swipeTicks = 0;
	private int messageTicks = 0;
	private String idNum = "";
	private String[] message = { "", "", "" };

	private String creditsFileName;
	private EasyReader inFileCredits;
	private EasyReader inFileLog;
	private EasyWriter outFileCredits;
	private HashMap<String, Integer> creditsMap;
	private boolean statsPrinted;

	/**
	 * constructor takes a MultiGame as a parameter
	 */
	public Menu(MultiGame mg) {
		this.mg = mg;
		this.gameList = mg.getGameList();
		this.imageList = mg.getImageList();
		this.kh = mg.getKeyHandler();
		this.gameIndex = mg.getGameIndex();
		infoFont = new Font("Lucida Console", Font.BOLD, 28);
		currentStat = new ArrayList<Integer>();
		initMenu();

		// sound.loadSoundFiles(soundFiles);
	}

	/**
	 * Called each time menu state is entered to init instance variables and
	 * load sounds and images.
	 */
	public void initMenu() {
		/**
		 * This will check if we've printed the stats at the begining of the
		 * game if it hasn't we'll print them.
		 */
		if (!statsPrinted) {
			statsPrinted = true;

			try {
				this.praseLog();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// force garbage collection ...
		sound = new Sound();
		sound.loadSoundFiles(soundFiles);
		gameIndex = mg.getGameIndex();
		// gameIndex = 1;
		// load the image list
		imageList = new ArrayList<BufferedImage>();
		String dirString = "menuimages";
		File dir = new File("menuimages");
		// menuimages resides in the src folder in eclipse project folder
		// a copy is in the bin for running MultiGame outside of eclipse
		if (!dir.exists()) {
			dir = new File("src\\menuimages");
			dirString = "src\\menuimages";
		} else if (!dir.exists()) {
			dir = new File(".");
		}

		String dirList[] = dir.list();
		for (String str : dirList) {
			try {
				File imageFile = new File(dirString + "\\" + str);
				BufferedImage bi = ImageIO.read(imageFile);
				imageList.add(bi);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Similar method to gameUpdate()
	 */
	public void update() {
		if (kh.wasRightJustPressed(1)) {
			gameIndex = (gameIndex + 1) % imageList.size();
			mg.getMultiGameState().resetMenuTicks();
		} else if (kh.wasLeftJustPressed(1)) {
			gameIndex = (gameIndex - 1);
			if (gameIndex < 0)
				gameIndex = imageList.size() - 1;
			mg.getMultiGameState().resetMenuTicks();
		}
		// mg.setGameIndex(gameIndex);

	}

	/**
	 * Similar to gameRender(Graphics2D g) renders all text and graphics
	 * (images) while in menu state.. players 1 and 4 can use their arrow keys
	 * to scroll
	 */
	public void render(Graphics2D g) {

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, mg.pWidth(), mg.pHeight());

		// draw all text
		g.setColor(Color.YELLOW);
		g.setFont(infoFont);

		// g.drawString("~~ Game Index ~~ " + gameIndex, 50, 50);

		g.drawString("This game has been played: " + currentStat.get(gameIndex)
				+ " times.", 50, 50);

		String line1 = "...", line2 = "....";

		if (mg.getFreePlay()) {
			line1 = "FREE PLAY.. ";
			line2 = "PRESS BTN TO START SELECTED GAME..";
		} else if (mg.getCredits() > 1) {
			line1 = "..";
			line2 = "PRESS BTN TO START SELECTED GAME..            ";
		} else if (mg.getCredits() == 1) {
			line1 = "..";
			line2 = "PRESS BTN TO START SELECTED GAME..";
		} else if (mg.getCredits() == 0) {
			line1 = "..";
			line2 = "INSERT $0.25";
		}

		// g.drawString(line1, 20, 715);
		g.drawString(line2, 50, mg.pHeight() - 40);

		g.drawString("CREDITS : " + mg.getCredits(), mg.pWidth() - 300,
				mg.pHeight() - 40);

		int numImg = imageList.size();
		try {
			int bigImgX = (mg.pWidth() - 600) / 2;
			// bigImgX = 300;
			g.drawImage(imageList.get(gameIndex), bigImgX,
					(mg.pHeight() - 600) / 2, 600, 600, null);
			int g1 = gameIndex - 2;
			int g2 = gameIndex - 1;
			int g4 = gameIndex + 1;
			int g5 = gameIndex + 2;

			if (gameIndex == 0) {
				g1 = numImg - 2;
				g2 = numImg - 1;
			} else if (gameIndex == 1) {
				g1 = numImg - 1;
			} else if (gameIndex == numImg - 1) {
				g4 = 0;
				g5 = 1;
			} else if (gameIndex == numImg - 2) {
				g5 = 0;
			}
			// int spacer = ( bigImgX - 120
			g.drawImage(imageList.get(g1), 60, 500, 60, 60, null);
			g.drawImage(imageList.get(g2), 150, 500, 60, 60, null);
			g.drawImage(imageList.get(g4), 1280 - 60 - 60, 500, 60, 60, null);
			g.drawImage(imageList.get(g5), 1280 - 60 - 150, 500, 60, 60, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (messageTicks > 0) {
			g.setColor(Color.YELLOW);
			g.fillRect(212 + 50, 84 + 50, 500, 500);
			Point pos = new Point(300, 200);
			for (int i = 0; i < message.length; i++) {
				g.setColor(Color.RED);
				g.drawString(message[i], pos.x, pos.y);
				pos.y += 50;
			}
			messageTicks--;
		}
	}

	/**
	 * Sets the image list to null so as to free up some memory while a game is
	 * playing<br>
	 * This method is currently not functioning...<br>
	 * DO NOT CALL THIS METHOD!
	 */
	public void destroyMenu() {
		sound.stopAll();
		imageList = null;
		// sound = null;//to release memory
	}

	/**
	 * Plays the credit sound once.
	 */
	public void playCreditSound() {
		sound.play(0, false);
	}

	/**
	 * Returns current gameIndex.
	 */
	public int getGameIndex() {
		return gameIndex;
	}

	/**
	 * This will return the amount of times the SubString is in the String
	 */
	public static int count(final String string, final String substring) {
		int count = 0;
		int idx = 0;

		while ((idx = string.indexOf(substring, idx)) != -1) {
			idx++;
			count++;
		}

		return count;
	}

	/**
	 * This will handle loading the stats file, Adding the entire text file into
	 * a string. Then after it closes it, it will print the stats by looping
	 * through all the gamemodes
	 */
	public String praseLog() throws IOException {

		File file = new File("stats.txt");
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.seperator");

		try {
			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			// System.out.println(fileContents);
			return fileContents.toString();

		} finally {
			scanner.close();
			// System.err.println("-------------- STATS -------------");
			for (int i = 0; i < gameList.size(); i++) {
				String game = String.valueOf(mg.stats.get(i));

				// System.out.println(game + " -----> "
				// + count(String.valueOf(fileContents), gameList.get(i))
				// + " times.");

				// currentStat.add(gameList.get(i) + "."
				// + count(String.valueOf(fileContents), gameList.get(i)));

				currentStat.add(count(String.valueOf(fileContents),
						gameList.get(i)));

			}
			// System.out.println(currentStat);
			// System.err.println("-------------- STATS -------------");

		}

	}

	/**
	 * Finds out the count of stats IDK how this works whatever
	 */

	public static int count(final String string, final char c) {
		return count(string, String.valueOf(c));
	}

}
