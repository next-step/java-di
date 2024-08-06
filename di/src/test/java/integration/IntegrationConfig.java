package integration;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import org.h2.jdbcx.JdbcDataSource;
import samples.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class IntegrationConfig {

    @Bean
    public DataSource IntegrationDataSource() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");
        return jdbcDataSource;
    }

    @Bean
    public JdbcTemplate IntegrationJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
