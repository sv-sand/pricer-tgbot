package ru.svsand.pricer.tgbot.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Contract for a Telegram bot command. Each command handles three interaction modes:
 * initial invocation, callback queries, and follow-up text answers.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 04.11.2025
 */
public interface Command {

    /** Telegram command prefix. */
    String PREFIX = "/";

    /**
     * Handles the initial command message sent by the user.
     *
     * @param update the incoming Telegram update
     * @return the response message to send back
     */
    SendMessage process(Update update);

    /**
     * Handles an inline keyboard callback query related to this command.
     *
     * @param update the incoming Telegram update containing the callback query
     * @return the response message to send back
     */
    SendMessage processCallback(Update update);

    /**
     * Handles a follow-up text answer from the user when this command is awaiting input.
     *
     * @param update the incoming Telegram update containing the user's answer
     * @return the response message to send back
     */
    SendMessage processAnswer(Update update);
}
