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

--- 

## 요구사항

- [x] 인스턴스 생성 및 의존관계 설정을 애너테이션으로 자동화한다
  - 지원하는 애너테이션은 
   - [x] @Controller
   - [x] 서비스는 @Service
   - [x] DAO는 @Repository
   - [x] 이 3개의 설정으로 생성된 각 인스턴스 간의 의존관계는 @Autowired를 사용한다
     - @Autowired가 선언된 생성자로 인스턴스를 생성한다
     - @Autowired가 선언된 생성자가 없으면 기본 생성자로 인스턴스를 생성한다

- [x] MVC 프레임워크의 AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 리팩터링한다

----

- Bean
  - 스프링 프레임워크에 의해 생성되고 의존성 및 라이프 사이클이 관리되는 객체를 의미한다
- BeanDefinition
  - 빈의 `이름`, `타입` 정보를 가지고 있다
  - 유형으로는 `AnnotationBeanDefinition`이 있다. 어노테이션으로 선언한 빈의 정보를 관리한다
- BeanDefinitionRegistry
  - `BeanDefinition`을 등록하고 조회하는 기능을 제공한다
- BeanRegistry
  - `Bean`을 등록하고 조회하는 기능을 제공한다