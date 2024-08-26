package com.interface21.core.util;

import java.util.Collection;

public abstract class StringUtils {

    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * Copy the given {@link Collection} into a {@code String} array.
     * <p>The {@code Collection} must contain {@code String} elements only.
     *
     * @param collection the {@code Collection} to copy
     *                   (potentially {@code null} or empty)
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(Collection<String> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    public static String upperFirstChar(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        }

        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    /**
     * 스프링 빈 이름 생성
     * <p>
     * <a href=https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-beanname>스프링 빈 네임 명명규칙</a>
     * @param className
     * @return
     */
    public static String makeBeanName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
