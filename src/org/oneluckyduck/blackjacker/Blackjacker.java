package org.oneluckyduck.blackjacker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.oneluckyduck.blackjacker.misc.Const;
import org.oneluckyduck.blackjacker.misc.Methods;
import org.oneluckyduck.blackjacker.misc.Variables;
import org.oneluckyduck.blackjacker.misc.skilldata.SkillData;
import org.oneluckyduck.blackjacker.nodes.Failsafe;
import org.oneluckyduck.blackjacker.nodes.Looting;
import org.oneluckyduck.blackjacker.nodes.Luring;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.LoopTask;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.input.Mouse.Speed;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.bot.Context;

@Manifest(authors = { "OneLuckyDuck" }, name = "Thieves Guild Blackjacking", description = "Knocks that fatass on his fat ass.", version = 1.02)
public class Blackjacker extends ActiveScript implements PaintListener,
		MessageListener {

	public static Tree jobContainer = null;
	public static ArrayList<Node> jobs = new ArrayList<Node>();
	static boolean world;

	public void onStop() {
		Environment.saveScreenCapture(String.format("%s-%d", Environment.getDisplayName(), Variables.gainedLevels));
	}

	public void onStart() {
		Mouse.setSpeed(Speed.FAST);
		Camera.setPitch(Random.nextInt(80, 92));
		Variables.startTime = System.currentTimeMillis();
		Variables.startingExperience = Settings.get(91) / 10;
		try{
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					final String input = (String) JOptionPane.showInputDialog(null, "Choose the world to relog into");
					Variables.world = input!= null && input.length() > 0 ? Integer.parseInt(input) : Random.nextInt(62, 79);
					world = true;
				}
				
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		while(!world) {
			Task.sleep(150);
		}
		Methods.s(String.format("Logging into world %d", Variables.world));
		Context.setLoginWorld(Variables.world);
		
		Variables.startingLevel = SkillData.getLevel(Skills.THIEVING);
		Methods.s(String.format("Starting at level %d", Variables.startingLevel));
		Tabs.INVENTORY.open();
		
		Variables.ready = !Variables.ready;
		
		getContainer().submit(new LoopTask() {

			@Override
			public int loop() {
				if (Methods.isInGame()) {
					if (Variables.ready) {
						if (Widgets.canContinue()) {
							Methods.s("Continuing");
							Keyboard.sendText(" ", false);
						}
					}
				} else {
					return 500;
				}
				return 10;
			}
		});
	}

	public int loop() {
		if (Methods.isInGame()) {
			if (Variables.ready) {
				if (jobContainer != null) {
					final Node job = jobContainer.state();
					if (job != null) {
						jobContainer.set(job);
						getContainer().submit(job);
						job.join();
					}
				} else {
					jobs.add(new Failsafe());
					jobs.add(new Luring());
					jobs.add(new Looting());
					jobContainer = new Tree(jobs.toArray(new Node[jobs.size()]));
				}
			}
		} else {
			return 500;
		}
		return 100;
	}

	public void onRepaint(Graphics g1) {
		if (!Methods.isInGame())
			return;
		if (Methods.isInGame()) {
			if(Variables.ready && world) {
				final Graphics2D g = (Graphics2D) g1;
	
				final int experienceGained = (Settings.get(91) / 10)
						- Variables.startingExperience;
				final int experienceHour = Methods.getPerHour(experienceGained,
						Variables.startTime);
	
				final int blackjacksHour = Methods.getPerHour(Variables.blackjacks,
						Variables.startTime);
	
				final String blackjacks = DecimalFormat.getInstance().format(
						Variables.blackjacks);
				final String experience = DecimalFormat.getInstance().format(
						experienceGained);
	
				final String experienceHourly = DecimalFormat.getInstance().format(
						experienceHour);
				final String blackjacksHourly = DecimalFormat.getInstance().format(
						blackjacksHour);
				final Point p = Mouse.getLocation();
	
				g.setColor(Mouse.isPressed() ? Color.GREEN : Color.white);
				g.drawOval(p.x - 3, p.y - 3, 6, 6);
	
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, (int) Game.getDimensions().getWidth(), 50);
	
				g.setColor(Color.GRAY);
				g.setFont(new Font("Arial", Font.BOLD, 11));
				g.drawString("Run Time: " + Const.TIMER.toElapsedString(), 3, 12);
				g.drawString(String.format("Experience Gained (hr): %s (%s)",
						experience, experienceHourly), 3, 25);
				g.drawString(String.format("Blackjacks (hr): %s (%s)", blackjacks,
						blackjacksHourly), 3, 38);
				g.drawString(
						String.format("Level info: %d/%d", Variables.startingLevel
								+ Variables.gainedLevels, Variables.startingLevel),
						180, 12);
	
				g.setFont(new Font("Kristen ITC", Font.BOLD, 11));
				g.drawString("Thieves Guild Blackjacking by OneLuckyDuck", 5, 375);
	
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setColor(Color.BLUE);
				g2.setFont(new Font("Arial", Font.BOLD, 12));
				g2.drawImage(Const.BAR, 0, 510, null, null);
				g2.drawString("Status: " + Variables.status, 15, 522);
			}
		}
	}

	@Override
	public void messageReceived(MessageEvent m) {
		final String message = m.getMessage();
		Variables.blackjacks = message.toLowerCase().contains("as politely ") ? Variables.blackjacks + 1
				: Variables.blackjacks;
		if (message.toLowerCase().contains("ongrat")) {
			Variables.gainedLevels++;
			Environment.saveScreenCapture(String.format("Gained level: %d",
					Variables.gainedLevels));
		}

	}

}