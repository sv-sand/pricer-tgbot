package ru.svsand.pricer.tgbot.bot;

import ru.svsand.pricer.tgbot.logic.User;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 28.01.2026
 */

public class BotObjectMapper {
	public static User fromDto(org.telegram.telegrambots.meta.api.objects.User user) {
		return User.builder()
				.tgId(user.getId())
				.name(user.getUserName())
				.build();
	}
}
