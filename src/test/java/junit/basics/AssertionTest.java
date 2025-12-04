package junit.basics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AssertionTest {

    @Nested
    class AssertEquals {
        @Test
        void 기본() {
            assertEquals(3, 1 + 2);
        }

        @Test
        void message_실패_시_원인_파악용() {
            // 실패 시 메세지 출력
            assertEquals("hello", "hello", "문자열이 일치해야 함");
        }

        @Test
        void delta_부동소수점_허용_오차() {
            // delta 없으면 실패 (부동소수점 오차)
            assertEquals(0.3, 0.1 + 0.2, 0.0001);
        }

        @Test
        void supplier_실패_시에만_메시지_생성() {
            List<Integer> hugeList = IntStream.range(0, 100_000).boxed().toList();

            // 메시지 생성이 무거울 때, 실패 시에만 실행되도록 지연
            assertEquals(100_000, hugeList.size(), () -> "리스트 전체 내용: " + hugeList); // 10만개 출력
        }
    }

    @Nested
    class AssertNotEquals {
        /**
         * @see AssertNotEquals 구현체
         * 내부 구현:
         * - primitive (byte, short, int, long, char): == 비교
         * - float: Float.floatToIntBits(v1) == Float.floatToIntBits(v2)
         * - double: Double.doubleToLongBits(v1) == Double.doubleToLongBits(v2)
         * - float/double (delta):
         *     - 비트일치 || Math.abs(v1 - v2) <= delta
         *     - delta 검증: NaN이거나 음수면 예외
         * - Object: obj1 == null ? obj2 == null : obj1.equals(obj2)
         *
         * 참고: https://github.com/junit-team/junit-framework/blob/main/junit-jupiter-api/src/main/java/org/junit/jupiter/api/AssertNotEquals.java
         */
        @Test
        void primitive_등호연산자_비교() {
            assertNotEquals((byte) 1, (byte) 2);
            assertNotEquals((short) 1, (short) 2);
            assertNotEquals(1, 2);
            assertNotEquals(1L, 2L);
            assertNotEquals('a', 'b');
        }

        @Test
        void float_비트패턴_비교() {
            assertNotEquals(0.1f, 0.2f);
        }

        @Test
        void double_비트패턴_비교() {
            assertNotEquals(0.1, 0.2);
        }

        @Test
        void float_delta_허용오차_비교() {
            assertNotEquals(0.1f, 0.5f, 0.01f);
        }

        @Test
        void Object_equals_비교() {
            assertNotEquals("hello", "world");
            assertNotEquals(List.of(1, 2), List.of(1, 2, 3));
        }

        @Test
        void Object_null_비교() {
            assertNotEquals(null, "hello");
            assertNotEquals("hello", null);

            // 둘 다 null → NPE_없이 equal 판정 → assertNotEquals 실패
            assertThrows(AssertionFailedError.class, () -> assertNotEquals((Object) null, (Object) null));
        }

        @Test
        void message_실패_시_원인_파악용() {
            assertNotEquals(1, 2, "성공하면 이 메시지는 보이지 않음");
        }
    }
}
