package ru.svsand.pricer.tgbot.bot.commands;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.Data;

import static org.junit.jupiter.api.Assertions.*;

class CommandBaseTest {

	@Test
	void process() {
		// Arrange
		Command command = new Data.TestCommand(null);
		Update update = Data.tgUpdate("Some text");

		// Act
		SendMessage result = command.process(update);

		// Assert
		assertNotNull(result);
		assertEquals("Упс, что-то пошло не так... Не установлен обработчик команды", result.getText());
	}

	@Test
	void processAnswer() {
		// Arrange
		Command command = new Data.TestCommand(null);
		Update update = Data.tgUpdate("Some text");

		// Act
		SendMessage result = command.processAnswer(update);

		// Assert
		assertNotNull(result);
		assertEquals("Упс, что-то пошло не так... Не установлен ответ команды", result.getText());
	}

	@Test
	void processCallback() {
		// Arrange
		Command command = new Data.TestCommand(null);
		Update update = Data.tgUpdate("Some text");

		// Act
		SendMessage result = command.processCallback(update);

		// Assert
		assertNotNull(result);
		assertEquals("Упс, что-то пошло не так... Не установлен колбек команды", result.getText());
	}
}