package junit.basics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
         * 내부 구현:
         * <pre>
         * - primitive (byte, short, int, long, char): == 비교
         * - float: Float.floatToIntBits(v1) == Float.floatToIntBits(v2)
         * - double: Double.doubleToLongBits(v1) == Double.doubleToLongBits(v2)
         * </pre>
         *
         * @see <a href="https://github.com/junit-team/junit-framework/blob/main/junit-jupiter-api/src/main/java/org/junit/jupiter/api/AssertNotEquals.java">AssertNotEquals</a>
         */
        @Test
        void primitive_등호연산자_비교() {
            // if (unexpected == actual) failEqual()
            assertNotEquals((byte) 1, (byte) 2);
            assertNotEquals((short) 1, (short) 2);
            assertNotEquals(1, 2);
            assertNotEquals(1L, 2L);
            assertNotEquals('a', 'b');
        }

        @Test
        void float_비트패턴_비교() {
            // Float.floatToIntBits(v1) == Float.floatToIntBits(v2)
            assertNotEquals(0.1f, 0.2f);

            // 같은 비트 패턴이면 equal → assertNotEquals 실패
            // assertNotEquals(0.5f, 0.5f);  // 실패
        }
    }
}
