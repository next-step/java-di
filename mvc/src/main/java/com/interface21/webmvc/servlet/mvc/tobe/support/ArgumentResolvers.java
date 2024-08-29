package com.interface21.webmvc.servlet.mvc.tobe.support;

import com.interface21.core.MethodParameter;
import com.interface21.web.method.support.ArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ArgumentResolvers implements ArgumentResolver {

  private final List<ArgumentResolver> resolvers;

  public ArgumentResolvers(List<ArgumentResolver> argumentResolvers) {
    this.resolvers = argumentResolvers;
  }

  public static ArgumentResolvers of(List<ArgumentResolver> argumentResolvers) {

    return Optional.ofNullable(argumentResolvers)
        .map(ArgumentResolvers::new)
        .orElseThrow(() -> new IllegalArgumentException("Argument resolvers cannot be empty. Args: " + argumentResolvers));
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return resolvers.stream().anyMatch(resolver -> resolver.supportsParameter(parameter));
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, HttpServletRequest request,
      HttpServletResponse response) {
    return resolvers.stream()
        .filter(resolver -> resolver.supportsParameter(parameter))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "No suitable resolver for argument: " + parameter.getType()))
        .resolveArgument(parameter, request, response);
  }
}
