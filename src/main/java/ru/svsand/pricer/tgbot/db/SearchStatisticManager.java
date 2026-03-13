package ru.svsand.pricer.tgbot.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for querying search request statistics.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 07.11.2025
 */
@Service
public class SearchStatisticManager {

	@Autowired
	SearchStatisticRepository repository;

	/**
	 * Projection interface for aggregated search statistics returned by native queries.
	 */
	public interface SearchStatistic {

		/**
		 * Returns the HTTP status code of the search request.
		 *
		 * @return HTTP status code
		 */
		int getStatusCode();

		/**
		 * Returns the number of requests with this status code.
		 *
		 * @return request count
		 */
		int getRequestCount();
	}

	/**
	 * Returns search request statistics for the last 24 hours, grouped by HTTP status code.
	 *
	 * @return list of statistic projections
	 */
	public List<SearchStatistic> getStatistic() {
		LocalDateTime currentDateTime = LocalDateTime.now().minusDays(1);
		return repository.getStatistic(Timestamp.valueOf(currentDateTime));
	}

}
