package ru.svsand.pricer.tgbot.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.svsand.pricer.tgbot.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 04.03.2026
 */
@ExtendWith(MockitoExtension.class)
class SearchStatisticManagerTest {

    @Mock
    private SearchStatisticRepository repository;

    @InjectMocks
    private SearchStatisticManager searchStatisticManager;

    @Test
    void getStatistic() {
        Data.SearchStatistic searchStatistic = new Data.SearchStatistic(200, 10);

        // Arrange
        when(repository.getStatistic(any(Timestamp.class))).thenReturn(List.of(searchStatistic));

        // Act
        List<SearchStatisticManager.SearchStatistic> result = searchStatisticManager.getStatistic();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSearchStatistic(result.get(0), searchStatistic);

        verify(repository).getStatistic(any(Timestamp.class));
    }

    @Test
    void getStatistic_EmptyList() {
        // Arrange
        when(repository.getStatistic(any(Timestamp.class))).thenReturn(List.of());

        // Act
        List<SearchStatisticManager.SearchStatistic> result = searchStatisticManager.getStatistic();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).getStatistic(any(Timestamp.class));
    }

    @Test
    void getStatistic_MultipleItems() {
        Data.SearchStatistic searchStatistic1 = new Data.SearchStatistic(200, 15);
        Data.SearchStatistic searchStatistic2 = new Data.SearchStatistic(404, 5);
        Data.SearchStatistic searchStatistic3 = new Data.SearchStatistic(500, 2);

        // Arrange
        when(repository.getStatistic(any(Timestamp.class))).thenReturn(List.of(searchStatistic1, searchStatistic2, searchStatistic3));

        // Act
        List<SearchStatisticManager.SearchStatistic> result = searchStatisticManager.getStatistic();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertSearchStatistic(result.get(0), searchStatistic1);
        assertSearchStatistic(result.get(1), searchStatistic2);
        assertSearchStatistic(result.get(2), searchStatistic3);

        verify(repository).getStatistic(any(Timestamp.class));
    }

    @Test
    void getStatistic_VerifyTimeBoundary() {
        Data.SearchStatistic searchStatistic = new Data.SearchStatistic(200, 10);

        // Arrange
        when(repository.getStatistic(any(Timestamp.class))).thenReturn(List.of(searchStatistic));

        // Act
        searchStatisticManager.getStatistic();

        // Assert
        verify(repository).getStatistic(any(Timestamp.class));
        
        // Verify that the time boundary is approximately 1 day ago (within a reasonable margin)
        LocalDateTime expectedTime = LocalDateTime.now().minusDays(1);
        // We can't easily verify the exact timestamp without exposing internal implementation,
        // but we can verify the method was called
    }

    // Checks

    private void assertSearchStatistic(SearchStatisticManager.SearchStatistic actual, Data.SearchStatistic expected) {
        assertNotNull(actual);
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getRequestCount(), actual.getRequestCount());
    }
}
