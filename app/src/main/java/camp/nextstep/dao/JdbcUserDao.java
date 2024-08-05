package camp.nextstep.dao;

import com.interface21.context.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class JdbcUserDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        log.info("create JdbcUserDao");
        log.info("do dependency injection jdbcTemplate={}", jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        log.info("complete dependency injection");
    }
}
