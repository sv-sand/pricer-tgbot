package ru.svsand.pricer.tgbot.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;
import ru.svsand.pricer.tgbot.db.UserManager;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 04.11.2025
 */

@Slf4j
@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
	private final TelegramClient telegramClient;
	private final String token;
	private BotSession session;

	@Autowired
	CommandService commandService;

	@Autowired
	UserManager userManager;

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
		session = botSession;
		setMenu(BotMenu.userMenu());
	}

	@Override
	public LongPollingUpdateConsumer getUpdatesConsumer() {
		return this;
	}

	@Override
	public void consume(Update update) {
		log.info("Update received {}", update);

		if (update.hasMessage())
			registerUser(update.getMessage().getFrom());
		else
			registerUser(update.getCallbackQuery().getFrom());

		SendMessage message = commandService.processUpdate(update);
		sendMessage(message);
	}

	public boolean isRunning() {
		return session != null && session.isRunning();
	}

	public void sendMessage(@NotNull SendMessage message) {
		log.info("Send message {}", message.getText());
		try {
			telegramClient.execute(message);
		} catch (TelegramApiException e) {
			log.error("Failed to send message", e);
		}
	}

	private void setMenu(SetMyCommands commands) {
		log.info("Set bot command set");
		try {
			telegramClient.execute(commands);
		} catch (TelegramApiException e) {
			log.error("Failed to set bot menu", e);
		}
	}

	private void registerUser(User user) {
		if (userManager.findByTgId(user.getId()) != null)
			return;

		userManager.save(BotObjectMapper.fromDto(user));
	}
}
