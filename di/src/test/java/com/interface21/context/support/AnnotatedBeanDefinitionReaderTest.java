package com.interface21.context.support;

import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import java.util.Set;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.IntegrationConfig;
import samples.JdbcTemplate;

@DisplayName("AnnotatedBeanDefinitionReader 클래스의")
class AnnotatedBeanDefinitionReaderTest {

    @DisplayName("loadBeanDefinitions 메서드는")
    @Nested
    class LoadBeanDefinitions {

        @DisplayName("config 클래스를 주입받아 BeanDefinition을 등록한다.")
        @Test
        void loadBeanDefinitions() {
            // given
            DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
            final var reader = new AnnotatedBeanDefinitionReader(beanFactory);

            //when
            reader.loadBeanDefinitions(IntegrationConfig.class);

            //then
            Set<Class<?>> beanClasses = beanFactory.getBeanClasses();
            assertTrue(beanClasses.contains(IntegrationConfig.class));
            assertTrue(beanClasses.contains(DataSource.class));
            assertTrue(beanClasses.contains(JdbcTemplate.class));
        }
    }
}
