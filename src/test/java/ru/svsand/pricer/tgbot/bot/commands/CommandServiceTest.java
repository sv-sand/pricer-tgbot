package ru.svsand.pricer.tgbot.bot.commands;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.Context;
import ru.svsand.pricer.tgbot.Data;
import ru.svsand.pricer.tgbot.bot.commands.impl.StatisticCommand;
import ru.svsand.pricer.tgbot.db.SearchStatisticManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

	@InjectMocks
	private CommandService service;

	@Mock
	private SearchStatisticManager searchStatisticManager;

	private static MockedStatic<Context> mockedContext;

	@BeforeAll
	static void beforeAll() {
		mockedContext = Mockito.mockStatic(Context.class);
	}

	@AfterAll
	static void afterAll() {
		mockedContext.close();
	}

	@Test
	void processUnknownCommand() {
		// Arrange
		Update update = Data.tgUpdate("/unknown_command");

		// Act
		SendMessage result = service.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Неизвестная команда", result.getText());
		assertFalse(service.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processStartCommand() {
		// Arrange
		Update update = Data.tgUpdate("/start");

		// Act
		SendMessage result = service.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Привет! Для управления ботом используйте меню.", result.getText());
		assertFalse(service.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processHelpCommand() {
		// Arrange
		Update update = Data.tgUpdate("/help");

		// Act
		SendMessage result = service.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("<b>Помощь</b>", result.getText().split("\n")[0]);
		assertFalse(service.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processStatisticCommand() {
		List<SearchStatisticManager.SearchStatistic> statistic = List.of(
				new Data.SearchStatistic(200, 101),
				new Data.SearchStatistic(500, 201)
		);
		Update update = Data.tgUpdate("/statistic");

		// Arrange
		when(searchStatisticManager.getStatistic()).thenReturn(statistic);
		when(Context.getBean(SearchStatisticManager.class)).thenReturn(searchStatisticManager);

		// Act
		SendMessage result = service.processUpdate(update);

		// Assert
		String answerText = "<b>Статистика поисков за последние сутки:</b><BR>\n" +
				"Успешных запросов [200]: 101<BR>\n" +
				"Ошибок [500]: 201<BR>\n";
		assertNotNull(result);
		assertEquals(answerText, result.getText());
		assertFalse(service.isWaitingAnswer(update.getMessage().getFrom()));
	}
}