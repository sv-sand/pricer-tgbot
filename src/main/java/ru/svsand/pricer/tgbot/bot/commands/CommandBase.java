package ru.svsand.pricer.tgbot.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Abstract base class for all bot commands. Provides default fallback implementations
 * for {@link #process}, {@link #processAnswer}, and {@link #processCallback} that return
 * an error message, so subclasses only need to override the methods they actually handle.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 05.11.2025
 */
public abstract class CommandBase implements Command {

	protected CommandService commandService;

	/**
	 * Constructs the command with access to the shared {@link CommandService}.
	 *
	 * @param commandService the service used for routing and answer-waiting state management
	 */
	public CommandBase(CommandService commandService) {
		this.commandService = commandService;
	}

	/**
	 * {@inheritDoc}
	 * <p>Default implementation returns an error message indicating no handler is set.</p>
	 */
	@Override
	public SendMessage process(Update update) {
		return SendMessage.builder()
				.chatId(update.getMessage().getFrom().getId())
				.text("Упс, что-то пошло не так... Не установлен обработчик команды")
				.build();
	}

	/**
	 * {@inheritDoc}
	 * <p>Default implementation returns an error message indicating no answer handler is set.</p>
	 */
	@Override
	public SendMessage processAnswer(Update update) {
		return SendMessage.builder()
				.chatId(update.getMessage().getFrom().getId())
				.text("Упс, что-то пошло не так... Не установлен ответ команды")
				.build();
	}

	/**
	 * {@inheritDoc}
	 * <p>Default implementation returns an error message indicating no callback handler is set.</p>
	 */
	@Override
	public SendMessage processCallback(Update update) {
		return SendMessage.builder()
				.chatId(update.getMessage().getFrom().getId())
				.text("Упс, что-то пошло не так... Не установлен колбек команды")
				.build();
	}
}
