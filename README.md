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

1. [스프링 IoC 컨테이너와 Bean 소개](src/test/kotlin/ioc/Introduction.kt)
2. [컨테이너 개요](src/test/kotlin/ioc/Container.kt)
3. [Bean 개요](src/test/kotlin/ioc/Bean.kt)
4. [의존성(Dependencies)](src/test/kotlin/ioc/Dependencies.kt)
5. [Bean 스코프](src/test/kotlin/ioc/BeanScopes.kt)
6. [Bean의 라이프 사이클](src/test/kotlin/ioc/Lifecycle.kt)
7. [어노테이션 기반 컨테이너 구성](src/test/kotlin/ioc/AnnotationBasedConfiguration.kt)
8. [자바 기반 컨테이 구성](src/test/kotlin/ioc/JavaBasedConfiguration.kt)

### 싱글톤 스코프
<img src="docs/images/singleton.png" alt="singleton">

### 프로토타입 스코프
<img src="docs/images/prototype.png" alt="prototype">

## 1단계 - DI 컨테이너 구현하기
- BeanDefinition
  - SingletonBeanDefinition, PrototypeBeanDefinition을 저장한다
  - 어떤 타입인지 반환할 수 있다
- initialize
  - beanDefinitionMap에 `@Component`가 달려있는 모든 클래스를 스캔하여 저장한다
  - beanDefinitionMap에 저장된 클래스 중 싱글톤 빈들만 필터링하여 stream을 돌린다
    - 이때 생성할 수 있는 방법은 총 2가지다
      - `@Autowired`가 없다면 기본 생성자를 우선한다
      - `@Autowired`가 달려있다면 `@Autowired` 생성자를 우선한다
        1. 만약 파라미터가 필요한 생성자라면 이미 생성된 bean에 있는지 확인한다
        2. 파라미터 요청 bean 중 이미 생성된 bean이 없다면 다음 빈 생성으로 넘어간다
        3. 다음 빈 생성에서 이전 bean과 동일하다면 순환참조 예외를 발생시킨다
