package samples;

import javax.sql.DataSource;

import com.interface21.context.stereotype.Repository;

@Repository
public class JdbcSampleRepository implements SampleRepository {

    private DataSource dataSource;

//    public JdbcSampleRepository(final DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
