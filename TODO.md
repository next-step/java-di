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


# 🚀 3단계 - @Configuration 구현하기

## 요구사항

- 데이터베이스 설정을 관리하는 책임을 맡은 빈을 생성하기 위해 @Configuration을 추가하자. 각 메소드에서 생성하는 인스턴스가 BeanFactory에 빈으로 등록하라는 설정은 @Bean으로 한다.
- BeanScanner에서 사용할 기본 패키지에 대한 설정을 하드코딩했는데 @ComponentScan으로 패키지 경로를 설정할 수 있도록 지원하자.
- @Configuration 설정 파일을 통해 등록한 빈과 BeanScanner를 통해 등록한 빈 간에도 DI가 가능해야 한다.
