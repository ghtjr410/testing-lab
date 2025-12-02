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
}
