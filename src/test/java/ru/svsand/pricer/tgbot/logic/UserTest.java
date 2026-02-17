package ru.svsand.pricer.tgbot.logic;

import org.junit.jupiter.api.Test;
import ru.svsand.pricer.tgbot.Data;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

	@Test
	void isNew_True() {
		User user = User.builder().build();
		assertTrue(user.isNew());
	}

	@Test
	void isNew_False() {
		User user = Data.user();
		assertFalse(user.isNew());
	}
}