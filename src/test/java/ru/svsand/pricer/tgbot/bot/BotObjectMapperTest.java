package ru.svsand.pricer.tgbot.bot;

import org.junit.jupiter.api.Test;
import ru.svsand.pricer.tgbot.Data;
import ru.svsand.pricer.tgbot.logic.User;

import static org.junit.jupiter.api.Assertions.*;

class BotObjectMapperTest {
	@Test
	void constructor() {
		BotObjectMapper mapper = new BotObjectMapper();
		assertNotNull(mapper);
	}

	@Test
	void fromDto() {
		// Arrange
		org.telegram.telegrambots.meta.api.objects.User telegramUser = Data.telegramUser();

		// Act
		User user = BotObjectMapper.fromDto(telegramUser);

		// Assert
		assertNotNull(user);
		assertEquals(1001L, user.getTgId());
		assertEquals("test_user", user.getName());
	}
}
