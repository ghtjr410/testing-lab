package junit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ParameterizedTestTest {

    @Nested
    class ValueSource_단일_값_배열 {
        /**
         * @ValueSource 지원 타입:
         * strings, ints, longs, doubles, floats, bytes, shorts, chars, booleans, classes
         */
        @ParameterizedTest
        @ValueSource(strings = {"hello", ". ", " . ", " ."})
        void 문자열_테스트(String input) {
            assertThat(input).isNotBlank();
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 1000})
        void 정수_테스트(int number) {
            assertThat(number).isPositive();
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 1000L})
        void long_테스트(long number) {
            assertThat(number).isPositive();
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.1, 0.5, 1.0, 3.14})
        void double_테스트(double number) {
            assertThat(number).isPositive();
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void boolean_테스트(boolean value) {
            assertThat(value).isIn(true, false);
        }
    }
}
