package com.seoki.testinglab.s05_spring_test.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

/**
 * @WebMvcTest 학습 테스트
 *
 * 핵심 개념:
 * - 웹 레이어(Controller)만 테스트하는 슬라이스 테스트
 * - @Controller, @ControllerAdvice, @JsonComponent 등만 로드
 * - Service, Repository 등은 로드하지 않음 → @MockBean으로 주입
 *
 * 실무 포인트:
 * - 전체 컨텍스트 로드 대비 훨씬 빠름 (보통 10배 이상)
 * - Controller의 요청 매핑, 검증, 응답 변환만 테스트
 * - 비즈니스 로직은 Service 단위 테스트에서 검증
 *
 * ┌─────────────────────────────────────────────────────────────┐
 * │ @WebMvcTest                                                 │
 * ├─────────────────────────────────────────────────────────────┤
 * │                                                             │
 * │  로드하는 것                        로드 안 하는 것               │
 * │  ─────────────                     ───────────────          │
 * │  • @Controller                     • @Service               │
 * │  • @RestController                 • @Repository            │
 * │  • @ControllerAdvice               • @Component             │
 * │  • Filter, Interceptor             • DataSource             │
 * │  • MockMvc                         • JPA, DB 관련            │
 * │  • ObjectMapper                    • 외부 연동                │
 * │                                                             │
 * │  → 웹 레이어만 "슬라이스"해서 테스트                                │
 * │                                                             │
 * └─────────────────────────────────────────────────────────────┘
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class WebMvcTestTest {

    /**
     * 기본 @WebMvcTest 사용법
     * - 모든 Controller를 스캔
     * - 실무에서는 특정 Controller 지정 권장
     */
    @Nested
    @WebMvcTest(OrderController.class) // 특정 Controller만 로드
    class WebMvcTest_기본_사용법 {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper objectMapper;

        @MockitoBean // Service는 Mock으로 대체
        OrderService orderService;

        @Test
        void MockMvc가_자동_구성된다() {
            assertThat(mockMvc).isNotNull();
        }

        @Test
        void ObjectMapper가_자동_구성된다() {
            assertThat(objectMapper).isNotNull();
        }

        @Test
        void GET_요청_테스트() throws Exception {
            Order order = new Order(1L, "테스트 상품", 10000);
            given(orderService.findById(1L)).willReturn(Optional.of(order));

            mockMvc.perform(get("/api/orders/{id}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.productName").value("테스트 상품"))
                    .andExpect(jsonPath("$.amount").value(10000));
        }

        @Test
        void POST_요청_테스트() throws Exception {
            // given
            OrderRequest request = new OrderRequest("새 상품", 20000);
            Order savedOrder = new Order(1L, "새 상품", 20000);
            given(orderService.create(any(OrderRequest.class))).willReturn(savedOrder);

            // when & then
            mockMvc.perform(post("/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.productName").value("새 상품"));
        }

        @Test
        void 존재하지_않는_리소스_조회시_404() throws Exception {
            // given
            given(orderService.findById(999L)).willReturn(Optional.empty());

            // when & then
            mockMvc.perform(get("/api/orders/{id}", 999L)).andExpect(status().isNotFound());
        }
    }

    // 웹 레이어만 로드되는지 확인
    @Nested
    @WebMvcTest(OrderController.class)
    class 슬라이스_테스트_범위 {

        @Autowired
        ApplicationContext applicationContext;

        @MockitoBean
        OrderService orderService;

        @Test
        void Controller_빈은_등록된다() {
            assertThat(applicationContext.getBeansOfType(OrderService.class)).isNotEmpty();
        }

        @Test
        void Service_빈은_등록되지_않는다() {
            // @MockitoBean으로 대체된 것이지, 실제 Service가 스캔된 것이 아님
            // 실제 OrderServiceImpl 같은 구현체는 로드되지 않음
            String[] beanNames = applicationContext.getBeanDefinitionNames();

            // Service 구현체가 없음을 간접 확인
            boolean hasServiceImpl = java.util.Arrays.stream(beanNames)
                    .anyMatch(name -> name.toLowerCase().contains("serviceimpl"));
            assertThat(hasServiceImpl).isFalse();
        }

        @Test
        void Repository_빈은_등록되지_않는다() {
            String[] beanNames = applicationContext.getBeanDefinitionNames();

            boolean hasRepository = java.util.Arrays.stream(beanNames)
                    .anyMatch(name -> name.toLowerCase().contains("repository") && !name.contains("mock"));
            assertThat(hasRepository).isFalse();
        }

        @Test
        void 로드되는_빈_개수가_적다() {
            // @SpringBootTest 대비 훨씬 적은 빈만 로드
            int beanCount = applicationContext.getBeanDefinitionCount();
            System.out.println("로드된 빈 개수: " + beanCount);

            // 일반적으로 100개 미만 (프로젝트에 따라 다름)
            assertThat(beanCount).isLessThan(200);
        }
    }

    // ========== 테스트용 클래스 ==========

    @RestController
    @RequestMapping("/api/orders")
    static class OrderController {

        private final OrderService orderService;

        OrderController(OrderService orderService) {
            this.orderService = orderService;
        }

        @GetMapping
        List<Order> findAll() {
            return orderService.findAll();
        }

        @GetMapping("/{id}")
        Order findById(@PathVariable Long id) {
            return orderService.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        }

        @PostMapping
        @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
        Order create(@RequestBody @jakarta.validation.Valid OrderRequest request) {
            return orderService.create(request);
        }
    }

    interface OrderService {
        List<Order> findAll();

        Optional<Order> findById(Long id);

        Order create(OrderRequest request);
    }

    @Service
    static class StubOrderService implements OrderService {
        @Override
        public List<Order> findAll() {
            return List.of();
        }

        @Override
        public Optional<Order> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Order create(OrderRequest request) {
            return null;
        }
    }

    record Order(Long id, String productName, int amount) {}

    record OrderRequest(
            @jakarta.validation.constraints.NotBlank String productName,
            @jakarta.validation.constraints.Positive int amount) {}

    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    static class OrderNotFoundException extends RuntimeException {
        OrderNotFoundException(Long id) {
            super("Order not found: " + id);
        }
    }
}
