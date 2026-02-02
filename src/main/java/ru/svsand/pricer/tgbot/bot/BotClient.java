package ru.svsand.pricer.tgbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 01.02.2026
 */

@Component
public class BotClient {
	private final TelegramClient telegramClient;

	public BotClient(@Value("${bot.token}") String botToken) {
		telegramClient = new OkHttpTelegramClient(botToken);
	}

	public void sendMessage(@NotNull SendMessage message) throws TelegramApiException {
		telegramClient.execute(message);
	}

	public void setMenu(@NotNull SetMyCommands commands) throws TelegramApiException {
		telegramClient.execute(commands);
	}
}
