package org.oneluckyduck.blackjacker.misc.skilldata;

import java.util.Collections;
import java.util.Map;

import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class SkillData {
	private static int widget;
	private static final Map<Integer, Integer> MAP = Collections
			.unmodifiableMap(new WidgetMap());

	public static int getRemainderExperience(final int index) {
		setWidget(index);
		if (Tabs.getCurrent() != Tabs.STATS) {
			Tabs.STATS.open();
		}
		final WidgetChild skills = Widgets.get(WidgetInfo.WIDGET,
				WidgetInfo.WIDGET_POPUP);
		if (isWidgetChildVisible(skills)) {
			final WidgetChild skill = Widgets.get(WidgetInfo.WIDGET, widget);
			Mouse.move(skill.getCentralPoint());
			String contents = skills.getChild(
					WidgetInfo.WIDGET_POPUP_REMAINDER_XP).getText();
			return parseExperience(contents);
		}
		return -1;
	}

	public static int getExperience(final int index) {
		setWidget(index);
		if (Tabs.getCurrent() != Tabs.STATS) {
			Tabs.STATS.open();
		}
		final WidgetChild skills = Widgets.get(WidgetInfo.WIDGET,
				WidgetInfo.WIDGET_POPUP);
		if (isWidgetChildVisible(skills)) {
			final WidgetChild skill = Widgets.get(WidgetInfo.WIDGET, widget);
			Mouse.move(skill.getCentralPoint());
			String contents = skills.getChild(
					WidgetInfo.WIDGET_POPUP_CURRENT_XP).getText();
			return parseExperience(contents);
		}
		return -1;
	}

	public static int getTotalExperience() {
		setWidget(SkillIndices.TOTAL_LEVEL);
		if (Tabs.getCurrent() != Tabs.STATS) {
			Tabs.STATS.open();
		}
		final WidgetChild skills = Widgets.get(WidgetInfo.WIDGET,
				WidgetInfo.WIDGET_POPUP);
		if (isWidgetChildVisible(skills)) {
			final WidgetChild skill = Widgets.get(WidgetInfo.WIDGET, widget);
			Mouse.move(skill.getCentralPoint());
			String contents = skills.getChild(WidgetInfo.WIDGET_POPUP_LEVEL)
					.getText();
			return parseTotalExperience(contents);
		}
		return -1;
	}

	public static int getTotalLevel() {
		if (Tabs.getCurrent() != Tabs.STATS) {
			Tabs.STATS.open();
		}
		final WidgetChild skills = Widgets.get(WidgetInfo.WIDGET,
				WidgetInfo.WIDGET_POPUP);
		if (isWidgetChildVisible(skills)) {
			final WidgetChild level = Widgets.get(WidgetInfo.WIDGET,
					WidgetInfo.WIDGET_TOTAL_LEVEL);
			String contents = level.getText();
			return parseTotalLevel(contents);
		}
		return -1;
	}

	public static int getRealLevel(final int index) {
		setWidget(index);
		if (Tabs.getCurrent() != Tabs.STATS) {
			Tabs.STATS.open();
		}
		final WidgetChild skills = Widgets.get(WidgetInfo.WIDGET,
				WidgetInfo.WIDGET_POPUP);
		if (isWidgetChildVisible(skills)) {
			final WidgetChild skill = Widgets.get(WidgetInfo.WIDGET, widget);
			Mouse.move(skill.getCentralPoint());
			String contents = skills.getChild(WidgetInfo.WIDGET_POPUP_LEVEL)
					.getText();
			return parseRealLevel(contents);
		}
		return -1;
	}

	public static int getLevel(final int index) {
		setWidget(index);
		if (Tabs.getCurrent() != Tabs.STATS) {
			Tabs.STATS.open();
		}
		final WidgetChild skills = Widgets.get(WidgetInfo.WIDGET,
				WidgetInfo.WIDGET_POPUP);
		if (isWidgetChildVisible(skills)) {
			final WidgetChild skill = Widgets.get(WidgetInfo.WIDGET, widget);
			Mouse.move(skill.getCentralPoint());
			String contents = skills.getChild(WidgetInfo.WIDGET_POPUP_LEVEL)
					.getText();
			return parseLevel(contents);
		}
		return -1;
	}

	private static int parseExperience(final String s) {
		String xp;
		if (s.length() > 0) {
			xp = s;
			if (xp.contains(",")) {
				xp = xp.replace(",", "");
			}
			return Integer.parseInt(xp);
		}
		return -1;
	}

	private static int parseTotalExperience(final String s) {
		String xp;
		if (s.contains("XP: ")) {
			xp = s.substring(s.indexOf(": ") + 2);
			if (xp.contains(",")) {
				xp = xp.replace(",", "");
			}
			return Integer.parseInt(xp);
		}
		return -1;
	}

	private static int parseTotalLevel(final String s) {
		String level;
		if (s.contains("el: ")) {
			level = s.substring(s.indexOf(": ") + 2);
			return Integer.parseInt(level);
		}
		return -1;
	}

	private static int parseRealLevel(final String s) {
		String level;
		if (s.contains(": ") && s.contains("/")) {
			level = s.substring(s.indexOf("/") + 1);
			return Integer.parseInt(level);
		} else if (s.contains("embe")) {
			return -1;
		}
		return -1;
	}

	private static int parseLevel(final String s) {
		String level;
		if (s.contains(": ") && s.contains("/")) {
			level = s.substring(s.indexOf(": ") + 2, s.indexOf("/"));
			return Integer.parseInt(level);
		} else if (s.contains("embe")) {
			return -1;
		}
		return -1;
	}

	private static boolean isWidgetChildVisible(final WidgetChild wc) {
		return wc != null && wc.validate() && wc.visible() && wc.isOnScreen();
	}

	private static void setWidget(final int index) {
		widget = MAP.get(index);
	}
}
