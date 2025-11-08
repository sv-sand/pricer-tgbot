package ru.svsand.pricer.tgbot.bot.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    String PREFIX = "/";
    String getId();

    SendMessage process(Update update);
    SendMessage processCallback(Update update);
    SendMessage processAnswer(Update update);
}
