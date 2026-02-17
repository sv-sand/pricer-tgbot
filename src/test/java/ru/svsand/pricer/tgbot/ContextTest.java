package ru.svsand.pricer.tgbot;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import ru.svsand.pricer.tgbot.logic.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContextTest {

	@InjectMocks
	private Context context;

	@Mock
	private ApplicationContext applicationContext;

	@Test
	void setApplicationContext() {
		context.setApplicationContext(applicationContext);
		assertEquals(applicationContext, Context.get());
	}

	@Test
	void getBean() {
		// Arrange
		context.setApplicationContext(applicationContext);
		when(applicationContext.getBean(any(Class.class))).thenReturn(context);

		// Act & Assert
		assertEquals(context, Context.getBean(Context.class));
	}
}