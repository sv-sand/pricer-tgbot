package ru.svsand.pricer.tgbot.logic;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 24.10.2025
 */

@Data
@Builder
@ToString(of = {"store", "name"})
public class Product {
	private Long id;

	private String name;
	private Store store;
	private Search search;
	private Long storeProductId;
	private String storeProductLink;
	private double price;
	private boolean userNotified;

	private Long version;

	public User getUser() {
		return search.getUser();
	}

	public String getRepresentationHtml() {
		return String.format("<a href=\"%s\">%s</a> - %.2f₽", storeProductLink, name, price);
	}

	public boolean isNew() {
		return id == null;
	}
}
