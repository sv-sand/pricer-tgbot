package ru.svsand.pricer.tgbot.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BotClientTest {
	@Test
	void constructor() {
		BotClient client = new BotClient("test_token");
		assertNotNull(client);
	}

	@Test
	void constructor_EmptyToken() {
		BotClient client = new BotClient("");
		assertNotNull(client);
	}
}
