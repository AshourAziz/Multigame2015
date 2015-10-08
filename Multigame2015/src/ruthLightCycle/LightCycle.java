package ruthLightCycle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import multigame.*;

/**
 * LightCycle .. based on SimpleGame template
 */

public class LightCycle implements Game {

  // private RuthCycle2 cyc1,cyc2;
  private KeyHandler kh;
  private boolean gameOver;
  private Font font;
  private Font shadowFont;
  private FontMetrics metrics;

  private int frameCount = 0;
  private int numPlayers;
  private ArrayList<Cycle> cycles;
  private int scores[];
  private final int WINSCORE = 4;

  private int pWidth, pHeight;
  private MultiGame mg;

  private GameState gameState;

  public LightCycle(MultiGame mg) {
    pWidth = mg.pWidth();
    pHeight = mg.pHeight();
    // System.out.println("" + w + " , " + h);
    kh = mg.getKeyHandler();
    numPlayers = 2;
    font = new Font("Courier New", Font.BOLD, 36);
    shadowFont = new Font("Courier New", Font.BOLD, 38);
    this.mg = mg;
    metrics = mg.getFontMetrics(font);
    gameState = new GameState(mg);

    initGame();
  }

  public void initGame() {
    // invoked at the beginning of every game
    gameOver = false;
    scores = new int[4];// each score is 0 ..
    initRound();

  }

  public void initRound() {
    // invoked at the beginning of every round
    cycles = new ArrayList<Cycle>();
    // numPlayers should be 2,3 or 4
    cycles.add(new Cycle(100, pHeight / 2, Color.red, kh, 2, pWidth, pHeight));

    cycles.add(new Cycle(pWidth - 100, pHeight / 2, Color.blue, kh, 1, pWidth,
        pHeight));

    if (numPlayers >= 3)
      cycles
          .add(new Cycle(pWidth / 2, 100, Color.green, kh, 3, pWidth, pHeight));
    if (numPlayers >= 4)
      cycles.add(new Cycle(pWidth / 2, pHeight - 100, Color.cyan, kh, 4,
          pWidth, pHeight));
  }

  public void gameUpdate() {
    gameState.tick();

    if (gameState.inState(gameState.READY)) {
      if (gameState.getCurTick() == 1) {// just entered READY state
        if (arrayMax(scores) == WINSCORE) {
          gameOver = true;
          return;
        } else {
          initRound();
        }
      }
    }

    else if (gameState.inState(gameState.PLAYING)) {
      for (Cycle cyc : cycles)
        // move cycles
        cyc.move();
      checkCollisions();
      checkRoundOverGameOver();
    }

    else if (gameState.inState(gameState.DEAD)) {

    }

  }

  public void checkRoundOverGameOver() {
    // see how many are alive
    int numAlive = 0, playerAlive = 0;
    int len = cycles.size();
    for (int i = 0; i < len; i++) {
      Cycle cyc = cycles.get(i);
      if (cyc.isAlive()) {
        numAlive++;
        playerAlive = i;
      }
    }
    if (numAlive <= 1) { // if roundOver
      scores[playerAlive]++;
      gameState.toState(gameState.DEAD);
    }
  }

  public void checkCollisions() {
    for (int i = 0; i < numPlayers; i++) {
      Cycle cyc = cycles.get(i);
      if (!cyc.isAlive())
        continue;
      if (cyc.collidesWithWall() || cyc.collidesWithSelf())
        cyc.live(false);
      for (int j = 0; j < numPlayers; j++)
        if (i != j && cycles.get(j).isAlive()
            && cyc.collidesWith(cycles.get(j)))
          cyc.live(false);
    }
  }

  public static int WALLSPACER = 15;
  public static int WALLSPACER2 = 30;

  public void gameRender(Graphics2D g) {

    // clear the screen
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, mg.pWidth(), mg.pHeight());
    g.setColor(Color.BLACK);
    g.fillRect(WALLSPACER, WALLSPACER, pWidth - WALLSPACER2, pHeight
        - WALLSPACER2);
    for (Cycle cyc : cycles)
      cyc.draw(g);
    
