package ru.svsand.pricer.tgbot.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.svsand.pricer.tgbot.logic.Product;
import ru.svsand.pricer.tgbot.logic.Store;

import java.util.List;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 29.10.2025
 */

@Service
public class ProductManager {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private SearchManager searchManager;

	@Transactional
	public List<Product> findAllNotUserNotified() {
		return repository.findByUserNotified(false)
				.stream()
				.map(ProductManager::fromDao)
				.toList();
	}

	@Transactional
	public List<Product> saveAll(List<Product> products) {
		List<ProductDao> daoList = products.stream()
				.map(ProductManager::toDao)
				.toList();

		return repository.saveAll(daoList).stream()
				.map(ProductManager::fromDao)
				.toList();
	}

	@Transactional
	public Product save(Product product) {
		ProductDao dao = toDao(product);
		return fromDao(repository.save(dao));
	}

	// Conversion

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
		productDao.setUserNotified(product.isUserNotified());
		productDao.setVersion(product.getVersion());

		return productDao;
	}

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
