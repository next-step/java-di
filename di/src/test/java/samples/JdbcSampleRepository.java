package samples;

import com.interface21.context.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcSampleRepository implements SampleRepository {

    private final DataSource dataSource;

    //TODO configuration을 없애면 제거 예정
    public JdbcSampleRepository() {
        dataSource = null;
    }

    public JdbcSampleRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
