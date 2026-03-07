package ru.svsand.pricer.tgbot.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.svsand.pricer.tgbot.Data;
import ru.svsand.pricer.tgbot.logic.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 04.03.2026
 */
@ExtendWith(MockitoExtension.class)
class ProductManagerTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private SearchManager searchManager;

    @InjectMocks
    private ProductManager productManager;

    @Test
    void findAllForNotify() {
        ProductDao productDao = Data.productDao();

        // Arrange
        when(repository.findByUserNotified(anyBoolean())).thenReturn(List.of(productDao));

        // Act
        List<Product> result = productManager.findAllForNotify();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertProduct(result.get(0), productDao);

        verify(repository).findByUserNotified(false);
    }

    @Test
    void findAllForNotify_EmptyList() {
        // Arrange
        when(repository.findByUserNotified(anyBoolean())).thenReturn(List.of());

        // Act
        List<Product> result = productManager.findAllForNotify();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findByUserNotified(false);
        verifyNoInteractions(searchManager);
    }

    @Test
    void saveAll() {
        Product product = Data.product();
        ProductDao productDao = Data.productDao();

        // Arrange
        when(repository.saveAll(anyList())).thenReturn(List.of(productDao));

        // Act
        List<Product> result = productManager.saveAll(List.of(product));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertProduct(result.get(0), productDao);

        verify(repository).saveAll(List.of(productDao));
    }

    @Test
    void saveAll_EmptyList() {
        // Arrange
        List<Product> emptyProducts = List.of();

        // Act
        List<Product> result = productManager.saveAll(emptyProducts);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).saveAll(List.of());
    }

    @Test
    void save() {
        Product product = Data.product();
        ProductDao productDao = Data.productDao();

        // Arrange
        when(repository.save(any(ProductDao.class))).thenReturn(productDao);

        // Act
        Product result = productManager.save(product);

        // Assert
        assertProduct(result, productDao);

        verify(repository).save(productDao);
    }

    // Checks

    private void assertProduct(Product product, ProductDao productDao) {
        assertNotNull(productDao);
        assertEquals(product.getId(), productDao.getId());
        assertEquals(product.getName(), productDao.getName());
        assertEquals(product.getStore().name(), productDao.getStore());
        assertEquals(product.getStoreProductId(), productDao.getStoreProductId());
        assertEquals(product.getStoreProductLink(), productDao.getStoreProductLink());
        assertEquals(product.getPrice(), productDao.getPrice());
        assertEquals(product.getUserNotified(), productDao.isUserNotified());
        assertEquals(product.getVersion(), productDao.getVersion());
    }
}
