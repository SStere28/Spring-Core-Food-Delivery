package com.epam.training.fooddelivery;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSpring {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext applicationContext = new
                AnnotationConfigApplicationContext(ApplicationConfig.class)) {
            Application application = applicationContext.getBean(Application.class);
            application.run();
        }
    }
}
