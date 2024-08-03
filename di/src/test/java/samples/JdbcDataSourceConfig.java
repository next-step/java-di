package samples;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class JdbcDataSourceConfig {
    @Bean
    public DataSource jdbcSampleDataSource() {
        return new JdbcSampleDataSource();
    }
}
