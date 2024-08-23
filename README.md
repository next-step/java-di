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

## 1단계 요구사항
- [x] 사전 요구사항 1 - 학습테스트를 진행한다.
- [x] 요구사항 1 - DefaultListableBeanFactoryTest 가 통과하게 만든다.
    - @Controller, @Service, @Repository를 스캔해서 클래스들을 가져온다. (BeanScanner) 
    - @Autowired을 사용한 생성자를 이용해서 의존관계를 생성한다. (BeanFactory)
- [x] 요구사항 2 - AnnotationMapping이 동작하도록 리팩터링한다.
    - BeanFactory와 BeanScanner를 잘 활용해서 동작하면 된다.
      
- [x] 공통 요구사항
  - [x] DefaultListableBean이 Bean 조립 즉, Injection을 책임으로 가진 구현체이다. 책임에 맞게 클래스 설계하라 
    - doResolveDependency(Dependency-descriptor, beanName)  (의존 관계에 대한 descriptor, bean 이름을 가지고 주입해준다.)
      - findAutowireCandidates(qualifier를 가지고 주입하게된다.)
      - ConstructorResolver를 통해 injectionPoint를 가지고 bean 주입을 하게된다. 
  - [x] BeanScanner 는 Bean을 scan 하고 registerBean하는 책임을 가진다.
  - [x] BeanDefinitionRegistry 에서 등록된 bean을 반환 책임을 가진다. (BeanDefinitionMap에 넣어준다. beanName, beanDefinition)
  - [x] BeanDefinition은 instance 에 대한 정보를 담는다 + scope

- [] 요구사항 분석
- 호출 순서
    - ApplicationContext -> DefaultListableBeanFactory(Scanner)
    - scaner 호출로 registry에 bean definition 등록
    - beanfactory.initialize로 객체들 definition에 맞게 구현체 생성
    - ApplicationContext 반환
- DefaultListableBean (BeanDefinitionRegistry 인터페이스, )
  - beanDefinitionMap (beanName, beanDefinition) 해쉬맵
  - singletonObjects (clazz, 구현체들) 해쉬맵
- 라이프사이클
  - REGISTRY 등록 
  - initialize 하면 Registry에서 BeanDefinition을 읽어서 instance 생성하는 방향이다.
    - injector를 확인해보고 다른 의존관계를 getBean으로 가져오는데 없다면 생성하면된다. 


## 피드백 사항
- [x] @Autowired 검사
- [x] fields 주입 안하면 삭제
- [x] DefaultInjector 생성하지 않고 전달 방법 확인
- [x] @Component를 가진 애노테이션을 스캔하는 메서드를 분리해서 가독성 늘리기

## 3 단계 요구사항

- [x] 요구사항 1 - DataSource 설정을 관리하는 빈을 생성하기 위해 @Configuration을 추가하자.
    - [x] @Bean 으로 명시되어있는 설정은 인스턴스로 등록한다.
- [x] 요구사항 2 - BeanScanner 에서 @ComponentScan을 패키지 경로로 설정할 수 있도록 지원하자
- [x] 요구사항 3 - @Configuration 설정파일으로 등록한 빈과 BeanScanner를 통해 빈간 DI가 가능해야한다.

- 공통 요구사항
- [x] 단위 테스트 통과
- [x] 설정 파일을 통해 등록한 Bean과 ClassPathBeanScanner 추가로 bean 통합
- [x] ConfigurationBeanScanner와 ClasspathBeanScanner 통합하는 클래스 추가

## 4 단계 요구사항
- [x] 모든 요구사항 3단계에서 구현 확인
- [x] ConfigurationBeanScanner와 ClasspathBeanScanner 통합하는 클래스 추가 -> Application Context 구현되어 있습니다.



