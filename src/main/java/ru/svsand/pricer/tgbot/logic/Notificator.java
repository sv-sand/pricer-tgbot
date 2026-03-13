package ru.svsand.pricer.tgbot.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.svsand.pricer.tgbot.bot.Bot;
import ru.svsand.pricer.tgbot.db.ProductManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Scheduled service that periodically checks for products matching user searches
 * and sends Telegram notifications for products not yet reported to the user.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 28.01.2026
 */
@Service
@Slf4j
public class Notificator {

	@Autowired
	private Bot bot;

	@Autowired
	ProductManager productManager;

	/**
	 * Runs every 60 seconds. Finds all products pending notification, groups them by user,
	 * sends a Telegram message to each user, and marks the products as notified.
	 * Skips execution if the bot session is not currently running.
	 */
	@Scheduled(fixedRate = 60 * 1000)
	public void notifyUsers() {
		log.info("Notify users started");

		if (!bot.isRunning()) {
			log.info("Bot is not running, skipping notification");
			return;
		}

		List<Product> products = productManager.findAllForNotify();
		Map<User, List<Product>> productMap = products.stream()
				.collect(Collectors.groupingBy(Product::getUser));

		for (var entry : productMap.entrySet()) {
			User user = entry.getKey();
			List<Product> productList = entry.getValue();

			// Send message
			String representation = productList.stream()
					.map(Product::getRepresentationHtml)
					.collect(Collectors.joining("\n"));

			SendMessage message = SendMessage.builder()
					.chatId(user.getTgId())
					.text("<b>Новые товары:</b>\n" + representation)
					.parseMode(ParseMode.HTML)
					.disableWebPagePreview(true)
					.build();
			bot.sendMessage(message);

			// Set user notified
			productList.forEach(product -> product.setUserNotified(true));
			productManager.saveAll(productList);
		}

		log.info("Notify users finished");
	}

}

