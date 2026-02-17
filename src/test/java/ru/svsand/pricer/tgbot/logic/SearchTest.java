package ru.svsand.pricer.tgbot.logic;

import org.junit.jupiter.api.Test;
import ru.svsand.pricer.tgbot.Data;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

	@Test
	void isNew_True() {
		Search search = Search.builder().build();
		assertTrue(search.isNew());
	}

	@Test
	void isNew_False() {
		Search search = Data.search();
		assertFalse(search.isNew());
	}

	@Test
	void getRepresentation() {
		Search search = Data.search();
		assertEquals("test keyword - 100.00₽", search.getRepresentation());
	}

	@Test
	void getRepresentation_Undefined() {
		Search search = Search.builder().build();
		assertEquals("<undefined>", search.getRepresentation());

		search.setKeyWords("keywords");
		assertEquals("<undefined>", search.getRepresentation());
	}
}