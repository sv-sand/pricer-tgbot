package ru.svsand.pricer.tgbot.logic;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Domain object representing a user's product search configuration,
 * including keywords, target price, and the marketplace to search.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 29.10.2025
 */
@Data
@Builder
@ToString(of = {"store", "keyWords"})
public class Search {
	private Long id;

	private Store store;
	private String keyWords;
	private Double targetPrice;
	private User user;

	private Long version;

	/**
	 * Returns {@code true} if this search has not yet been persisted (has no database ID).
	 *
	 * @return {@code true} if the search is new
	 */
	public boolean isNew() {
		return id == null;
	}

	/**
	 * Returns a human-readable representation of this search, e.g. {@code "laptop - 50000.00₽"}.
	 * Returns {@code "<undefined>"} if keywords or target price are not set.
	 *
	 * @return a display string for the search
	 */
	public String getRepresentation() {
		if (keyWords == null || targetPrice == null) {
			return "<undefined>";
		}
		return String.format("%s - %.2f₽", keyWords, targetPrice);
	}
}
