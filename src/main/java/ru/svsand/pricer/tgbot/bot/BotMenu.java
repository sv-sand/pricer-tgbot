package ru.svsand.pricer.tgbot.bot;

import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

/**
 * Factory for Telegram bot menu command sets.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 28.01.2026
 */
public class BotMenu {

	/**
	 * Builds the default user command menu displayed in the Telegram UI.
	 *
	 * @return a {@link SetMyCommands} request containing all user-facing commands
	 */
	public static SetMyCommands userMenu() {
		return SetMyCommands.builder()
				.scope(new BotCommandScopeDefault())
				.command(new BotCommand("/help", "Помощь"))
				.command(new BotCommand("/searches_list", "Список всех поисков"))
				.command(new BotCommand("/new_search", "Создать новый поиск товара"))
				.command(new BotCommand("/delete_search", "Удалить поиск товара"))
				.command(new BotCommand("/statistic", "Статистика поисков"))
				.build();
	}
}
