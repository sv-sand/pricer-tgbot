package ru.svsand.pricer.tgbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 01.02.2026
 */

@Component
public class BotClient extends OkHttpTelegramClient {
	public BotClient(@Value("${bot.token}") String token) {
		super(token);
	}
}
