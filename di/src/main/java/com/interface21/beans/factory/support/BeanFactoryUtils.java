package com.interface21.beans.factory.support;

import com.interface21.beans.factory.annotation.Autowired;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanFactoryUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanFactoryUtils.class);

    @SuppressWarnings({ "unchecked" })
    public static Set<Method> getAutowiredMethods(Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Autowired.class), ReflectionUtils.withReturnType(void.class));
    }

    @SuppressWarnings({ "unchecked" })
    public static Set<Method> getBeanMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(annotation));
    }

    @SuppressWarnings({ "unchecked" })
    public static Set<Field> getAutowiredFields(Class<?> clazz) {
        return ReflectionUtils.getAllFields(clazz, ReflectionUtils.withAnnotation(Autowired.class));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Set<Constructor> getAutowiredConstructors(Class<?> clazz) {
        return ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Autowired.class));
    }

    public static Optional<Object> invokeMethod(Method method, Object bean, Object[] args) {
        try {
            return Optional.ofNullable(method.invoke(bean, args));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 인자로 전달하는 클래스의 생성자 중 @Autowired 애노테이션이 설정되어 있는 생성자를 반환
     * 
     * @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
     * @param clazz
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Constructor<?> getAutowiredConstructor(Class<?> clazz) {
        Set<Constructor> injectedConstructors = ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Autowired.class));
        if (injectedConstructors.isEmpty()) {
            return null;
        }
        return injectedConstructors.iterator().next();
    }

    /**
     * 인자로 전달되는 클래스의 구현 클래스. 만약 인자로 전달되는 Class가 인터페이스가 아니면 전달되는 인자가 구현 클래스,
     * 인터페이스인 경우 BeanFactory가 관리하는 모든 클래스 중에 인터페이스를 구현하는 클래스를 찾아 반환
     * 
     * @param clazz 구현 클래스를 찾아야 할 대상
     * @param beanClasses 구현 클래스가 들어있는 List
     * @return clazz가 구현 클래스인 경우 그대로 반환, 인터페이스인 경우 구현 클래스를 찾아서 반환
     */
    public static Optional<Class<?>> findConcreteClass(Class<?> clazz, Set<Class<?>> beanClasses) {
        if (clazz == null) {
            throw new IllegalArgumentException("class가 null일 수 없습니다.");
        }

        if (clazz.isInterface()) {
            Set<Class<?>> concreteClass = findAllConcreteClasses(clazz, beanClasses);
            if (concreteClass.size() > 1) {
                throw new IllegalArgumentException("해당 Interface를 구현한 클래스가 2개 이상입니다.");
            }
            return concreteClass.stream().findAny();
        }

        return Optional.of(clazz);
    }

    private static Set<Class<?>> findAllConcreteClasses(Class<?> clazz, Set<Class<?>> beanClasses) {
        return beanClasses.stream()
                .filter(beanClass -> {
                    Set<Class<?>> interfaces = Set.of(beanClass.getInterfaces());
                    return interfaces.contains(clazz);
                })
                .collect(Collectors.toSet());
    }
}
