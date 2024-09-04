package samples;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.Configuration;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public class ExampleFailConfig {

    public DataSource dataSource() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");
        return jdbcDataSource;
    }
}
