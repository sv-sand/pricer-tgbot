package ru.svsand.pricer.tgbot.logic;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Domain object representing a product found on a marketplace that matches a user's search.
 *
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
	private Double price;
	private Boolean userNotified;

	private Long version;

	/**
	 * Returns the user who owns the search this product belongs to.
	 *
	 * @return the owning {@link User}
	 */
	public User getUser() {
		return search.getUser();
	}

	/**
	 * Returns an HTML-formatted representation of this product with a clickable link and price.
	 * Returns {@code "<undefined>"} if any required field is missing.
	 *
	 * @return an HTML string suitable for a Telegram message
	 */
	public String getRepresentationHtml() {
		if (storeProductLink == null || name == null || price == null) {
			return "<undefined>";
		}
		return String.format("<a href=\"%s\">%s</a> - %.2f₽", storeProductLink, name, price);
	}

	/**
	 * Returns {@code true} if this product has not yet been persisted (has no database ID).
	 *
	 * @return {@code true} if the product is new
	 */
	public boolean isNew() {
		return id == null;
	}
}
