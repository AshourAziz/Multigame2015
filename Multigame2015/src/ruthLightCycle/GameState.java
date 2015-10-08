package ruthLightCycle;

import multigame.*;

/**
 * This class handles all of the game states and the timing and switching
 * between states of your Game
 */
public class GameState {

  private MultiGame mg;
  private int numStates = 3;

  public final int READY = 0;
  public final int PLAYING = 1;
  public final int DEAD = 2;

  private int[] ticks = new int[numStates];
  private int[] maxTicks = new int[numStates];

  private int curState = READY;

  public GameState(MultiGame mg) {

    this.mg = mg;
    maxTicks[READY] = 300;// 2 seconds by defaultdays
    maxTicks[PLAYING] = 15000;// 100 seconds
    maxTicks[DEAD] = 300;// 2 seconds
  }

  public void tick() {
    ticks[curState]++;
    if (ticks[curState] > maxTicks[curState]) {
      switch (curState) {

      case READY:
        toState(PLAYING);
        break;
      case PLAYING:
        // should never be reached
        toState(DEAD);
        break;
      case DEAD:
        // should never be reached
        toState(READY);
        break;
      default:
        ;
      }
    }
  }

  public void resetTicks() {
    ticks[curState] = 0;
  }

  public int getCurTick() {
    return ticks[curState];
  }

  public boolean inState(int state) {
    return curState == state;
  }

  public void toState(int state) {
    ticks[curState] = 0;
    curState = state;
  }

}