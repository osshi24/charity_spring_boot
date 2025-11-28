package com.example.charitybe.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Context Utility
 * Provides static access to Spring beans from non-Spring managed classes (like JPA EntityListeners)
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
    }

    /**
     * Get a Spring bean by class type
     *
     * @param beanClass The class of the bean
     * @param <T> Bean type
     * @return The bean instance
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * Get a Spring bean by name
     *
     * @param beanName The name of the bean
     * @return The bean instance
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * Check if a bean exists in the context
     *
     * @param beanClass The class of the bean
     * @return true if bean exists
     */
    public static boolean containsBean(Class<?> beanClass) {
        try {
            context.getBean(beanClass);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
