package junit;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

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

    @Nested
    class NullSource_EmptySource_특수값 {

        @ParameterizedTest
        @NullSource
        void null_테스트(String input) {
            assertThat(input).isNull();
        }

        @ParameterizedTest
        @EmptySource
        void 빈값_테스트_문자열(String input) {
            assertThat(input).isEmpty();
        }

        @ParameterizedTest
        @EmptySource
        void 빈값_테스트_리스트(List<String> input) {
            assertThat(input).isEmpty();
        }

        @ParameterizedTest
        @NullAndEmptySource
        void null과_빈값_둘다(String input) {
            assertThat(input == null || input.isEmpty()).isTrue();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        void null_빈값_공백_모두_테스트(String input) {
            // null, "", "  ", "\t", "\n" 모두 테스트
            assertThat(input == null || input.isBlank()).isTrue();
        }
    }

    @Nested
    class EnumSource_Enum_값 {

        enum Status {
            PENDING,
            APPROVED,
            REJECTED,
            CANCELLED
        }

        @ParameterizedTest
        @EnumSource(Status.class)
        void 모든_Enum_값_테스트(Status status) {
            assertThat(status).isNotNull();
        }

        @ParameterizedTest
        @EnumSource(
                value = Status.class,
                names = {"APPROVED", "REJECTED"})
        void 특정_Enum만_테스트(Status status) {
            assertThat(status).isIn(Status.APPROVED, Status.REJECTED);
        }

        @ParameterizedTest
        @EnumSource(
                value = Status.class,
                names = {"CANCELLED"},
                mode = EnumSource.Mode.EXCLUDE)
        void 특정_Enum_제외(Status status) {
            assertThat(status).isNotEqualTo(Status.CANCELLED);
        }

        @ParameterizedTest
        @EnumSource(value = Status.class, names = ".*ED", mode = EnumSource.Mode.MATCH_ALL)
        void 정규식_매칭(Status status) {
            // APPROVED, REJECTED, CANCELLED (ED로 끝나는 것들)
            assertThat(status.name()).endsWith("ED");
        }
    }

    @Nested
    class CsvSource_여러_파라미터 {

        @ParameterizedTest
        @CsvSource({"1, 2, 3", "10, 20, 30", "100, 200, 300"})
        void 덧셈_테스트(int a, int b, int expected) {
            assertThat(a + b).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({"hello, 5", "world, 5", "test, 4"})
        void 문자열_길이_테스트(String input, int expectedLength) {
            assertThat(input).hasSize(expectedLength);
        }

        @ParameterizedTest
        @CsvSource(
                value = {"apple | 1000", "banana | 2000", "cherry | 3000"},
                delimiter = '|')
        void 구분자_변경(String fruit, int price) {
            assertThat(fruit).isNotBlank();
            assertThat(price).isPositive();
        }

        @ParameterizedTest
        @CsvSource({"'hello, world', 12", "'foo, bar', 8"})
        void 쉼표_포함_문자열은_따옴표로_감싸기(String input, int length) {
            assertThat(input).hasSize(length);
        }

        @ParameterizedTest
        @CsvSource(
                value = {
                    "test, ",
                },
                nullValues = "")
        void 빈값을_null로_처리(String first, String second) {
            assertThat(second).isNull();
        }

        @ParameterizedTest
        @CsvSource(textBlock = """
            1, 2, 3
            10, 20, 30
            100, 200, 300
            """)
        void TextBlock_사용(int a, int b, int expected) {
            assertThat(a + b).isEqualTo(expected);
        }
    }

    @Nested
    class CsvFileSource_외부_CSV_파일 {

        /**
         * src/test/resources/test-csv-file-source.csv 파일 필요:
         * name,age
         * 철수,20
         * 영희,25
         */
        @ParameterizedTest
        @CsvFileSource(resources = "/test-csv-file-source.csv", numLinesToSkip = 1)
        void CSV_파일에서_데이터_읽기(String name, int age) {
            assertThat(name).isNotBlank();
            assertThat(age).isPositive();
        }
    }
}
