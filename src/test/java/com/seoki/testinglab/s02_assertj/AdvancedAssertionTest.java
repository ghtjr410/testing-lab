package com.seoki.testinglab.s02_assertj;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AdvancedAssertionTest {

    @Nested
    class SoftAssertions_모든_검증_실행 {

        @Test
        void 일반_assertion은_첫_실패에서_멈춤() {
            // 첫 번째 실패하면 두 번째는 실행 안 됨
            // assertThat(1).isEqualTo(2);  // 여기서 멈춤
            // assertThat(3).isEqualTo(4);  // 실행 안 됨
        }

        @Test
        void soft_assertion은_모든_검증_실행_후_한번에_실패() {
            var softly = new org.assertj.core.api.SoftAssertions();

            softly.assertThat(1).isEqualTo(1);
            softly.assertThat("hello").startsWith("h");
            softly.assertThat(List.of(1, 2, 3)).hasSize(3);

            softly.assertAll(); // 여기서 모든 실패 한번에 보고
        }

        @Test
        void assertSoftly_람다_버전() {
            assertSoftly(softly -> {
                softly.assertThat(1).isEqualTo(1);
                softly.assertThat("hello").isNotEmpty();
                softly.assertThat(List.of("a", "b")).contains("a");
            });
        }
    }

    @Nested
    class satisfies_커스텀_검증 {

        record User(String name, int age) {}

        @Test
        void 복잡한_조건_검증() {
            User user = new User("철수", 25);

            assertThat(user).satisfies(u -> {
                assertThat(u.name()).isNotBlank();
                assertThat(u.age()).isBetween(18, 65);
            });
        }

        @Test
        void 컬렉션_각_요소_검증() {
            List<User> users = List.of(new User("철수", 20), new User("영희", 25));

            assertThat(users).allSatisfy(user -> {
                assertThat(user.name()).isNotBlank();
                assertThat(user.age()).isPositive();
            });
        }

        @Test
        void 최소_하나_만족() {
            List<User> users = List.of(new User("철수", 17), new User("영희", 25));

            assertThat(users).anySatisfy(user -> {
                assertThat(user.age()).isGreaterThanOrEqualTo(18);
            });
        }
    }

    @Nested
    class 날짜_시간_검증 {

        @Test
        void LocalDate_비교() {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            assertThat(today).isAfter(yesterday);
            assertThat(today).isAfterOrEqualTo(today);
            assertThat(yesterday).isBefore(today);
            assertThat(yesterday).isBeforeOrEqualTo(yesterday);
        }

        @Test
        void LocalDateTime_범위_검증() {
            LocalDateTime now = LocalDateTime.now();

            assertThat(now).isBetween(now.minusHours(1), now.plusHours(1));
        }

        @Test
        void 날짜_필드별_검증() {
            LocalDate date = LocalDate.of(2024, 12, 25);

            assertThat(date).hasYear(2024).hasMonth(java.time.Month.DECEMBER).hasDayOfMonth(25);
        }
    }

    @Nested
    class Optional_검증 {

        @Test
        void 값이_있으면_통과() {
            var optional = java.util.Optional.of("hello");

            assertThat(optional).isPresent().hasValue("hello");
        }

        @Test
        void 비어있으면_통과() {
            var optional = java.util.Optional.empty();

            assertThat(optional).isEmpty();
        }

        @Test
        void 값_꺼내서_추가_검증() {
            var optional = java.util.Optional.of("hello world");

            assertThat(optional).isPresent().get().asString().startsWith("hello");
        }
    }

    @Nested
    class 조건부_검증 {

        @Test
        void is_조건_검증() {
            String str = "hello";

            assertThat(str).is(new org.assertj.core.api.Condition<>(s -> s.length() > 3, "길이는 3보다 커야 한다"));
            /**
             * 실패 메세지
             * Expecting:
             *   "he"
             * to be:
             *   길이가 3보다 커야 한다
             */
        }

        @Test
        void matches_Predicate_검증() {
            int number = 10;

            assertThat(number).matches(n -> n % 2 == 0, "짝수");
        }
    }
}
