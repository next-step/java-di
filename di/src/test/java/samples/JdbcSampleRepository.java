package samples;

import com.interface21.beans.factory.annotation.Autowired;
import com.interface21.context.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcSampleRepository implements SampleRepository {

    private final DataSource dataSource;

    @Autowired
    public JdbcSampleRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
