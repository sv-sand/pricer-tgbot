package ru.svsand.pricer.tgbot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.svsand.pricer.tgbot.bot.commands.impl.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This service manage the process of run commands
 * @author sand <sve.snd@gmail.com>
 * @since 04.11.2025
 */

@Slf4j
@Service
public class CommandService {

    private final Map<String, Class<? extends CommandBase>> commands = new HashMap<>();
    private final Map<User, Command> commandsAwaitingAnswer = new HashMap<>();

    public CommandService() {
        commands.put(HelpCommand.ID, HelpCommand.class);
        commands.put(NewSearchCommand.ID, NewSearchCommand.class);
        commands.put(SearchesListCommand.ID, SearchesListCommand.class);
        commands.put(DeleteSearchCommand.ID, DeleteSearchCommand.class);
    }

    public SendMessage processUpdate(Update update) {
        if (isCommand(update))
            return processCommand(update);
        else if (isAnswer(update))
            return processAnswer(update);
        else if (update.hasCallbackQuery())
            return processCallback(update);
        else
            return new UnknownCommand(this)
                    .process(update);
    }

    private boolean isCommand(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().getText().startsWith(Command.PREFIX);
    }

    private boolean isAnswer(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && isWaitingAnswer(update.getMessage().getFrom());
    }

    private SendMessage processCommand(Update update) {
        log.info("Processing command {}", update.getMessage().getText());

        String commandId = defineCommandId(update.getMessage().getText());
        if (commands.containsKey(commandId)) {
            stopWaitingAnswer(update.getMessage().getFrom());
            return newCommand(commands.get(commandId))
                    .process(update);
        } else {
            return new UnknownCommand(this)
                    .process(update);
        }
    }

    private SendMessage processAnswer(Update update) {
        log.info("Processing answer {}", update.getMessage().getText());

        if (commandsAwaitingAnswer.containsKey(update.getMessage().getFrom())) {
            return commandsAwaitingAnswer.get(update.getMessage().getFrom())
                    .processAnswer(update);
        } else {
            return new UnknownCommand(this)
                    .process(update);
        }
    }

    private SendMessage processCallback(Update update) {
        log.info("Processing callback {}", update.getCallbackQuery().getData());

        String commandId = defineCommandId(update.getCallbackQuery().getData());
        if (commands.containsKey(commandId)) {
            return newCommand(commands.get(commandId))
                    .processCallback(update);
        } else {
            return new UnknownCommand(this)
                    .process(update);
        }
    }

    private String defineCommandId(String text) {
        return text.split(" ")[0];
    }

    private Command newCommand(Class clazz) {
        Command command;
        try {
            Constructor constructor = clazz.getConstructor(CommandService.class);
            command = (Command) constructor.newInstance(this);
        } catch (
                NoSuchMethodException | SecurityException | InstantiationException |
                IllegalAccessException | InvocationTargetException | IllegalArgumentException e
        ) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return command;
    }

    // Await answer management

    public boolean isWaitingAnswer(User user) {
        return commandsAwaitingAnswer.containsKey(user);
    }

    public void waitAnswer(User user, Command command) {
        commandsAwaitingAnswer.put(user, command);
    }

    public void stopWaitingAnswer(User user) {
        commandsAwaitingAnswer.remove(user);
    }
}
