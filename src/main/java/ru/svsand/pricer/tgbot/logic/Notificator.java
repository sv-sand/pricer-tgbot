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

	@Scheduled(fixedRate = 60 * 1000)
	public void notifyUsers() {
		log.info("Notify users started");

		if (!bot.getSession().isRunning())
			return;

		List<Product> products = productManager.findAllNotUserNotified();
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

