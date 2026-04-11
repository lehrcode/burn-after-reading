package de.lehrcode.burnafterreading;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.dialect.JdbcDialect;
import org.springframework.data.jdbc.core.dialect.JdbcPostgresDialect;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@Configuration
@EnableJdbcAuditing
public class DataJdbcConfiguration {
    /**
     * @see "https://github.com/spring-projects/spring-boot/issues/48240"
     * @see "https://github.com/spring-projects/spring-boot/issues/47781"
     */
    @Bean
    @ConditionalOnMissingBean
    public JdbcDialect jdbcDialect() {
        return JdbcPostgresDialect.INSTANCE;
    }
}
