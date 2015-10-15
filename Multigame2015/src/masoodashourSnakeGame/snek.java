package masoodashourSnakeGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import multigame.*;
import static masoodashourSnakeGame.GameState.*;

//main menu
// single player
// co-op
// competitive co-op
// tron - If tron no nibbles and no minus body
public class snek {
	int size;

	int direction;
	int bodyAdd;
	Rectangle head;
	ArrayList<Rectangle> body;
	ArrayList<Integer> directionQ;// 32
	private int gm;
	private int pn;

	public snek(int x, int y, int playerNum, int gameMode) {
		gm = gameMode;
		pn = playerNum;
		size = 8;
		if (playerNum == 1) {
			direction = 6;
		}
		if (playerNum == 2) {
			direction = 4;
		}

		// 10
		bodyAdd = 30;
		head = new Rectangle(x, y, size, size);
		body = new ArrayList<Rectangle>();
		directionQ = new ArrayList<Integer>();

	}

	public int getDirection() {
		return direction;
	}

	public void getHead(Rectangle h) {
		h = head;
	}

	public boolean isDead() {
		for (int i = 0; i < body.size(); i++) {

			if (body.get(i).intersects(head)) {
				return true;

			}
		}
		return false;
	}

	public void move() {

		body.add(0, new Rectangle(head.x, head.y, size, size));
		if (directionQ.size() != 0) {
			direction = directionQ.get(0);
			directionQ.remove(0);

		}

		if (direction == 4) {
			head.x -= size;
		}
		if (direction == 2) {
			head.y += size;
		}
		if (direction == 8) {
			head.y -= size;
		}
		if (direction == 6) {
			head.x += size;

		}
		if (bodyAdd > 0) {
			bodyAdd -= 1;
		} else {

			body.remove(body.size() - 1);
		}
	}

	public void grow() {

		if (gm == 1) {
			bodyAdd += 10;
		} else if (gm == 5) {
			// Do nothing because gameMode 5 is the bullet gamemode.
		} else {
		}
		bodyAdd += 5;
	}

	public void setDirection(int dirVal) {

		if (directionQ.isEmpty()) {
			if (direction + dirVal != 10) {
				directionQ.add(dirVal);
			}

		} else {
			if (directionQ.get(directionQ.size() - 1) != dirVal) {
				if (directionQ.get(directionQ.size() - 1) + direction != 10) {
					directionQ.add(dirVal);
				}
			}

		}

	}

	public void draw(Graphics2D g, int playerNum, Color c) {

		g.setColor(c);

		g.draw(head);

		for (int i = 0; i < body.size(); i++) {

			g.draw(body.get(i));

		}

	}

}
