package ru.svsand.pricer.tgbot.logic;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Domain object representing a registered Telegram user.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 04.11.2025
 */
@Data
@Builder
@ToString(of = {"name", "id", "tgId"})
@EqualsAndHashCode(of = {"id"})
public class User {
	private Long id;
	private String name;
	private Long tgId;

	private Long version;

	/**
	 * Returns {@code true} if this user has not yet been persisted (has no database ID).
	 *
	 * @return {@code true} if the user is new
	 */
	public boolean isNew() {
		return id == null;
	}
}
