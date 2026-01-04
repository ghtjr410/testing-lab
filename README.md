# Testing Lab

> 테스트 도구와 기법을 마스터하기 위한 학습 테스트 저장소

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![JUnit Version](https://img.shields.io/badge/JUnit-5.10-green.svg)](https://junit.org/junit5/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

## 📌 소개

이 저장소는 **테스트 작성의 모든 것**을 학습 테스트를 통해 익힙니다.

단순 도구 사용법이 아닌, **언제 어떤 테스트를 선택해야 하는지**, **왜 이 방식이 효과적인지**를 코드로 검증합니다.

```
"테스트 코드는 품질의 증거이자, 설계의 피드백이다"
```

## 🎯 학습 목표

- JUnit5와 AssertJ로 **가독성 높은 테스트** 작성
- Mock과 Stub의 **올바른 사용법과 차이** 이해
- **슬라이스 테스트 vs 통합 테스트** 선택 기준 체득
- Testcontainers로 **실제 환경과 동일한 테스트** 구성
- 테스트 코드의 **유지보수성과 성능** 최적화

## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 21 |
| Test Framework | JUnit 5 |
| Assertion | AssertJ |
| Mock | Mockito |
| Container | Testcontainers |
| API Test | RestAssured, MockMvc |
| External Mock | WireMock |
| BDD | Cucumber |
| Architecture | ArchUnit |
| Framework | Spring Boot 3.x |

## 📁 프로젝트 구조

```
src/test/java/
├── s01_junit5/           # JUnit5 핵심 기능
├── s02_assertj/          # 유창한 단언문
├── s03_mockito/          # 테스트 대역
├── s04_testcontainers/   # 컨테이너 기반 통합 테스트
├── s05_spring_test/      # Spring 테스트 통합
├── s06_restassured/      # API E2E 테스트
├── s07_cucumber/         # BDD 시나리오 테스트
├── s08_wiremock/         # 외부 API Mock
├── s09_security_test/    # 보안 테스트
├── s10_archunit/         # 아키텍처 검증
├── s11_fixture/          # 테스트 데이터 관리
└── s12_parallel/         # 테스트 병렬 실행
```

## 📚 학습 내용

### Part 1: 테스트 기초 (01 ~ 02)

<details>
<summary><b>01. JUnit5</b> - 테스트 프레임워크 핵심</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `LifecycleTest` | @BeforeEach, @AfterEach, @BeforeAll, @AfterAll |
| `AssertionsTest` | JUnit5 기본 단언문 |
| `NestedTest` | @Nested로 테스트 계층 구조화 |
| `ParameterizedTest` | @ParameterizedTest, @ValueSource, @CsvSource |
| `DynamicTest` | @TestFactory, 동적 테스트 생성 |
| `RepeatedTest` | @RepeatedTest, 반복 테스트 |
| `DisplayNameTest` | @DisplayName, @DisplayNameGeneration |
| `DisabledTest` | @Disabled, 조건부 실행 |
| `TimeoutTest` | @Timeout, 시간 제한 테스트 |
| `ExtensionTest` | Extension 모델, 커스텀 확장 |

**핵심 질문**
- @BeforeEach와 @BeforeAll의 차이와 사용 시점은?
- @ParameterizedTest로 중복 테스트를 어떻게 줄이는가?
- JUnit5 Extension 모델이 JUnit4 Runner보다 나은 점은?

</details>

<details>
<summary><b>02. AssertJ</b> - 유창한 단언문</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `BasicAssertionTest` | assertThat 기본 사용법 |
| `StringAssertionTest` | 문자열 검증 메서드들 |
| `CollectionAssertionTest` | 컬렉션 검증, contains, extracting |
| `ExceptionAssertionTest` | assertThatThrownBy, assertThatCode |
| `SoftAssertionTest` | SoftAssertions, 다중 검증 |
| `CustomAssertionTest` | 커스텀 Assertion 클래스 작성 |

**핵심 질문**
- AssertJ가 JUnit5 기본 Assertion보다 나은 점은?
- extracting()은 어떤 상황에서 유용한가?
- SoftAssertions를 사용해야 하는 경우는?

</details>

---

### Part 2: 테스트 대역 (03)

<details>
<summary><b>03. Mockito</b> - Mock 객체 활용</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `MockBasicTest` | @Mock, mock() 기본 사용법 |
| `StubTest` | when().thenReturn(), given().willReturn() |
| `VerifyTest` | verify(), 호출 검증 |
| `SpyTest` | @Spy, 실제 객체 부분 모킹 |
| `ArgumentCaptorTest` | ArgumentCaptor, 인자 캡처 |
| `MockVsSpyTest` | Mock과 Spy의 차이 |
| `BDDMockitoTest` | BDDMockito, given/when/then 스타일 |

**핵심 질문**
- Mock과 Spy는 언제 각각 사용하는가?
- verify()로 검증해야 하는 것과 하지 말아야 하는 것은?
- ArgumentCaptor는 어떤 상황에서 유용한가?

</details>

---

### Part 3: 통합 테스트 (04 ~ 05)

<details>
<summary><b>04. Testcontainers</b> - 컨테이너 기반 테스트</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `TestcontainersBasicTest` | Testcontainers 기본 설정 |
| `MySQLContainerTest` | MySQL 컨테이너 테스트 |
| `RedisContainerTest` | Redis 컨테이너 테스트 |
| `KafkaContainerTest` | Kafka 컨테이너 테스트 |
| `ContainerReuseTest` | 컨테이너 재사용으로 속도 개선 |
| `DynamicPropertyTest` | @DynamicPropertySource 활용 |

**핵심 질문**
- Testcontainers가 H2보다 나은 점은?
- 컨테이너 재사용으로 테스트 속도를 얼마나 개선할 수 있는가?
- @DynamicPropertySource의 역할은?

</details>

<details>
<summary><b>05. Spring Test</b> - Spring 테스트 통합</summary>

#### annotation/

| 테스트 | 학습 내용 |
|--------|-----------|
| `SpringBootTestTest` | @SpringBootTest 전체 컨텍스트 로드 |
| `WebMvcTestTest` | @WebMvcTest 웹 레이어 슬라이스 |
| `DataJpaTestTest` | @DataJpaTest JPA 레이어 슬라이스 |
| `TestConfigurationTest` | @TestConfiguration 테스트 전용 설정 |
| `ActiveProfilesTest` | @ActiveProfiles 프로파일 활성화 |

#### mockmvc/

| 테스트 | 학습 내용 |
|--------|-----------|
| `MockMvcBasicTest` | MockMvc 기본 사용법 |
| `MockMvcRequestTest` | GET, POST, PUT, DELETE 요청 |
| `MockMvcResponseTest` | 응답 검증, jsonPath, content |
| `MockMvcMultipartTest` | 파일 업로드 테스트 |
| `MockMvcPrintTest` | print(), 디버깅 |

#### mockbean/

| 테스트 | 학습 내용 |
|--------|-----------|
| `MockBeanBasicTest` | @MockBean 기본 사용법 |
| `MockBeanVsMockTest` | @MockBean vs @Mock 차이 |
| `SpyBeanTest` | @SpyBean 부분 모킹 |
| `MockBeanResetTest` | Mock 리셋 타이밍 |

#### strategy/

| 테스트 | 학습 내용 |
|--------|-----------|
| `SliceVsIntegrationTest` | 슬라이스 vs 통합 테스트 선택 기준 |
| `TestIsolationTest` | 테스트 격리 전략 |
| `TestTransactionTest` | @Transactional 롤백 동작 |

**핵심 질문**
- @SpringBootTest와 @WebMvcTest의 차이와 선택 기준은?
- @MockBean과 @Mock의 차이는?
- 슬라이스 테스트가 통합 테스트보다 빠른 이유는?

</details>

---

### Part 4: API 테스트 (06 ~ 07)

<details>
<summary><b>06. RestAssured</b> - API E2E 테스트</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `RestAssuredBasicTest` | RestAssured 기본 설정, given/when/then |
| `RestAssuredRequestTest` | GET, POST, PUT, DELETE 요청 |
| `RestAssuredResponseTest` | 응답 검증, body, jsonPath |
| `RestAssuredAuthTest` | 인증 테스트 (Bearer, Basic) |
| `RestAssuredSpecTest` | RequestSpecification, ResponseSpecification 재사용 |

**핵심 질문**
- RestAssured와 MockMvc의 차이와 선택 기준은?
- RequestSpecification을 재사용하면 좋은 점은?
- RestAssured로 인증이 필요한 API를 테스트하는 방법은?

</details>

<details>
<summary><b>07. Cucumber</b> - BDD 시나리오 테스트</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `CucumberBasicTest` | Cucumber 프로젝트 구조, 실행 방법 |
| `GherkinSyntaxTest` | Feature, Scenario, Given/When/Then 문법 |
| `StepDefinitionTest` | Step Definition 작성 방법 |
| `DataTableTest` | 데이터 테이블 활용 |
| `ScenarioOutlineTest` | Scenario Outline, 파라미터화 |
| `HooksTest` | @Before, @After 훅 |
| `CucumberRestAssuredTest` | Cucumber + RestAssured 조합 |

**핵심 질문**
- Cucumber를 도입해야 하는 상황은?
- Gherkin 문법의 장점은?
- Cucumber의 유지보수 비용은?

</details>

---

### Part 5: 외부 의존성 (08 ~ 09)

<details>
<summary><b>08. WireMock</b> - 외부 API Mock</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `WireMockBasicTest` | WireMock 서버 설정 |
| `WireMockStubTest` | stubFor(), 응답 스텁 |
| `WireMockVerifyTest` | verify(), 요청 검증 |
| `WireMockFaultTest` | 장애 시뮬레이션 (지연, 에러) |
| `WireMockRecordTest` | 실제 응답 녹화 |

**핵심 질문**
- WireMock이 @MockBean보다 나은 상황은?
- 외부 API 장애를 시뮬레이션하는 방법은?
- WireMock 녹화 기능의 활용 방법은?

</details>

<details>
<summary><b>09. Security Test</b> - 보안 테스트</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `WithMockUserTest` | @WithMockUser 기본 사용법 |
| `WithUserDetailsTest` | @WithUserDetails 커스텀 UserDetails |
| `SecurityMockMvcTest` | Security + MockMvc 조합 |
| `OAuthMockTest` | OAuth2 인증 Mock |

**핵심 질문**
- @WithMockUser와 @WithUserDetails의 차이는?
- 인증이 필요한 API를 테스트하는 방법은?
- OAuth2 인증 흐름을 테스트하는 방법은?

</details>

---

### Part 6: 테스트 구조화 (10 ~ 12)

<details>
<summary><b>10. ArchUnit</b> - 아키텍처 검증</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `ArchUnitBasicTest` | ArchUnit 기본 사용법 |
| `LayerDependencyTest` | 레이어 의존성 검증 |
| `NamingConventionTest` | 네이밍 규칙 검증 |
| `PackageDependencyTest` | 패키지 의존성 검증 |
| `CyclicDependencyTest` | 순환 참조 검증 |

**핵심 질문**
- ArchUnit으로 어떤 아키텍처 규칙을 강제할 수 있는가?
- 순환 의존성을 검출하는 방법은?
- ArchUnit 테스트를 CI에 통합하면 좋은 점은?

</details>

<details>
<summary><b>11. Fixture</b> - 테스트 데이터 관리</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `FixtureBuilderTest` | 테스트 데이터 빌더 패턴 |
| `FixtureFactoryTest` | 팩토리 메서드 패턴 |
| `FixtureMotherTest` | Object Mother 패턴 |
| `FixtureRandomTest` | 랜덤 데이터 생성 (Instancio, EasyRandom) |

**핵심 질문**
- 테스트 데이터 생성에 빌더 패턴이 좋은 이유는?
- Object Mother 패턴이란?
- 랜덤 데이터 생성의 장단점은?

</details>

<details>
<summary><b>12. Parallel</b> - 테스트 병렬 실행</summary>

| 테스트 | 학습 내용 |
|--------|-----------|
| `ParallelExecutionTest` | JUnit5 병렬 실행 설정 |
| `ResourceLockTest` | @ResourceLock, 공유 자원 제어 |
| `IsolationModeTest` | 격리 모드 (PER_METHOD, PER_CLASS) |
| `ParallelPerformanceTest` | 병렬 실행 성능 비교 |

**핵심 질문**
- 테스트 병렬 실행 시 주의할 점은?
- @ResourceLock은 언제 사용하는가?
- 병렬 실행으로 테스트 시간을 얼마나 줄일 수 있는가?

</details>

---

## 📝 학습 테스트 작성 원칙

### 1. 테스트 구조

```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MockitoStubTest {

    @Nested
    class when_thenReturn_스터빙 {

        @Mock
        OrderRepository orderRepository;

        @Test
        void 존재하는_주문_조회() {
            Order order = new Order(1L, "상품A");
            when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

            Optional<Order> result = orderRepository.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("상품A");
        }

        @Test
        void 존재하지_않는_주문_조회() {
            when(orderRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Order> result = orderRepository.findById(999L);

            assertThat(result).isEmpty();
        }
    }
}
```

### 2. 원칙

| 원칙 | 설명 |
|------|------|
| **@DisplayNameGeneration** | 언더스코어를 공백으로 자동 변환 |
| **한글 메서드명** | `존재하지_않는_주문_조회()` |
| **@Nested** | 관련 테스트 그룹핑 |
| **given/when/then** | 구조는 유지하되 주석 생략 |

### 3. 학습 테스트가 다루는 것

```
✅ 도구의 기본 사용법
✅ 실무에서 자주 쓰는 패턴
✅ 흔한 실수와 안티패턴
✅ 도구 간 비교 (MockMvc vs RestAssured 등)
✅ 성능 최적화 기법
✅ 테스트 전략 선택 기준
```

---

## 🚀 실행 방법

```bash
# 전체 테스트 실행
./gradlew test

# 특정 도구만 실행
./gradlew test --tests "*.01_junit5.*"
./gradlew test --tests "*.03_mockito.*"
./gradlew test --tests "*.05_spring_test.*"

# 테스트 리포트 확인
open build/reports/tests/test/index.html
```

---

## 📖 참고 자료

- [JUnit5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Testcontainers Documentation](https://www.testcontainers.org/)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [RestAssured Wiki](https://github.com/rest-assured/rest-assured/wiki/Usage)
- [WireMock Documentation](https://wiremock.org/docs/)
- [ArchUnit User Guide](https://www.archunit.org/userguide/html/000_Index.html)

---

<div align="center">

**"테스트는 버그를 찾는 것이 아니라, 자신감을 얻는 것이다"**

*— Kent Beck*

</div>