package ru.svsand.pricer.tgbot.bot.commands.impl;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.Context;
import ru.svsand.pricer.tgbot.bot.commands.CommandBase;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;
import ru.svsand.pricer.tgbot.db.SearchStatisticManager;

import java.util.List;

/**
 * Handles the {@code /statistic} command. Displays search request statistics for the last 24 hours.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */
public class StatisticCommand extends CommandBase {

    public static final String ID = "/statistic";

    /**
     * @param commandService the shared command routing service
     */
    public StatisticCommand(CommandService commandService) {
        super(commandService);
    }

    /** {@inheritDoc} */
    @Override
    public SendMessage process(Update update) {
        List<SearchStatisticManager.SearchStatistic> statistics = Context.getBean(SearchStatisticManager.class)
                .getStatistic();

        StringBuilder sb = new StringBuilder();
        sb.append("<b>Статистика поисков за последние сутки:</b>\n");
        for (var statistic : statistics) {
            if (statistic.getStatusCode() == 200) {
                sb.append(String.format("Успешных запросов [%s]: %s\n", statistic.getStatusCode(), statistic.getRequestCount()));
            } else {
                sb.append(String.format("Ошибок [%s]: %s\n", statistic.getStatusCode(), statistic.getRequestCount()));
            }
        }

        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text(sb.toString())
                .parseMode(ParseMode.HTML)
                .build();
    }
}
