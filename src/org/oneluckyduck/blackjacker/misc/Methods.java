package org.oneluckyduck.blackjacker.misc;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;

public class Methods {

	public static boolean openMenu(final Entity e, final boolean fast) {
		if (fast) {
			final Point p = e.getCentralPoint();
			Mouse.hop(p.x, p.y);
			return Mouse.click(false);
		} else {
			return Mouse.click(e.getCentralPoint(), false);
		}
	}

	public static void stopScript(final String s) {
		s(s);
		Context.get().getScriptHandler().shutdown();
	}

	public static boolean isWidgetChildVisible(final WidgetChild wc) {
		return wc != null && wc.validate() && wc.visible() && wc.isOnScreen();
	}

	public static boolean isWidgetVisible(final Widget w) {
		return w != null && w.validate();
	}

	public static boolean isInGame() {
		return Game.isLoggedIn() && Game.getClientState() == 11
				&& !Context.resolve().refreshing && !Lobby.isOpen();
	}

	public static void s(final String s) {
		Variables.status = s;
		System.out.println("[Thieves Guild Blacjacker] " + s);
	}

	public boolean isIdle() {
		return (Players.getLocal() != null && !Players.getLocal().isMoving()
				&& Players.getLocal().getAnimation() == -1 && (Players
				.getLocal().getInteracting() == null || Players.getLocal()
				.getInteracting().getHpRatio() == 0));
	}

	public static int getPerHour(final int base, final long time) {
		return (int) ((base) * 3600000D / (System.currentTimeMillis() - time));
	}

	public static Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}
}
