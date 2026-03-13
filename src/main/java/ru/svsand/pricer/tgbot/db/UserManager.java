package ru.svsand.pricer.tgbot.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.svsand.pricer.tgbot.logic.User;

import java.util.List;

/**
 * Service for persisting and retrieving {@link User} domain objects.
 * Handles conversion between domain objects and JPA entities.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 05.11.2025
 */
@Service
public class UserManager {
	@Autowired
	UserRepository repository;

	/**
	 * Finds a user by their Telegram ID. Returns {@code null} if not found.
	 *
	 * @param tgId the Telegram user ID
	 * @return the matching {@link User}, or {@code null}
	 */
	@Transactional
	public User findByTgId(Long tgId) {
		List<UserDao> entities = repository.findByTgId(tgId);
		if (entities.isEmpty())
			return null;

		return fromDao(entities.get(0));
	}

	/**
	 * Persists the given user and returns the saved domain object (with generated ID).
	 *
	 * @param user the user to save; must not be {@code null}
	 * @return the saved {@link User}
	 */
	@Transactional
	public User save(@NotNull User user) {
		UserDao dao = repository.save(toDao(user));
		return fromDao(dao);
	}

	/**
	 * Converts a {@link User} domain object to a {@link UserDao} JPA entity.
	 *
	 * @param User the domain user; must not be {@code null}
	 * @return the corresponding JPA entity
	 */
	public static UserDao toDao(@NotNull User User) {
		UserDao UserDao = new UserDao();
		if (!User.isNew())
			UserDao.setId(User.getId());

		UserDao.setName(User.getName());
		UserDao.setTgId(User.getTgId());
		UserDao.setVersion(User.getVersion());

		return UserDao;
	}

	/**
	 * Converts a {@link UserDao} JPA entity to a {@link User} domain object.
	 * Returns {@code null} if the entity is {@code null}.
	 *
	 * @param UserDao the JPA entity
	 * @return the corresponding domain object, or {@code null}
	 */
	public static User fromDao(UserDao UserDao) {
		if (UserDao == null)
			return null;

		return User.builder()
				.id(UserDao.getId())
				.name(UserDao.getName())
				.tgId(UserDao.getTgId())
				.version(UserDao.getVersion())
				.build();
	}
}
