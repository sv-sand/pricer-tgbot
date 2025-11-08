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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */

public class DeleteSearchCommand extends CommandBase {

    public static final String ID = "/delete_search";
    private Map<Long, Search> availableSearches;

    public DeleteSearchCommand(CommandService commandService) {
        super(commandService);
    }

    @Override
    public SendMessage process(Update update) {
        commandService.waitAnswer(update.getMessage().getFrom(), this);

        User user = Context.getBean(UserManager.class)
                .findByTgId(update.getMessage().getFrom().getId());

        List<Search> searches = Context.getBean(SearchManager.class)
                .findByUserId(user.getId());

        availableSearches = searches.stream()
                .collect(Collectors.toMap(Search::getId, obj -> obj));

        String representation = searches.stream()
                .map(search -> String.format("[%s] %s", search.getId(), search.getRepresentation()))
                .collect(Collectors.joining("\n"));

        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text("<b>Список запросов:</b>\n" + representation + "\n\nВведите [ID] запроса для удаления")
                .parseMode(ParseMode.HTML)
                .build();
    }

    @Override
    public SendMessage processAnswer(Update update) {
        Search search;
        try {
            Long id = Long.valueOf(update.getMessage().getText());
            search = availableSearches.get(id);
        } catch (NumberFormatException e) {
            return SendMessage.builder()
                    .chatId(update.getMessage().getFrom().getId())
                    .text("Неверный ID запроса")
                    .build();
        }
        Context.getBean(SearchManager.class)
                .delete(search);

        commandService.stopWaitingAnswer(update.getMessage().getFrom());

        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text(String.format("Запрос %s удален", search.getRepresentation()))
                .build();
    }
}
