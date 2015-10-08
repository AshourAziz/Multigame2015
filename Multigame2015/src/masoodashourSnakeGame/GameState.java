package masoodashourSnakeGame;

import multigame.*;

/**
 * This class handles all of the game states and the timing and switching
 * between states of your Game
 */
public class GameState {

	private MultiGame mg;
	private int numStates = 4;

	public static final int READY = 0;
	public static final int PLAYING = 1;
	public static final int DEAD = 2;
	public static final int MENU = 3;

	private int[] ticks = new int[numStates];
	private int[] maxTicks = new int[numStates];

	private int curState = MENU;

	/**
	 * Constructor has a parameter so GameState has a reference to the MultiGame
	 * Object.
	 */
	public GameState(MultiGame mg) {

		this.mg = mg;
		maxTicks[READY] = 450;// 3 seconds by default
		maxTicks[PLAYING] = 18450;// 2 minutes and 3 seconds
		maxTicks[DEAD] = 1500;// 10 seconds
		maxTicks[MENU] = 9000;// 1 minutes
	}

	/**
	 * Constructor has a parameter so GameState has a reference to the MultiGame
	 * Object.
	 */
	public GameState(MultiGame mg, int numStates, int[] maxTickVals) {

		this.mg = mg;
		ticks = new int[numStates];
		maxTicks = new int[numStates];
		maxTicks[READY] = 90000;// 3 seconds by default
		maxTicks[PLAYING] = 90000;// 10 minutes
		maxTicks[DEAD] = 1500;// 4 seconds
		maxTicks[MENU] = 90000;// 10 minutes
	}

	/**
	 * This method should be called in gameUpdate() only once. It keeps track of
	 * how many frames the Game has spent in each state and will switch states
	 * after a specified number of frames.
	 */
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

	/**
	 * Sets the number of ticks of the current state to 0.
	 */
	public void resetTicks() {
		ticks[curState] = 0;
	}

	/**
	 * Returns the the number of updates (ticks) that have been done in the
	 * current GameState.
	 */
	public int getCurTick() {
		return ticks[curState];
	}

	/**
	 * Returns the number of updates (ticks) that will occur before
	 * automatically switching to the next GameState.
	 */
	public int getCurTicksLeft() {
		return maxTicks[curState] - ticks[curState];
	}

	/**
	 * Returns the total number of updates (ticks) that will occur before
	 * automatically switching to the next GameState.
	 */
	public int getCurMaxTicks() {
		return maxTicks[curState];
	}

	/**
	 * Returns true if the current GameState is state, false otherwise.<br>
	 * Sample usage: if (gameState.inState(gameState.READY)) ...
	 */
	public boolean inState(int state) {
		return curState == state;
	}

	/**
	 * Zeros out the ticks of the current GameState and changes the GameState to
	 * state.
	 */
	public void toState(int state) {
		ticks[curState] = 0;
		curState = state;
	}

}