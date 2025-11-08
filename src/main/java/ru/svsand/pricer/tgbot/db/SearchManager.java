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
	SearchRepository searchesRepository;

	@Transactional
	public List<Search> findByUserId(Long UserId) {
		return searchesRepository.findByUserId(UserId)
				.stream().map(SearchManager::map)
				.toList();
	}

	@Transactional
	public void save(@NotNull Search search) {
		searchesRepository.save(map(search));
	}

	@Transactional
	public void delete(@NotNull Search search) {
		searchesRepository.delete(map(search));
	}

	// Conversion

	public static SearchEntity map(@NotNull Search search) {
		SearchEntity searchEntity = new SearchEntity();
		if (!search.isNew())
			searchEntity.setId(search.getId());

		searchEntity.setUser(UserManager.map(search.getUser()));
		searchEntity.setStore(search.getStore().name());
		searchEntity.setKeyWords(search.getKeyWords());
		searchEntity.setTargetPrice(search.getTargetPrice());
		searchEntity.setVersion(search.getVersion());

		return searchEntity;
	}

	public static Search map(SearchEntity searchEntity) {
		return Search.builder()
				.id(searchEntity.getId())
				.user(UserManager.map(searchEntity.getUser()))
				.store(Store.valueOf(searchEntity.getStore()))
				.keyWords(searchEntity.getKeyWords())
				.targetPrice(searchEntity.getTargetPrice())
				.version(searchEntity.getVersion())
				.build();
	}
}
