package ru.svsand.pricer.tgbot.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.svsand.pricer.tgbot.logic.User;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;
import ru.svsand.pricer.tgbot.db.UserManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotTest {

	@InjectMocks
	private Bot bot;

	@Mock
	private BotSession session;

	@Mock
	private BotClient client;

	@Mock
	private CommandService commandService;

	@Mock
	private UserManager userManager;

	@Test
	void getBotToken() {
		// Act
		bot = new Bot("bot_token", client, commandService, userManager);
		assertEquals("bot_token", bot.getBotToken());
	}

	@Test
	void afterRegistration() throws TelegramApiException {
		List<String> expectedCommands = List.of(
				"/help", "/searches_list", "/new_search", "/delete_search", "/statistic"
		);

		// Arrange
		when(client.execute(any(SetMyCommands.class))).thenReturn(null);
		when(session.isRunning()).thenReturn(true);

		// Act
		bot.afterRegistration(session);

		// Assert
		assertTrue(bot.isRunning());
		checkCall_Client_SetMenu(expectedCommands);
	}

	@Test
	void afterRegistration_SessionNotRunning() throws TelegramApiException {
		// Arrange
		when(session.isRunning()).thenReturn(false);

		// Arrange
		bot.afterRegistration(session);

		// Act & Assert
		assertFalse(bot.isRunning());
		verify(client, times(0)).execute(any(SendMessage.class));
	}

	@Test
	void afterRegistration_ErrorSetMenu() throws TelegramApiException {
		List<String> expectedCommands = List.of(
				"/help", "/searches_list", "/new_search", "/delete_search", "/statistic"
		);

		// Arrange
		when(session.isRunning()).thenReturn(true);
		doThrow(new TelegramApiException("Test exception"))
				.when(client)
				.execute(any(SetMyCommands.class));

		// Act
		bot.afterRegistration(session);

		// Assert
		assertTrue(bot.isRunning());
		checkCall_Client_SetMenu(expectedCommands);
	}

	@Test
	void getUpdatesConsumer() {
		// Act
		LongPollingUpdateConsumer consumer = bot.getUpdatesConsumer();

		// Assert
		assertEquals(bot, consumer);
	}

	@Test
	void consume() throws TelegramApiException {
		User user = createUser();
		org.telegram.telegrambots.meta.api.objects.User userDto = createUserDto();
		Update update = createMessageUpdate(userDto);
		SendMessage response = new SendMessage("123", "Response message");

		// Arrange
		when(userManager.findByTgId(anyLong())).thenReturn(user);
		when(commandService.processUpdate(any(Update.class))).thenReturn(response);
		when(client.execute(any(SendMessage.class))).thenReturn(null);

		// Act
		bot.consume(update);

		// Assert
		verify(userManager, times(1)).findByTgId(101L);
		verify(userManager, times(0)).save(user);
		verify(commandService, times(1)).processUpdate(update);
		verify(client, times(1)).execute(response);
	}

	@Test
	void consume_NewUser() throws TelegramApiException {
		User user = createUser();
		org.telegram.telegrambots.meta.api.objects.User userDto = createUserDto();
		Update update = createMessageUpdate(userDto);
		SendMessage response = new SendMessage("123", "Response message");

		// Arrange
		when(userManager.findByTgId(anyLong())).thenReturn(null);
		when(userManager.save(any(User.class))).thenReturn(null);
		when(commandService.processUpdate(any(Update.class))).thenReturn(response);
		when(client.execute(any(SendMessage.class))).thenReturn(null);

		// Act
		bot.consume(update);

		// Assert
		verify(userManager, times(1)).findByTgId(101L);
		verify(userManager, times(1)).save(user);
		verify(commandService, times(1)).processUpdate(update);
		verify(client, times(1)).execute(response);
	}

	@Test
	void consume_Callback() throws TelegramApiException {
		User user = createUser();
		org.telegram.telegrambots.meta.api.objects.User userDto = createUserDto();
		Update update = createCallbackQueryUpdate(userDto);
		SendMessage response = new SendMessage("123", "Response message");

		// Arrange
		when(userManager.findByTgId(anyLong())).thenReturn(null);
		when(userManager.save(any(User.class))).thenReturn(null);
		when(commandService.processUpdate(any(Update.class))).thenReturn(response);
		when(client.execute(any(SendMessage.class))).thenReturn(null);

		// Act
		bot.consume(update);

		// Assert
		verify(userManager, times(1)).findByTgId(101L);
		verify(userManager, times(1)).save(user);
		verify(commandService, times(1)).processUpdate(update);
		verify(client, times(1)).execute(response);
	}

	@Test
	void consume_Error() throws TelegramApiException {
		User user = createUser();
		org.telegram.telegrambots.meta.api.objects.User userDto = createUserDto();
		Update update = createMessageUpdate(userDto);
		SendMessage response = new SendMessage("123", "Response message");

		// Arrange
		when(userManager.findByTgId(anyLong())).thenReturn(null);
		when(userManager.save(any(User.class))).thenReturn(null);
		when(commandService.processUpdate(any(Update.class))).thenReturn(response);
		doThrow(new TelegramApiException("API Error")).when(client).execute(any(SendMessage.class));

		// Act
		bot.consume(update);

		// Assert
		verify(userManager, times(1)).findByTgId(101L);
		verify(userManager, times(1)).save(user);
		verify(commandService, times(1)).processUpdate(update);
		verify(client, times(1)).execute(response);
	}

	// Checks

	private void checkCall_Client_SetMenu(List<String> expectedCommands) throws TelegramApiException {
		ArgumentCaptor<SetMyCommands> captor = ArgumentCaptor.forClass(SetMyCommands.class);
		verify(client, times(1)).execute(captor.capture());

		assertNotNull(captor.getValue());
		List<BotCommand> commands = captor.getValue().getCommands();
		assertEquals(expectedCommands.size(), commands.size());

		for (int i = 0; i < expectedCommands.size(); i++)
			assertEquals(expectedCommands.get(i), commands.get(i).getCommand());
	}

	// Test data

	private User createUser() {
		return ru.svsand.pricer.tgbot.logic.User.builder()
				.id(null)
				.tgId(101L)
				.name("user_name")
				.version(0L)
				.build();
	}

	private org.telegram.telegrambots.meta.api.objects.User createUserDto() {
		return new org.telegram.telegrambots.meta.api.objects.User(
				101L, "first_name", false, "last_name", "user_name", "ru",
				false, false, false, false, false, false, false);
	}

	private Update createMessageUpdate(org.telegram.telegrambots.meta.api.objects.User user) {
		Update update = new Update();
		Message message = new Message();
		message.setMessageId(1);
		message.setFrom(user);
		message.setChat(new Chat(123L, "private"));
		message.setText("/start");
		message.setEntities(
				List.of(new MessageEntity("bot_command", 0, 6))
		);
		update.setMessage(message);
		return update;
	}

	private Update createCallbackQueryUpdate(org.telegram.telegrambots.meta.api.objects.User user) {
		Update update = new Update();
		CallbackQuery callbackQuery = new CallbackQuery();
		callbackQuery.setId("123");
		callbackQuery.setFrom(user);
		callbackQuery.setData("test_callback");
		update.setCallbackQuery(callbackQuery);
		return update;
	}
}