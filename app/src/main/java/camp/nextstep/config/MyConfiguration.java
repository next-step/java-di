package com.interface21.beans.factory.config;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

@Configuration
@ComponentScan({ "camp.nextstep", "com.interface21" })
public class MyConfiguration {

  @Bean
  public DataSource dataSource() {
    final var jdbcDataSource = new JdbcDataSource();
    jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
    jdbcDataSource.setUser("");
    jdbcDataSource.setPassword("");
    return jdbcDataSource;
  }

  @Bean
  public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}