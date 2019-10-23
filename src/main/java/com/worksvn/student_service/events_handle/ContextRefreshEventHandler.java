package com.worksvn.student_service.events_handle;

import com.worksvn.common.annotations.event.EventHandler;
import com.worksvn.common.utils.core.ClassScannerUtils;
import com.worksvn.student_service.constants.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.instrument.IllegalClassFormatException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ContextRefreshEventHandler implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ContextRefreshEventHandler.class);

    private boolean completed;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!completed) {
            completed = true;
            logger.info("[START] APPLICATION STARTED UP");
            try {
                String scannedPackage = ApplicationConstants.BASE_PACKAGE_NAME + ".events_handle";
                Set<Class<? extends Annotation>> includedAnnotations = new HashSet<>();
                includedAnnotations.add(EventHandler.class);
                List<Class<?>> eventHandlerClassFounds = ClassScannerUtils
                        .scanAnnotatedClasses(scannedPackage, includedAnnotations, null,
                                clazz -> {
                                    EventHandler eventHandler = clazz.getDeclaredAnnotation(EventHandler.class);
                                    return eventHandler.event().equals(ApplicationEvent.ON_APPLICATION_STARTED_UP);
                                },
                                (clazz1, clazz2) -> {
                                    int order1 = clazz1.getDeclaredAnnotation(EventHandler.class).order();
                                    int order2 = clazz2.getDeclaredAnnotation(EventHandler.class).order();
                                    return Integer.compare(order1, order2);
                                });

                for (Class<?> eventHandlerClass : eventHandlerClassFounds) {
                    if (ApplicationEventHandle.class.isAssignableFrom(eventHandlerClass)) {
                        ApplicationEventHandle applicationEventHandle;
                        try {
                            applicationEventHandle = ((ApplicationEventHandle) event.getApplicationContext().getBean(eventHandlerClass));
                        } catch (NoSuchBeanDefinitionException e) {
                            applicationEventHandle = ((ApplicationEventHandle) eventHandlerClass.newInstance());
                        }
                        logger.info(applicationEventHandle.startMessage());
                        applicationEventHandle.handleEvent();
                        logger.info(applicationEventHandle.successMessage());
                    } else {
                        throw new IllegalClassFormatException(eventHandlerClass.getName() +
                                " is annotated with @EventHandler must implement " + ApplicationEventHandle.class.getName());

                    }
                }
                unregisterListener(event.getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                logger.info("[END] APPLICATION STARTED UP");
            }
        }
    }

    private void unregisterListener(ApplicationContext context) {
        ApplicationEventMulticaster applicationEvents = context
                .getBean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                        ApplicationEventMulticaster.class);
        applicationEvents.removeApplicationListener(this);
    }
}
