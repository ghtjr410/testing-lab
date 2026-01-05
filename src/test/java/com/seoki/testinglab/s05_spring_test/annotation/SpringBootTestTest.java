package com.seoki.testinglab.s05_spring_test.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;

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
}
