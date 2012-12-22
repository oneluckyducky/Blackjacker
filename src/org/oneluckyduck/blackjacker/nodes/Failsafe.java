package org.oneluckyduck.blackjacker.nodes;

import org.oneluckyduck.blackjacker.misc.Const;
import org.oneluckyduck.blackjacker.misc.Methods;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class Failsafe extends Node {

	@Override
	public boolean activate() {
		final NPC trainer = NPCs.getNearest(Const.ADVANCED_TRAINER_ID);
		return Methods.isInGame() && trainer != null
				&& !trainer.getLocation().canReach();
	}

	@Override
	public void execute() {
		if (Methods.isInGame())
			Methods.stopScript("Advanced trainer is out of reach. Stopping script after running for "
					+ Const.TIMER.toElapsedString());

	}

}
