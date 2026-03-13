package ru.svsand.pricer.tgbot.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.svsand.pricer.tgbot.logic.Search;
import ru.svsand.pricer.tgbot.logic.Store;

import java.util.List;

/**
 * Service for persisting and retrieving {@link Search} domain objects.
 * Handles conversion between domain objects and JPA entities.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 29.10.2025
 */
@Service
public class SearchManager {

	@Autowired
	SearchRepository repository;

	@Autowired
	private UserManager userManager;

	/**
	 * Returns all searches for the user with the given database ID.
	 *
	 * @param UserId the user's database ID
	 * @return list of the user's searches
	 */
	@Transactional
	public List<Search> findByUserId(Long UserId) {
		return repository.findByUserId(UserId)
				.stream().map(SearchManager::fromDao)
				.toList();
	}

	/**
	 * Persists the given search and returns the saved domain object.
	 *
	 * @param search the search to save; must not be {@code null}
	 * @return the saved {@link Search}
	 */
	@Transactional
	public Search save(@NotNull Search search) {
		SearchDao dao = repository.save(toDao(search));
		return fromDao(dao);
	}

	/**
	 * Deletes the given search from the database.
	 *
	 * @param search the search to delete; must not be {@code null}
	 */
	@Transactional
	public void delete(@NotNull Search search) {
		repository.delete(toDao(search));
	}

	// Conversion

	/**
	 * Converts a {@link Search} domain object to a {@link SearchDao} JPA entity.
	 *
	 * @param search the domain search; must not be {@code null}
	 * @return the corresponding JPA entity
	 */
	public static SearchDao toDao(@NotNull Search search) {
		SearchDao searchDao = new SearchDao();
		if (!search.isNew())
			searchDao.setId(search.getId());

		searchDao.setUser(UserManager.toDao(search.getUser()));
		searchDao.setStore(search.getStore().name());
		searchDao.setKeyWords(search.getKeyWords());
		searchDao.setTargetPrice(search.getTargetPrice());
		searchDao.setVersion(search.getVersion());

		return searchDao;
	}

	/**
	 * Converts a {@link SearchDao} JPA entity to a {@link Search} domain object.
	 * Returns {@code null} if the entity is {@code null}.
	 *
	 * @param searchDao the JPA entity
	 * @return the corresponding domain object, or {@code null}
	 */
	public static Search fromDao(SearchDao searchDao) {
		if (searchDao == null)
			return null;

		return Search.builder()
				.id(searchDao.getId())
				.user(UserManager.fromDao(searchDao.getUser()))
				.store(Store.valueOf(searchDao.getStore()))
				.keyWords(searchDao.getKeyWords())
				.targetPrice(searchDao.getTargetPrice())
				.version(searchDao.getVersion())
				.build();
	}
}
