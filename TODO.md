# 기본 사항들

## 프로그래밍 요구사항

- 모든 로직에 단위테스트 구현
- 자바 코드 컨벤션 지키면서 프로그래밍
- 한 메서드에 한 단계의 들여쓰기만 한다.
- 익셉션을 신중하게 정의할 것
- XXX, TODO 제거할 것

## 기능목록 및 commit 로그 요구사항

- feat (feature)
- fix (bug fix)
- docs (documentation)
- style (formatting, missing semi colons, …)
- refactor
- test (when adding missing tests)
- chore (maintain)

# 🚀 1단계 - DI 컨테이너 구현하기

## 기본 요구사항

새로 만든 MVC 프레임워크는 자바 리플렉션을 활용해 @Controller이 설정되어 있는 클래스를 찾아 인스턴스를 생성하고, URL 매핑 작업을 자동화했다. 같은 방법으로 각 클래스에 대한 인스턴스 생성 및
의존관계 설정을 애너테이션으로 자동화한다.

먼저 애너테이션은 각 클래스 역할에 맞도록 컨트롤러는 이미 추가되어 있는 @Controller, 서비스는 @Service, DAO는 @Repository을 설정한다. 이 3개의 설정으로 생성된 각 인스턴스 간의
의존관계는 @Autowired를 사용한다.

### DI 컨테이너를 활용한 DI 예제

```java

@Controller
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView view(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}
```

```java

@Service
public class UserService {

    private final UserDao userDao;
    private final UserHistoryDao userHistoryDao;

    @Autowired
    public UserService(final UserDao userDao, final UserHistoryDao userHistoryDao) {
        this.userDao = userDao;
        this.userHistoryDao = userHistoryDao;
    }
    ...
}
```

```java

@Repository
public class UserDao {
    ...
}
```

### DI 컨테이너 테스트 및 팁

효과적인 실습을 위해 요구사항을 만족해야 하는 테스트 코드(DefaultListableBeanFactoryTest)를 제공하고 있다.
DefaultListableBeanFactoryTest의 di() 테스트가 성공하면 생성자를 활용하는 DI 컨테이너 구현을 완료한 것이다. 또한 구현 중 필요한 기능을 도와주기 위해
core.di.factory.BeanFactoryUtils 클래스를 제공하고 있다.

## 추가 요구사항

지금까지의 과정을 통해 DI 컨테이너를 완료했다면 다음 단계는 앞에서 구현한 MVC 프레임워크와의 통합이 필요하다. 여기서 구현한 DI 컨테이너를 활용할 경우 앞에서 @Controller이 설정되어 있는 클래스를
찾는 ControllerScanner를 DI 컨테이너가 있는 패키지로 옮겨서 @Controller, @Service, @Repository에 대한 지원이 가능하도록 개선한다.

클래스 이름도 @Controller만 찾던 역할에서 @Service, @Repository까지 확대 되었으니 BeanScanner로 이름을 리팩토링한다.

MVC 프레임워크의 AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 리팩터링한다.
