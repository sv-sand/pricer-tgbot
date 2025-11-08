package ru.svsand.pricer.tgbot.bot.commands.impl;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.Context;
import ru.svsand.pricer.tgbot.bot.commands.CommandBase;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;
import ru.svsand.pricer.tgbot.db.SearchManager;
import ru.svsand.pricer.tgbot.db.UserManager;
import ru.svsand.pricer.tgbot.logic.Search;
import ru.svsand.pricer.tgbot.logic.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */

public class SearchesListCommand extends CommandBase {

    public static final String ID = "/searches_list";

    public SearchesListCommand(CommandService commandService) {
        super(commandService);
    }

    @Override
    public SendMessage process(Update update) {
        User user = Context.getBean(UserManager.class)
                .findByTgId(update.getMessage().getFrom().getId());

        List<Search> searches = Context.getBean(SearchManager.class)
                .findByUserId(user.getId());

        String representation = searches.stream()
                .map(Search::getRepresentation)
                .collect(Collectors.joining("\n"));

        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text("<b>Список запросов:</b>\n" + representation)
                .parseMode(ParseMode.HTML)
                .build();
    }
}
