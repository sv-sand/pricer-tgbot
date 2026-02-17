package ru.svsand.pricer.tgbot.bot;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link BotMenu}.
 */
class BotMenuTest {

	@Test
	void constructor() {
		BotMenu menu = new BotMenu();
		assertNotNull(menu);
	}

	@Test
	void userMenu() {
		// Action
		SetMyCommands commands = BotMenu.userMenu();

		// Assert
		assertNotNull(commands, "SetMyCommands must not be null");
		assertTrue(commands.getScope() instanceof BotCommandScopeDefault,
				"Scope must be BotCommandScopeDefault");

		List<BotCommand> botCommands = commands.getCommands();
		assertNotNull(botCommands, "Commands list must not be null");
		assertEquals(5, botCommands.size(), "There must be exactly 5 commands");

		assertCommand(botCommands.get(0), "/help", "Помощь");
		assertCommand(botCommands.get(1), "/searches_list", "Список всех поисков");
		assertCommand(botCommands.get(2), "/new_search", "Создать новый поиск товара");
		assertCommand(botCommands.get(3), "/delete_search", "Удалить поиск товара");
		assertCommand(botCommands.get(4), "/statistic", "Статистика поисков");
	}

	// Checks

	private void assertCommand(BotCommand actual, String expectedCmd, String expectedDesc) {
		assertNotNull(actual, "BotCommand must not be null");
		assertEquals(expectedCmd, actual.getCommand(), "Unexpected command");
		assertEquals(expectedDesc, actual.getDescription(), "Unexpected description");
	}
}
