package ru.svsand.pricer.tgbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;
import ru.svsand.pricer.tgbot.db.ProductManager;
import ru.svsand.pricer.tgbot.db.UserManager;
import ru.svsand.pricer.tgbot.logic.Product;
import ru.svsand.pricer.tgbot.logic.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 04.11.2025
 */

@Slf4j
@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
	private final TelegramClient telegramClient;
	private final String token;
	private BotSession botSession;

	@Autowired
	CommandService commandService;

	@Autowired
	UserManager userManager;

	@Autowired
	ProductManager productManager;

	public Bot(@Value("${bot.token}") String botToken) {
		token = botToken;
		telegramClient = new OkHttpTelegramClient(token);
	}

	@Override
	public String getBotToken() {
		return token;
	}

	@AfterBotRegistration
	public void afterRegistration(BotSession botSession) {
		log.info("Bot registered with state: {}", botSession.isRunning());
		this.botSession = botSession;
		setMenu();
	}

	private void setMenu() {
		log.info("Set menu");

		SetMyCommands menu = SetMyCommands.builder()
				.command(new BotCommand("/help", "Помощь"))
				.command(new BotCommand("/searches_list", "Список всех поисков"))
				.command(new BotCommand("/new_search", "Создать новый поиск товара"))
				.command(new BotCommand("/delete_search", "Удалить поиск товара"))
				.build();

		try {
			telegramClient.execute(menu);
		} catch (TelegramApiException e) {
			log.error("Failed to set menu", e);
		}
	}

	@Scheduled(fixedRate = 60 * 1000)
	public void notifyUsers() {
		log.info("Notify users started");

		if (!botSession.isRunning())
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
			sendMessage(message);

			// Set user notified
			productList.forEach(product -> product.setUserNotified(true));
			productManager.saveAll(productList);
		}

		log.info("Notify users finished");
	}

	@Override
	public LongPollingUpdateConsumer getUpdatesConsumer() {
		return this;
	}

	@Override
	public void consume(Update update) {
		log.info("Update received {}", update);

		org.telegram.telegrambots.meta.api.objects.User tgUser = getTgUser(update);
		registerTgUser(tgUser);

		SendMessage message = commandService.processUpdate(update);
		sendMessage(message);
	}

	private void sendMessage(SendMessage message) {
		log.info("Send message {}", message.getText());
		try {
			telegramClient.execute(message);
		} catch (TelegramApiException e) {
			log.error("Failed to send message", e);
		}
	}

	private org.telegram.telegrambots.meta.api.objects.User getTgUser(Update update) {
		if (update.hasMessage())
			return update.getMessage().getFrom();
		else if(update.hasCallbackQuery())
			return update.getCallbackQuery().getFrom();
		else
			throw new RuntimeException("Can't define telegram user");
	}

	public void registerTgUser(@NotNull org.telegram.telegrambots.meta.api.objects.User tgUser) {
		User user = userManager.findByTgId(tgUser.getId());
		if (user != null)
			return;

		user = User.builder()
				.tgId(tgUser.getId())
				.name(tgUser.getUserName())
				.build();
		userManager.save(user);
	}
}
