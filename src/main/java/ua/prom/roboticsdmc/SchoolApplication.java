package ua.prom.roboticsdmc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.prom.roboticsdmc.config.SchoolApplicationConfig;
import ua.prom.roboticsdmc.controller.FrontController;

public class SchoolApplication {

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SchoolApplicationConfig.class)) {

            FrontController frontController = context.getBean(FrontController.class);
            frontController.run();
        }
    }
}
