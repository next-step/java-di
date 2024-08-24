package com.interface21.beans;

public class BeanCircularException extends RuntimeException {

  /**
   * Create a new BeanCircularException.
   * @param beanName the name of the bean requested
   */
  public BeanCircularException(String beanName) {
    super("빈 생성 과정에서 순환 참조가 발생했습니다. : " + beanName);
  }
}

