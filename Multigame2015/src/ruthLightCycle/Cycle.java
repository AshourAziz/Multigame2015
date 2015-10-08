package ruthLightCycle;

import java.util.*;
import java.awt.*;

import multigame.KeyHandler;


public class Cycle {

  private Rectangle head;
  private Rectangle tip;
  private ArrayList<Rectangle> tail;

  private double x,y,w,h;
  
  private int MAXVELOCITY = 2, MINVELOCITY = 1;
  // MINVELOCITY <= velocity <= MAXVELOCITY

  private int velocity;
  // 4 - LEFT, 6 - RIGHT, 2 - DOWN, 8 - UP
  private int direction;
  private final int UP = 8;
  private final int LEFT = 4;
  private final int DOWN = 2;
  private final int RIGHT = 6;
  
  private Color color;
  private boolean alive;
  private int pWidth, pHeight;//panel dimensions
  
  private int playerNum;//1 to 4
  private KeyHandler kh;

  private boolean debug = true;

  /**
   * Constructor: (x,y) coordinates of initial head position
   */
  public Cycle(int x, int y, Color c, KeyHandler kh, int playerNum, int w,
      int h) {
    this.x = x;
    this.y = y;
    head = new Rectangle(x, y, 20, 20);
    direction = UP;// default direction
    color = c;
    this.kh = kh;
    this.playerNum = playerNum;
    head = new Rectangle(x, y, 5, 5);
    tip = new Rectangle(x + 1, y + 1, 3, 3);
    velocity = MINVELOCITY;
    alive = true;
    tail = new ArrayList<Rectangle>();
    pWidth = w;
    pHeight = h;
  }

  public int getDir() {
    return direction;
  }

  public Color getColor() {
    return color;
  }

  public boolean isAlive() {
    return alive;
  }

  public void live(boolean tf) {
    alive = tf;
  }

//  public void draw(Graphics g) {
//    Graphics2D g2 = (Graphics2D) g;
//    g2.setColor(color);
//    if (alive) {
//      g2.fill(head);
//      for (Rectangle r : tail)
//        g2.fill(r);
//    } else {
//      g2.draw(head);
//      for (Rectangle r : tail)
//        g2.draw(r);
//    }
//    g2.setColor(Color.white);
//    g2.fill(tip);
//
//  }

  /**
   * called from the move method... checks the KeyHandler for the game to see if
   * the current direction of the cycle needs to change... .. if so, it changes
   * the direction
   */
  public void checkForDirectionChange() {

    // if currently going up...
    if (direction == UP) {
      // if player playerNum joystick is NOT up
      // then change direction if either left or right is pressed
      // ... note do not move down (which is opposite of up)
      if (!kh.isUpPressed(playerNum)) {
        if (kh.isLeftPressed(playerNum))
          turn(LEFT);
        if (kh.isRightPressed(playerNum))
          turn(RIGHT);
      }
    }
    if (direction == LEFT) {
      if (!kh.isLeftPressed(playerNum)) {
        if (kh.isUpPressed(playerNum))
          turn(UP);
        if (kh.isDownPressed(playerNum))
          turn(DOWN);
      }
    }
    if (direction == DOWN) {
      if (!kh.isDownPressed(playerNum)) {
        if (kh.isLeftPressed(playerNum))
          turn(LEFT);
        if (kh.isRightPressed(playerNum))
          turn(RIGHT);
      }
    }
    if (direction == RIGHT) {
      if (!kh.isRightPressed(playerNum)) {
        if (kh.isUpPressed(playerNum))
          turn(UP);
        if (kh.isDownPressed(playerNum))
          turn(DOWN);
      }
    }
    // obviously we need all four direction cases

  }// end checkForDirectionChange()

  /**
   * called from the move method... checks the KeyHandler for the game to see if
   * the current direction of the cycle needs to change... .. if so, it changes
   * the direction
   */
  public void checkForAccelerating() {

    if (kh.isBtn1Pressed(playerNum)) {
      velocity = MAXVELOCITY;
    } else
      velocity = MINVELOCITY;

  }// end checkForAccelerating()

