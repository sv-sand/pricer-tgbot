package ru.svsand.pricer.tgbot;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.svsand.pricer.tgbot.bot.commands.CommandBase;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;
import ru.svsand.pricer.tgbot.db.ProductDao;
import ru.svsand.pricer.tgbot.db.SearchDao;
import ru.svsand.pricer.tgbot.db.SearchStatisticManager;
import ru.svsand.pricer.tgbot.db.UserDao;
import ru.svsand.pricer.tgbot.logic.Product;
import ru.svsand.pricer.tgbot.logic.Search;
import ru.svsand.pricer.tgbot.logic.Store;
import ru.svsand.pricer.tgbot.logic.User;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 05.02.2026
 */

public class Data {

	// Domain objects

	public static User user() {
		return User.builder()
				.id(1L)
				.name("test user")
				.tgId(101L)
				.version(1000L)
				.build();
	}

	public static Search search() {
		return Search.builder()
				.id(1L)
				.store(Store.WB)
				.keyWords("test keyword")
				.targetPrice(100.0)
				.user(user())
				.version(1000L)
				.build();
	}

	public static Product product() {
		return Product.builder()
				.id(1L)
				.name("test product")
				.store(Store.WB)
				.search(search())
				.storeProductId(101L)
				.storeProductLink("https://example.com")
				.price(100.0)
				.userNotified(false)
				.version(1000L)
				.build();
	}

	// Telegram objects

	public static org.telegram.telegrambots.meta.api.objects.User tgUser() {
		return org.telegram.telegrambots.meta.api.objects.User.builder()
				.id(1001L)
				.userName("test_user")
				.firstName("Test")
				.lastName("User")
				.isBot(false)
				.build();
	}

	public static Message tgMessage(String text) {
		Message message = new Message();
		message.setText(text);
		message.setFrom(Data.tgUser());

		return message;
	}

	public static Update tgUpdate(String text) {
		Update update = new Update();
		update.setMessage(tgMessage(text));
		return update;
	}

	public static CallbackQuery tgCallbackQuery(String data) {
		CallbackQuery callbackQuery = new CallbackQuery();
		callbackQuery.setData(data);
		callbackQuery.setFrom(tgUser());
		return callbackQuery;
	}

	// Commands

	public static class TestCommand extends CommandBase {
		public static final String ID = "/test";

		public TestCommand(CommandService commandService) {
			super(commandService);
		}
	}

	// Database objects

	@AllArgsConstructor
	@lombok.Data
	public static class SearchStatistic implements SearchStatisticManager.SearchStatistic {
		private int statusCode;
		private int requestCount;
	}

	public static UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setId(1L);
		userDao.setName("test user");
		userDao.setTgId(101L);
		userDao.setVersion(1000L);
		return userDao;
	}

	public static SearchDao searchDao() {
		SearchDao searchDao = new SearchDao();
		searchDao.setId(1L);
		searchDao.setUser(userDao());
		searchDao.setStore(Store.WB.name());
		searchDao.setKeyWords("test keyword");
		searchDao.setTargetPrice(100.0);
		searchDao.setVersion(1000L);
		return searchDao;
	}

	public static ProductDao productDao() {
		ProductDao productDao = new ProductDao();
		productDao.setId(1L);
		productDao.setName("test product");
		productDao.setSearch(searchDao());
		productDao.setStore(Store.WB.name());
		productDao.setStoreProductId(101L);
		productDao.setStoreProductLink("https://example.com");
		productDao.setPrice(100.0);
		productDao.setUserNotified(false);
		productDao.setVersion(1000L);

		return productDao;
	}
}

