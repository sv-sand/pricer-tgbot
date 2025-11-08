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
 * @author sand <sve.snd@gmail.com>
 * @since 31.05.2023
 */

public class NewSearchCommand extends CommandBase {

    public static final String ID = "/new_search";
    private String keywords = "";
    private Double price = 0.0;

    public NewSearchCommand(CommandService commandService) {
        super(commandService);
    }

    @Override
    public SendMessage process(Update update) {
        commandService.waitAnswer(update.getMessage().getFrom(), this);
        return SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId())
                .text("Введите ключевые слова для поиска товара")
                .build();
    }

    @Override
    public SendMessage processAnswer(Update update) {
        if (keywords.isEmpty()) {
            keywords = update.getMessage().getText();
            return SendMessage.builder()
                    .chatId(update.getMessage().getFrom().getId())
                    .text("Введите желаемую цену")
                    .build();
        }
        if (price == 0.0) {
            try {
                price = Double.parseDouble(update.getMessage().getText());
            } catch (NumberFormatException e) {
                return SendMessage.builder()
                        .chatId(update.getMessage().getFrom().getId())
                        .text("Неверный формат цены. Введите число, если есть копейки введите число c точкой")
                        .build();
            }
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
