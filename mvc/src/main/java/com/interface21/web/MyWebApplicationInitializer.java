package com.interface21.web;

import com.interface21.context.annotation.ComponentScan;
import com.interface21.context.annotation.Configuration;
import com.interface21.context.support.NewAnnotationConfigWebApplicationContext;
import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@ComponentScan({ "camp.nextstep", "com.interface21" })
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(MyWebApplicationInitializer.class);

    @Override
    public void onStartup(final ServletContext container) {
        //final var applicationContext = new AnnotationConfigWebApplicationContext(getClass());
        final var applicationContext = new NewAnnotationConfigWebApplicationContext(getClass());

        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(applicationContext));

        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        final var dispatcher = container.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }
}
