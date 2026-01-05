package com.seoki.testinglab.s05_spring_test.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

/**
 * @SpringBootTest 학습 테스트
 *
 * 핵심 개념:
 * - 전체 ApplicationContext를 로드하는 통합 테스트용 어노테이션
 * - 실제 애플리케이션과 동일한 환경에서 테스트 가능
 * - WebEnvironment 옵션으로 웹 서버 구동 방식 제어
 *
 * 실무 포인트:
 * - 전체 컨텍스트 로드로 테스트 속도가 느림 → 꼭 필요한 경우만 사용
 * - 슬라이스 테스트(@WebMvcTest, @DataJpaTest)로 해결 안 되는 경우에 사용
 * - E2E 테스트, 여러 레이어 통합 검증 시 적합
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SpringBootTestTest {

    /**
     * WebEnvironment.MOCK (기본값)
     * - 실제 서블릿 컨테이너를 띄우지 않음
     * - MockMvc와 함께 사용
     * - 가장 빠른 옵션
     */
    @Nested
    @SpringBootTest(webEnvironment = WebEnvironment.MOCK)
    class WebEnvironment_MOCK_기본값 {

        @Autowired
        ApplicationContext applicationContext;

        @Test
        void 전체_컨텍스트가_로드된다() {
            assertThat(applicationContext).isNotNull();
        }

        @Test
        void 모든_빈이_등록된다() {
            // 컨트롤러, 서비스, 레포지토리 등 모든 빈이 로드됨
            String[] beanNames = applicationContext.getBeanDefinitionNames();

            assertThat(beanNames).isNotEmpty();
            // 실제 프로젝트에서는 특정 빈 존재 여부 확인
            // assertThat(applicationContext.containsBean("orderService")).isTrue();
        }

        @Test
        void 서블릿_컨테이너는_시작되지_않는다() {
            // MOCK 환경에서는 실제 포트가 할당되지 않음
            // TestRestTemplate 사용 불가 (실제 HTTP 요청 불가)
            // 대신 MockMvc를 사용해야 함
            assertThat(applicationContext.getEnvironment().getProperty("local.server.port"))
                    .isNull();
        }
    }

    /**
     * WebEnvironment.RANDOM_PORT
     * - 실제 서블릿 컨테이너를 랜덤 포트로 시작
     * - TestRestTemplate으로 실제 HTTP 요청 테스트 가능
     * - 포트 충돌 방지
     */
    @Nested
    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    class WebEnvironment_RANDOM_PORT {

        @LocalServerPort
        int port;

        @Autowired
        TestRestTemplate restTemplate;

        @Test
        void 랜덤_포트가_할당된다() {
            assertThat(port).isGreaterThan(0);
            System.out.println("할당된 포트: " + port);
        }

        @Test
        void TestRestTemplate으로_실제_HTTP_요청이_가능하다() {
            // 실제 서버가 떠있으므로 HTTP 요청 가능
            assertThat(restTemplate).isNotNull();

            // 실제 프로젝트에서는:
            // String response = restTemplate.getForObject("/api/health", String.class);
            // assertThat(response).isEqualTo("OK");
        }

        @Test
        void LocalServerPort로_할당된_포트를_주입받는다() {
            // @LocalServerPort는 RANDOM_PORT, DEFINED_PORT에서만 동작
            assertThat(port).isBetween(1024, 65535);
        }
    }

    /**
     * WebEnvironment.DEFINED_PORT
     * - application.properties의 server.port 사용
     * - 포트 충돌 주의 필요
     * - 실무에서는 RANDOM_PORT 권장
     *
     * 포트 설정 우선순위 (높은 순):
     * 1. @TestPropertySource(properties = "server.port=...")
     * 2. @SpringBootTest(properties = "server.port=...")
     * 3. src/test/resources/application.yml (또는 .properties)
     * 4. src/main/resources/application.yml (또는 .properties)
     * 5. 기본값: 8080
     *
     * DEFINED_PORT가 필요한 경우:
     * - OAuth 콜백 URL이 특정 포트로 고정된 경우 (예: localhost:8080/callback)
     * - 외부 Mock 서버(Wiremock 등)가 특정 포트로 요청해야 하는 경우
     * - 레거시 시스템이 하드코딩된 포트로만 통신 가능한 경우
     * - Docker Compose 등 외부 인프라와 포트가 미리 약속된 경우
     *
     * 주의사항:
     * - 동일 포트를 사용하는 테스트 병렬 실행 불가
     * - 로컬에서 해당 포트가 이미 사용 중이면 테스트 실패
     * - CI/CD 환경에서 포트 충돌 가능성 존재
     */
    @Nested
    @SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
    @TestPropertySource(properties = "server.port=9999")
    class WebEnvironment_DEFINED_PORT {

        @LocalServerPort
        int port;

        @Test
        void 지정된_포트로_서버가_시작된다() {
            assertThat(port).isEqualTo(9999);
        }
    }

    /**
     * properties 속성으로 테스트 전용 설정
     */
    @Nested
    @SpringBootTest(
            webEnvironment = WebEnvironment.NONE,
            properties = {"spring.datasource.url=jdbc:h2:mem:testdb", "custom.feature.enabled=true"})
    class properties_속성_활용 {

        @Autowired
        ApplicationContext applicationContext;

        @Test
        void 테스트_전용_프로퍼티가_적용된다() {
            String featureEnabled = applicationContext.getEnvironment().getProperty("custom.feature.enabled");

            assertThat(featureEnabled).isEqualTo("true");
        }

        @Test
        void 인라인_프로퍼티가_파일_프로퍼티보다_우선한다() {
            // @SpringBootTest의 properties는 application.properties보다 우선
            String datasourceUrl = applicationContext.getEnvironment().getProperty("spring.datasource.url");

            assertThat(datasourceUrl).isEqualTo("jdbc:h2:mem:testdb");
        }
    }

    /**
     * classes 속성으로 특정 설정만 로드
     * - 전체 컨텍스트 대신 필요한 설정만 로드하여 속도 개선
     */
    @Nested
    @SpringBootTest(
            webEnvironment = WebEnvironment.NONE,
            classes = {TestConfig.class} // 특정 설정 클래스만 로드
            )
    class classes_속성_활용 {

        @Autowired
        ApplicationContext applicationContext;

        @Test
        void 지정한_설정_클래스만_로드된다() {
            // TestConfig에서 정의한 빈만 존재
            assertThat(applicationContext.getBeanDefinitionCount()).isLessThan(100);
        }
    }

    // 실무 안티패턴: @SpringBootTest 남용
    @Nested
    class 실무_안티패턴 {

        @Test
        @Disabled("개념 설명용 - 실행 불필요")
        void 단위_테스트에_SpringBootTest_사용하지_말것() {
            /*
             * 안티패턴: 단순 서비스 로직에 @SpringBootTest 사용
             *
             * @SpringBootTest
             * class OrderServiceTest {
             *     @Autowired OrderService orderService;
             *
             *     @Test
             *     void 주문_금액_계산() {
             *         // 단순 계산 로직인데 전체 컨텍스트 로드?
             *     }
             * }
             *
             * 올바른 방법: 순수 단위 테스트
             *
             * class OrderServiceTest {
             *     OrderService orderService = new OrderService(mock(OrderRepository.class));
             *
             *     @Test
             *     void 주문_금액_계산() {
             *         // 빠르고 격리된 테스트
             *     }
             * }
             */
        }

        @Test
        @Disabled("개념 설명용 - 실행 불필요")
        void SpringBootTest_선택_기준() {
            /*
             * @SpringBootTest를 사용해야 하는 경우:
             * 1. 여러 레이어를 통합하여 E2E 검증이 필요할 때
             * 2. 실제 HTTP 요청/응답 테스트가 필요할 때 (RANDOM_PORT)
             * 3. 전체 빈 구성이 올바른지 검증할 때
             * 4. @Transactional 전파, 이벤트 발행 등 통합 동작 검증 시
             *
             * @SpringBootTest를 피해야 하는 경우:
             * 1. 단일 클래스의 비즈니스 로직 테스트 → 단위 테스트
             * 2. 컨트롤러만 테스트 → @WebMvcTest
             * 3. JPA 레포지토리만 테스트 → @DataJpaTest
             * 4. 외부 의존성 없는 순수 로직 → Mockito만 사용
             */

            /**
             * ## 테스트 피라미드
             *                     △
             *                    /  \
             *                   / E2E \          ← @SpringBootTest (RANDOM_PORT)
             *                  /________\           몇 개만 (느림, 비쌈)
             *                 /          \
             *                / Integration \      ← @WebMvcTest, @DataJpaTest
             *               /______________\        적당히 (중간)
             *              /                \
             *             /    Unit Tests    \    ← new + Mockito
             *            /____________________\     많이 (빠름, 쌈)
             *
             * 실무 권장 비율:
             * - Unit: 70%
             * - Integration (슬라이스): 20%
             * - E2E (@SpringBootTest): 10%
             *
             * ┌─────────────────────────────────────────────────────────┐
             * │ 단위 테스트 관점: "이 메서드가 잘 동작하나?"                      │
             * ├─────────────────────────────────────────────────────────┤
             * │                                                         │
             * │   @Test                                                 │
             * │   void 주문_금액_계산() {                                   │
             * │       assertThat(calculator.calculate(1000, 2))         │
             * │               .isEqualTo(2000);                         │
             * │   }                                                     │
             * │                                                         │
             * │   → 기술적 검증, 개발자 관점                                  │
             * │                                                         │
             * └─────────────────────────────────────────────────────────┘
             *
             * ┌─────────────────────────────────────────────────────────┐
             * │ E2E 테스트 관점: "사용자가 주문을 완료할 수 있나?"                │
             * ├─────────────────────────────────────────────────────────┤
             * │                                                         │
             * │   @Test                                                 │
             * │   void 사용자가_상품을_주문하고_결제를_완료한다() {               │
             * │       // 1. 로그인                                        │
             * │       // 2. 상품 조회                                     │
             * │       // 3. 장바구니 담기                                  │
             * │       // 4. 주문 생성                                     │
             * │       // 5. 결제 완료                                     │
             * │       // 6. 주문 내역 확인                                  │
             * │   }                                                     │
             * │                                                         │
             * │   → 비즈니스 시나리오, 사용자 관점                             │
             * │                                                         │
             * └─────────────────────────────────────────────────────────┘
             */
        }
    }

    // 테스트용 헬퍼 클래스
    @org.springframework.context.annotation.Configuration
    static class TestConfig {
        // 필요한 빈만 정의
    }
}
