package zhihui.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author CHENPrime-coder
 */
@EnableCaching
@SpringBootApplication
public class ZhihuiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuiBackendApplication.class, args);
    }

}
