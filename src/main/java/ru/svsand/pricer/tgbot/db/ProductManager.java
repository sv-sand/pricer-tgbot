package ru.svsand.pricer.tgbot.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.svsand.pricer.tgbot.logic.Product;
import ru.svsand.pricer.tgbot.logic.Store;

import java.util.List;

/**
 * Service for persisting and retrieving {@link Product} domain objects.
 * Handles conversion between domain objects and JPA entities.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 29.10.2025
 */
@Service
public class ProductManager {

	private final ProductRepository repository;

	/**
	 * @param repository the Spring Data repository for product persistence
	 */
	@Autowired
	public ProductManager(ProductRepository repository) {
		this.repository = repository;
	}

	/**
	 * Returns all products that have not yet been sent to the user as notifications.
	 *
	 * @return list of products pending notification
	 */
	@Transactional
	public List<Product> findAllForNotify() {
		return repository.findByUserNotified(false)
				.stream()
				.map(ProductManager::fromDao)
				.toList();
	}

	/**
	 * Persists all given products and returns the saved domain objects.
	 *
	 * @param products the list of products to save
	 * @return list of saved {@link Product} objects
	 */
	@Transactional
	public List<Product> saveAll(List<Product> products) {
		List<ProductDao> daoList = products.stream()
				.map(ProductManager::toDao)
				.toList();

		return repository.saveAll(daoList).stream()
				.map(ProductManager::fromDao)
				.toList();
	}

	/**
	 * Persists the given product and returns the saved domain object.
	 *
	 * @param product the product to save
	 * @return the saved {@link Product}
	 */
	@Transactional
	public Product save(Product product) {
		ProductDao dao = toDao(product);
		return fromDao(repository.save(dao));
	}

	// Conversion

	/**
	 * Converts a {@link Product} domain object to a {@link ProductDao} JPA entity.
	 *
	 * @param product the domain product
	 * @return the corresponding JPA entity
	 */
	public static ProductDao toDao(Product product) {
		SearchDao searchDao = SearchManager.toDao(product.getSearch());

		ProductDao productDao = new ProductDao();
		if (!product.isNew())
			productDao.setId(product.getId());

		productDao.setName(product.getName());
		productDao.setSearch(searchDao);
		productDao.setStore(product.getStore().name());
		productDao.setStoreProductId(product.getStoreProductId());
		productDao.setStoreProductLink(product.getStoreProductLink());
		productDao.setPrice(product.getPrice());
		productDao.setUserNotified(product.getUserNotified());
		productDao.setVersion(product.getVersion());

		return productDao;
	}

	/**
	 * Converts a {@link ProductDao} JPA entity to a {@link Product} domain object.
	 * Returns {@code null} if the entity is {@code null}.
	 *
	 * @param productDao the JPA entity
	 * @return the corresponding domain object, or {@code null}
	 */
	public static Product fromDao(ProductDao productDao) {
		if (productDao == null)
			return null;

		return Product.builder()
				.id(productDao.getId())
				.name(productDao.getName())
				.search(SearchManager.fromDao(productDao.getSearch()))
				.store(Store.valueOf(productDao.getStore()))
				.storeProductId(productDao.getStoreProductId())
				.storeProductLink(productDao.getStoreProductLink())
				.price(productDao.getPrice())
				.userNotified(productDao.isUserNotified())
				.version(productDao.getVersion())
				.build();
	}
}
