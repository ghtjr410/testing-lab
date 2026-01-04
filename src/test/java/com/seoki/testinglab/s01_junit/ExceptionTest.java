package com.seoki.testinglab.s01_junit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ExceptionTest {

    @Nested
    class assertTimeout_시간_제한 {

        @Test
        void 시간_내_완료되면_통과() {
            assertTimeout(Duration.ofSeconds(1), () -> {
                Thread.sleep(100);
            });
        }

        @Test
        void 반환값_받기() {
            String result = assertTimeout(Duration.ofSeconds(1), () -> {
                return "완료";
            });

            assertThat(result).isEqualTo("완료");
        }

        @Test
        void assertTimeoutPreemptively_즉시_중단() {
            // 시간 초과 시 바로 실패 (스레드 강제 종료)
            // 주의: ThreadLocal 등 스레드 의존 코드에서 문제 발생 가능
            assertTimeoutPreemptively(Duration.ofMillis(500), () -> {
                Thread.sleep(100);
            });
        }
    }

    @Nested
    class assertTimeout_vs_assertTimeoutPreemptively {

        /**
         * assertTimeout
         * - 작업 완료까지 기다린 후 시간 초과 여부 판단
         * - 같은 스레드에서 실행
         * - ThreadLocal, 트랜잭션 등 안전하게 동작
         */
        @Test
        void assertTimeout은_같은_스레드() {
            Thread testThread = Thread.currentThread();

            assertTimeout(Duration.ofSeconds(1), () -> {
                assertThat(Thread.currentThread()).isSameAs(testThread);
            });
        }

        /**
         * assertTimeoutPreemptively
         * - 시간 초과 시 즉시 중단
         * - 별도 스레드에서 실행
         * - 주의: @Transactional 테스트에서 롤백 안 될 수 있음
         */
        @Test
        void assertTimeoutPreemptively는_다른_스레드() {
            Thread testThread = Thread.currentThread();

            assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
                assertThat(Thread.currentThread()).isNotSameAs(testThread);
            });
        }
    }
}
