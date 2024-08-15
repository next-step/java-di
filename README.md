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

### Mission 1

- [X] DI 컨테이너 구현한다.
    - [X] 생성자를 활용하는 DI 컨테이너를 구현한다.
    - [X] DefaultListableBeanFactoryTest의 di() 테스트가 성공하도록 구현한다.
- [X] ControllerScanner를 BeanScanner로 변경한다.
    - [X] @Controller, @Service, @Repository에 대한 지원이 가능하도록 개선한다.
    - [X] AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 리팩터링한다.

### Mission 2

- [X] @Autowired가 설정되어 있는 생성자를 통해 빈을 생성
    - [X] 다른 빈과 의존관계를 가지지 않는 빈을 찾아 인스턴스를 생성할 때까지 재귀를 실행하는 방식으로 구현
- [X] 인스턴스 생성 기본 뼈대 구현
    - [X] Bean 저장소에 clazz에 해당하는 인스턴스가 이미 존재하면 해당 인스턴스 반환
    - [X] 생성자를 활용한 인스턴스 생성 구현
        - [X] clazz에 @Autowired가 설정되어 있는 생성자를 찾는다. BeanFactoryUtils 활용
        - [X] @Autowired로 설정한 생성자가 없으면 Default 생성자로 인스턴스 생성 후 Bean 저장소에 추가 후 반환
        - [X] @Autowired로 설정한 생성자가 있으면 찾은 생성자를 활용해 인스턴스 생성 후 Bean 저장소에 추가 후 반환
