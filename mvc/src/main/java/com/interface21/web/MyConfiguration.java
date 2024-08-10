package com.interface21.web;

import com.interface21.context.annotation.Bean;
import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
@ComponentScan({ "camp.nextstep", "com.interface21" })
public class MyConfiguration {
}
