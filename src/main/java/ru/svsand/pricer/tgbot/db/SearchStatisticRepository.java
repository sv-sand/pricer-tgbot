package ru.svsand.pricer.tgbot.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * Spring Data JPA repository for {@link SearchStatisticDao} entities.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 07.11.2025
 */
public interface SearchStatisticRepository extends JpaRepository<SearchStatisticDao, Long> {

	/**
	 * Returns aggregated search request counts grouped by HTTP status code
	 * for all records after the given time boundary.
	 *
	 * @param timeBoundary the lower timestamp bound (exclusive); typically 24 hours ago
	 * @return list of statistic projections, each with a status code and request count
	 */
	@Query(value =
			"""
					SELECT\s
					t.status_code,
					COUNT(*) as request_count
					FROM public.search_statistic t
					WHERE t.timestamp IS NOT NULL AND t.timestamp > :time_boundary
					GROUP BY t.status_code""", nativeQuery = true)
	List<SearchStatisticManager.SearchStatistic> getStatistic(@Param("time_boundary") Timestamp timeBoundary);
}
