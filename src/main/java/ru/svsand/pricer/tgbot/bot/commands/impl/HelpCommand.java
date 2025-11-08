package ru.svsand.pricer.tgbot.bot.commands.impl;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.bot.commands.CommandBase;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */

public class HelpCommand extends CommandBase {

    public static final String ID = "/help";

    public HelpCommand(CommandService commandService) {
        super(commandService);
    }

    @Override
    public SendMessage process(Update update) {
        String text =
                "<b>Помощь</b>\n" +
                "Это бот для мониторинга цен на товары в интернет-магазинах\n" +
                "Доступные команды:\n" +
                "/help - помощь\n" +
                "/searches_list - список всех поисков\n" +
                "/new_search - создать новый поиск товара\n" +
                "/delete_search - остановить поиск";

        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text(text)
                .parseMode(ParseMode.HTML)
                .build();
    }
}
