package junit.basics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AssertionTest {

    @Test
    void assertEquals_기본() {
        int a = 1, b = 2;

        int result = a + b;

        assertEquals(3, result);
    }

    @Test
    void assertEquals_메시지_실패_시_원인_파악용() {
        String expected = "hello";
        String actual = "hello";

        // 실패 시 메세지 출력
        assertEquals(expected, actual, "문자열이 일치해야 함");
    }

    @Test
    void assertEqauls_부동소수점_delta_허용_오차() {
        double expected = 0.3;
        double actual = 0.1 + 0.2;

        // delta 없으면 실패 (부동소수점 오차)
        assertEquals(expected, actual, 0.0001);
    }
}
