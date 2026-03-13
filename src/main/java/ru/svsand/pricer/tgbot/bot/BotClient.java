package ru.svsand.pricer.tgbot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

/**
 * Spring-managed Telegram API client backed by OkHttp.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 01.02.2026
 */
@Component
public class BotClient extends OkHttpTelegramClient {

	/**
	 * Creates the client using the configured bot token.
	 *
	 * @param token the Telegram bot API token (from {@code bot.token} property)
	 */
	public BotClient(@Value("${bot.token}") String token) {
		super(token);
	}
}
