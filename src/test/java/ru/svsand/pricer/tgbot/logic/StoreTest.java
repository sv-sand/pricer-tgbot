package ru.svsand.pricer.tgbot.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

	@Test
	void valueOf() {
		// Action
		Store store = Store.valueOf("WB");

		// Assert
		assertEquals(Store.WB, store);
	}
}