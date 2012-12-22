package org.oneluckyduck.blackjacker.nodes;

import org.oneluckyduck.blackjacker.misc.Const;
import org.oneluckyduck.blackjacker.misc.Methods;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class Luring extends Node {

	@Override
	public void execute() {
		final NPC trainer = NPCs.getNearest(Const.ADVANCED_TRAINER_ID);
		Methods.s("Opening menu");
		Methods.openMenu(trainer, true);
		Methods.s("Waiting for menu");
		final Timer t = new Timer(2000);
		while (!Menu.isOpen()) {
			Task.sleep(5);
			if (!t.isRunning())
				break;
		}
		if (Menu.isOpen() && Menu.contains("Lure", Const.NAME)) {
			Methods.s("Selecting lure");
			Menu.select("Lure", Const.NAME);
			Methods.s("Sleeping");
			final Timer timer = new Timer(2000);
			while (timer.isRunning()) {
				Task.sleep(5);
			}
			Methods.s("Opening menu");
			Methods.openMenu(trainer, true);
			Methods.s("Waiting for menu");
			while (!Menu.isOpen()) {
				Task.sleep(5);
			}
			if (Menu.isOpen() && Menu.contains("Knock-out", Const.NAME)) {
				Methods.s("Knocking the fatass out");
				Menu.select("Knock-out", Const.NAME);
				Timer timer1 = new Timer(5600);
				while (trainer.getAnimation() != 12413) {
					Task.sleep(10);
					if (!timer1.isRunning()) {
						Methods.s("Think i failed");
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean activate() {
		final NPC trainer = NPCs.getNearest(Const.ADVANCED_TRAINER_ID);
		return Methods.isInGame() && trainer != null
				&& trainer.getAnimation() != 12413;
	}
}