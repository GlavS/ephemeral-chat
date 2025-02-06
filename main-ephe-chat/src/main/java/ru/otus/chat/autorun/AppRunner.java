package ru.otus.chat.autorun;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.otus.chat.controller.UploadException;

@Component
@Slf4j
public class AppRunner implements CommandLineRunner {

    private final Environment environment;

    public AppRunner(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        log.info("Creating upload directory...");
        String uploadPath = environment.getProperty("chat.upload-path");
        if (uploadPath == null) {
            throw new UploadException("Upload path not set");
        }
        File uploadDir = new File(uploadPath);
        boolean mkdirs = uploadDir.mkdirs();
        if (!mkdirs && !uploadDir.exists()) {
            log.warn("Was unable to create directory: {}", uploadDir.getAbsolutePath());
        } else {
            log.info("...created: {}", uploadDir.getAbsolutePath());
        }
        String url = environment.getProperty("spring.flyway.url");
        String user = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .cleanDisabled(false)
                .load();
        flyway.clean();
        flyway.migrate();
    }
}