    if (gameState.inState(gameState.READY)) {
      renderScores(g);
    }

    else if (gameState.inState(gameState.PLAYING)) {

    }

    else if (gameState.inState(gameState.DEAD)) {
      renderScores(g);
      if (arrayMax(scores) == WINSCORE) {
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Player: " + (arrayMaxIndex(scores) + 1) + " WINS!", 400,
            400);
      }
    }

  }

  public void renderScores(Graphics2D g2d) {
    String msg = "WINS: " + scores[0];// player 1 .. LEFT
    int x, y;
    x = (pWidth - metrics.stringWidth(msg)) / 2;
    y = (pHeight - metrics.getHeight()) / 2;
    // g2d.setColor(Color.red);
    // g2d.setFont(font);
    // g2d.drawString(msg, x, y);

    int spacer = 150;

    // player 1
    msg = "WINS: " + scores[0];// player 1 .. LEFT
    x = spacer;// spacer of 50
    y = (pHeight - metrics.stringWidth(msg)) / 2;

    g2d.translate(x, y);
    g2d.rotate(1.57);
    g2d.setFont(font);
    g2d.setColor(Color.WHITE);
    g2d.drawString(msg, 2, -2);
    g2d.setColor(cycles.get(0).getColor());
    g2d.drawString(msg, 0, 0);
    g2d.rotate(-1.57);
    g2d.translate(-x, -y);

    // player 2
    g2d.setColor(cycles.get(1).getColor());
    msg = "WINS: " + scores[1];// player 2 .. RIGHT
    x = pWidth - spacer;// spacer of 50
    y = (pHeight - metrics.stringWidth(msg)) / 2 + metrics.stringWidth(msg);

    g2d.translate(x, y);
    g2d.rotate(4.71);
    g2d.setFont(font);
    g2d.setColor(Color.WHITE);
    g2d.drawString(msg, 2, -2);
    g2d.setColor(cycles.get(1).getColor());
    g2d.drawString(msg, 0, 0);
    g2d.rotate(-4.71);
    g2d.translate(-x, -y);

    if (numPlayers >= 3) {
      // player 3
      msg = "WINS: " + scores[2];// player 1 .. LEFT
      x = (pWidth - metrics.stringWidth(msg)) / 2 + metrics.stringWidth(msg);
      y = spacer;

      g2d.translate(x, y);
      g2d.rotate(3.14);
      g2d.setFont(font);
      g2d.setColor(Color.WHITE);
      g2d.drawString(msg, 2, -2);
      g2d.setColor(cycles.get(2).getColor());
      g2d.drawString(msg, 0, 0);
      g2d.rotate(-3.14);
      g2d.translate(-x, -y);
    }

    if (numPlayers >= 4) {
      // player 4
      msg = "WINS: " + scores[3];// player 1 .. LEFT
      x = (pWidth - metrics.stringWidth(msg)) / 2;// spacer of 50
      y = pHeight - spacer;

      g2d.translate(x, y);
      // g2d.rotate(1.57);
      g2d.setFont(font);
      g2d.setColor(Color.WHITE);
      g2d.drawString(msg, 2, -2);
      g2d.setColor(cycles.get(3).getColor());
      g2d.drawString(msg, 0, 0);
      // g2d.rotate(-1.57);
      g2d.translate(-x, -y);
    }

    // g2d.translate(-500,-500);
    // g2d.rotate(-1.57);

    // g2d.setFont(font1);
    // g2d.setColor(Color.BLACK);

    // g2d.translate(200,150);g2d.rotate(-1.57);
    // g2d.drawString("ROTATE", 0, 0);
    // g2d.translate(-500,-500);
    // g2d.rotate(-1.57);

  }

  public boolean isGameOver() {
    return gameOver;
  }

  public int arrayMax(int a[]) {
    int max = 0;
    for (int i = 0; i < a.length; i++) {
      if (a[i] > max)
        max = a[i];
    }
    return max;
  }

  public int arrayMaxIndex(int a[]) {
    int imax = 0;
    for (int i = 0; i < a.length; i++) {
      if (a[i] > a[imax])
        imax = i;
    }
    return imax;
  }

}