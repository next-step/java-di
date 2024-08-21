package com.interface21.beans.factory.support;

import java.lang.reflect.Constructor;

public record ConstructorHolder(Constructor<?> constructor, boolean autowiredMode) {
}
