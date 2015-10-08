package ruthHeliGame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import multigame.*;

/**
 * simple fullscreen application.. used for learning and as a template for other
 * games
 * 
 */
public class HeliGame implements Game {

  private GameState gameState;
  private ruthHeliGame.Sound sound;
  int pWidth;
  int pHeight;

  private ArrayList<Copter> copters;
  // private ArrayList<Cave> caves;
  // private ArrayList<viewPort> viewPorts;
  // private Copter copter;
  private Cave cave;

  private Rectangle viewPort;
  private int numPlayers;
  private Color bgColor;
  private Color hiScoreColor;
  private int updateCount = 0;
  private Font font;
  private Color fontColor;
  private int round = 0;
  private final int ROUNDS = 7;
  private int scores[];

  private MultiGame mg;
  private KeyHandler kh;
  private boolean gameOver = false;

  /**
   * Constructor.. parameter is the number of frames per second
   */
  public HeliGame(MultiGame mg) {

    this.mg = mg;
    numPlayers = 2;
    scores = new int[numPlayers];
    sound = new ruthHeliGame.Sound();
    String soundFiles[] = { "thrust.wav", "explode.wav" };
    sound.loadSoundFiles(soundFiles);
    gameState = new GameState(mg);
    this.kh = mg.getKeyHandler();
    font = new Font("Digital-7", Font.BOLD, 30);
    fontColor = new Color(51, 204, 255);
    pWidth = mg.pWidth();
    pHeight = mg.pHeight();
    // viewPort = new Rectangle(spacer , pHeight / 2, pWidth - 2 * spacer ,
    // pHeight / 2);
    viewPort = new Rectangle(0, 0, 400, 300);

    bgColor = new Color(50, 50, 50);
    hiScoreColor = new Color(100, 0, 0);
    round = 1;

    initRound();
  }

  public void initRound() {
    cave = new Cave(this, viewPort);
    // caves = new ArrayList<Cave>();
    copters = new ArrayList<Copter>();
    // currently one player
    for (int i = 0; i < numPlayers; i++) {
      copters.add(new Copter(this, viewPort, kh, i + 1));
      // caves.add(new Cave(this, viewPort, i + 1));
    }
  }

  /**
   * called fps times per second .. it will 'tick' the gameState in order to
   * handle timing events NOTICE: the else-if conditions must match the
   * gameRender exactly
   */
  public void gameUpdate() {
    gameState.tick();
    if (gameState.inState(gameState.READY)) {
      if (gameState.getCurTick() == 1) {// just entered READY state
        if (round > ROUNDS) {
          gameOver = true;
          return;
        }
        initRound();
      }
    }

    else if (gameState.inState(gameState.PLAYING)) {
      updateCount++;
      for (int i = 0; i < numPlayers; i++) {
        copters.get(i).updatePos();
        if (copters.get(i).isAlive()) {
          scores[i]++;
        }
        // caves.get(i).updatePos();

        if (cave.hasCollision(copters.get(i))) {
          copters.get(i).setAlive(false);
          copters.get(i).updatePos();
        }
      }
      cave.updatePos();
      boolean allDead = true;
      for (int i = 0; i < numPlayers; i++) {
        allDead = allDead && !copters.get(i).isAlive();
      }
      if (allDead) {
        round++;
        // gameState.toReadyToStart();
        gameState.toState(gameState.DEAD);
      }

    }

    else if (gameState.inState(gameState.DEAD)) {

    }

  }

  private Point center = new Point(512, 384);

  /**
   * called as many times as possible to optimize frame rate
   */
  public void gameRender(Graphics2D g) {

    g.setColor(bgColor);
    g.fillRect(0, 0, pWidth, pHeight);

    // g.rotate(-Math.PI, pWidth / 2, pHeight / 2);
    g.setFont(font);
    for (int k = numPlayers; k >= 1; k--) {
      int extra = (viewPort.width - viewPort.height) / 2;
      int x = 0, y = 0;

      g.scale(1.4, 1.4);
      
      if (k == 2) {
        x = 340;
        y = 0;
        g.translate(x, y);

      } else if (k == 1) {
        x = -(mg.pHeight() - viewPort.width) / 2 - extra;
        y = (mg.pWidth() - viewPort.height) - extra;
        x = 340 ;//1280 / 2 - 600 / 2
        y = 350;
        g.translate(x, y);
      }

      // if (k == 1) {
      // g.translate(0, -viewPort.height);
      // }
      g.setColor(Color.black);
      g.fill(viewPort);

      if (gameState.inState(gameState.READY)) {
        g.setColor(fontColor);
        g.setFont(font);
        g.drawString("ROUND " + round + " OF " + ROUNDS, viewPort.x + 100,
            viewPort.y + 100);
        g.drawString("READY!", viewPort.x + 100, viewPort.y + 200);

        cave.render(g);
        copters.get(k - 1).render(g);
      }

      else if (gameState.inState(gameState.PLAYING)) {
        for (int i = 0; i < numPlayers; i++) {
          cave.render(g);
          copters.get(k - 1).render(g);
        }
      }

      else if (gameState.inState(gameState.DEAD)) {
        cave.render(g);
        copters.get(k - 1).render(g);
      }

      g.setColor(bgColor);
      g.fillRect(viewPort.x - cave.getBlockWidth(), viewPort.y,
          cave.getBlockWidth(), viewPort.height);
      g.fillRect(viewPort.x + viewPort.width, viewPort.y, cave.getBlockWidth(),
          viewPort.height);

      // draw scores in all states
      // g.setColor(Color.GRAY);
      // g.drawString("SCORE", viewPort.x + 300, viewPort.y - 30);
      g.setColor(Color.RED);
      boolean max = true;
      for (int score : scores) {
        if (score > scores[k - 1]) {
          max = false;
        }
      }
      if (max) {
        g.fillRect(viewPort.x + viewPort.width + 40, viewPort.y + 100, 100, 40);
      }
      g.setColor(fontColor);
      g.setFont(font);
      g.drawString("" + (scores[k - 1] / 4), viewPort.x + viewPort.width + 50, viewPort.y + 130);

      if (k == 2) {
        g.translate(-x, -y);
        // g.translate(-(viewPort.width - viewPort.height)/2,0);
      } else if (k == 1) {
        g.translate(-x, -y);
      }
      
      g.scale(0.7142857, 0.7142857);
      
    }// end for each player

  }

  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * returns the Sound variable
   */
  public Sound getSound() {
    return sound;
  }

  /**
   * returns the Sound variable
   */
  public Cave getCave() {
    return cave;
  }

  /**
   * returns the viewPort variable
   */
  public Rectangle getViewPort() {
    return viewPort;
  }
}// end class HeliGame