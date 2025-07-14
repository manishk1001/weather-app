package weatherApp.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppConfigTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void restTemplateBeanShouldBeLoaded() {
        assertThat(restTemplate).isNotNull();
    }
}

