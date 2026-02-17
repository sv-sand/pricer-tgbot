package ru.svsand.pricer.tgbot.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.svsand.pricer.tgbot.Data;
import ru.svsand.pricer.tgbot.bot.Bot;
import ru.svsand.pricer.tgbot.db.ProductManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificatorTest {

	@InjectMocks
	private Notificator notificator;

	@Mock
	private Bot bot;

	@Mock
	private ProductManager productManager;

	@Test
	void notifyUsers() {
		Product product = Data.product();

		// Arrange
		when(bot.isRunning()).thenReturn(true);
		doNothing().when(bot).sendMessage(any(SendMessage.class));
		when(productManager.findAllForNotify()).thenReturn(List.of(product));
		when(productManager.saveAll(anyList())).thenReturn(null);

		// Act
		notificator.notifyUsers();

		// Assert
		verify(bot, times(1)).isRunning();
		verify(productManager, times(1)).findAllForNotify();
		verify_Bot_SendMessage("101", "<b>Новые товары:</b>\n<a href=\"https://example.com\">test product</a> - 100.00₽");
		verify_ProductManager_SaveAll();
	}

	@Test
	void notifyUsers_BotNotRunning() {
		// Arrange
		when(bot.isRunning()).thenReturn(false);

		// Act
		notificator.notifyUsers();

		// Assert
		verify(bot, times(1)).isRunning();
		verify(productManager, times(0)).findAllForNotify();
		verify(bot, times(0)).sendMessage(any(SendMessage.class));
		verify(productManager, times(0)).saveAll(anyList());
	}

	// Checks
	private void verify_Bot_SendMessage(String chatId, String text) {
		ArgumentCaptor<SendMessage> captorSendMessage = ArgumentCaptor.forClass(SendMessage.class);
		verify(bot, times(1)).sendMessage(captorSendMessage.capture());

		SendMessage sendMessage = captorSendMessage.getValue();
		assertNotNull(sendMessage);
		assertEquals(chatId, sendMessage.getChatId());
		assertEquals(text, sendMessage.getText());
	}

	private void verify_ProductManager_SaveAll() {
		ArgumentCaptor<List<Product>> captorSaveAll = ArgumentCaptor.forClass(List.class);
		verify(productManager, times(1)).saveAll(captorSaveAll.capture());

		List<Product> products = captorSaveAll.getValue();
		assertNotNull(products);
		assertEquals(1, products.size());

		for (Product product : products) {
			assertTrue(product.getUserNotified());
		}
	}

}
