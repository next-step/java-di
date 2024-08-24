package com.interface21.beans;

public class BeanCurrentlyInCreationException extends IllegalArgumentException{

  public BeanCurrentlyInCreationException(String beanName) {
    super(String.format("Error creating bean with name '<%s>': Requested bean is currently in creation", beanName));
  }
}
