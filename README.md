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
- BeanConstructor
  - 빈의 생성에 사용되는 생성자를 가진다
  - Autowired가 있는 생성자를 우선한다
  - Autowired가 있는 생성자가 없다면 기본 생성자를 우선한다
  - 둘다 없다면 랜덤한 생성자를 사용한다

- BeanDefinition
  - SingletonBeanDefinition, PrototypeBeanDefinition을 저장한다
  - Scope가 다른 bean을 구현체로 생성하려하는 경우 예외가 발생한다
  - 어떤 타입인지 반환할 수 있다

- DefaultListableBeanFactory (implements BeanFactory)
  - initialize
    - beanDefinitionMap에 저장된 클래스 중 싱글톤 빈들만 필터링하여 stream을 돌려 빈을 생성한다
      1. 만약 파라미터가 필요한 생성자라면 이미 생성된 bean에 있는지 확인한다
      2. 파라미터 요청 bean 중 이미 생성된 bean이 없다면 다음 빈 생성으로 넘어간다
      3. 다음 빈 생성이 되었다면 2로 돌아가 이전 bean 생성을 재개한다
      4. 이미 생성된 빈이라면 생성하지 않고 생성된 빈을 사용하도록 한다
  - getBean
    - 요청된 클래스에 맞는 빈을 찾아 반환한다
    - 생성되지 않은 빈을 가져가려하는 경우 예외가 발생한다
  - getBeanClasses
    - 생성된 모든 빈들의 클래스를 반환한다

- DefaultBeanDefinitionRegistry (implements BeanDefinitionRegistry)
  - registerBeanDefinition
    - 클래스와 BeanDefinition을 받아 저장한다
    - 클래스가 Component가 아닌 경우 예외가 발생한다
  - getBeanDefinition
    - 입력받은 클래스에 매핑되는 BeanDefinition을 찾아 반환한다
    - 매핑되는 BeanDefinition이 없으면 생성 불가 예외를 던진다
  - getBeanDefinitions
    - 보유중인 모든 BeanDefinition을 반환한다

## 2단계 - DI 컨테이너 구현하기(힌트)
- BeanFactory
  - getController를 통해 `@Controller`로 설정된 빈목록을 반환한다
  - 빈 생성 중 순환참조가 발생하면 예외가 발생한다.
- BeanScanner
  - BeanFactory에서 받은 Controller로 HandlerExecution를 초기화한다
