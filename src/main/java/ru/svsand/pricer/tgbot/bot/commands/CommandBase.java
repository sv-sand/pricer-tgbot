package ru.svsand.pricer.tgbot.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 05.11.2025
 */

public abstract class CommandBase implements Command {

	public static final String ID = "";
	protected CommandService commandService;
	public String getId() {
		return ID;
	}

	public CommandBase(CommandService commandService) {
		this.commandService = commandService;
	}

	@Override
	public SendMessage process(Update update) {
		return SendMessage.builder()
				.chatId(update.getMessage().getFrom().getId())
				.text("Упс, что-то пошло не так... Не установлен обработчик команды")
				.build();
	}

	@Override
	public SendMessage processCallback(Update update) {
		return SendMessage.builder()
				.chatId(update.getMessage().getFrom().getId())
				.text("Упс, что-то пошло не так... Не установлен колбек команды")
				.build();
	}

	@Override
	public SendMessage processAnswer(Update update) {
		return SendMessage.builder()
				.chatId(update.getMessage().getFrom().getId())
				.text("Упс, что-то пошло не так... Не установлен ответ команды")
				.build();
	}
}
