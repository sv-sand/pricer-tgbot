package ru.svsand.pricer.tgbot;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Provides static access to the Spring {@link ApplicationContext}.
 * Useful for obtaining beans in non-Spring-managed classes (e.g. command implementations).
 *
 * @author sand <sve.snd@gmail.com>
 * @since 07.06.2023
 */
@Component
public class Context implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * Called by Spring to inject the application context after startup.
     *
     * @param context the Spring application context
     * @throws BeansException if an error occurs during context initialization
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * Returns the Spring application context.
     *
     * @return the current {@link ApplicationContext}
     */
    public static ApplicationContext get() {
        return applicationContext;
    }

    /**
     * Returns a Spring-managed bean of the given type.
     *
     * @param <T>   the bean type
     * @param clazz the class of the desired bean
     * @return the bean instance
     */
    @NotNull
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
