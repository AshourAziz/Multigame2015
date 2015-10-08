package ruthHeliGame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import multigame.*;

/**
 * Copter will be the Helicopter Object responsible for holding its position and
 * smoke trail positions as well as rendering itself, smoke trail an collision
 * detection with cave and obstacles.
 */

public class Copter {

  private HeliGame hg;
  private Rectangle body;
  private KeyHandler kh;
  private Rectangle viewPort;
  private int playerNum;
  private boolean alive = true;
  private ArrayList<Rectangle> tail;
  private int numTailPieces;
  private int tailPieceWidth;
  private double offset;

  private double velocity = 0.0;
  private double yPos = 0.0;
  private final double ACCELUP = 0.04;
  private final double ACCELDOWN = 0.05;
  private final double MAXVELOCITY = 1.2;
  private final double MINVELOCITY = -1.6;
  private double caveVelocity;
  
  private BufferedImage copterBody;
  private Stroke stk;

  private ruthHeliGame.Sound sound;
  private boolean soundPlaying = false;

  /**
   * Constructor will get the initial size and position
   */
  public Copter(HeliGame hg, Rectangle viewPort, KeyHandler kh, int playerNum) {
    this.hg = hg;
    int blockWidth = viewPort.width / 18;
    this.viewPort = viewPort;
    body = new Rectangle(viewPort.x + blockWidth * 5, viewPort.y
        + viewPort.height / 2 - blockWidth / 2, blockWidth * 2 - 8,
        blockWidth - 8);
    yPos = body.y;
    this.kh = kh;
    this.playerNum = playerNum;
    sound = new Sound();
    String []soundFiles = {"thrust.wav", "explode.wav"};
    sound.loadSoundFiles(soundFiles);
    tail = new ArrayList<Rectangle>();
    numTailPieces = 14;
    tailPieceWidth = (int) ((double) (body.x - viewPort.x) / numTailPieces + 0.5);
    for (int i = 0; i < numTailPieces; i++) {
      tail.add(new Rectangle());
    }
    try {
      copterBody = ImageIO.read(getClass().getResource("copter2.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    stk = new BasicStroke(5);
    
    caveVelocity = hg.getCave().getVelocity();
    
    
  }

  /**
   * update all appropriate fields.. only called if playing
   */
  public void updatePos() {
    // body update
    if (!alive) {
      soundPlaying = false;
      sound.stop(0);
      return;
    }
    if (kh.isBtn1Pressed(playerNum)) {// if thrusting
      velocity += ACCELUP;
      if (velocity > MAXVELOCITY)
        velocity = MAXVELOCITY;
      if (!soundPlaying) {
        soundPlaying = true;
        sound.play(0,false);
      }
    } else {
      velocity -= ACCELDOWN;
      if (velocity < MINVELOCITY)
        velocity = MINVELOCITY;
      soundPlaying = false;
      sound.stop(0);
    }
    yPos -= velocity;
    body.y = (int) (yPos + 0.5);

    // tail update
    // offset += hg.getCave().getVelocity();
    // offset = hg.getCave().getOffset();
    offset += caveVelocity;
    int offSetTemp = (int) (offset + 0.5);
    for (int i = 0; i < numTailPieces; i++) {
      tail.get(i).x = viewPort.x + i * tailPieceWidth - offSetTemp;
    }
    if (offset > tailPieceWidth) {
      offset -= tailPieceWidth;
      tail.add(new Rectangle(body.x - tailPieceWidth, body.y + tailPieceWidth,
          tailPieceWidth, tailPieceWidth));
      tail.remove(0);
    }
  }

  /**
   * render entire Copter to g
   */
  public void render(Graphics2D g) {

    g.setColor(Color.blue);
    double val = 0.04;
    if (kh.isBtn1Pressed(playerNum)) {
      g.rotate(-val, body.x + body.width / 2, body.y + body.height / 2);
      g.drawImage(copterBody, null, body.x, body.y);
      // g.fill(body);
      g.rotate(val, body.x + body.width / 2, body.y + body.height / 2);
    } else {
      g.rotate(val, body.x + body.width / 2, body.y + body.height / 2);
      g.drawImage(copterBody, null, body.x, body.y);
      // g.fill(body);
      g.rotate(-val, body.x + body.width / 2, body.y + body.height / 2);
    }

    if (alive) {
      g.setColor(Color.WHITE);
      for (Rectangle r : tail) {
        // g.fill(r);
        g.fillOval(r.x + 1, r.y + 1, r.width -3, r.height -2);
      }
    } else {
      int sp = 15;
      //Stroke stroke = g.getStroke();
      g.setStroke(stk);
      g.setColor(Color.red);
      g.drawOval(body.x - sp, body.y - sp, body.width + 2 * sp, body.height + 2
          * sp);
      g
          .drawLine(body.x - 5, body.y + body.height, body.x + body.width + 5,
              body.y);
      //g.setStroke(stroke);
    }

  }

  public void setAlive(boolean tf) {
    alive = tf;
  }

  public boolean isAlive() {
    return alive;
  }

  public Rectangle getBody() {
    return body;
  }
  
}