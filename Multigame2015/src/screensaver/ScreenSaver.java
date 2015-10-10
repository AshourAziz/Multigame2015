package screensaver;

import javax.swing.*;

import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import multigame.*;

public class ScreenSaver {
	private MultiGame mg;
	private Point pos;
	private int counter = 0;
	private Font font;
	Color rainbow;
	private int counting = 0;

	public ScreenSaver(MultiGame mg) {
		this.mg = mg;
		pos = new Point(200, 200);
		counter = 0;
		font = new Font("Lucida Console", Font.BOLD, 60);

	}

	public void render(Graphics2D g) {

		if (counter % 50 == 0) {
			pos.x = (int) (Math.random() * 300);
			pos.y = (int) (Math.random() * 300) + 300;

		}
		counter++;
		if (counting % 3 == 0) {
			Random random = new Random();
			final float hue = random.nextFloat();
			final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
			final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
			rainbow = Color.getHSBColor(hue, saturation, luminance);
		}

		counting++;

		g.setColor(rainbow);
		g.setFont(font);
		g.drawString("DM   FUNDRAISER", pos.x, pos.y);
		g.drawString("25 CENTS / GAME", pos.x, pos.y + 60);

		g.drawString("PUSH BUTTON", pos.x, pos.y + 120);
		g.drawString("FOR  MENU", pos.x, pos.y + 180);

	}
}