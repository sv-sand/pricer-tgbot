package ru.svsand.pricer.tgbot.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 28.10.2025
 */

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	List<ProductEntity> findByStoreAndStoreProductId(String store, Long storeProductId);
	List<ProductEntity> findByUserNotified(boolean userNotified);
}
