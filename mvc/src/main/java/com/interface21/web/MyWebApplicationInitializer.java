package com.interface21.web;

import com.interface21.context.support.WebApplicationContext;
import com.interface21.webmvc.servlet.mvc.DispatcherServlet;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

  private static final Logger log = LoggerFactory.getLogger(MyWebApplicationInitializer.class);

  @Override
  public void onStartup(final ServletContext container) {
    final var applicationContext = new WebApplicationContext(
        (Class<?>) container.getAttribute("ConfigClass"));

    final var dispatcherServlet = new DispatcherServlet();
    dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(applicationContext));
    dispatcherServlet.addHandlerMapping(new ManualHandlerMapping());

    dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());
    dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

    final var dispatcher = container.addServlet("dispatcher", dispatcherServlet);
    dispatcher.setLoadOnStartup(1);
    dispatcher.addMapping("/");

    log.info("Start AppWebApplication Initializer");
  }
}
