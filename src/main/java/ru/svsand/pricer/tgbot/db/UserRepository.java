package ru.svsand.pricer.tgbot.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link UserDao} entities.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 05.11.2025
 */
public interface UserRepository extends JpaRepository<UserDao, Long>  {

	/**
	 * Finds all users with the given Telegram user ID.
	 *
	 * @param tgId the Telegram user ID
	 * @return list of matching user entities
	 */
	List<UserDao> findByTgId(Long tgId);
}
