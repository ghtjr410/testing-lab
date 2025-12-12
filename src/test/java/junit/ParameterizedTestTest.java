package junit;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
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

    @Nested
    class MethodSource_메서드에서_데이터_제공 {

        @ParameterizedTest
        @MethodSource("stringProvider")
        void 메서드에서_단일값_제공(String input) {
            assertThat(input).isNotBlank();
        }

        static Stream<String> stringProvider() {
            return Stream.of("hello", "world", "test");
        }

        @ParameterizedTest
        @MethodSource("numberProvider")
        void 메서드에서_여러값_제공(int a, int b, int expected) {
            assertThat(a + b).isEqualTo(expected);
        }

        static Stream<Arguments> numberProvider() {
            return Stream.of(Arguments.of(1, 2, 3), Arguments.of(10, 20, 30), Arguments.of(-1, 1, 0));
        }

        @ParameterizedTest
        @MethodSource // 메서드명 생략시 테스트 메서드명과 동일한 메서드 찾음
        void 메서드명_생략_가능(String input) {
            assertThat(input).isNotBlank();
        }

        static Stream<String> 메서드명_생략_가능() {
            return Stream.of("auto", "matched");
        }

        @ParameterizedTest
        @MethodSource("junit.ParameterizedTestTest#externalProvider")
        void 외부_클래스_메서드_참조(String input) {
            assertThat(input).isNotBlank();
        }
    }

    static Stream<String> externalProvider() {
        return Stream.of("external", "data");
    }

    @Nested
    class MethodSource_복잡한_객체 {

        record User(String name, int age, boolean active) {}

        @ParameterizedTest
        @MethodSource("userProvider")
        void 복잡한_객체_테스트(User user) {
            assertThat(user.name()).isNotBlank();
            assertThat(user.age()).isPositive();
        }

        static Stream<User> userProvider() {
            return Stream.of(new User("철수", 20, true), new User("영희", 25, false), new User("민수", 30, true));
        }

        @ParameterizedTest
        @MethodSource("userWithExpectedProvider")
        void 객체와_기대값_함께(User user, boolean expectedActive) {
            assertThat(user.active()).isEqualTo(expectedActive);
        }

        static Stream<Arguments> userWithExpectedProvider() {
            return Stream.of(
                    Arguments.of(new User("철수", 20, true), true), Arguments.of(new User("영희", 25, false), false));
        }
    }

    @Nested
    class ArgumentsSource_커스텀_Provider {

        /**
         * ArgumentsProvider 활용 예시
         *
         * - @ValueSource, @CsvSource 등 정적 데이터 공급 방식으로 표현하기 어려울 때 사용한다.
         * - 테스트 입력값을 코드로 동적으로 생성하거나,
         *   외부 파일/DB/JSON 등에서 읽어와 파라미터를 구성할 때 매우 유용하다.
         * - JUnit이 Provider 클래스를 리플렉션으로 인스턴스화하므로 반드시 기본 생성자가 필요하다.
         *
         * 사용 시나리오:
         * 1) 복잡한 객체 조합이 필요한 도메인 테스트
         * 2) 다양한 케이스를 동적으로 생성하는 Property-based 테스트의 기초 형태
         * 3) JSON/CSV 파일을 파싱하여 파라미터 제공
         * 4) 비즈니스 규칙을 계산하여 여러 입력값을 자동 생성
         *
         */
        @ParameterizedTest
        @ArgumentsSource(CustomArgumentsProvider.class)
        void 커스텀_Provider_사용(String input, int expected) {
            assertThat(input.length()).isEqualTo(expected);
        }

        static class CustomArgumentsProvider implements ArgumentsProvider {
            @Override
            public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
                return Stream.of(Arguments.of("hello", 5), Arguments.of("world", 5), Arguments.of("test", 4));
            }
        }
    }
}
