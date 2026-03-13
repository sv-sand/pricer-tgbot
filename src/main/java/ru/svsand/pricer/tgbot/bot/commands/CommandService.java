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
 * Routes incoming Telegram updates to the appropriate {@link Command} handler.
 * Maintains per-user state for multi-step commands that await a follow-up answer.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 04.11.2025
 */
@Slf4j
@Service
public class CommandService {

    private final Map<String, Class<? extends CommandBase>> userCommands;
    private final Map<User, Command> commandsAwaitingAnswer = new HashMap<>();

    /** Initializes the service and registers all supported user commands. */
    public CommandService() {
        userCommands = userCommands();
    }

    private Map<String, Class<? extends CommandBase>> userCommands() {
        Map<String, Class<? extends CommandBase>> commands = new HashMap<>();

        commands.put(StartCommand.ID, StartCommand.class);
        commands.put(HelpCommand.ID, HelpCommand.class);
        commands.put(NewSearchCommand.ID, NewSearchCommand.class);
        commands.put(SearchesListCommand.ID, SearchesListCommand.class);
        commands.put(DeleteSearchCommand.ID, DeleteSearchCommand.class);
        commands.put(StatisticCommand.ID, StatisticCommand.class);

        return commands;
    }

    /**
     * Dispatches the incoming update to the appropriate command handler.
     * Handles commands (prefixed with {@code /}), awaited text answers, and unknown input.
     *
     * @param update the incoming Telegram update
     * @return the response message to send back to the user
     */
    public SendMessage processUpdate(Update update) {
        if (isCommand(update))
            return processCommand(update);
        else if (isAnswer(update))
            return processAnswer(update);
        //else if (update.hasCallbackQuery())
        //    return processCallback(update);
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
        if (userCommands.containsKey(commandId)) {
            stopWaitingAnswer(update.getMessage().getFrom());
            return newCommand(userCommands.get(commandId))
                    .process(update);
        } else {
            return new UnknownCommand(this)
                    .process(update);
        }
    }

    private SendMessage processAnswer(Update update) {
        log.info("Processing answer {}", update.getMessage().getText());

        return commandsAwaitingAnswer.get(update.getMessage().getFrom())
                .processAnswer(update);
    }

//    private SendMessage processCallback(Update update) {
//        log.info("Processing callback {}", update.getCallbackQuery().getData());
//
//        String commandId = defineCommandId(update.getCallbackQuery().getData());
//        if (userCommands.containsKey(commandId)) {
//            return newCommand(userCommands.get(commandId))
//                    .processCallback(update);
//        } else {
//            return new UnknownCommand(this)
//                    .process(update);
//        }
//    }

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

    /**
     * Returns {@code true} if the given user has a command awaiting their next text answer.
     *
     * @param user the Telegram user
     * @return {@code true} if waiting for an answer from this user
     */
    public boolean isWaitingAnswer(User user) {
        return commandsAwaitingAnswer.containsKey(user);
    }

    /**
     * Registers a command as waiting for a follow-up answer from the given user.
     *
     * @param user    the Telegram user whose next message should be routed to {@code command}
     * @param command the command that is awaiting the answer
     */
    public void waitAnswer(User user, Command command) {
        commandsAwaitingAnswer.put(user, command);
    }

    /**
     * Removes the awaiting-answer state for the given user.
     *
     * @param user the Telegram user whose awaiting state should be cleared
     */
    public void stopWaitingAnswer(User user) {
        commandsAwaitingAnswer.remove(user);
    }
}
