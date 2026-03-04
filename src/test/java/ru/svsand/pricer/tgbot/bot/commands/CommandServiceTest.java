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
import ru.svsand.pricer.tgbot.db.SearchManager;
import ru.svsand.pricer.tgbot.db.SearchStatisticManager;
import ru.svsand.pricer.tgbot.db.UserManager;
import ru.svsand.pricer.tgbot.logic.Search;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandServiceTest {

	@InjectMocks
	private CommandService commandService;

	@Mock
	private SearchStatisticManager searchStatisticManager;

	@Mock
	private UserManager userManager;

	@Mock
	private SearchManager searchManager;

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
		Update update = Data.tgUpdate("/unknown");

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Неизвестная команда", result.getText());
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processStartCommand() {
		// Arrange
		Update update = Data.tgUpdate("/start");

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Привет! Для управления ботом используйте меню.", result.getText());
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processHelpCommand() {
		// Arrange
		Update update = Data.tgUpdate("/help");

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("<b>Помощь</b>", result.getText().split("\n")[0]);
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
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
		SendMessage result = commandService.processUpdate(update);

		// Assert
		String answerText = "<b>Статистика поисков за последние сутки:</b>\n" +
				"Успешных запросов [200]: 101\n" +
				"Ошибок [500]: 201\n";
		assertNotNull(result);
		assertEquals(answerText, result.getText());
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processNewSearchCommand_Start() {
		// Arrange
		Update update = Data.tgUpdate("/new_search");

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Введите ключевые слова для поиска товара", result.getText());
		assertTrue(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processNewSearchCommand_Keywords() {
		// Arrange
		Update startUpdate = Data.tgUpdate("/new_search");
		Update keywordsUpdate = Data.tgUpdate("product товар");

		// Act
		commandService.processUpdate(startUpdate);
		SendMessage result = commandService.processUpdate(keywordsUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Введите желаемую цену", result.getText());
		assertTrue(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	@Test
	void processNewSearchCommand_Price() {
		Search search = Data.search();
		Update startUpdate = Data.tgUpdate("/new_search");
		Update keywordsUpdate = Data.tgUpdate("product товар");
		Update priceUpdate = Data.tgUpdate("100.01");

		// Arrange
		when(userManager.findByTgId(any(Long.class))).thenReturn(search.getUser());
		when(searchManager.save(any(Search.class))).thenReturn(search);
		when(Context.getBean(UserManager.class)).thenReturn(userManager);
		when(Context.getBean(SearchManager.class)).thenReturn(searchManager);

		// Act
		commandService.processUpdate(startUpdate);
		commandService.processUpdate(keywordsUpdate);
		SendMessage result = commandService.processUpdate(priceUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Поиск сохранен", result.getText());
		assertFalse(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	@Test
	void processNewSearchCommand_WrongPrice() {
		// Arrange
		Update startUpdate = Data.tgUpdate("/new_search");
		Update keywordsUpdate = Data.tgUpdate("product товар");
		Update priceUpdate = Data.tgUpdate("a1");

		// Act
		commandService.processUpdate(startUpdate);
		commandService.processUpdate(keywordsUpdate);
		SendMessage result = commandService.processUpdate(priceUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Неверный формат цены. Введите число, если есть копейки введите число c точкой", result.getText());
		assertTrue(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	@Test
	void processNewSearchCommand_NegativePrice() {
		// Arrange
		Update startUpdate = Data.tgUpdate("/new_search");
		Update keywordsUpdate = Data.tgUpdate("product товар");
		Update priceUpdate = Data.tgUpdate("-100.0");

		// Act
		commandService.processUpdate(startUpdate);
		commandService.processUpdate(keywordsUpdate);
		SendMessage result = commandService.processUpdate(priceUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Цена должна быть больше 0", result.getText());
		assertTrue(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	@Test
	void processSearchListCommand() {
		Search search = Data.search();
		Update update = Data.tgUpdate("/searches_list");

		// Arrange
		when(userManager.findByTgId(any(Long.class))).thenReturn(search.getUser());
		when(searchManager.findByUserId(any(Long.class))).thenReturn(List.of(search));
		when(Context.getBean(UserManager.class)).thenReturn(userManager);
		when(Context.getBean(SearchManager.class)).thenReturn(searchManager);

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("<b>Список запросов:</b>", result.getText().split("\n")[0]);
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processDeleteSearchCommand_Start() {
		Search search = Data.search();
		Update update = Data.tgUpdate("/delete_search");

		// Arrange
		when(userManager.findByTgId(any(Long.class))).thenReturn(search.getUser());
		when(searchManager.findByUserId(any(Long.class))).thenReturn(List.of(search));
		when(Context.getBean(UserManager.class)).thenReturn(userManager);
		when(Context.getBean(SearchManager.class)).thenReturn(searchManager);

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		String text = "<b>Список запросов:</b>\n" +
				"[1] test keyword - 100.00₽\n" +
				"\n" +
				"Введите [ID] запроса для удаления";

		assertNotNull(result);
		assertEquals(text, result.getText());
		assertTrue(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processDeleteSearchCommand_EnterId() {
		Search search = Data.search();
		Update startUpdate = Data.tgUpdate("/delete_search");
		Update idUpdate = Data.tgUpdate("1");

		// Arrange
		when(userManager.findByTgId(any(Long.class))).thenReturn(search.getUser());
		when(searchManager.findByUserId(any(Long.class))).thenReturn(List.of(search));
		when(Context.getBean(UserManager.class)).thenReturn(userManager);
		when(Context.getBean(SearchManager.class)).thenReturn(searchManager);

		// Act
		commandService.processUpdate(startUpdate);
		SendMessage result = commandService.processUpdate(idUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Запрос test keyword - 100.00₽ удален", result.getText());
		assertFalse(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	@Test
	void processDeleteSearchCommand_WrongId() {
		Search search = Data.search();
		Update startUpdate = Data.tgUpdate("/delete_search");
		Update idUpdate = Data.tgUpdate("a1");

		// Arrange
		when(userManager.findByTgId(any(Long.class))).thenReturn(search.getUser());
		when(searchManager.findByUserId(any(Long.class))).thenReturn(List.of(search));
		when(Context.getBean(UserManager.class)).thenReturn(userManager);
		when(Context.getBean(SearchManager.class)).thenReturn(searchManager);

		// Act
		commandService.processUpdate(startUpdate);
		SendMessage result = commandService.processUpdate(idUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Неверный ID запроса", result.getText());
		assertTrue(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	@Test
	void processDeleteSearchCommand_UndefinedId() {
		Search search = Data.search();
		Update startUpdate = Data.tgUpdate("/delete_search");
		Update idUpdate = Data.tgUpdate("0");

		// Arrange
		when(userManager.findByTgId(any(Long.class))).thenReturn(search.getUser());
		when(searchManager.findByUserId(any(Long.class))).thenReturn(List.of(search));
		when(Context.getBean(UserManager.class)).thenReturn(userManager);
		when(Context.getBean(SearchManager.class)).thenReturn(searchManager);

		// Act
		commandService.processUpdate(startUpdate);
		SendMessage result = commandService.processUpdate(idUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Запрос не найден", result.getText());
		assertTrue(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	// Negative tests

	@Test
	void processEmptyCommand() {
		// Arrange
		Update update = Data.tgUpdate(null);

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Неизвестная команда", result.getText());
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processEmptyAnswer() {
		// Arrange
		Update startUpdate = Data.tgUpdate("/new_search");
		Update keywordsUpdate = Data.tgUpdate("");

		// Act
		commandService.processUpdate(startUpdate);
		SendMessage result = commandService.processUpdate(keywordsUpdate);

		// Assert
		assertNotNull(result);
		assertEquals("Неизвестная команда", result.getText());
		assertTrue(commandService.isWaitingAnswer(startUpdate.getMessage().getFrom()));
	}

	@Test
	void processUndefinedCommand() {
		// Arrange
		Update update = Data.tgUpdate("/wrong_command");

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Неизвестная команда", result.getText());
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}

	@Test
	void processWrongCommand() {
		// Arrange
		Update update = Data.tgUpdate("no command");

		// Act
		SendMessage result = commandService.processUpdate(update);

		// Assert
		assertNotNull(result);
		assertEquals("Неизвестная команда", result.getText());
		assertFalse(commandService.isWaitingAnswer(update.getMessage().getFrom()));
	}
}