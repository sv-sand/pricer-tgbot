package ru.svsand.pricer.tgbot.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 07.11.2025
 */

public interface SearchStatisticRepository extends JpaRepository<SearchStatisticEntity, Long> {

	@Query(value =
			"SELECT \n" +
			"t.status_code,\n" +
			"COUNT(*) as request_count\n" +
			"FROM public.search_statistic t\n" +
			"WHERE t.timestamp IS NOT NULL AND t.timestamp > :time_boundary\n" +
			"GROUP BY t.status_code", nativeQuery = true)
	List<SearchStatisticManager.SearchStatistic> getStatistic(@Param("time_boundary") Timestamp timeBoundary);
}
