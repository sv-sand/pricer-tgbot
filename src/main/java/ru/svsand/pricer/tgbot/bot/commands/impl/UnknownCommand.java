package ru.svsand.pricer.tgbot.bot.commands.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.bot.commands.CommandBase;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */

public class UnknownCommand extends CommandBase {

    public static final String ID = "/unknown";

    public UnknownCommand(CommandService commandService) {
        super(commandService);
    }

    @Override
    public SendMessage process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text("Неизвестная команда")
                .build();
    }
}
