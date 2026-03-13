package ru.svsand.pricer.tgbot.bot.commands.impl;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.bot.commands.CommandBase;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;

/**
 * Handles the {@code /help} command. Sends a description of the bot and all available commands.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */
public class HelpCommand extends CommandBase {

    public static final String ID = "/help";

    /**
     * @param commandService the shared command routing service
     */
    public HelpCommand(CommandService commandService) {
        super(commandService);
    }

    /** {@inheritDoc} */
    @Override
    public SendMessage process(Update update) {
        String text =
				"""
                <b>Помощь</b>
                Это бот для мониторинга цен на товары в интернет-магазинах
                Доступные команды:
                /help - помощь
                /searches_list - список всех поисков
                /new_search - создать новый поиск товара
                /delete_search - остановить поиск
                /statistic - показать статистику поисков за последние сутки
                
                Каждый час бот запускает поиск товаров на маркетплейсах, выбирает товары цена которых ниже желаемой, \
                и отправляет уведомление пользователю.""";

        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text(text)
                .parseMode(ParseMode.HTML)
                .build();
    }
}
