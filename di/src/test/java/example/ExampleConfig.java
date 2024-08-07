package example;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"example", "samples"})
public class ExampleConfig {

    @Bean
    public DataSource exampleDataSource() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");
        return jdbcDataSource;
    }
}
