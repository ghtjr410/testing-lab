package s02_assertj;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BasicAssertionTest {

    @Nested
    class isEqualTo_동등성_비교 {

        @Test
        void 문자열_값_비교() {
            assertThat("hello").isEqualTo("hello");
        }

        @Test
        void 숫자_값_비교() {
            assertThat(100).isEqualTo(100);
        }

        @Test
        void 다른_인스턴스지만_값이_같으면_통과() {
            String str1 = new String("test");
            String str2 = new String("test");

            assertThat(str1).isEqualTo(str2);
        }
    }

    @Nested
    class isSameAs_동일성_비교 {

        @Test
        void 같은_인스턴스면_통과() {
            String str = "hello";
            assertThat(str).isSameAs(str);
        }

        @Test
        void 다른_인스턴스면_실패() {
            String str1 = new String("test");
            String str2 = new String("test");

            assertThat(str1).isNotSameAs(str2);
        }
    }

    @Nested
    class isNull_null_검증 {

        @Test
        void null이면_통과() {
            String str = null;
            assertThat(str).isNull();
        }

        @Test
        void null이_아니면_통과() {
            String str = "hello";
            assertThat(str).isNotNull();
        }
    }

    @Nested
    class 문자열_검증 {

        @Test
        void isEmpty_빈_문자열() {
            assertThat("").isEmpty();
        }

        @Test
        void isNotEmpty_비어있지_않음() {
            assertThat("hello").isNotEmpty();
        }

        @Test
        void isBlank_공백_있는_문자열() {
            assertThat(" ").isBlank();
        }

        @Test
        void contains_포함_여부() {
            assertThat("hello world").contains("world");
        }

        @Test
        void startsWith_접두사() {
            assertThat("hello world").startsWith("hello");
        }

        @Test
        void endsWith_접미사() {
            assertThat("hello world").endsWith("world");
        }

        @Test
        void matches_정규식() {
            assertThat("abc123").matches("[a-z]+[0-9]+");
        }
    }

    @Nested
    class 숫자_검증 {

        @Test
        void isPositive_양수() {
            assertThat(10).isPositive();
        }

        @Test
        void isNegative_음수() {
            assertThat(-10).isNegative();
        }

        @Test
        void isZero_영() {
            assertThat(0).isZero();
        }

        @Test
        void isGreaterThan_초과() {
            assertThat(6).isGreaterThan(5);
        }

        @Test
        void isGreaterThanOrEqualTo_이상() {
            assertThat(5).isGreaterThanOrEqualTo(5);
        }

        @Test
        void isLessThanOrEqualTo_이하() {
            assertThat(5).isLessThanOrEqualTo(5);
        }

        @Test
        void isLessThan_미만() {
            assertThat(4).isLessThan(5);
        }

        @Test
        void isBetween_범위() {
            assertThat(5).isBetween(1, 10);
        }
    }

    @Nested
    class Boolean_검증 {

        @Test
        void isTrue_참() {
            assertThat(true).isTrue();
        }

        @Test
        void isFalse_거짓() {
            assertThat(false).isFalse();
        }
    }

    @Nested
    class 체이닝 {

        @Test
        void 여러_조건을_한번에_검증() {
            String str = "hello world";

            assertThat(str)
                    .isNotNull()
                    .isNotEmpty()
                    .startsWith("hello")
                    .endsWith("world")
                    .contains(" ");
        }
    }
}
