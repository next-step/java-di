package camp.nextstep;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import com.interface21.context.WebApplicationContext;
import com.interface21.context.annotation.ComponentScan;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Set;

public class BeanFactoryServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(final Set<Class<?>> set, final ServletContext servletContext) throws ServletException {
        final WebApplicationContext applicationContext = new WebApplicationContext(extractBasePackage());
        servletContext.setAttribute(BeanFactory.BEAN_FACTORY_CONTEXT_ATTRIBUTE, applicationContext);
        applicationContext.initialize();
    }

    private String[] extractBasePackage() {
        final Class<Application> applicationClass = Application.class;
        final ComponentScan componentScan = applicationClass.getAnnotation(ComponentScan.class);

        final String[] value = componentScan.value();

        if (value.length == 0) {
            return new String[]{applicationClass.getPackage().getName()};
        }

        return value;
    }
}
