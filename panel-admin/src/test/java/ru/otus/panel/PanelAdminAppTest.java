package ru.otus.panel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PanelAdminAppTest {

    @Value("${panel.admin.username}")
    private String admin;

    @Value("${panel.admin.password}")
    private String password;

    @Test
    void contextLoads() {
        assertThat(admin).isEqualTo("testAdmin");
        assertThat(password).isEqualTo("testPassword");
    }
}
