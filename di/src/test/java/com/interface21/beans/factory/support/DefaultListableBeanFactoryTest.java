package com.interface21.beans.factory.support;

import com.interface21.beans.BeanFactoryException;
import com.interface21.beans.BeanInstantiationException;
import com.interface21.beans.factory.config.ConfigurationClassBeanDefinitionReader;
import com.interface21.context.stereotype.Controller;
import di.DiRepository;
import di.DiService;
import org.h2.jdbcx.JdbcDataSource;
import samples.JdbcSampleRepository;
import wrong.CorrectBean;
import wrong.CorrectBeanImpl;
import wrong.DuplicateBean;
import wrong.EmptyBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import samples.SampleController;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultListableBeanFactoryTest {

    private Reflections reflections;
    private DefaultListableBeanFactory beanFactory;
    private ConfigurationClassBeanDefinitionReader reader;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        reflections = new Reflections("samples");
        beanFactory = new DefaultListableBeanFactory("samples");
        beanFactory.initialize();
        reader = new ConfigurationClassBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(beanFactory.getBeanClasses().toArray(new Class[0]));
        beanFactory.refresh();
    }

    @Test
    public void di() {
        final var sampleController = beanFactory.getBean(SampleController.class);

        assertNotNull(sampleController);
        assertNotNull(sampleController.getSampleService());

        final var sampleService = sampleController.getSampleService();
        assertNotNull(sampleService.getSampleRepository());
    }

    @DisplayName("요구 하는 빈 클래스와 매핑 된 빈 객체가 두개 이상인 경우 예외를 던진다")
    @Test
    public void getDuplicateBean() throws Exception {
        // given
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory("wrong");
        beanFactory.initialize();
        reader.loadBeanDefinitions(beanFactory.getBeanClasses().toArray(new Class[0]));
        beanFactory.refresh();

        // when then
        assertThatThrownBy(() -> beanFactory.getBean(DuplicateBean.class))
                .isExactlyInstanceOf(BeanFactoryException.class)
                .hasMessageContainingAll("No qualifying bean of type 'interface wrong.DuplicateBean' available: expected single matching bean but found 2:", "class wrong.DuplicateBeanImpl2", "class wrong.DuplicateBeanImpl1");
    }

    @DisplayName("요구 하는 빈 클래스와 매핑 된 빈 객체가 없는 경우 예외를 던진다")
    @Test
    public void getEmptyBean() throws Exception {
        // given
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory("wrong");

        // when then
        assertThatThrownBy(() -> beanFactory.getBean(EmptyBean.class))
                .isExactlyInstanceOf(BeanFactoryException.class)
                .hasMessage("Bean not found 'interface wrong.EmptyBean'");
    }

    @DisplayName("생성된 빈 중 일치 하는 타입을 반환 한다")
    @Test
    public void getBean() throws Exception {
        // given
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory("wrong");
        final ConfigurationClassBeanDefinitionReader reader = new ConfigurationClassBeanDefinitionReader(beanFactory);
        beanFactory.initialize();
        reader.loadBeanDefinitions(beanFactory.getBeanClasses().toArray(new Class[0]));
        beanFactory.refresh();

        // when
        final CorrectBean actual = beanFactory.getBean(CorrectBean.class);

        // then
        assertThat(actual).isExactlyInstanceOf(CorrectBeanImpl.class);
    }

    @DisplayName("빈이 아닌 객체를 주입 받는 빈을 생성할 때 예외를 던진다")
    @Test
    public void illDependentBean() throws Exception {
        // given
        final String basePackage = "ill_dependent";
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(basePackage);
        final ConfigurationClassBeanDefinitionReader reader = new ConfigurationClassBeanDefinitionReader(beanFactory);
        beanFactory.initialize();
        reader.loadBeanDefinitions(beanFactory.getBeanClasses().toArray(new Class[0]));

        // when then
        assertThatThrownBy(beanFactory::refresh)
                .isExactlyInstanceOf(BeanInstantiationException.class)
                .hasMessage("Failed to instantiate [ill_dependent.NotBean]: Class not BeanType");
    }

    @DisplayName("애너테이션이 붙은 빈들을 반환 한다")
    @Test
    public void getBeansForAnnotation() throws Exception {
        // given
        final Class<Controller> controllerAnnotation = Controller.class;

        // when
        final List<Object> actual = beanFactory.getBeansForAnnotation(controllerAnnotation);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull().hasSize(1),
                () -> assertInstanceOf(SampleController.class, actual.get(0))
        );
    }

    @DisplayName("@Bean 으로 선언 된 빈을 생성 하여 @Component 빈에 주입 한다")
    @Test
    public void diBeanMethod() throws Exception {
        // when
        final DataSource actual = beanFactory.getBean(JdbcSampleRepository.class).getDataSource();

        // then
        assertThat(actual).isNotNull().isInstanceOf(JdbcDataSource.class);
    }

    @DisplayName("@Component 로 생성한 빈을 @Bean 빈에 주입 한다")
    @Test
    public void diComponent() throws Exception {
        // given
        final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory("di");
        final ConfigurationClassBeanDefinitionReader reader = new ConfigurationClassBeanDefinitionReader(beanFactory);
        beanFactory.initialize();
        reader.loadBeanDefinitions(beanFactory.getBeanClasses().toArray(new Class[0]));
        beanFactory.refresh();

        // when
        final DiRepository actual = beanFactory.getBean(DiService.class).getRepository();

        // then
        assertThat(actual).isNotNull();
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
