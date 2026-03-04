package ru.svsand.pricer.tgbot.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.svsand.pricer.tgbot.Data;
import ru.svsand.pricer.tgbot.logic.Search;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 04.03.2026
 */
@ExtendWith(MockitoExtension.class)
class SearchManagerTest {

    @Mock
    private SearchRepository repository;

    @Mock
    private UserManager userManager;

    @InjectMocks
    private SearchManager searchManager;

    @Test
    void findByUserId() {
        SearchDao searchDao = Data.searchDao();

        // Arrange
        when(repository.findByUserId(anyLong())).thenReturn(List.of(searchDao));

        // Act
        List<Search> result = searchManager.findByUserId(100L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSearch(result.get(0), searchDao);

        verify(repository).findByUserId(100L);
    }

    @Test
    void findByUserId_EmptyList() {
        // Arrange
        when(repository.findByUserId(anyLong())).thenReturn(List.of());

        // Act
        List<Search> result = searchManager.findByUserId(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findByUserId(1L);
        verifyNoInteractions(userManager);
    }

    @Test
    void save() {
        Search search = Data.search();
        SearchDao searchDao = Data.searchDao();

        // Arrange
        when(repository.save(any(SearchDao.class))).thenReturn(searchDao);

        // Act
        Search result = searchManager.save(search);

        // Assert
        assertSearch(result, searchDao);

        verify(repository).save(searchDao);
    }


    @Test
    void delete() {
        // Arrange
        Search search = Data.search();

        // Act
        searchManager.delete(search);

        // Assert
        verify(repository).delete(any(SearchDao.class));
    }

    // Checks

    private void assertSearch(Search search, SearchDao searchDao) {
        assertNotNull(search);
        assertNotNull(searchDao);
        assertEquals(search.getId(), searchDao.getId());
        assertEquals(search.getStore().name(), searchDao.getStore());
        assertEquals(search.getKeyWords(), searchDao.getKeyWords());
        assertEquals(search.getTargetPrice(), searchDao.getTargetPrice());
        assertEquals(search.getVersion(), searchDao.getVersion());
    }
}
