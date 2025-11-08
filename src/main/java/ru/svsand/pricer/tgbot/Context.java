package ru.svsand.pricer.tgbot;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author sand <sve.snd@gmail.com>
 * @since 07.06.2023
 */

@Component
public class Context implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public static ApplicationContext get() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
