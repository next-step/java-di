package camp.nextstep;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.DefaultListableBeanFactory;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Set;

public class BeanFactoryServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(final Set<Class<?>> set, final ServletContext servletContext) throws ServletException {
        servletContext.setAttribute(BeanFactory.BEAN_FACTORY_CONTEXT_ATTRIBUTE, new DefaultListableBeanFactory("camp.nextstep"));
    }
}
