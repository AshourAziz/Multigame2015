package ruthHeliGame;

import java.awt.*;
import java.util.ArrayList;

/**
 * Copter will be the Helicopter Object responsible for holding its position and
 * smoke trail positions as well as rendering itself, smoke trail an collision
 * detection with cave and obstacles.
 */

public class Cave {

  private HeliGame hg;
  private ArrayList<Rectangle> top;
  private ArrayList<Rectangle> bottom;
  private ArrayList<Rectangle> blocks;
  private Rectangle viewPort;

  // private int playerNum;

  private double velocity;
  private double offset;

  private int blockWidth;
  private int blockHeight;
  private int caveHeight;
  private final int minCaveHeight ;
  private static int numTiles = 18;

  private int blockCount;
  private int caveIncrement;
  private int caveRun;
  private int buffer = 10;

  /**
   * Constructor will get the initial size and position
   */
  public Cave(HeliGame hg, Rectangle viewPort) {
    this.hg = hg;
    // this.playerNum = playerNum;

    velocity = 1.8;
    blockCount = 0;
    blockWidth = viewPort.width / numTiles;// number of tiles in cave
    top = new ArrayList<Rectangle>();
    bottom = new ArrayList<Rectangle>();
    blocks = new ArrayList<Rectangle>();
    caveHeight = (int) (0.80 * viewPort.height);// 80% of viewPort height
    minCaveHeight = (int) (0.55 * viewPort.height);// 55% of viewPort height
   
    int caveTopHeight = (int) (0.10 * viewPort.height);// 15% of viewPort height
    blockHeight = (int) (2.0 * blockWidth) - 10;

    caveIncrement = 2;
    caveRun = 5;
    buffer = 10;

    for (int i = 0; i <= numTiles; i++) {
      top.add(new Rectangle(viewPort.x + i * blockWidth, viewPort.y,
          blockWidth, caveTopHeight));
      bottom.add(new Rectangle(viewPort.x + i * blockWidth, viewPort.y
          + viewPort.height - caveTopHeight, blockWidth, caveTopHeight - 1));
      blocks
          .add(new Rectangle(viewPort.x + i * blockWidth, viewPort.y, -1, -1));
    }
    this.viewPort = viewPort;
  }

 

  /**
   * update all appropriate fields.. only called if playing
   */
  public void updatePos() {

    offset += velocity;
    int offSetTemp = (int) (offset + 0.5);
    // shift all Rectangles in top
    for (int i = 0; i <= numTiles; i++) {
      top.get(i).x = viewPort.x + i * blockWidth - offSetTemp;
      bottom.get(i).x = viewPort.x + i * blockWidth - offSetTemp;
      blocks.get(i).x = viewPort.x + i * blockWidth - offSetTemp;
    }

    if (offset > blockWidth) {
      blockCount++;
      offset -= blockWidth;
      top.remove(0);
      bottom.remove(0);
      blocks.remove(0);
      Rectangle lastTop = top.get(top.size() - 1);// top.get(top.size()-1);
      // Rectangle lastBottom = bottom.getLast();// top.get(top.size()-1);
      // Rectangle lastBlock = blocks.getLast();
      // int rand = (int) (Math.random() * 5) - 2;
      // int ht = 0;

      caveRun--;
      int newHeight;
      newHeight = lastTop.height + caveIncrement;
      if (caveRun <= 0 || newHeight < buffer
          || newHeight + caveHeight > viewPort.height - buffer) {
        caveRun = (int) (Math.random() * 10) + 3;
        //caveRun = 8;
        if (caveIncrement < 0)
          caveIncrement = (int) (Math.random() * 12) + 2;
        else
          caveIncrement = -(int) (Math.random() * 12) - 2;
        newHeight = lastTop.height + caveIncrement;
      }

      top.add(new Rectangle(lastTop.x + blockWidth, viewPort.y, blockWidth,
          newHeight));

      int newYpos = viewPort.y + newHeight + caveHeight;

      bottom.add(new Rectangle(lastTop.x + blockWidth, newYpos, blockWidth,
          viewPort.height + viewPort.y - newYpos));

      if (blockCount % 12 == 3) {
        caveHeight-=4;
        if (caveHeight < minCaveHeight)
          caveHeight = minCaveHeight;
        int newPos = (int) (Math.random() * (caveHeight - blockHeight - 2 * buffer))
            + newHeight + buffer;
        blocks.add(new Rectangle(lastTop.x + blockWidth, viewPort.y + newPos,
            blockWidth, blockHeight));
      } else {
        blocks.add(new Rectangle(lastTop.x + blockWidth, viewPort.y, 0, 0));
      }
    }
  }

  /**
   * render entire Cave to g
   */
  public void render(Graphics2D g) {

    g.setColor(Color.red);
    // cave top
    for (Rectangle r : top) {
      g.setColor(Color.green);
      g.fill(r);
      g.setColor(Color.red);
      //g.draw(r);
    }
    // cave bottom
    for (Rectangle r : bottom) {
      g.setColor(Color.green);
      g.fill(r);
      g.setColor(Color.red);
      //g.draw(r);
    }
    // blocks
    for (Rectangle r : blocks) {
      g.setColor(Color.green);
      g.fill(r);
      g.setColor(Color.red);
      //g.draw(r);
    }

    // g.fill(body);

  }

  public boolean hasCollision(Copter cop) {
    Rectangle body = cop.getBody();
    boolean result = false;
    for (Rectangle r : top) {
      if (r.intersects(body)) {
        result = true;
      }
    }
    for (Rectangle r : bottom) {
      if (r.intersects(body)) {
        result = true;
      }
    }
    for (Rectangle r : blocks) {
      if (r.intersects(body)) {
        result = true;
      }
    }
    if (result && cop.isAlive())
      hg.getSound().play(1, false);
    return result;
  }

  public static int numTiles() {
    return numTiles;
  }

  public double getVelocity() {
    return velocity;
  }

  public double getOffset() {
    return offset;
  }

  public int getBlockWidth() {
    return blockWidth;
  }
}