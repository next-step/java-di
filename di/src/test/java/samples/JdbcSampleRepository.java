package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Repository;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

@Repository
public class JdbcSampleRepository implements SampleRepository {

    private final DataSource dataSource;

    @Autowired
    public JdbcSampleRepository() {
        this.dataSource = new JdbcDataSource();
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
