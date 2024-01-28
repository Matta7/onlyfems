package fr.univrouen.onlyfems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class OnlyfemsApplication {
// public class OnlyfemsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OnlyfemsApplication.class, args);
    }

    // @Override
    // protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    //     return application.sources(OnlyfemsApplication.class);
    // }
}