  /**
   * converts the current direction from the viewpoint of the player to the
   * actual screen orientation... recall player 1 sits on the left side which is
   * why as player 1 sees the cycle going UP... the screen actually displays the
   * cycle going RIGHT
   */
  public int rotatedDirection() {

    if (debug)
      return direction;

    if (playerNum == 1) {
      if (direction == UP)
        return RIGHT;
      if (direction == LEFT)
        return UP;
      if (direction == DOWN)
        return LEFT;
      if (direction == RIGHT)
        return DOWN;
    }

    if (playerNum == 2) {
      if (direction == UP)
        return LEFT;
      if (direction == LEFT)
        return DOWN;
      if (direction == DOWN)
        return RIGHT;
      if (direction == RIGHT)
        return UP;
    }

    if (playerNum == 3) {
      if (direction == UP)
        return DOWN;
      if (direction == LEFT)
        return RIGHT;
      if (direction == DOWN)
        return UP;
      if (direction == RIGHT)
        return LEFT;
    }

    // default: player 4 ... no rotation
    return direction;

  }// end rotatedDirection

  /**
   * update the position and other fields if necessary
   */
  public void move() {
    if (!alive)
      return;

    checkForDirectionChange();
    checkForAccelerating();

    int rdir = rotatedDirection();

    if (rdir == UP) {
      head.y -= velocity;
      head.height += velocity;
      tip.y -= velocity;
    }
    if (rdir == DOWN) {
      // head.y += velocity;
      head.height += velocity;
      tip.y += velocity;
    }
    if (rdir == LEFT) {
      head.x -= velocity;
      head.width += velocity;
      tip.x -= velocity;
    }
    if (rdir == RIGHT) {
      // head.x += velocity;
      head.width += velocity;
      tip.x += velocity;
    }

  }// end move()

  public boolean collidesWith(Cycle cyc) {
    if (tip.intersects(cyc.head))
      return true;
    for (Rectangle rect : cyc.tail) {
      if (tip.intersects(rect))
        return true;
    }
    return false;
  }

  public boolean collidesWithSelf() {
    int last = tail.size() - 2;
    for (int i = 0; i < last; i++) {
      if (tip.intersects(tail.get(i)))
        return true;
    }
    return false;
  }

  public boolean collidesWithWall() {
    // check if cycle has left the game grid
    int rdir = rotatedDirection();
    //System.out.println("" + pWidth + "," + pHeight);
    return (rdir == DOWN && tip.y + tip.height >= pHeight || rdir == LEFT
        && tip.x <= 0 || rdir == RIGHT && tip.x + tip.width >= pWidth || rdir == UP
        && tip.y <= 0);

  }

  public void turn(int dir) {

    // add the current head rectangle to the tail
    tail.add(head);
    // create a new head Rectangle
    head = new Rectangle(tip.x - 1, tip.y - 1, 5, 5);

    // int rdir= rotatedDirection();
    // if (rdir == RIGHT) {
    // head = new Rectangle(head.x + head.width - 5, head.y, 5, 5);
    // } else if (rdir == DOWN) {
    // head = new Rectangle(head.x, head.y + head.height - 5, 5, 5);
    // } else {
    // head = new Rectangle(head.x, head.y, 5, 5);
    // }
    // tip = new Rectangle(head.x + 1, head.y + 1, 3, 3);

    direction = dir;// change direction
  }

  /**
   * render the RuthCycle to Graphics2D g2d
   * 
   */
  public void draw(Graphics2D g2d) {
    g2d.setColor(color);
    if (alive) {
      g2d.fill(head);
      for (Rectangle r : tail)
        g2d.fill(r);
    } else {
      g2d.draw(head);
      for (Rectangle r : tail)
        g2d.draw(r);
    }
    g2d.setColor(Color.white);
    g2d.fill(tip);

  }// end draw(Graphics g)

}// end class RuthCycle