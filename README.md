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

## 1단계 - DI 컨테이너 구현하기

### 요구사항

- 클래스에 대한 인스턴스 생성 및 의존관계 설정을 애너테이션으로 자동화한다.
- 컨트롤러는 `@Controller`, 서비스는 `@Service`, DAO는 `@Repository`을 설정한다.
- `DefaultListableBeanFactoryTest` 통과시키기

### 요구사항 정리

- `@Controller`, `@Service`, `@Repository` 가 붙어있는 클래스 정보를 스캔한다.
- `DefaultListableBeanFactory.initialize` 메서드 호출 시 Bean 들을 초기화 하기 위해 스캔된 클래스 정보를 활용한다.
- 차례로 클래스를 생성하기 위해 생성자를 찾는다.
    - @Autowired 가 붙은 생성자를 찾는다.
        - @Autowired 가 붙은 생성자가 2개 이상이라면 예외를 던진다.
    - 없다면 모든 생성자를 찾는다.
        - 생성자가 2개 이상라면 예외를 던진다.
        - 하나뿐인 생성자를 이용한다.
- 생성자 파라미터 타입에 또 다른 Bean 클래스 정보가 있다면 해당 클래스를 먼저 생성한다.
    - 재귀적으로 탐색해서 먼저 생성할 수 있는 Bean 을 생성한다.
    - 파라미터 타입의 클래스 정보가 Scan 된 클래스 정보에 없다면 예외를 던진다.

## 2단계 - DI 컨테이너 구현하기(힌트)

### 요구사항

- 재귀를 통해 의존관계를 해결할 수 있는 빈을 생성한다. (전 단계 해결)
- MVC 프레임워크와의 통합
    - `ControllerScanner` 를 DI 컨테이너가 있는 패키지로 이동
    - `BeanSacnner` 로 이름을 변경
    - `AnnotationHandlerMapping`이 `BeanFactory`와 `BeanScanner`를 활용해 동작하도록 리팩터링
