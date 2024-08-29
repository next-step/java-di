package samples.config;

import javax.sql.DataSource;

import com.interface21.context.annotation.ComponentScan;
import org.h2.jdbcx.JdbcDataSource;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import samples.JdbcTemplate;

@Configuration
@ComponentScan({"samples"})
public class IntegrationConfig {

    @Bean
    public DataSource dataSource() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");
        return jdbcDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
