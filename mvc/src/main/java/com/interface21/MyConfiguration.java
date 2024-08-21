package com.interface21;

import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;

@Configuration
@ComponentScan({ "camp.nextstep", "com.interface21" })
public class MyConfiguration {
}
