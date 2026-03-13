package ru.svsand.pricer.tgbot.bot;

import ru.svsand.pricer.tgbot.logic.User;

/**
 * Maps Telegram API DTOs to domain objects.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 28.01.2026
 */
public class BotObjectMapper {

	/**
	 * Converts a Telegram {@link org.telegram.telegrambots.meta.api.objects.User} DTO
	 * to the domain {@link User} object.
	 *
	 * @param user the Telegram user DTO
	 * @return the corresponding domain user
	 */
	public static User fromDto(org.telegram.telegrambots.meta.api.objects.User user) {
		return User.builder()
				.tgId(user.getId())
				.name(user.getUserName())
				.build();
	}
}
