package ru.svsand.pricer.tgbot.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link SearchDao} entities.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 28.10.2025
 */
public interface SearchRepository extends JpaRepository<SearchDao, Long> {

	/**
	 * Finds all searches belonging to the user with the given ID.
	 *
	 * @param UserId the user's database ID
	 * @return list of matching search entities
	 */
	List<SearchDao> findByUserId(Long UserId);
}
