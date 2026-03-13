package ru.svsand.pricer.tgbot.bot.commands.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.svsand.pricer.tgbot.Context;
import ru.svsand.pricer.tgbot.bot.commands.CommandBase;
import ru.svsand.pricer.tgbot.bot.commands.CommandService;
import ru.svsand.pricer.tgbot.db.SearchManager;
import ru.svsand.pricer.tgbot.db.UserManager;
import ru.svsand.pricer.tgbot.logic.Search;
import ru.svsand.pricer.tgbot.logic.Store;
import ru.svsand.pricer.tgbot.logic.User;

/**
 * Handles the {@code /new_search} command. Guides the user through a two-step dialog
 * to collect search keywords and a target price, then persists the new search.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */
public class NewSearchCommand extends CommandBase {

    public static final String ID = "/new_search";
    private String keywords = "";
    private Double price = null;

    /**
     * @param commandService the shared command routing service
     */
    public NewSearchCommand(CommandService commandService) {
        super(commandService);
    }

    /**
     * {@inheritDoc}
     * <p>Prompts the user to enter search keywords and registers this command as awaiting an answer.</p>
     */
    @Override
    public SendMessage process(Update update) {
        commandService.waitAnswer(update.getMessage().getFrom(), this);
        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text("Введите ключевые слова для поиска товара")
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>Collects keywords on the first call and target price on the second.
     * Saves the search once both values are provided.</p>
     */
    @Override
    public SendMessage processAnswer(Update update) {
        if (keywords.isEmpty()) {
            keywords = update.getMessage().getText();
            return SendMessage.builder()
                    .chatId(update.getMessage().getFrom().getId())
                    .text("Введите желаемую цену")
                    .build();
        }
        if (price == null) {
            double parsedPrice;
            try {
                parsedPrice = Double.parseDouble(update.getMessage().getText());
            } catch (NumberFormatException e) {
                return SendMessage.builder()
                        .chatId(update.getMessage().getFrom().getId())
                        .text("Неверный формат цены. Введите число, если есть копейки введите число c точкой")
                        .build();
            }
            if (parsedPrice <= 0) {
                return SendMessage.builder()
                        .chatId(update.getMessage().getFrom().getId())
                        .text("Цена должна быть больше 0")
                        .build();
            }
            price = parsedPrice;
        }

        commandService.stopWaitingAnswer(update.getMessage().getFrom());
        saveSearch(update.getMessage().getFrom().getId(), keywords, price);

        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text("Поиск сохранен")
                .build();
    }

    private void saveSearch(Long tgUserId, String keywords, Double price) {
        User user = Context.getBean(UserManager.class)
                .findByTgId(tgUserId);

        Search search = Search.builder()
                .store(Store.WB)
                .keyWords(keywords)
                .targetPrice(price)
                .user(user)
                .build();

        Context.getBean(SearchManager.class)
                .save(search);
    }
}
