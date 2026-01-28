package ru.svsand.pricer.tgbot.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 07.11.2025
 */

@Service
public class SearchStatisticManager {

	@Autowired
	SearchStatisticRepository repository;

	public interface SearchStatistic {
		int getStatusCode();
		int getRequestCount();
	}

	public List<SearchStatistic> getStatistic() {
		LocalDateTime currentDateTime = LocalDateTime.now().minusDays(1);
		return repository.getStatistic(Timestamp.valueOf(currentDateTime));
	}

}
