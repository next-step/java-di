# 만들면서 배우는 스프링
[Next Step - 과정 소개](https://edu.nextstep.camp/c/4YUvqn9V)

## DI 컨테이너 구현하기

### 학습목표
- DI 컨테이너 구현을 통해 내부 동작 원리를 이해한다.
- IoC와 DI의 이해도를 높인다.

### 시작 가이드
1. 이전 미션에서 진행한 코드를 사용하고 싶다면, 마이그레이션 작업을 진행합니다.
2. 학습 테스트는 강의 시간에 풀어봅시다.
3. LMS의 1단계 미션부터 진행합니다.

### 준비 사항
- IntelliJ에 Kotest 플러그인 설치

## 학습 테스트
- 스프링 IoC 컨테이너에 대해 좀 더 자세히 알아봅시다.
- 실패하는 학습 테스트를 통과시키시면 됩니다.
- 학습 테스트는 ioc 패키지 또는 클래스 단위로 실행하세요.

1. [스프링 IoC 컨테이너와 Bean 소개](study/src/test/kotlin/ioc/Introduction.kt)
2. [컨테이너 개요](study/src/test/kotlin/ioc/Container.kt)
3. [Bean 개요](study/src/test/kotlin/ioc/Bean.kt)
4. [의존성(Dependencies)](study/src/test/kotlin/ioc/Dependencies.kt)
5. [Bean 스코프](study/src/test/kotlin/ioc/BeanScopes.kt)
6. [Bean의 라이프 사이클](study/src/test/kotlin/ioc/Lifecycle.kt)
7. [어노테이션 기반 컨테이너 구성](study/src/test/kotlin/ioc/AnnotationBasedConfiguration.kt)
8. [자바 기반 컨테이 구성](study/src/test/kotlin/ioc/JavaBasedConfiguration.kt)

### 싱글톤 스코프
<img src="docs/images/singleton.png" alt="singleton">

### 프로토타입 스코프
<img src="docs/images/prototype.png" alt="prototype">

## 🚀 2단계 - DI 컨테이너 구현하기(힌트)

### 힌트

1. 재귀호출 활용

@Autowired가 설정되어 있는 생성자를 통해 빈을 생성해야 한다. 그런데 이 생성자의 인자로 전달할 빈도 다른 빈과 의존관계에 있다.
이와 같이 꼬리에 꼬리를 물고 빈 간의 의존관계가 발생할 수 있다.
다른 빈과 의존관계를 가지지 않는 빈을 찾아 인스턴스를 생성할 때까지 재귀를 실행하는 방식으로 구현할 수 있다.

재귀를 통해 새로 생성한 빈은 DefaultListableBeanFactory의 Map<Class<?>, Object>에 추가해 관리한다.
인스턴스를 생성하기 전에 먼저 Class에 해당하는 빈이 Map<Class<?>, Object>에 존재하는지 여부를 판단한 후에 존재하지 않을 경우 생성하는
방식으로 구현하면 된다. 이 같은 재사용 방법이 일반적인 캐시의 동작 원리이다.

2. 인스턴스 생성 기본 뼈대 구현

3. 빈(Bean) 인스턴스를 생성하기 위한 재귀 함수를 지원하려면 Class에 대한 빈 인스턴스를 생성하는 메소드와 Constructor에 대한 빈 인스턴스를 생성하는 메소드가 필요하다.

```
private Object instantiateClass(Class<?> clazz) {
    ...

    return null;
}

private Object instantiateConstructor(Constructor<?> constructor) {
    ...

    return null;
}
```

#### Bean 인스턴스 생성 로직 구현

빈(bean) 인스턴스를 생성하는 로직은 다음과 같다.
Map<Class<?>, Object> beans = new HashMap(); // Bean 저장소

```
public <T> T getBean(Class<T> clazz) {
    // Bean 저장소에 clazz에 해당하는 인스턴스가 이미 존재하면 해당 인스턴스 반환
    
    // clazz에 @Autowired가 설정되어 있는 생성자를 찾는다. BeanFactoryUtils 활용
    
    // @Autowired로 설정한 생성자가 없으면 Default 생성자로 인스턴스 생성 후 Bean 저장소에 추가 후 반환
    
    // @Autowired로 설정한 생성자가 있으면 찾은 생성자를 활용해 인스턴스 생성 후 Bean 저장소에 추가 후 반환
}
```

#### 생성자를 활용한 인스턴스 생성 구현

재귀함수의 시작은 instantiateClass()에서 시작한다. @Autowired가 설정되어 있는 생성자가 존재하면 instantiateConstructor() 메소드를 통해 인스턴스를 생성하고, 존재하지 않을
경우 기본 생성자로 인스턴스를 생성한다.
instantiateConstructor() 메소드는 생성자의 인자로 전달할 빈이 생성되어 Map<Class<?>, Object>에 이미 존재하면 해당 빈을 활용하고, 존재하지 않을 경우
instantiateClass() 메소드를 통해 빈을 생성한다.

```
private Object instantiateConstructor(Constructor<?> constructor) {
    Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = new ArrayList();
        for (Class<?> clazz : parameterTypes) {
            ...
            args.add(bean);
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }
}
```

### 추가 요구사항

지금까지의 과정을 통해 DI 컨테이너를 완료했다면 다음 단계는 앞에서 구현한 MVC 프레임워크와의 통합이 필요하다. 여기서 구현한 DI 컨테이너를 활용할 경우 앞에서 @Controller이 설정되어 있는 클래스를
찾는 ControllerScanner를 DI 컨테이너가 있는 패키지로 이동해 @Controller, @Service, @Repository에 대한 지원이 가능하도록 개선한다.

클래스 이름도 @Controller 애노테이션만 찾던 역할에서 @Service, @Repository 애노테이션까지 확대 되었으니 BeanScanner로 이름을 리팩터링한다.

MVC 프레임워크의 AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 리팩터링한다.

### 힌트

이 리팩터링 과정은 생각보다 간단하다. BeanScanner는 @Controller, @Service, @Repository이 설정되어 있는 모든 클래스를 찾아 Set에 저장하도록 만들어보자. 기존의
ControllerScanner와 같이 이 단계에서 빈 생성을 담당하지 않아도 된다. AnnotationHandlerMapping은 @Controller가 설정되어 있는 빈만 관심이 있다. 따라서
BeanFactory에 getControllers() 메소드를 추가해 @Controller가 설정되어 있는 빈 목록을 Map<Class<?>, Object>으로 제공하면 된다.

```
public Map<Class<?>, Object> getControllers() {
    Map<Class<?>, Object> controllers = new HashMap();
    for (Class<?> clazz : preInstanticateBeans) {
        if (clazz.isAnnotationPresent(Controller.class)) {
            controllers.put(clazz, beans.get(clazz));
        }
    }
    return controllers;
}
```
