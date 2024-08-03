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


## 요구사항 정리 
### 🚀 1단계 - DI 컨테이너 구현하기
- [x] @Controller, @Service, @Repository 를 스캔해서 인스턴스를 생성하고 등록한다 
- [x] 의존관계 주입에는 @Autowired 를 사용한다 
  - 위 세 개의 설정으로 생성된 인스턴스 간의 의존관계를 설정해야 한다. 
- [x] DefaultListableBeanFactoryTest 를 통과해야 한다 
- [x] MVC 프레임워크와 통합 
  - [x] @Controller, @Service, @Repository에 대해 지원이 가능하도록 개선한다
  - 아래 요구사항은 HandlerExecutionFactory가 어울린다고 생각해서 제 기준대로 변경하였습니다. 
  - [x] ControllerScanner를 DI 컨테이너가 있는 패키지로 옮긴다
    - 기존 ControllerScanner를 HandlerExecutionFactory로 변경하고 이를 생성하는 책임 부여 
  - [x] 클래스 이름을 BeanScanner로 변경한다 
    - Bean을 스캔하는 BeanScanner는 따로 구현하였음 
  - [x] AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 변경한다
    - ApplicationContext를 활용해 동작하도록 변경하였음 

### 🚀 2단계 - DI 컨테이너 구현하기(힌트) 
#### 인스턴스 생성 기본 뼈대 구현하기
- 이미 구현을 해둔것 같긴 한데, 메서드를 다시 분리해보자
- [x] Bean 인스턴스 생성 로직 구현
  - [x] 책임을 (나름) 잘 분리 해보기
    - getBean 에서 빈이 없을 경우 다시 생성해주는 책임을 부여하는 것이 좋은 선택일까 고민 해보기
    - [x] `List<Class> beanClasses`를 오만군데 다 들고 다니는데, 해결 방안?
      - BeanDefinition과 getBean()을 이용해 Lazy하게 초기화를 시도하니 어느정도 해결이 되었다
    - [x] BeanDefinition은 왜 필요할까?
      - Class 타입 만으로 빈 검색이 어려울 때 사용하려나? 빈 이름을 identifier로 두고 사용하면 좋을 것 같다.
        - 실제로도 타입으로 먼저 찾고, 빈 이름을 이용해 먼저 찾지 않던가? 
    - [x] 왜 getBean에서 초기화를 수행할까?
      - 호출하는 시점에 생성하기 위해 ? 
        - 그런데 빈은 다 등록하고 시작하지 않나? 
      - DI를 위해? 
        - getBean을 통해 싱글톤 객체를 DI 시켜주는데, 파라미터에 해당하는 Bean이 없을 경우 생성해야 하기 때문?
        - 그렇다면 getOrCreate의 성질을 갖는 메서드인데 그냥 getBean이라고 이름을 붙인걸까? 
  - [x] 생성자를 활용한 인스턴스 생성 구현 
    - BeanUtils가 있던데, 이를 이용하는 방법을 생각 해볼까?
    - 파라미터가 null일 경우에 대한 기본값 바인딩 전략을 생각하지 못했는데 BeanUtil에 구현이 되어있는걸 보고 깨달았다. 
#### 놓친 예외 처리 사항은 뭐가있을까 
- [x] 인터페이스의 구현체가 2개 이상인 경우 처리 방안 
  - 예외를 발생시키도록 구현 
- [x] BeanDefinition에 없는 경우는 Bean이 아닌 경우인데, Bean이 아닌 경우를 getBean()으로 불러 들일 때, 예외를 발생시켜 주는 것이 좋을 것 같다
- [x] 순환 참조는 어떻게 처리할 수 있을까?

### 🚀 3단계 - @Configuration 구현하기
- [] 빈을 등록할 패키지 패스를 하드코딩 하지 않고 `@ComponentScan`을 통해 동적으로 스캔할 수 있어야 한다
- [] `@Configuration` + `@Bean` 조합으로 빈을 등록할 수 있어야 한다
- [] `@Configuration`을 통해 등록한 빈 또한 `@Component`로 등록한 빈을 DI 할 수 있어야 한다
#### `@Configuration`예제
```java
import org.h2.jdbcx.JdbcDataSource;
import javax.sql.DataSource;

@Configuration
@ComponentScan({ "camp.nextstep", "com.interface21" })
public class MyConfiguration {

    @Bean
    public DataSource dataSource() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");
        return jdbcDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```
