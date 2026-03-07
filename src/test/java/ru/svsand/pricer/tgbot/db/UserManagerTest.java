package ru.svsand.pricer.tgbot.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.svsand.pricer.tgbot.Data;
import ru.svsand.pricer.tgbot.logic.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 04.03.2026
 */
@ExtendWith(MockitoExtension.class)
class UserManagerTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserManager userManager;

    @Test
    void findByTgId() {
        UserDao userDao = Data.userDao();

        // Arrange
        when(repository.findByTgId(101L)).thenReturn(List.of(userDao));

        // Act
        User result = userManager.findByTgId(101L);

        // Assert
        assertNotNull(result);
        assertUser(result, userDao);

        verify(repository).findByTgId(101L);
    }

    @Test
    void findByTgId_NotFound() {
        // Arrange
        when(repository.findByTgId(999L)).thenReturn(List.of());

        // Act
        User result = userManager.findByTgId(999L);

        // Assert
        assertNull(result);
        verify(repository).findByTgId(999L);
    }

    @Test
    void save() {
        User user = Data.user();
        UserDao userDao = Data.userDao();

        // Arrange
        when(repository.save(any(UserDao.class))).thenReturn(userDao);

        // Act
        User result = userManager.save(user);

        // Assert
        assertNotNull(result);
        assertUser(result, userDao);

        verify(repository).save(userDao);
    }

    // Checks

    private void assertUser(User user, UserDao userDao) {
        assertNotNull(user);
        assertNotNull(userDao);
        assertEquals(user.getId(), userDao.getId());
        assertEquals(user.getName(), userDao.getName());
        assertEquals(user.getTgId(), userDao.getTgId());
        assertEquals(user.getVersion(), userDao.getVersion());
    }
}
