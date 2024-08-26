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

### 🚀 @Configuration 구현하기

 - [ ] @Configuration 애노테이션을 사용하여 Bean을 등록할 수 있어야 한다.
    - [ ] Bean등록 설정은 @Bean 애노테이션을 사용한다.
    - [ ] ComponentScan을 사용하여 패키지 경로를 지정할 수 있다.
    - [ ] BeanScanner가 BeanFactory를 사용하도록 변경

- [ ] ConfigurationBeanScannerTest 테스트를 성공시킨다.
   - [ ] BeanSanner를 ClasspathBeanSacnner로 변경하고 설정 파일로 등록한 Bean과 통합한다.

- [ ] ConfigurationBeanScanner와 ClasspathBeanScanner을 통합하는 ApplicationContext 추가한다.
   - [ ] @ComponentScan 애노테이션의 경로 정보를 가져와 ClasspathBeanScanner을 초기화 한다 
   - [ ] 설정 파일의 @Bean 정보를 바탕으로 ConfigurationBeanScanner을 초기화한다
  
``java
ApplicationContext ac = new ApplicationContext(MyConfiguration.class);
AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(ac);
ahm.initialize();``