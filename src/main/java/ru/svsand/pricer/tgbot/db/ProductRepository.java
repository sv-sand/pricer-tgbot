package ru.svsand.pricer.tgbot.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link ProductDao} entities.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 28.10.2025
 */
public interface ProductRepository extends JpaRepository<ProductDao, Long> {

	/**
	 * Finds products by store name and store-specific product ID.
	 *
	 * @param store          the store name (e.g. "WB")
	 * @param storeProductId the product ID in the store's system
	 * @return list of matching product entities
	 */
	List<ProductDao> findByStoreAndStoreProductId(String store, Long storeProductId);

	/**
	 * Finds all products with the given user-notified flag value.
	 *
	 * @param userNotified {@code false} to find products pending notification
	 * @return list of matching product entities
	 */
	List<ProductDao> findByUserNotified(boolean userNotified);
}
