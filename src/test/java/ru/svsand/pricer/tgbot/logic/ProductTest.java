package ru.svsand.pricer.tgbot.logic;

import org.junit.jupiter.api.Test;
import ru.svsand.pricer.tgbot.Data;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

	@Test
	void getUser() {
		Product product = Data.product();
		assertEquals(product.getSearch().getUser(), product.getUser());
	}

	@Test
	void getRepresentationHtml() {
		Product product = Data.product();
		assertEquals("<a href=\"https://example.com\">test product</a> - 100.00₽", product.getRepresentationHtml());
	}

	@Test
	void getRepresentationHtml_Undefined() {
		Product product = Product.builder().build();
		assertEquals("<undefined>", product.getRepresentationHtml());

		product = Product.builder().build();
		product.setStoreProductLink("https://example.com");
		assertEquals("<undefined>", product.getRepresentationHtml());
		product.setName("name");
		assertEquals("<undefined>", product.getRepresentationHtml());

		product = Product.builder().build();
		product.setName("name");
		assertEquals("<undefined>", product.getRepresentationHtml());
		product.setPrice(100.0);
		assertEquals("<undefined>", product.getRepresentationHtml());

		product = Product.builder().build();
		product.setStoreProductLink("https://example.com");
		assertEquals("<undefined>", product.getRepresentationHtml());
		product.setPrice(100.0);
		assertEquals("<undefined>", product.getRepresentationHtml());
	}

	@Test
	void isNew_True() {
		Product product = Product.builder().build();
		assertTrue(product.isNew());
	}

	@Test
	void isNew_False() {
		Product product = Product.builder()
				.id(1L)
				.build();
		assertFalse(product.isNew());
	}
}