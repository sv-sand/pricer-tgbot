package ru.svsand.pricer.tgbot.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.svsand.pricer.tgbot.logic.Search;
import ru.svsand.pricer.tgbot.logic.Store;

import java.util.List;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 29.10.2025
 */

@Service
public class SearchManager {

	@Autowired
	SearchRepository repository;

	@Autowired
	private UserManager userManager;

	@Transactional
	public List<Search> findByUserId(Long UserId) {
		return repository.findByUserId(UserId)
				.stream().map(SearchManager::fromDao)
				.toList();
	}

	@Transactional
	public Search save(@NotNull Search search) {
		SearchDao dao = repository.save(toDao(search));
		return fromDao(dao);
	}

	@Transactional
	public void delete(@NotNull Search search) {
		repository.delete(toDao(search));
	}

	// Conversion

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
